# Server

The best server in the world ;)

## Running the application

The recommend way is to use the IntelliJ run-configuration type for SpringBoot.
Alternatively you can use the `gradlew :server:bootRun` task.

## Test

The project consist of Unit and IntegrationTests.
You can run the unit test with `gradlew :server:test`.
The command `gradlew :server:check` runs all tests.

## Formatting

You can run `gradlew :server:format` command to format all Kotlin Code.

To check that your format is correctly applied use  `gradlew :server:checkFormat`

## Updating the API

After you changed the API, you have to update the openApi json in the server-api module. Use the
command `gradlew :server:generateOpenApiDocs`. This will update the json automatically.
