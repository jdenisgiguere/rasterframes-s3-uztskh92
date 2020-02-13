name := "read-with-rasterframes09-uztskh92"

version := "0.1"

scalaVersion := "2.11.12"

resolvers ++= Seq(
    "Locationtech" at "https://repo.locationtech.org/content/repositories/releases/",
  "azavea" at "https://dl.bintray.com/azavea/geotrellis/"
)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.4",
  "org.apache.spark" %% "spark-sql" % "2.4.4",
  "org.locationtech.geotrellis" %% "geotrellis-store" % "3.2.0",
  "org.locationtech.geotrellis" %% "geotrellis-spark" % "3.2.0",
  "org.locationtech.geotrellis" %% "geotrellis-raster" % "3.2.0",
  "org.locationtech.geotrellis" %% "geotrellis-vector" % "3.2.0",
  "org.locationtech.geotrellis" %% "geotrellis-s3" % "3.2.0",
  "org.locationtech.geotrellis" %% "geotrellis-s3-spark" % "3.2.0",
  "org.locationtech.rasterframes" %% "rasterframes" % "0.9.0-RC2",
  "org.locationtech.rasterframes" %% "rasterframes-datasource" % "0.9.0-RC2",
  "org.locationtech.rasterframes" %% "rasterframes-experimental" % "0.9.0-RC2"
)

scalacOptions ++= Seq("-Xmax-classfile-name", "78")

assemblyMergeStrategy in assembly := {
  case PathList("org", "aopalliance", xs @ _*)      => MergeStrategy.last
  case PathList("javax", "inject", xs @ _*)         => MergeStrategy.last
  case PathList("javax", "servlet", xs @ _*)        => MergeStrategy.last
  case PathList("javax", "activation", xs @ _*)     => MergeStrategy.last
  case PathList("javax", "annotation", xs @ _*)     => MergeStrategy.last
  case PathList("javax", "xml", xs @ _*)            => MergeStrategy.last
  case PathList("javax", "ws", xs @ _*)             => MergeStrategy.last
  case PathList("org", "apache", xs @ _*)           => MergeStrategy.last
  case PathList("com", "google", xs @ _*)           => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
  case PathList("com", "codahale", xs @ _*)         => MergeStrategy.last
  case PathList("com", "yammer", xs @ _*)           => MergeStrategy.last
  case PathList("com", "sun", "research", xs @ _*)  => MergeStrategy.last
  case PathList("io", "netty", xs @ _*)             => MergeStrategy.last
  case PathList("com", "amazonaws", xs @ _*)        => MergeStrategy.last
  case PathList("repackaged", "org", "apache", "hadoop", xs @ _*) => MergeStrategy.last
  case "about.html"                                 => MergeStrategy.rename
  case "META-INF/ECLIPSEF.RSA"                      => MergeStrategy.last
  case "META-INF/mailcap"                           => MergeStrategy.last
  case "META-INF/mimetypes.default"                 => MergeStrategy.last
  case "plugin.properties"                          => MergeStrategy.last
  case "log4j.properties"                           => MergeStrategy.last
  case "META-INF/io.netty.versions.properties"      => MergeStrategy.last
  case "git.properties"                             => MergeStrategy.last
  case "mozilla/public-suffix-list.txt"             => MergeStrategy.last
  case "mime.types"                                 => MergeStrategy.last
  case "META-INF/native/libnetty_transport_native_epoll_x86_64.so" => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

dependencyOverrides += "com.fasterxml.jackson.core"   % "jackson-core"              % "2.9.6"
dependencyOverrides += "com.fasterxml.jackson.core"   % "jackson-databind"          % "2.9.6"
dependencyOverrides += "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.9.6"

assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("com.google.common.**" -> "repackaged.com.google.common.@1").inAll,
  ShadeRule.rename("com.google.protobuf.**" -> "repackaged.com.google.protobuf.@1").inAll,
  ShadeRule.rename("org.apache.hadoop.**" -> "repackaged.org.apache.hadoop.@1").inAll
)


