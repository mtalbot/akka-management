/*
 * Copyright (C) 2017-2018 Lightbend Inc. <http://www.lightbend.com>
 */

package akka.management.http

import akka.actor.ActorSystem
import akka.http.scaladsl.server.directives.Credentials
import akka.management.AkkaManagement

import scala.concurrent.Future

object CompileOnly {
  val system: ActorSystem = null
  implicit val ec = system.dispatcher
  //#basic-auth
  def myUserPassAuthenticator(credentials: Credentials): Future[Option[String]] =
    credentials match {
      case p @ Credentials.Provided(id) ⇒
        Future {
          // potentially
          if (p.verify("p4ssw0rd")) Some(id)
          else None
        }
      case _ ⇒ Future.successful(None)
    }
  // ...
  val management = AkkaManagement(system)
  management.setAsyncAuthenticator(myUserPassAuthenticator)
  management.start()
  //#basic-auth

  object stopping {
    //#stopping
    val management = AkkaManagement(system)
    management.start()
    //...
    val bindingFuture = management.stop()
    bindingFuture.onComplete { _ =>
      println("It's stopped")
    }
    //#stopping
  }
}
