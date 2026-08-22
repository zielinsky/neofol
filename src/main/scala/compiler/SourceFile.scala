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

  def read(path: Path): Compilation[SourceFile] =
    Try(Files.readString(path, StandardCharsets.UTF_8)).fold(
      error =>
        Compilation.failure(
          Vector(
            SourceReadError(path, error.getMessage)
          )
        ),
      content => Compilation.success(SourceFile(path, content))
    )
