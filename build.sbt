//Dependency versions
val akkaV = "2.6.19"
val munitV = "0.7.29"

val scala212 = "2.12.13"
val scala213 = "2.13.6"
val scala3 = "3.1.1"
val scalaVersions = Seq(scala212, scala213, scala3)

//Dependency Definitions
val akkaActorTestKitTyped = "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaV
val akkaPersistenceTestkit = "com.typesafe.akka" %% "akka-persistence-testkit" % akkaV
val munit = "org.scalameta" %% "munit" % munitV

inThisBuild(
  List(
    developers := List(
      Developer(
        "sloshy",
        "Ryan Peters",
        "me@rpeters.dev",
        url("https://github.com/sloshy")
      )
    ),
    homepage := Some(url("https://github.com/sloshy/munit-akka")),
    licenses := List(
      "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")
    ),
    githubWorkflowJavaVersions := Seq(JavaSpec.temurin("11")),
    githubWorkflowTargetTags ++= Seq("v*"),
    githubWorkflowPublishTargetBranches := Seq(
      RefPredicate.StartsWith(Ref.Tag("v"))
    ),
    githubWorkflowPublish := Seq(
      WorkflowStep.Sbt(
        List("ci-release"),
        env = Map(
          "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
          "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
          "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
          "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}"
        )
      )
    ),
    scalaVersion := scala213,
    crossScalaVersions := Seq(scala3, scala213, scala212)
  )
)

lazy val root = (project in file("."))
  .settings(
    publish / skip := true
  )

lazy val munitAkkaTyped = (project in file("typed"))
  .settings(
    name := "munit-akka-typed",
    libraryDependencies ++= List(
      munit,
      akkaActorTestKitTyped
    )
  )

lazy val munitAkkaTypedPersistence = (project in file("typed-persistence"))
  .settings(
    name := "munit-akka-typed-persistence",
    libraryDependencies ++= List(
      akkaPersistenceTestkit
    )
  )
  .dependsOn(munitAkkaTyped % "compile->compile;test->test")
