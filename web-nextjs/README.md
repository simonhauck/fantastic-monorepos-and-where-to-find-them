# NextJs WebFrontend

This is a [Next.js](https://nextjs.org/) project bootstrapped
with [`create-next-app`](https://github.com/vercel/next.js/tree/canary/packages/create-next-app).

## Running the application

Before you run the application execute `gradlew :web-nextjs:prepareEnv`. This will install all required node modules as
well as generate the server api client.
Execute this command every time the API code was changed.

Run the development server:

```bash
npm run dev
# or
yarn dev
# or
pnpm dev
# or
bun dev
```

## Formatting

This project uses prettier and eslint.

You can run `gradlew :web-nextjs:format` command to run eslint and prettier with the fix option.

To check that your format is correctly applied use `gradlew :web-nextjs:checkFormat`

## Test

The command `gradlew :server:check` runs all tests (currently only esLint).
