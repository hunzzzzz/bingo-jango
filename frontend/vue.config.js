// const { defineConfig } = require('@vue/cli-service')
// module.exports = defineConfig({
//   transpileDependencies: true
// })
module.exports = {
  outputDir: "../src/main/resources/static",
  devServer: {
    port: 8081,
    proxy: {
      '/example': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
};
