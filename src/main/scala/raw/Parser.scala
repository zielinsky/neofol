package raw

import compiler.{Compilation, Diagnostic, SourceFile}
import org.antlr.v4.runtime.{
  BaseErrorListener,
  CharStreams,
  CommonTokenStream,
  RecognitionException,
  Recognizer
}
import raw.antlr.{NeofolLexer, NeofolParser}

import scala.collection.mutable.ArrayBuffer
import scala.util.Try

object Raw:
  def parse(source: SourceFile): Compilation[Expr] =
    val diagnostics = ArrayBuffer.empty[Diagnostic]
    val errorListener = DiagnosticErrorListener(source.name, diagnostics)

    val lexer = new NeofolLexer(
      CharStreams.fromString(source.content, source.name)
    )
    lexer.removeErrorListeners()
    lexer.addErrorListener(errorListener)

    val tokens = new CommonTokenStream(lexer)
    val parser = new NeofolParser(tokens)
    parser.removeErrorListeners()
    parser.addErrorListener(errorListener)

    val tree = parser.program()
    val output =
      Option(tree.expression())
        .flatMap(expression => Option(expression.INTEGER()))
        .flatMap(token => Try(BigInt(token.getText)).toOption)
        .map(Literal.Int.apply)

    Compilation(output, diagnostics.toVector)

  private final class DiagnosticErrorListener(
      sourceName: String,
      diagnostics: ArrayBuffer[Diagnostic]
  ) extends BaseErrorListener:
    override def syntaxError(
        recognizer: Recognizer[?, ?],
        offendingSymbol: Object,
        line: Int,
        charPositionInLine: Int,
        message: String,
        exception: RecognitionException
    ): Unit =
      diagnostics += SyntaxError(
        sourceName,
        line,
        charPositionInLine + 1,
        message
      )
