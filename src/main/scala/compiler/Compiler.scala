package compiler

import raw.{Expr, Raw}

final case class Compilation[+A](
    output: Option[A],
    diagnostics: Vector[Diagnostic]
):
  def map[B](f: A => B): Compilation[B] =
    Compilation(output.map(f), diagnostics)

  def flatMap[B](f: A => Compilation[B]): Compilation[B] =
    output match
      case Some(value) =>
        val next = f(value)
        Compilation(next.output, diagnostics ++ next.diagnostics)
      case None =>
        Compilation(None, diagnostics)

  def errors: Vector[Diagnostic] =
    diagnostics.filter(_.level == DiagnosticLevel.Error)

  def warnings: Vector[Diagnostic] =
    diagnostics.filter(_.level == DiagnosticLevel.Warning)

  def hasErrors: Boolean =
    errors.nonEmpty

  def hasWarnings: Boolean =
    warnings.nonEmpty

  def succeeded: Boolean =
    output.nonEmpty && !hasErrors

object Compilation:
  def success[A](output: A): Compilation[A] =
    Compilation(Some(output), Vector.empty)

  def successWith[A](
      output: A,
      diagnostics: Vector[Diagnostic]
  ): Compilation[A] =
    Compilation(Some(output), diagnostics)

  def failure[A](diagnostics: Vector[Diagnostic]): Compilation[A] =
    Compilation(None, diagnostics)

final class Compiler(options: Options):

  def compile(source: SourceFile): Compilation[Expr] =
    Raw.parse(source)
