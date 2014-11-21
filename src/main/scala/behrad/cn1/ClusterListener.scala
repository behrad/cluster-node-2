package behrad.cn1

import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.actor.{ActorPath, ActorLogging, Actor}
import com.typesafe.config.ConfigFactory

/**
 * Created by behrad on 10/28/14.
 */

class ClusterListener extends Actor with ActorLogging {

  val config = ConfigFactory.load()

  val cluster = Cluster(context.system)

  override def preStart(): Unit = {
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }

  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case MemberUp(member) => //TODO reachable address !!!
      log.info( s"------------------------\nI see Node Up as ${member.address}" )

    case UnreachableMember(member) =>
      log.info( s"------------------------\nNode unreachable ${member.address}" )

    case MemberRemoved(member, previousStatus) =>
      log.info( s"------------------------\nNode removed ${member.address}" )

    case _: MemberEvent => // ignore
  }
}
