package compiler

import raw.{Expr, Raw}

final class Compiler(options: Options):

  def compile(source: SourceFile): Either[Diagnostic, Expr] =
    for raw <- Raw.parse(source.content)
    yield raw
