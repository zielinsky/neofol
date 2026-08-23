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
import scala.jdk.CollectionConverters.*
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
    val output = Option(tree.letExpression()).flatMap(buildLet)

    Compilation(output, diagnostics.toVector)

  private def buildLet(
      context: NeofolParser.LetExpressionContext
  ): Option[Expr] =
    context match
      case inferred: NeofolParser.InferredLetContext =>
        buildBody(inferred.body()).map(body =>
          Let(
            name = inferred.IDENTIFIER().getText,
            typeExpression = None,
            parameters =
              inferred.typedBinding().asScala.toList.map(buildBinding),
            body = body
          )
        )

      case annotated: NeofolParser.AnnotatedLetContext =>
        val bindings = annotated.typedBinding().asScala.toList.map(buildBinding)

        bindings.headOption.flatMap { binding =>
          buildBody(annotated.body()).map(body =>
            Let(
              name = binding.name,
              typeExpression = Some(binding.typeExpression),
              parameters = bindings.tail,
              body = body
            )
          )
        }

      case _ =>
        None

  private def buildBinding(
      context: NeofolParser.TypedBindingContext
  ): Parameter =
    Parameter(
      name = context.IDENTIFIER().getText,
      typeExpression = TypeExpression(context.typeExpression().getText)
    )

  private def buildBody(context: NeofolParser.BodyContext): Option[Expr] =
    Option(context.letExpression())
      .flatMap(buildLet)
      .orElse(
        Option(context.INTEGER())
          .flatMap(token => Try(BigInt(token.getText)).toOption)
          .map(Literal.Int.apply)
      )
      .orElse(
        Option(context.STRING_LITERAL()).map(token =>
          Literal.String(token.getText.drop(1).dropRight(1))
        )
      )
      .orElse(
        Option(context.TRUE()).map(_ => Literal.Bool(true))
      )
      .orElse(
        Option(context.FALSE()).map(_ => Literal.Bool(false))
      )
      .orElse(
        Option(context.IDENTIFIER()).map(token => Variable(token.getText))
      )

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
