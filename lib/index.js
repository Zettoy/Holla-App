'use strict';

require('@babel/polyfill');

var _express = require('express');

var _express2 = _interopRequireDefault(_express);

var _constants = require('./config/constants');

var _constants2 = _interopRequireDefault(_constants);

require('./config/database');

var _middleware = require('./config/middleware');

var _middleware2 = _interopRequireDefault(_middleware);

var _modules = require('./modules');

var _modules2 = _interopRequireDefault(_modules);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var app = (0, _express2.default)();

(0, _middleware2.default)(app);

app.get('/', function (req, res) {
  //res.send('Hello World');
  return res.json({
    'title': 'OI m8 this is just a test post calm down',
    'text': 'sample textd,fvdsklvjdvkldjvdklvjxcklvxcjkl',
    'latitude': -33.912383,
    'longitude': 151.223480
  });
});
(0, _modules2.default)(app);

app.listen(_constants2.default.PORT, function () {
  console.log('Server running on ', process.env.NODE_ENV);
});