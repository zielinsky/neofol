package raw

sealed trait Expr

enum Literal extends Expr:
  case Int(value: BigInt)
  case Bool(value: Boolean)
  case String(value: String)
