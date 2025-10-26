plugins {
    application
    java
}

group = "no.bankers"
version = "0.1.0"

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(21)) }
}

repositories { mavenCentral() }

dependencies {
    implementation("org.xerial:sqlite-jdbc:3.46.0.1")
    implementation("org.slf4j:slf4j-simple:2.0.13")

    implementation("io.javalin:javalin:6.7.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")

    testImplementation(platform("org.junit:junit-bom:5.11.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass.set("no.setup.bankers.App")
}


tasks.test {
    useJUnitPlatform()
}
