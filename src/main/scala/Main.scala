import compiler.{Compiler, Options, SourceFile}

import java.nio.file.Path

@main def neofol(file: String): Unit =
  val compiler = Compiler(Options())

  val result =
    for
      source <- SourceFile.read(Path.of(file))
      expression <- compiler.compile(source)
    yield expression

  result match
    case Right(expression) =>
      println(expression)

    case Left(diagnostic) =>
      Console.err.println(s"Error: ${diagnostic.message}")
