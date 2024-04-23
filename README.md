## How to start development

[![Master](https://github.com/simonhauck/fantastic-monorepos-and-where-to-find-them/actions/workflows/on-master-push.yml/badge.svg)](https://github.com/simonhauck/fantastic-monorepos-and-where-to-find-them/actions/workflows/on-master-push.yml)

### Other Module documentation

- [The server documentation](server/README.md)
- [The NextJs documentation](web-nextjs/README.md)

### Important commands and development setup

Run the following command before starting frontend development:

```shell
./gradlew prepareEnv
```

After changing the backend API run the following command:

```shell
./gradlew generateOpenApiDocs
```

Run this command to format frontend and backend code

```shell
./gradlew format
```

Run this command to check the format in frontend and backend code

```shell
./gradlew checkFormat
```

To assemble all artifacts run

```shell
./gradlew assemble
```

To run all tests/checks run

```shell
./gradlew check
```