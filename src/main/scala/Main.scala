import raw.Raw

@main def neofol(): Unit = 
  val source = "123"

  Raw.transform(source) match
    case Right(expression) =>
      println(s"AST: $expression")
    case Left(diagnostic) =>
      Console.err.println(s"Error: ${diagnostic.message}")
