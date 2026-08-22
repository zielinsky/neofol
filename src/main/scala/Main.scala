import compiler.{Compiler, Options, SourceFile}

import java.nio.file.Path

@main def neofol(file: String): Unit =
  val compiler = Compiler(Options())

  val result =
    for
      source <- SourceFile.read(Path.of(file))
      expression <- compiler.compile(source)
    yield expression

  result.diagnostics.foreach(diagnostic =>
    Console.err.println(diagnostic.render)
  )

  if result.succeeded then result.output.foreach(println)
