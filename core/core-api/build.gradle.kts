tasks.named<Jar>("bootJar").configure {
    enabled = true
}

tasks.named<Jar>("jar").configure {
    enabled = false
}

dependencies {
    implementation(project(":support:logging"))
    implementation(project(":storage:db-core"))
    implementation(project(":clients:client-example"))

    implementation("org.springframework.boot:spring-boot-starter-webmvc")
}
