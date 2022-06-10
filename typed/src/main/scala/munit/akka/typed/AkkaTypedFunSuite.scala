package munit.akka.typed

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import com.typesafe.config.Config
import munit.FunSuite
import akka.actor.testkit.typed.TestKitSettings
import akka.actor.typed.ActorSystem

trait AkkaTypedFunSuite extends FunSuite {

  /** Provides an unconfigured Akka Typed `ActorTestKit`. */
  val akkaTestKit = FunFixture[ActorTestKit](
    _ => ActorTestKit(),
    tk => tk.shutdownTestKit()
  )

  /** Provides an Akka Typed `ActorTestKit` with the name of your choosing. */
  def namedAkkaTestKit(name: String) = FunFixture[ActorTestKit](
    _ => ActorTestKit(name),
    tk => tk.shutdownTestKit()
  )

  /** Provides an Akka Typed `ActorTestKit` with the config you specify. */
  def configuredAkkaTestKit(config: Config) = FunFixture[ActorTestKit](
    _ => ActorTestKit(config),
    tk => tk.shutdownTestKit()
  )

  /** Provides an Akka Typed `ActorTestKit` with the name and config you specify. */
  def namedConfiguredAkkaTestKit(name: String, config: Config) = FunFixture[ActorTestKit](
    _ => ActorTestKit(name, config),
    tk => tk.shutdownTestKit()
  )

  /** Provides an Akka Typed `ActorTestKit` with the name, config, and settings you specify. */
  def customAkkaTestKit(name: String, config: Config, settings: TestKitSettings) =
    FunFixture[ActorTestKit](
      _ => ActorTestKit(name, config, settings),
      tk => tk.shutdownTestKit()
    )

  /** Provides an Akka Typed `ActorTestKit` based on an existing `ActorSystem`. */
  def akkaTestKitForSystem(sys: ActorSystem[_]) = FunFixture[ActorTestKit](
    _ => ActorTestKit(sys),
    tk => tk.shutdownTestKit()
  )
}
