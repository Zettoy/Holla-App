'use strict';

var _mongoose = require('mongoose');

var _mongoose2 = _interopRequireDefault(_mongoose);

var _constants = require('./constants');

var _constants2 = _interopRequireDefault(_constants);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

// tutorial : https://www.youtube.com/watch?v=PuY3w1VY0z8
// remove warning with mongoose promise
console.log(_constants2.default);
_mongoose2.default.Promise = global.Promise;
try {
  _mongoose2.default.connect(_constants2.default.MONGO_URL);
} catch (err) {
  _mongoose2.default.createConnection(_constants2.default.MONGO_URL);
}

_mongoose2.default.connection.once('open', function () {
  return console.log("MongoDB running");
}).on('error', function (e) {
  throw e;
});