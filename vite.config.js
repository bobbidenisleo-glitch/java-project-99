import { defineConfig } from 'vite';

export default defineConfig({
  root: 'src/main/resources/static/frontend',
  server: {
    proxy: {
      '/api': 'http://localhost:8080'
    }
  }
});
