'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _user = require('./users/user.routes');

var _user2 = _interopRequireDefault(_user);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

exports.default = function (app) {
  app.use('/api/v1/users', _user2.default);
};