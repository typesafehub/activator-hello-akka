import akka.actor.{ Actor, ActorRef, ActorSystem, Inbox, Props }
import scala.concurrent.duration._

case object Greet
case class WhoToGreet(who: String)
case class Greeting(message: String)

// Greeter actor

object Greeter {
  def props: Props =
    Props(new Greeter)
}

class Greeter extends Actor {

  private var greeting = ""

  override def receive: Receive = {
    case WhoToGreet(who) => greeting = s"hello, $who"
    case Greet           => sender() ! Greeting(greeting) // Send the current greeting back to the sender
  }
}

// GreetPrinter actor

object GreetPrinter {
  def props: Props =
    Props(new GreetPrinter)
}

class GreetPrinter extends Actor {
  override def receive: Receive = {
    case Greeting(message) => println(message)
  }
}

// App

object HelloAkkaScala extends App {

  // Create the 'hello-akka' actor system
  val system = ActorSystem("hello-akka")

  // Create the 'greeter' actor
  val greeter = system.actorOf(Greeter.props, "greeter")

  // Create an "actor-in-a-box"
  val inbox = Inbox.create(system)

  // Tell the 'greeter' to change its 'greeting' message
  greeter ! WhoToGreet("akka")

  // Ask the 'greeter for the latest 'greeting'
  // Reply should go to the "actor-in-a-box"
  inbox.send(greeter, Greet)

  // Wait 5 seconds for the reply with the 'greeting' message
  val Greeting(message1) = inbox.receive(5.seconds)
  println(s"Greeting: $message1")

  // Change the greeting and ask for it again
  greeter ! WhoToGreet("typesafe")
  inbox.send(greeter, Greet)
  val Greeting(message2) = inbox.receive(5.seconds)
  println(s"Greeting: $message2")

  val greetPrinter = system.actorOf(GreetPrinter.props, "greeter-printer")
  // after zero seconds, send a Greet message every second to the greeter with a sender of the greetPrinter
  system.scheduler.schedule(0.seconds, 1.second, greeter, Greet)(system.dispatcher, greetPrinter)
}
