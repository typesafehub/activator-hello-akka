import akka.actor.{Actor, Props, ActorSystem}
import akka.kernel.{Bootable, Main}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

// prints a greeting
class GreetPrinter extends Actor {
  def receive = {
    case Greeting(message) => println(message)
  }
}

// stays running until terminated
class HelloKernel extends Bootable {
  val system = ActorSystem("hellokernel")

  def startup = {
    // the greeter holds the state containing who to greet 
    val greeter = system.actorOf(Props[Greeter])
    
    // initialize the state in the greeter
    greeter ! WhoToGreet("kernel")
    
    // the actor that will print the greeting message
    val greetPrinter = system.actorOf(Props[GreetPrinter])
    
    // after zero seconds, send a Greet message every second to the greeter with a sender of the greetPrinter
    system.scheduler.schedule(0.seconds, 1.second, greeter, Greet)(global, greetPrinter)
  }

  def shutdown = {
    system.shutdown
  }

}

// provides a way to start the app
object HelloKernel extends App {
  Main.main(Array[String]("HelloKernel"))
}