package compiler

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}
import scala.util.Try

final case class SourceFile(
    path: Path,
    content: String
):
  def name: String = path.toString

object SourceFile:

  def read(path: Path): Either[Diagnostic, SourceFile] =
    Try(Files.readString(path, StandardCharsets.UTF_8)).toEither.left
      .map(error =>
        Diagnostic(
          s"Cannot read '$path': ${error.getMessage}"
        )
      )
      .map(content => SourceFile(path, content))
