import akka.actor.{ Actor, Props, ActorSystem }
import akka.testkit.{ ImplicitSender, TestKit, TestActorRef }
import org.scalatest.{ BeforeAndAfterAll, FlatSpecLike, Matchers }
import scala.concurrent.duration._

class HelloAkkaSpec extends TestKit(ActorSystem("HelloAkkaSpec")) with ImplicitSender
    with Matchers with FlatSpecLike with BeforeAndAfterAll {

  "An HelloAkkaActor" should "be able to get a new greeting" in {
    val greeter = system.actorOf(Greeter.props)
    greeter ! WhoToGreet("testkit")
    greeter ! Greet
    expectMsg(Greeting("hello, testkit"))
  }

  protected override def afterAll(): Unit =
    shutdown(system)
}
