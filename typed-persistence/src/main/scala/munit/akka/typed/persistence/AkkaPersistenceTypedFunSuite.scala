package munit.akka.typed.persistence

import munit.FunSuite
import com.typesafe.config.Config
import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.testkit.typed.TestKitSettings
import akka.persistence.testkit.scaladsl.EventSourcedBehaviorTestKit
import akka.actor.typed.Behavior
import akka.persistence.testkit.scaladsl.PersistenceTestKit
import akka.persistence.testkit.PersistenceTestKitPlugin
import akka.persistence.testkit.scaladsl.SnapshotTestKit
import akka.persistence.testkit.PersistenceTestKitSnapshotPlugin

trait AkkaPersistenceTypedFunSuite extends FunSuite {
  private val esBehaviorConfig = EventSourcedBehaviorTestKit.config

  /** Provides an Akka Typed `ActorTestKit` preconfigured for in-memory Akka Persistence tests. */
  val akkaTestKit = FunFixture[ActorTestKit](
    _ => ActorTestKit(esBehaviorConfig),
    tk => tk.shutdownTestKit()
  )

  /** Provides an Akka Typed `ActorTestKit` preconfigured for Akka Persistence with the name of your choosing.
    */
  def namedAkkaTestKit(name: String) = FunFixture[ActorTestKit](
    _ => ActorTestKit(name, esBehaviorConfig),
    tk => tk.shutdownTestKit()
  )

  /** Provides an Akka Typed `ActorTestKit` preconfigured for Akka Persistence as well as the config you specify.
    */
  def configuredAkkaTestKit(config: Config) = FunFixture[ActorTestKit](
    _ => ActorTestKit(config.withFallback(esBehaviorConfig)),
    tk => tk.shutdownTestKit()
  )

  /** Provides an Akka Typed `ActorTestKit` preconfigured for Akka Persistence with the name and config you specify.
    */
  def namedConfiguredAkkaTestKit(name: String, config: Config) = FunFixture[ActorTestKit](
    _ => ActorTestKit(name, esBehaviorConfig.withFallback(config)),
    tk => tk.shutdownTestKit()
  )

  /** Provides an Akka Typed `ActorTestKit` preconfigured for Akka Persistence with the name, config, and settings you
    * specify.
    */
  def customAkkaTestKit(name: String, config: Config, settings: TestKitSettings) =
    FunFixture[ActorTestKit](
      _ => ActorTestKit(name, config.withFallback(esBehaviorConfig), settings),
      tk => tk.shutdownTestKit()
    )

