var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var createError = require('http-errors');
var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
var roomRouter = require('./routes/room');

var app = express();

// var { io } = require('./bin/www');

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

// Routes
app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/room', roomRouter);

app.use(express.json());

app.post('/api/receive-message', (req, res) => {
  const message = req.body;
  
  // 메시지를 수신한 후 소켓을 통해 클라이언트로 전달
  app.locals.io.emit('chat message', {
    user: message.user,
    message: message.message
  });

  res.status(200).send('Message received');
});

app.post('/api/one-to-one', (req, res) => {
  const data = req.body;
  const receiver = data.receiver;
  const user = data.user;
  const message = data.message;
  console.log(data);
  console.log(app.locals.users);

  if (app.locals.users[receiver]) {
      // receiver의 socket.id로 해당 사용자에게만 메시지 전송
      app.locals.io.to(app.locals.users[receiver]).emit('chat message', { user, message });
      app.locals.io.to(app.locals.users[user]).emit('chat message', { user, message });
      res.status(200).send('Message sent to ' + receiver);
  } else {
      res.status(404).send('Receiver not connected');
  }
});

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;