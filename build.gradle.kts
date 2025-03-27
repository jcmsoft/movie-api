plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.google.protobuf") version "0.9.4"
}

group = "com.movie.app"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	google()
	mavenCentral()
}

kotlin {
	jvmToolchain(21)
}

kotlin.sourceSets.all {
	languageSettings.optIn("kotlin.RequiresOptIn")
}

val grpcVersion = "1.62.2"
val grpcKotlinVersion = "1.4.1"
val protobufVersion = "3.25.3"
val coroutinesVersion = "1.8.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
	implementation("io.grpc:grpc-protobuf:$grpcVersion")
	implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

	runtimeOnly("io.grpc:grpc-netty-shaded:$grpcVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:$protobufVersion"
	}

	plugins {
		create("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
		}
		create("grpckt") {
			artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk8@jar"
		}
	}

	generateProtoTasks {
		all().forEach {
			it.plugins {
				create("grpc")
				create("grpckt")
			}
			it.builtins {
				create("kotlin")
			}
		}
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

