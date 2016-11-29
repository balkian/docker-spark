package examples

import java.io._
import java.net.{InetAddress,ServerSocket,Socket,SocketException}
import java.util.Random

/** 
 * Simple client/server application using Java sockets. 
 * 
 * The server simply generates random integer values and 
 * the clients provide a filter function to the server 
 * to get only values they interested in (eg. even or 
 * odd values, and so on). 
 */
object randomclient {
     
  def main(args: Array[String]) {
    try {
      val ia = InetAddress.getByName("localhost")
      val socket = new Socket(ia, 9999)
      val out = new ObjectOutputStream(
        new DataOutputStream(socket.getOutputStream()))
      val in = new DataInputStream(socket.getInputStream())

      while (true) {
        val x = in.readChar()
        print(x)
      }
      out.close()
      in.close()
      socket.close()
    }
    catch {
      case e: IOException =>
        e.printStackTrace()
    }
  }

}

object randomserver {

  def main(args: Array[String]): Unit = {
    try {
      val listener = new ServerSocket(9999);
      val wait = if (args.length > 0) args(0).toInt else 1
      while (true)
        new ServerThread(listener.accept(), wait).start();
      listener.close()
    }
    catch {
      case e: IOException =>
        System.err.println("Could not listen on port: 9999.");
        System.exit(-1)
    }
  }

}

case class ServerThread(socket: Socket, waitTime: Int = 1000000) extends Thread("ServerThread") {

  val students = List("Alice", "Bob", "Charles", "Daisy", "Eve", "Frank", "George", "Hans", "Irene", "John", "Kevin")
  val topics = List("Scala", "spark", "java", "big data")
  val actions = List("asks", "knows")

  override def run(): Unit = {
    val rand = new Random(System.currentTimeMillis());
    try {
      val out = new DataOutputStream(socket.getOutputStream());

      while (true) {
        val name = students(rand.nextInt(students.length));
        val topic = topics(rand.nextInt(topics.length));
        val action = actions(rand.nextInt(actions.length));
        val message = name+" "+action+" about "+topic+"\n"
        out.writeChars(message)
        print(message)
        Thread.sleep(waitTime)
      }

      out.close();
      socket.close()
    }
    catch {
      case e: SocketException =>
        () // avoid stack trace when stopping a client with Ctrl-C
      case e: IOException =>
        e.printStackTrace();
    }
  }

}
