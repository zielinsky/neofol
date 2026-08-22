package compiler

trait Transform[Input, Output]:

  def transform(input: Input)(using Options): Either[Diagnostic, Output]

    def andThen[Next](next: Transform[Output, Next]): Transform[Input, Next] = 
        new Transform[Input, Next]:
            def transform(input: Input)(using Options): Either[Diagnostic, Next] =
                Transform.this.transform(input).flatMap(next.transform)
