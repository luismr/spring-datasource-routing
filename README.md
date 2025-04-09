# Spring DataSource Routing 🔄

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![SpringBoot](https://img.shields.io/badge/SpringBoot-3.4.4-green.svg)
![JUnit](https://img.shields.io/badge/JUnit-5-red.svg)
![JaCoCo](https://img.shields.io/badge/JaCoCo-0.8.13-blue.svg)

Spring DataSource Routing is a lightweight framework for implementing read-write splitting pattern in Java Spring Boot applications, powered by Spring's AbstractRoutingDataSource.

## Features

* Read-Write splitting pattern implementation
* Dynamic routing between reader and writer databases
* Health check monitoring for reader/writer availability
* Spring Boot auto-configuration
* AOP-based transaction routing
* Easy integration with Spring Boot applications
* Comprehensive test coverage
* GitHub Actions CI/CD pipeline

## Build Status

![Java CI with Maven](https://github.com/luismr/spring-datasource-routing/workflows/Java%20CI%20with%20Maven/badge.svg)
![Coverage](https://img.shields.io/badge/Coverage-100%25-brightgreen.svg)
![Branches](https://img.shields.io/badge/Branches-100%25-brightgreen.svg)

## GitHub Actions Setup

To enable automatic updates of code coverage badges, you need to configure GitHub Actions permissions:

1. Go to your repository settings
2. Navigate to "Actions" → "General"
3. Under "Workflow permissions", select:
   * "Read and write permissions"
   * Check "Allow GitHub Actions to create and approve pull requests"
4. Save the changes

## Getting Started

### Prerequisites

* Java 21
* Maven 3.6+
* Spring Boot 3.4.4+

### Installation

#### Using Git

```bash
git clone git@github.com:luismr/spring-datasource-routing.git
```

#### Using Maven

1. First, configure your GitHub token in `~/.m2/settings.xml`:

```xml
<settings>
  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_GITHUB_TOKEN</password>
    </server>
  </servers>
</settings>
```

2. Add the GitHub Packages repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <name>Spring DataSource Routing GitHub Packages</name>
        <url>https://maven.pkg.github.com/luismr/spring-datasource-routing</url>
    </repository>
</repositories>
```

3. Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>dev.luismachadoreis.blueprint</groupId>
    <artifactId>spring-datasource-routing</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Gradle

1. First, configure your GitHub token in `~/.gradle/gradle.properties`:

```properties
gpr.user=YOUR_GITHUB_USERNAME
gpr.key=YOUR_GITHUB_TOKEN
```

2. Add the GitHub Packages repository to your `build.gradle`:

```groovy
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/luismr/spring-datasource-routing")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_USERNAME")
            password = project.findProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}
```

3. Add the dependency to your `build.gradle`:

```groovy
implementation 'dev.luismachadoreis.blueprint:spring-datasource-routing:1.0.0-SNAPSHOT'
```

## Usage

### Configuration

Just add the following configuration to your `application.yml`:

```yaml
spring:
  datasource:
    writer:
      jdbcUrl: jdbc:postgresql://localhost:5432/flighttracker
      username: flighttracker
      password: flighttracker
      driverClassName: org.postgresql.Driver
      type: com.zaxxer.hikari.HikariDataSource
    reader:
      jdbcUrl: jdbc:postgresql://localhost:5433/flighttracker
      username: flighttracker
      password: flighttracker
      driverClassName: org.postgresql.Driver
      type: com.zaxxer.hikari.HikariDataSource

app:
  read-write-routing:
    enabled: true     # set to true to enable routing
```

That's it! All beans configuration in Spring Boot is done automatically. No additional Java configuration is needed.

### Using Transactions

The routing is automatically handled based on your `@Transactional` annotations:

```java
@Service
public class UserService {
    @Transactional(readOnly = true)
    public User findUser(Long id) {
        // This will use the reader datasource
        return repository.findById(id);
    }

    @Transactional
    public User createUser(User user) {
        // This will use the writer datasource
        return repository.save(user);
    }
}
```

## Development

### Prerequisites

* Java 21 or later
* Maven 3.8 or later
* Git

### Building

```bash
git clone https://github.com/luismr/spring-datasource-routing.git
cd spring-datasource-routing
mvn clean install
```

### Testing

```bash
mvn test
```

### Code Coverage

Code coverage reports are generated automatically during the build process. You can find the reports in the `target/site/jacoco` directory.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Author

* **Luis Machado Reis** - [luismr](https://github.com/luismr)

## Acknowledgments

* Spring Boot team for the amazing framework
* All contributors and users of this project 