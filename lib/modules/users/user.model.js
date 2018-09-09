'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _mongoose = require('mongoose');

var _mongoose2 = _interopRequireDefault(_mongoose);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var UserSchema = new _mongoose.Schema({
  firstName: {
    type: String,
    required: [true, 'firstName is required!'],
    trim: true
  },
  lastName: {
    type: String,
    required: [true, 'lastName is required!'],
    trim: true,
    minlength: [6, 'Password must be more than 6 chars']
  }
});

exports.default = _mongoose2.default.model('User', UserSchema);