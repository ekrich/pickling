package scala.pickling.non.public.separate

import org.scalatest.FunSuite
import scala.pickling._, scala.pickling.Defaults._, json._

class Person(private val name: String, age: Int, val hobby: Hobby) {
  // NOTE: be careful not to reference age anywhere, so that it's elided by the "constructors" phase
  override def toString = s"Person(name = $name, hobby = $hobby)"
}
final class Hobby(var name: String, private var notes: String, private val attitude: String) {
  override def toString = s"Hobby(name = $name, notes = $notes, attitude = $attitude)"
}

class NonPublicSeparateTest extends FunSuite {
  test("main") {
    val person = new Person("Eugene", 25, new Hobby("hacking", "mostly Scala", "loving it"))
    val anyPickle = (person: Any).pickle
    assert(anyPickle.toString === """
      |JSONPickle({
      |  "$type": "scala.pickling.non.public.separate.Person",
      |  "name": "Eugene",
      |  "hobby": {
      |    "name": "hacking",
      |    "notes": "mostly Scala",
      |    "attitude": "loving it"
      |  }
      |})
    """.stripMargin.trim)
    assert(anyPickle.unpickle[Any].toString === person.toString)
  }
}
