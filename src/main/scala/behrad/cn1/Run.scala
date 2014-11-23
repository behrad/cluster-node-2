package behrad.cn1

import akka.cluster.Cluster
import akka.kernel.Bootable
import akka.actor._
import com.typesafe.config.ConfigFactory

/**
 * Created by behrad on 9/13/14.
 */

/**
 * Created by behrad on 11/1/14.
 */
class Run extends Bootable {

  var system :ActorSystem = null

  def startup = {
    println( "Starting up Akka system" )
    val config = ConfigFactory.load()
    system = ActorSystem( "akka-cluster-reconnect-test" )
    system.actorOf( Props[ClusterListener] )
  }

  def shutdown = {
    val cluster = Cluster(system)
    println( s"Shutting down Akka system ${cluster.selfAddress}" )
    cluster.down(cluster.selfAddress)
    cluster.leave(cluster.selfAddress)
    system.shutdown()
    Thread.sleep( 2000 )
  }

}

object Run extends App {

  override def main(args: Array[String]): Unit = {
    val run = new Run
    run startup()
    //Thread.sleep( 5000 )
    //run shutdown()
    /*sys.ShutdownHookThread {
      println( "++++++++++++++++++++++++++++++++ Exiting" )
      run shutdown()
    }*/
  }

}
