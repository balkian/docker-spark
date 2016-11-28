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

  val vocabulary = List("hello", "world", "SIBD", "rules")
  val others = List(" ", "\n")

  override def run(): Unit = {
    val rand = new Random(System.currentTimeMillis());
    try {
      val out = new DataOutputStream(socket.getOutputStream());

      while (true) {
        val word = vocabulary(rand.nextInt(vocabulary.length));
        val other = others(rand.nextInt(others.length));
        out.writeChars(word)
        out.writeChars(other)
        print(word)
        print(other)
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
