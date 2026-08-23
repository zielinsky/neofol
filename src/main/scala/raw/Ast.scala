package raw

sealed trait Expr

enum Literal extends Expr:
  case Int(value: BigInt)
  case Bool(value: Boolean)
  case String(value: scala.Predef.String)

final case class Variable(name: String) extends Expr

final case class TypeExpression(name: String)

final case class Parameter(
    name: String,
    typeExpression: TypeExpression
)

final case class Let(
    name: String,
    typeExpression: Option[TypeExpression],
    parameters: List[Parameter],
    body: Expr
) extends Expr
