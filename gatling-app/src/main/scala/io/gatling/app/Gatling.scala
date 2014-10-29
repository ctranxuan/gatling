/**
 * Copyright 2011-2014 eBusiness Information, Groupe Excilys (www.ebusinessinformation.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gatling.app

import java.lang.System.currentTimeMillis

import scala.collection.mutable
import scala.io.StdIn
import scala.util.Try

import com.typesafe.scalalogging.StrictLogging

import io.gatling.app.CommandLineConstants._
import io.gatling.charts.report.ReportsGenerator
import io.gatling.core.assertion.AssertionValidator
import io.gatling.core.config.{ GatlingFiles, GatlingPropertiesBuilder }
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.config.GatlingConfiguration.configuration
import io.gatling.core.result.reader.DataReader
import io.gatling.core.runner.{ Runner, Selection }
import io.gatling.core.scenario.Simulation
import io.gatling.core.util.StringHelper
import io.gatling.core.util.StringHelper.RichString
import scopt.OptionParser

/**
 * Object containing entry point of application
 */
object Gatling {

  def main(args: Array[String]): Unit = sys.exit(runGatling(args, None))

  def runGatling(args: Array[String], simulationClass: Option[Class[Simulation]]): Int = {
    val props = new GatlingPropertiesBuilder

    val cliOptsParser = new OptionParser[Unit]("gatling") with CommandLineConstantsSupport[Unit] {
      help(Help).text("Show help (this message) and exit")
      opt[Unit](NoReports).foreach(_ => props.noReports()).text("Runs simulation but does not generate reports")
      opt[Unit](Mute).foreach(_ => props.mute()).text("Runs in mute mode: don't asks for run description nor simulation ID, use defaults").hidden()
      opt[String](ReportsOnly).foreach(props.reportsOnly).valueName("<directoryName>").text("Generates the reports for the simulation in <directoryName>")
      opt[String](DataFolder).foreach(props.dataDirectory).valueName("<directoryPath>").text("Uses <directoryPath> as the absolute path of the directory where feeders are stored")
      opt[String](ResultsFolder).foreach(props.resultsDirectory).valueName("<directoryPath>").text("Uses <directoryPath> as the absolute path of the directory where results are stored")
      opt[String](RequestBodiesFolder).foreach(props.requestBodiesDirectory).valueName("<directoryPath>").text("Uses <directoryPath> as the absolute path of the directory where request bodies are stored")
      opt[String](SimulationsFolder).foreach(props.sourcesDirectory).valueName("<directoryPath>").text("Uses <directoryPath> to discover simulations that could be run")
      opt[String](Simulation).foreach(props.simulationClass).valueName("<className>").text("Runs <className> simulation")
      opt[String](OutputDirectoryBaseName).foreach(props.outputDirectoryBaseName).valueName("<name>").text("Use <name> for the base name of the output directory")
      opt[String](SimulationDescription).foreach(props.runDescription).valueName("<description>").text("A short <description> of the run to include in the report")
    }

    // if arguments are incorrect, usage message is displayed
    if (cliOptsParser.parse(args)) new Gatling(props.build, simulationClass).start
    else GatlingStatusCodes.InvalidArguments
  }
}

class Gatling(props: mutable.Map[String, _], simulationClass: Option[Class[Simulation]]) extends StrictLogging {

  private def printAndReturn[T](msg: String, value: T) = {
    println(msg)
    value
  }

