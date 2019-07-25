import org.scalatest.FlatSpec

abstract class UnitSpec extends FlatSpec

class Test1 extends UnitSpec {
  "Twitter keys" must "not be null" in {
    val consumerToken = 1
    val accessToken = 1
    assert(consumerToken != null)
    assert(accessToken != null)
  }

}
