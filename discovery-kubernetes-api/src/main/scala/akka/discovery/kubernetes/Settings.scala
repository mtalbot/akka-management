/*
 * Copyright (C) 2017-2018 Lightbend Inc. <http://www.lightbend.com>
 */

package akka.discovery.kubernetes

import akka.actor._
import com.typesafe.config.Config

final class Settings(system: ExtendedActorSystem) extends Extension {

  /**
   * Copied from AkkaManagementSettings, which we don't depend on.
   */
  private implicit class HasDefined(val config: Config) {
    def hasDefined(key: String): Boolean =
      config.hasPath(key) &&
      config.getString(key).trim.nonEmpty &&
      config.getString(key) != s"<$key>"

    def optDefinedValue(key: String): Option[String] =
      if (hasDefined(key)) Some(config.getString(key)) else None
  }

  private val kubernetesApi = system.settings.config.getConfig("akka.discovery.kubernetes-api")

  val apiCaPath: String =
    kubernetesApi.getString("api-ca-path")

  val apiTokenPath: String =
    kubernetesApi.getString("api-token-path")

  val apiServiceHostEnvName: String =
    kubernetesApi.getString("api-service-host-env-name")

  val apiServicePortEnvName: String =
    kubernetesApi.getString("api-service-port-env-name")

  val podNamespacePath: String =
    kubernetesApi.getString("pod-namespace-path")

  val podNamespace: Option[String] =
    kubernetesApi.optDefinedValue("pod-namespace")

  val podDomain: String =
    kubernetesApi.getString("pod-domain")

  def podLabelSelector(name: String): String =
    kubernetesApi.getString("pod-label-selector").format(name)

  val podPortName: String =
    kubernetesApi.getString("pod-port-name")
}

object Settings extends ExtensionId[Settings] with ExtensionIdProvider {
  override def get(system: ActorSystem): Settings = super.get(system)

  override def lookup: Settings.type = Settings

  override def createExtension(system: ExtendedActorSystem): Settings = new Settings(system)
}
