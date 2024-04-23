# Server

The best server in the world ;)

## Running the application

The recommend way is to use the IntelliJ run-configuration type for SpringBoot.
Alternatively you can use the `gradlew :server:bootRun` task.

If you want to specify commands use e.g. use the dev profile
use `gradlew :server:bootRun --args='--spring.profiles.active=dev'`

## Profiles

- No Profile: The server expects tries to connect to a PostgreSQL database.
- dev: This requires an installed docker daemon. The server will start a PostgreSQL (or any other container specified in
  the `compose.yml`)
- h2: The server is using an in memory h2 database.

You can set the profiles vie the `spring.profiles.active` variable.

## Test

The project consist of Unit and IntegrationTests.
You can run the unit test with `gradlew :server:test` and the integrationTest with `gradlew :server:integrationTest`.
The command `gradlew :server:check` runs all tests.

## Formatting

You can run `gradlew :server:format` command to format all Kotlin Code.

To check that your format is correctly applied use  `gradlew :server:checkFormat`

## Updating the API

After you changed the API, you have to update the openApi json in the server-api module. Use the
command `gradlew :server:generateOpenApiDocs`. This will update the json automatically.

## Generating new Liquibase Changelogs

If you want to perform a database change, you can create a new liquibase changelog with `gradlew :server:newChangeLog`.
Check the liquibase documentation for the available commands.

## Docker

The plugin uses [Jib](https://github.com/GoogleContainerTools/jib) to build docker images. You can build an image
locally with `gradlew :server:jibDockerBuild`. For more commands check the Jib documentation
