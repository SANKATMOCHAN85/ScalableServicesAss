name := "TicketBookingApplication"

version := "0.1"

scalaVersion := "2.12.8"

resolvers ++= Seq(
  "Maven 1" at "https://repo1.maven.org/maven2/",
  "cdp repo" at "https://repository.cloudera.com/artifactory/cloudera-repos/"
)

libraryDependencies += "com.typesafe.akka" %% "akka-http"                % "10.1.8"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json"     % "10.1.8"
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed"         % "2.5.21"
libraryDependencies += "com.typesafe.akka" %% "akka-stream"              % "2.5.21"
libraryDependencies += "ch.qos.logback"    % "logback-classic"           % "1.2.3"

libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit"        % "10.1.8" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.5.21"     % Test
libraryDependencies += "org.scalatest"     %% "scalatest"                % "3.1.4"         % Test
libraryDependencies += "com.fasterxml.jackson.module"  % "jackson-module-scala_2.12" 	% "2.9.8"
libraryDependencies += "org.apache.httpcomponents" % "httpasyncclient" % "4.1.4"
libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.1.2"
libraryDependencies += "mysql"                         % "mysql-connector-java" 	% "6.0.6"
libraryDependencies += "ch.megard" %% "akka-http-cors" % "0.3.4"



assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case "reference.conf" => MergeStrategy.concat
  case _ => MergeStrategy.first
}

artifact in (Compile, assembly) := {
  val art = (artifact in (Compile, assembly)).value
  art.withClassifier(Some("assembly"))
}
addArtifact(artifact in (Compile, assembly), assembly)