  /** Provides an `ActorTestKit` as well as a configured `EventSourcedBehaviorTestKit` for your behavior. */
  def eventSourcedTestKit[Command, Event, State](behavior: Behavior[Command]) =
    FunFixture[(ActorTestKit, EventSourcedBehaviorTestKit[Command, Event, State])](
      { _ =>
        val tk = ActorTestKit(esBehaviorConfig)
        tk -> EventSourcedBehaviorTestKit(tk.system, behavior)
      },
      { case (tk, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides a named `ActorTestKit` as well as a configured `EventSourcedBehaviorTestKit` for your behavior. */
  def eventSourcedTestKit[Command, Event, State](name: String, behavior: Behavior[Command]) =
    FunFixture[(ActorTestKit, EventSourcedBehaviorTestKit[Command, Event, State])](
      { _ =>
        val tk = ActorTestKit(name, esBehaviorConfig)
        tk -> EventSourcedBehaviorTestKit(tk.system, behavior)
      },
      { case (tk, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides a named and configured `ActorTestKit` as well as a configured `EventSourcedBehaviorTestKit` for your
    * behavior.
    */
  def eventSourcedTestKit[Command, Event, State](config: Config, behavior: Behavior[Command]) =
    FunFixture[(ActorTestKit, EventSourcedBehaviorTestKit[Command, Event, State])](
      { _ =>
        val tk = ActorTestKit(config.withFallback(esBehaviorConfig))
        tk -> EventSourcedBehaviorTestKit(tk.system, behavior)
      },
      { case (tk, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides a named and configured `ActorTestKit` as well as a configured `EventSourcedBehaviorTestKit` for your
    * behavior.
    */
  def eventSourcedTestKit[Command, Event, State](name: String, config: Config, behavior: Behavior[Command]) =
    FunFixture[(ActorTestKit, EventSourcedBehaviorTestKit[Command, Event, State])](
      { _ =>
        val tk = ActorTestKit(name, config.withFallback(esBehaviorConfig))
        tk -> EventSourcedBehaviorTestKit(tk.system, behavior)
      },
      { case (tk, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides a named and configured `ActorTestKit` using custom settings, as well as a configured
    * `EventSourcedBehaviorTestKit` for your behavior.
    */
  def eventSourcedTestKit[Command, Event, State](
      name: String,
      config: Config,
      settings: TestKitSettings,
      behavior: Behavior[Command]
  ) =
    FunFixture[(ActorTestKit, EventSourcedBehaviorTestKit[Command, Event, State])](
      { _ =>
        val tk = ActorTestKit(name, config.withFallback(esBehaviorConfig), settings)
        tk -> EventSourcedBehaviorTestKit(tk.system, behavior)
      },
      { case (tk, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides an `ActorTestKit` as well as a `PersistenceTestKit` for testing Akka Persistence. */
  val persistenceTestKit =
    FunFixture[(ActorTestKit, PersistenceTestKit)](
      { _ =>
        val tk = ActorTestKit(PersistenceTestKitPlugin.config)
        tk -> PersistenceTestKit(tk.system)
      },
      { case (tk, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides an `ActorTestKit` as well as a `PersistenceTestKit` for testing Akka Persistence. */
  def persistenceTestKit(name: String) =
    FunFixture[(ActorTestKit, PersistenceTestKit)](
      { _ =>
        val tk = ActorTestKit(name, PersistenceTestKitPlugin.config)
        tk -> PersistenceTestKit(tk.system)
      },
      { case (tk, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides an `ActorTestKit` as well as a `PersistenceTestKit` for testing Akka Persistence. */
  def persistenceTestKit(config: Config) =
    FunFixture[(ActorTestKit, PersistenceTestKit)](
      { _ =>
        val tk = ActorTestKit(config.withFallback(PersistenceTestKitPlugin.config))
        tk -> PersistenceTestKit(tk.system)
      },
      { case (tk, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides an `ActorTestKit` as well as a `PersistenceTestKit` for testing Akka Persistence. */
  def persistenceTestKit(name: String, config: Config) =
    FunFixture[(ActorTestKit, PersistenceTestKit)](
      { _ =>
        val tk = ActorTestKit(name, config.withFallback(PersistenceTestKitPlugin.config))
        tk -> PersistenceTestKit(tk.system)
      },
      { case (tk, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides an `ActorTestKit` as well as a `PersistenceTestKit` for testing Akka Persistence. */
  def persistenceTestKit(name: String, config: Config, settings: TestKitSettings) =
    FunFixture[(ActorTestKit, PersistenceTestKit)](
      { _ =>
        val tk = ActorTestKit(name, config.withFallback(PersistenceTestKitPlugin.config), settings)
        tk -> PersistenceTestKit(tk.system)
      },
      { case (tk, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides an `ActorTestKit` as well as a `SnapshotTestKit` for testing Akka Persistence snapshots. */
  val snapshotTestKit =
    FunFixture[(ActorTestKit, SnapshotTestKit)](
      { _ =>
        val tk = ActorTestKit(PersistenceTestKitSnapshotPlugin.config)
        tk -> SnapshotTestKit(tk.system)
      },
      { case (tk, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides an `ActorTestKit` as well as a `SnapshotTestKit` for testing Akka Persistence snapshots. */
  def snapshotTestKit(name: String) =
    FunFixture[(ActorTestKit, SnapshotTestKit)](
      { _ =>
        val tk = ActorTestKit(name, PersistenceTestKitSnapshotPlugin.config)
        tk -> SnapshotTestKit(tk.system)
      },
      { case (tk, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides an `ActorTestKit` as well as a `SnapshotTestKit` for testing Akka Persistence snapshots. */
  def snapshotTestKit(config: Config) =
    FunFixture[(ActorTestKit, SnapshotTestKit)](
      { _ =>
        val tk = ActorTestKit(config.withFallback(PersistenceTestKitSnapshotPlugin.config))
        tk -> SnapshotTestKit(tk.system)
      },
      { case (tk, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides an `ActorTestKit` as well as a `SnapshotTestKit` for testing Akka Persistence snapshots. */
  def snapshotTestKit(name: String, config: Config) =
    FunFixture[(ActorTestKit, SnapshotTestKit)](
      { _ =>
        val tk = ActorTestKit(name, config.withFallback(PersistenceTestKitSnapshotPlugin.config))
        tk -> SnapshotTestKit(tk.system)
      },
      { case (tk, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides an `ActorTestKit` as well as a `SnapshotTestKit` for testing Akka Persistence snapshots. */
  def snapshotTestKit(name: String, config: Config, settings: TestKitSettings) =
    FunFixture[(ActorTestKit, SnapshotTestKit)](
      { _ =>
        val tk = ActorTestKit(name, config.withFallback(PersistenceTestKitSnapshotPlugin.config), settings)
        tk -> SnapshotTestKit(tk.system)
      },
      { case (tk, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides one of every test kit available for Akka Persistence. */
  def allPersistenceTestKits[Command, Event, State](
      behavior: Behavior[Command]
  ) =
    FunFixture[(ActorTestKit, EventSourcedBehaviorTestKit[Command, Event, State], PersistenceTestKit, SnapshotTestKit)](
      { _ =>
        val tk = ActorTestKit(esBehaviorConfig)
        (
          tk,
          EventSourcedBehaviorTestKit(tk.system, behavior),
          PersistenceTestKit(tk.system),
          SnapshotTestKit(tk.system)
        )
      },
      { case (tk, _, _, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides one of every test kit available for Akka Persistence. */
  def allPersistenceTestKits[Command, Event, State](
      name: String,
      behavior: Behavior[Command]
  ) =
    FunFixture[(ActorTestKit, EventSourcedBehaviorTestKit[Command, Event, State], PersistenceTestKit, SnapshotTestKit)](
      { _ =>
        val tk = ActorTestKit(name, esBehaviorConfig)
        (
          tk,
          EventSourcedBehaviorTestKit(tk.system, behavior),
          PersistenceTestKit(tk.system),
          SnapshotTestKit(tk.system)
        )
      },
      { case (tk, _, _, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides one of every test kit available for Akka Persistence. */
  def allPersistenceTestKits[Command, Event, State](
      config: Config,
      behavior: Behavior[Command]
  ) =
    FunFixture[(ActorTestKit, EventSourcedBehaviorTestKit[Command, Event, State], PersistenceTestKit, SnapshotTestKit)](
      { _ =>
        val tk = ActorTestKit(config.withFallback(esBehaviorConfig))
        (
          tk,
          EventSourcedBehaviorTestKit(tk.system, behavior),
          PersistenceTestKit(tk.system),
          SnapshotTestKit(tk.system)
        )
      },
      { case (tk, _, _, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides one of every test kit available for Akka Persistence. */
  def allPersistenceTestKits[Command, Event, State](
      name: String,
      config: Config,
      behavior: Behavior[Command]
  ) =
    FunFixture[(ActorTestKit, EventSourcedBehaviorTestKit[Command, Event, State], PersistenceTestKit, SnapshotTestKit)](
      { _ =>
        val tk = ActorTestKit(name, config.withFallback(esBehaviorConfig))
        (
          tk,
          EventSourcedBehaviorTestKit(tk.system, behavior),
          PersistenceTestKit(tk.system),
          SnapshotTestKit(tk.system)
        )
      },
      { case (tk, _, _, _) =>
        tk.shutdownTestKit()
      }
    )

  /** Provides one of every test kit available for Akka Persistence. */
  def allPersistenceTestKits[Command, Event, State](
      name: String,
      config: Config,
      settings: TestKitSettings,
      behavior: Behavior[Command]
  ) =
    FunFixture[(ActorTestKit, EventSourcedBehaviorTestKit[Command, Event, State], PersistenceTestKit, SnapshotTestKit)](
      { _ =>
        val tk = ActorTestKit(name, config.withFallback(esBehaviorConfig), settings)
        (
          tk,
          EventSourcedBehaviorTestKit(tk.system, behavior),
          PersistenceTestKit(tk.system),
          SnapshotTestKit(tk.system)
        )
      },
      { case (tk, _, _, _) =>
        tk.shutdownTestKit()
      }
    )
}
