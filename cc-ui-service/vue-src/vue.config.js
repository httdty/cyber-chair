module.exports = {
  devServer: {
      port: 1888,
      proxy: {
          '/api': {
              target: 'http://192.168.31.214:9999',
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
