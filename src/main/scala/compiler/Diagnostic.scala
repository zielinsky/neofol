package compiler

import java.nio.file.Path

enum DiagnosticLevel(val label: String):
  case Warning extends DiagnosticLevel("warning")
  case Error extends DiagnosticLevel("error")

trait Diagnostic:
  def message: String
  def level: DiagnosticLevel

  final def render: String =
    s"${level.label}: $message"

final case class SourceReadError(
    path: Path,
    reason: String
) extends Diagnostic:
  val level: DiagnosticLevel = DiagnosticLevel.Error
  val message: String = s"Cannot read '$path': $reason"
