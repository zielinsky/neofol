import raw.Raw
import compiler.Options

@main def neofol(): Unit = 
  val source = "123"

  given Options = Options()

  Raw.transform(source) match
    case Right(expression) =>
      println(s"AST: $expression")
    case Left(diagnostic) =>
      Console.err.println(s"Error: ${diagnostic.message}")
