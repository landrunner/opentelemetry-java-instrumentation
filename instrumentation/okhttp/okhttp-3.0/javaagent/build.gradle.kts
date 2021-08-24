plugins {
  id("otel.javaagent-instrumentation")
}

muzzle {
  pass {
    group.set("com.squareup.okhttp3")
    module.set("okhttp")
    versions.set("[3.0,)")
    assertInverse.set(true)
  }
}

/*
Note: there is a bit of dependency exclusion magic going on.
We have to exclude all transitive dependencies on 'okhttp' because we would like to force
specific version. We cannot use . Unfortunately we cannot just force version on
a dependency because this doesn't work well with version ranges - it doesn't select latest.
And we cannot use configurations to exclude this dependency from everywhere in one go
because it looks like exclusions using configurations excludes dependency even if it explicit
not transitive.
 */
dependencies {
  implementation(project(":instrumentation:okhttp:okhttp-3.0:library"))

  library("com.squareup.okhttp3:okhttp:3.0.0")

  testImplementation(project(":instrumentation:okhttp:okhttp-3.0:testing"))
}

tasks.withType<Test>().configureEach {
  jvmArgs("-Dio.opentelemetry.javaagent.shaded.io.opentelemetry.context.enableStrictContext=false")
}
