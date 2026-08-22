package raw

import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import raw.antlr.{NeofolLexer, NeofolParser}

object Parser:
  def parse(source: String): Expr =
    val lexer = new NeofolLexer(CharStreams.fromString(source))
    val tokens = new CommonTokenStream(lexer)
    val parser = new NeofolParser(tokens)

    val tree = parser.program()

    Expr.IntLiteral(BigInt(tree.expression().INTEGER().getText))