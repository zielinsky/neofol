package raw

import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import raw.antlr.{NeofolLexer, NeofolParser}

import compiler.{Diagnostic, Options, Context}

object Raw:
  def parse(
      source: String
  ): Either[Diagnostic, Expr] =
    val lexer = new NeofolLexer(CharStreams.fromString(source))
    val tokens = new CommonTokenStream(lexer)
    val parser = new NeofolParser(tokens)

    val tree = parser.program()

    if parser.getNumberOfSyntaxErrors() > 0 then
      Left(Diagnostic("Syntax error"))
    else
      Right(Literal.Int(BigInt(tree.expression().INTEGER().getText())))
