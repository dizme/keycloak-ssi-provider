This is a [Next.js](https://nextjs.org/) project bootstrapped with [`create-next-app`](https://github.com/vercel/next.js/tree/canary/packages/create-next-app).

## Getting Started

Fisrt, set environment variables in a `.env.local` file (copy from `.env.production`):


Then, run the development server:

```bash
npm run dev
# or
yarn dev
# or
pnpm dev
# or
bun dev
```

Open [http://localhost:88](http://localhost:3000) with your browser to see the result.

You can start editing the page by modifying `app/page.tsx`. The page auto-updates as you edit the file.

This project uses [`next/font`](https://nextjs.org/docs/basic-features/font-optimization) to automatically optimize and load Inter, a custom Google Font.

## Using Docker

1. [Install Docker](https://docs.docker.com/get-docker/) on your machine.
2. Set environment variables in the `.env.production` file:
3. Build your container: `docker build -t keycloak-ssi-idp .`.
4. Run your container: `docker run -p 88:3000 keycloak-ssi-idp`.

You can view your images created with `docker images`.

## Learn More

This idp uses [EUDI verifier endpoints](https://verifier-backend.eudiw.dev) to verify the pid credentials.