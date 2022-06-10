# MUnit Akka
![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/dev.rpeters/munit-akka-typed_2.13?label=latest&server=https%3A%2F%2Foss.sonatype.org)

This is a small, third-party set of fixtures to help get you started using Akka with the MUnit testing library for Scala.

## License
Copyright 2022 Ryan Peters

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## Setup
This library is published for Scala 2.12, 2.13, and 3.1.x. Add this to your build file if you are using sbt:
```
//For Akka Typed
libraryDependencies += "dev.rpeters" %% "munit-akka-typed" % "<current-version>" % Test

//For Akka Persistence
libraryDependencies += "dev.rpeters" %% "munit-akka-typed-persistence" % "<current-version>" % Test
```

Fixtures are provided in the `munit.akka.typed` and `munit.akka.typed.persistence` directories respectively.

## Usage
Fixtures are provided as single-use so you can enable them on a per-test basis.
To use, you can extend the `munit.akka.typed.AkkaTypedFunSuite` trait for Akka Typed support, and the `munit.akka.typed.persistence.AkkaPersistenceTypedFunSuite` trait for Akka Persistence support.

```scala
package com.example

import munit.akka.typed.AkkaTypedFunSuite
import com.example.MyBehavior

class MyTestSuite extends AkkaTypedFunSuite {
  akkaTestKit.test("My Example Test") { testKit =>
    //Spawn typed behaviors with your test kit
    val behavior = testKit.spawn(MyBehavior("some-id"))
    
    //Use the test kit as you normally would
    val probe = testKit.spawnTestProbe[MyBehavior.Reply]()

    behavior ! MyBehavior.Command.Echo("hello world!", replyTo = probe.ref)

    probe.expectMessage(MyBehavior.Reply.EchoReply("hello world!"))
  }
}
```

You can use your `ActorTestKit` just as [the main documentation describes](https://doc.akka.io/docs/akka/current/typed/testing.html).

## Persistence

The Akka Persistence support includes not just an `ActorTestKit` but also three other test kits you can opt into to test event-sourced behaviors as well as persistence and snapshotting.
All of the test kits provided by `AkkaPersistenceTypedFunSuite` are preconfigured using the internal configuration used in the normal Akka Persistence integrations, so you will automatically get in-memory persistence journaling and snapshotting.

```scala
package com.example

import munit.akka.typed.AkkaTypedFunSuite
import com.example.MyBehavior

class MyPersistenceTestSuite extends AkkaPersistenceTypedFunSuite {
  //Contains all of the same fixtures as normal Akka Typed
  akkaTestKit.test("My Example Test") { testKit =>
    ???
  }

  //Create multiple test kits in one fixture, and filter out the ones you need
  allPersistenceTestKits(MyBehavior("test-id")).test("My Persistence Test") { 
    case (actorTestKit, eventSourcedTestKit, persistenceTestKit, snapshotTestKit) =>
      ???
  }

  //Alternatively, just get a single additional test kit you need per-test
  snapshotTestKit.test("My Snapshot Test") { case (actorTestKit, snapshotTestKit) =>
    ???  
  }
}
```

Fixtures for all possible combinations of test kits are not available, but you can easily make per-test instances of the Persistence and Snapshot testkits by looking at the [existing Akka Persistence Typed testing documentation](https://doc.akka.io/docs/akka/current/typed/persistence-testing.html).

## What about suite-wide fixtures?
I do not recommend this style of testing, the kind that often makes use of functions called `beforeAndAfterAll` and similar.
This style of testing creates a linear dependency of all tests in the current suite so that their resulting states must be known for each test.
This increases necessary cognitive load, as well as making it difficult to add new tests or change existing ones without cascading effects or unexpected test bugs.
Instead, I would recommend that you use the included per-test fixtures and simply extract any shared stateful initiation between tests to a function of your own.
Alternatively, you can create your own per-test fixtures by studying how these fixtures are implemented and expanding them to fit your use-case.
