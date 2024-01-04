import type { Metadata } from 'next'
import { Inter } from 'next/font/google'
import { Providers as NextUiProviders } from "./providers";
import './globals.css'
import MediumNavbar from '@/components/MediumNavbar';

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: 'Medium',
  description: 'Best Web',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="ko">
      <body className={inter.className}>
        <NextUiProviders>
          <MediumNavbar />
          <div className="sm:w-3/5 mx-auto">{children}</div>
        </NextUiProviders>
      </body>
    </html>
  )
}
