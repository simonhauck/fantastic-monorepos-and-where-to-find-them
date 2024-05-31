# Fantastic MonoRepos and where to find them

[![Master](https://github.com/simonhauck/fantastic-monorepos-and-where-to-find-them/actions/workflows/on-master-push.yml/badge.svg)](https://github.com/simonhauck/fantastic-monorepos-and-where-to-find-them/actions/workflows/on-master-push.yml)

This is the companion project for a talk at the [Karlsruher Entwicklertag 2024](https://www.entwicklertag.de/) and the
talk [Fantastische Monorepos und wo sie zu finden sind](https://www.entwicklertag.de/programm/Fantastische%20Monorepos%20und%20wo%20sie%20zu%20finden%20sind).

## Project setup

This MonoRepo consist of 4 modules

- _build-logic_: Contains convention plugins and custom plugins
- _server-api_: Contains the api definition for the server module and generates the FE client
- _server_: Spring Application that implements the API
- _web-angular_: The Angular Frontend application calling the Server

### Build logic

This module contains any convention plugins and custom plugins. Convention plugins define logic that can be reused with
other modules to simplify their setup. A Kotlin convention plugin could contain for example the target java version,
formatting options and custom repositories.

### Server-Api

The server api contains primarily the Json definition for the API. With the help of the OpenAPIGenerator we can generate
a compatible angular frontend client.

### Server

The server module implements the API and provides your backend business logic. It also bundles the frontend application
and serves it additionally to the RestControllers.

### Web-Angular

This is a standard angular project. It has additionally a `build.gradle.kts` file to allow building and bundling the
frontend with gradle.

## Development

### Building

Build the project with `./gradlew assemble`. To bundle the frontend with the server run `./gradlew assemble -Pprod`.
This enables you to develop on the backend without having to build the frontend application.

To develop the frontend application use your standard Npm tools. Have a look at
the [Web-Angular-README](./web-angular/README.md)

### Testing

You can run the tests with `./gradlew check`. This will run checks against the current project format and execute the
specified tests.

### Formatting

To check if all files are correctly formatted run either `./gradlew check` or `./gradlew checkFormat`. If you want to
format files run `./gradlew format`.