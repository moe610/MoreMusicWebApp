import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: '0.0.0.0', // Allow access from any IP on your network
    port: 5173, // Optional: You can set a different port if needed
  },
  base:"/MoreMusicWebApp"
})