  def start: Int = {
    StringHelper.checkSupportedJavaVersion()

    GatlingConfiguration.setUp(props)

      def defaultOutputDirectoryBaseName(clazz: Class[Simulation]) =
        configuration.core.outputDirectoryBaseName.getOrElse(clazz.getSimpleName.clean)

      def getSingleSimulation(simulations: List[Class[Simulation]]) =
        configuration.core.simulationClass.map(_ => simulations.head.newInstance)

      def interactiveSelect(simulations: List[Class[Simulation]]): Selection = {

        val simulation: Class[Simulation] = {
            def loop(): Class[Simulation] = {

                def readSimulationNumber: Int =
                  Try(StdIn.readInt()).getOrElse {
                    printAndReturn("Invalid characters, please provide a correct simulation number:", readSimulationNumber)
                  }

              val selection = simulations.size match {
                case 0 =>
                  // If there is no simulation file
                  printAndReturn("There is no simulation script. Please check that your scripts are in user-files/simulations", sys.exit())
                case 1 =>
                  printAndReturn(s"${simulations.head.getName} is the only simulation, executing it.", 0)
                case _ =>
                  println("Choose a simulation number:")
                  for ((simulation, index) <- simulations.zipWithIndex) {
                    println(s"     [$index] ${simulation.getName}")
                  }
                  readSimulationNumber
              }

              val validRange = 0 until simulations.size
              if (validRange contains selection)
                simulations(selection)
              else {
                printAndReturn(s"Invalid selection, must be in $validRange", loop())
              }
            }

          loop()
        }

        val myDefaultOutputDirectoryBaseName = defaultOutputDirectoryBaseName(simulation)

        val userInput: String = {
            def loop(): String = {
              println(s"Select simulation id (default is '$myDefaultOutputDirectoryBaseName'). Accepted characters are a-z, A-Z, 0-9, - and _")
              val input = StdIn.readLine().trim
              if (input.matches("[\\w-_]*")) input
              else printAndReturn(s"$input contains illegal characters", loop())
            }
          loop()
        }

        val simulationId = if (!userInput.isEmpty) userInput else myDefaultOutputDirectoryBaseName

        val runDescription = printAndReturn("Select run description (optional)", StdIn.readLine().trim)

        new Selection(simulation, simulationId, runDescription)
      }

      def applyAssertions(dataReader: DataReader) =
        if (AssertionValidator.validateAssertions(dataReader))
          printAndReturn("Simulation successful.", GatlingStatusCodes.Success)
        else
          printAndReturn("Simulation failed.", GatlingStatusCodes.AssertionsFailed)

      def generateReports(outputDirectoryName: String, dataReader: => DataReader): Unit = {
        println("Generating reports...")
        val start = currentTimeMillis
        val indexFile = ReportsGenerator.generateFor(outputDirectoryName, dataReader)
        println(s"Reports generated in ${(currentTimeMillis - start) / 1000}s.")
        println(s"Please open the following file: ${indexFile.toFile}")
      }

    val simulations = simulationClass match {
      case Some(clazz) => List(clazz)
      case None =>
        val simulationClassLoader = SimulationClassLoader(GatlingFiles.binariesDirectory)

        simulationClassLoader
          .simulationClasses(configuration.core.simulationClass)
          .sortBy(_.getName)
    }

    val (outputDirectoryName, simulation) = GatlingFiles.reportsOnlyDirectory match {
      case Some(dir) =>
        (dir, getSingleSimulation(simulations))

      case None =>
        val selection = configuration.core.simulationClass match {
          case Some(_) =>
            // FIXME ugly
            val simulation = simulations.head
            val outputDirectoryBaseName = defaultOutputDirectoryBaseName(simulation)
            val runDescription = configuration.core.runDescription.getOrElse(outputDirectoryBaseName)
            new Selection(simulation, outputDirectoryBaseName, runDescription)

          case None =>
            if (configuration.core.muteMode)
              simulationClass match {
                case Some(clazz) =>
                  Selection(clazz, defaultOutputDirectoryBaseName(clazz), "")
                case None =>
                  throw new UnsupportedOperationException("Mute mode is currently used by Gatling SBT plugin only.")
              }
            else
              interactiveSelect(simulations)
        }

        val (runId, simulation) = new Runner(selection).run
        (runId, Some(simulation))
    }

    lazy val dataReader = DataReader.newInstance(outputDirectoryName)

    val result = {
      if (dataReader.assertions.nonEmpty) applyAssertions(dataReader)
      else GatlingStatusCodes.Success
    }

    if (!configuration.charting.noReports) generateReports(outputDirectoryName, dataReader)

    result
  }
}
