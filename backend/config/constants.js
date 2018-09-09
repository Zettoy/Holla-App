const devConfig = {
  MONGO_URL: 'mongodb://localhost/makeanodejsapi-dev',
};

const prodConfig = {
  MONGO_URL: 'mongodb://hollaAdmin1:hollaAdmin1@ds149252.mlab.com:49252/holla',
};

const defaultConfig = {
  PORT: process.env.PORT || 3000,
};

const envConfig = (env) => {
  switch (env) {
    case 'development':
      return devConfig;
    case 'production':
      return prodConfig;
    default:
      return prodConfig;
  }
};

export default {
  ...defaultConfig,
  ...envConfig(process.env.NODE_ENV),
};
