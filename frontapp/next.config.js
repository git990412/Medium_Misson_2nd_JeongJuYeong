/** @type {import('next').NextConfig} */
const nextConfig = {
  async rewrites() {
    return [
      {
        source: "/api/v1/:path*",
        destination: `http://localhost:${process.env.NEXT_PUBLIC_BACKEND_PORT}/api/v1/:path*`,
      },
    ];
  },
};

module.exports = nextConfig;
