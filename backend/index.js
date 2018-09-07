import "@babel/polyfill";
import express from 'express';
import constants from './config/constants';
import './config/database';
import middlewareConfig from './config/middleware';
import apiRoutes from './modules';


const app = express();

middlewareConfig(app);

app.get('/', (req, res) => {
  //res.send('Hello World');
  return res.json({
    'title':'OI m8 this is just a test post calm down',
    'text':'sample textd,fvdsklvjdvkldjvdklvjxcklvxcjkl',
    'latitude':-33.912383,
    'longitude': 151.223480,
  });
})
apiRoutes(app);

app.listen(constants.PORT, () => {
  console.log('Server running on ', process.env.NODE_ENV);
});
