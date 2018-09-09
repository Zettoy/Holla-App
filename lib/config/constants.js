'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var devConfig = {
  MONGO_URL: 'mongodb://localhost/makeanodejsapi-dev'
};

var prodConfig = {
  MONGO_URL: 'mongodb://hollaAdmin1:hollaAdmin1@ds149252.mlab.com:49252/holla'
};

var defaultConfig = {
  PORT: process.env.port || 3000
};

var envConfig = function envConfig(env) {
  switch (env) {
    case 'development':
      return devConfig;
    case 'production':
      return prodConfig;
    default:
      return prodConfig;
  }
};

exports.default = _extends({}, defaultConfig, envConfig(process.env.NODE_ENV));