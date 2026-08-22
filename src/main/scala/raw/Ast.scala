package raw

enum Expr:
  case IntLiteral(value: BigInt)
  case BoolLiteral(value: Boolean)
  case StringLiteral(value: String)
