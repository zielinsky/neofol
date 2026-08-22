package raw

import compiler.{Diagnostic, DiagnosticLevel}

final case class SyntaxError(
    sourceName: String,
    line: Int,
    column: Int,
    details: String
) extends Diagnostic:
  val level: DiagnosticLevel = DiagnosticLevel.Error
  val message: String = s"$sourceName:$line:$column: $details"
