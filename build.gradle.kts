val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.0"
}

group = "com.martige"
version = "0.5.3"
application {
    mainClass.set("com.martige.ApplicationKt")
}

repositories {
    jcenter()
    mavenCentral()
}



dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-apache:$ktor_version")
    implementation("com.dropbox.core:dropbox-core-sdk:5.0.0")
    implementation("io.ktor:ktor-client-gson:$ktor_version")
    implementation("net.dv8tion:JDA:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("org.jetbrains.exposed:exposed:0.17.14")
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("org.postgresql:postgresql:42.3.1")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
}
