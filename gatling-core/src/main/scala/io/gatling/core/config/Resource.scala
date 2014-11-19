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
package io.gatling.core.config

import java.io.{ FileInputStream, File, FileOutputStream, InputStream }
import java.net.URL
import java.nio.file.Path

import io.gatling.core.validation.{ FailureWrapper, SuccessWrapper, Validation }
import io.gatling.core.util.IO._
import io.gatling.core.util.PathHelper._

object Resource {

  private object ClasspathResource {
    private def extension(s: String) = {
      val lastIndex = s.lastIndexOf('.')
      if (lastIndex != -1) "" else s.substring(lastIndex + 1)
    }
    def unapply(location: Location): Option[Validation[Resource]] =
      Option(getClass.getClassLoader.getResource(location.path.replace('\\', '/'))).map { url =>
        url.getProtocol match {
          case "file" => FileResource(url.jfile).success
          case "jar"  => ArchiveResource(url, extension(location.path)).success
          case _      => s"$url is neither a file nor a jar".failure
        }
      }
  }

  private object FileInFolderResource {
    def unapply(location: Location): Option[Validation[Resource]] =
      (location.directory / location.path).ifFile(f => FileResource(f).success)
  }

  private object AbsoluteFileResource {
    def unapply(location: Location): Option[Validation[Resource]] =
      string2path(location.path).ifFile(f => FileResource(f).success)
  }

  private def load(directory: Path, path: String): Validation[Resource] =
    new Location(directory, path) match {
      case ClasspathResource(res)    => res
      case FileInFolderResource(res) => res
      case AbsoluteFileResource(res) => res
      case _                         => s"file $path doesn't exist".failure
    }

  private class Location(val directory: Path, val path: String)

  def feeder(fileName: String): Validation[Resource] = load(GatlingFiles.dataDirectory, fileName)
  def body(fileName: String): Validation[Resource] = load(GatlingFiles.bodiesDirectory, fileName)
}

sealed trait Resource {
  def inputStream: InputStream
  def file: File
}

case class FileResource(file: File) extends Resource {
  def inputStream = new FileInputStream(file)
}

case class ArchiveResource(url: URL, extension: String) extends Resource {

  def inputStream = url.openStream

  def file = {
    val tempFile = File.createTempFile("gatling", "." + extension)

    withCloseable(inputStream) { is =>
      withCloseable(new FileOutputStream(tempFile, false)) { os =>
        is.copyTo(os)
      }
    }
    tempFile
  }
}
