package com.jimo.compoundtype

trait MyCloneable extends Cloneable {
  override def clone(): Cloneable = {
    super.clone().asInstanceOf[Cloneable]
  }
}

trait Resetable {
  def reset(): Unit
}

object Test {

  def cloneAndReset(obj: MyCloneable with Resetable): Cloneable = {
    val cloned = obj.clone()
    obj.reset()
    cloned
  }
}
