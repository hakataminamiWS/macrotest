package example

import input.Color
import enumeratum._

object Hello extends Greeting with App {
  println(greeting)
}

trait Greeting {
  lazy val greeting: String = Color.withName("Red").toString
}
