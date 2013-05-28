import akka.actor.*;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class HelloAkkaTest {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        system.shutdown();
        system.awaitTermination();
    }

    @Test
    public void testSetGreeter() {
        new JavaTestKit(system) {{
            final TestActorRef<HelloAkkaJava.Greeter> greeter =
                TestActorRef.create(system, new Props(HelloAkkaJava.Greeter.class), "greeter1");

            greeter.tell(new HelloAkkaJava.WhoToGreet("testkit"), getRef());

            Assert.assertTrue(greeter.underlyingActor().greeting.equals("hello, testkit"));
        }};
    }

    @Test
    public void testGetGreeter() {
        new JavaTestKit(system) {{

            final ActorRef greeter = system.actorOf(new Props(HelloAkkaJava.Greeter.class), "greeter2");

            final JavaTestKit probe = new JavaTestKit(system);

            greeter.tell(new HelloAkkaJava.WhoToGreet("testkit"), getRef());
            greeter.tell(new HelloAkkaJava.Greet(), getRef());

            final HelloAkkaJava.Greeting greeting = expectMsgClass(HelloAkkaJava.Greeting.class);

            new Within(duration("10 seconds")) {
                protected void run() {
                    Assert.assertTrue(greeting.message.equals("hello, testkit"));
                }
            };
        }};
    }
}
