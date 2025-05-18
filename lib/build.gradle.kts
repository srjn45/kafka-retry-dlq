plugins {
    `java-library`
    id("com.diffplug.spotless") version "6.23.0"
    id("com.github.spotbugs") version "6.1.11"
    jacoco
}

group = "com.github.srjn45"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.slf4j)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

spotbugs {
    toolVersion.set("4.8.1")
    ignoreFailures.set(false)
    effort.set(com.github.spotbugs.snom.Effort.MAX)
    reportLevel.set(com.github.spotbugs.snom.Confidence.LOW)  // <-- Here use enum, import required
}

tasks.withType<com.github.spotbugs.snom.SpotBugsTask>().configureEach {
    reports {
        create("xml") {
            required.set(false)
        }
        create("html") {
            required.set(true)
            outputLocation.set(layout.buildDirectory.file("reports/spotbugs/spotbugs.html"))
        }
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
}