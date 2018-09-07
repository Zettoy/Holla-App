import morgan from 'morgan';

import bodyParser from 'body-parser';
import helmet from 'helmet';

export default (app) => {
  app.use(helmet());
  app.use(bodyParser.json());
  app.use(bodyParser.urlencoded({extended: true }));

  app.use(morgan('dev'));
}