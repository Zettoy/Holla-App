import mongoose from 'mongoose';
import constants from './constants';

// tutorial : https://www.youtube.com/watch?v=PuY3w1VY0z8
// remove warning with mongoose promise
console.log(constants)
  mongoose.Promise = global.Promise;
try {
  mongoose.connect(constants.MONGO_URL);
} catch (err) {
  mongoose.createConnection(constants.MONGO_URL);
}

mongoose.connection
  .once('open', () => console.log("MongoDB running"))
  .on('error', (e) => {
    throw e
  });