module.exports = {
  devServer: {
      port: 1888,
      // TODO: which ip address should be used？
      proxy: {
          '/api': {
              target: 'http://cc-gateway-service:9999',
              changeOrigin: true,
              ws: true,
              pathRewrite: {
                '^/api': ''
              }
          },

      },
      disableHostCheck: true,
  },

  transpileDependencies: ['vuetify'],

  pluginOptions: {
    i18n: {
      locale: 'en',
      fallbackLocale: 'en',
      localeDir: 'locales',
      enableInSFC: false,
    },
  },
}
