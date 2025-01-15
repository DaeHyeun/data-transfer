var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var createError = require('http-errors');
var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
var roomRouter = require('./routes/room');
const fs = require('fs');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');

app.use(logger('dev'));

app.use(express.json({ limit: '50mb' })); // JSON 요청의 크기 제한을 50MB로 설정
app.use(express.urlencoded({ limit: '50mb', extended: true })); // URL-encoded 요청의 크기 제한 설정
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

// Routes
app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/room', roomRouter);

// 업로드된 파일을 저장할 경로
const uploadDir = path.join(__dirname, 'uploads');

// 업로드 폴더가 없으면 생성
if (!fs.existsSync(uploadDir)) {
  fs.mkdirSync(uploadDir);
}

app.post('/api/receive-message', (req, res) => {
  const data = req.body;

  // 파일 처리
  let fileData = {};

  if (data.file) {
    const fileType = data.filetype; // 예: 'image/png', 'video/mp4' 등
    const base64Data = data.file; // Base64로 인코딩된 파일 데이터
    const fileName = data.fileName; // 파일 이름
    if (fileType.startsWith('image') || fileType.startsWith('video')) {
      fileData = {
        type: fileType, // 이미지/비디오 파일 타입
        url: base64Data // Base64로 인코딩된 이미지/비디오 URL
      };
      
      // 소켓을 통해 메시지와 함께 URL 전달
      app.locals.io.to(data.room).emit('chat message', {
        user: data.user,
        room: data.room,
        message: data.message,
        file: fileData,
        filename: fileName,
      });

      res.status(200).send('Message received');
      
    } else{
        // 파일을 저장할 경로 설정
        const filePath = path.join(uploadDir, fileName);
        
        // Base64 데이터를 Buffer로 변환하여 파일로 저장
        const buffer = Buffer.from(base64Data, 'base64');
        
        fs.writeFile(filePath, buffer, (err) => {
          if (err) {
            return res.status(500).send('Error saving file');
          }
          
          // 파일이 성공적으로 저장되었으면, 클라이언트에 파일 URL 제공
          fileData = {
          type: fileType,
          url: `http://localhost:3000/uploads/${fileName}`, // 파일 다운로드 URL
        };
        
        // 소켓을 통해 메시지와 함께 URL 전달
        app.locals.io.to(data.room).emit('chat message', {
          user: data.user,
          room: data.room,
          message: data.message,
          file: fileData,
          filename: fileName,
        });
        
        res.status(200).send('Message received');
      });
    }
  } else {
    res.status(400).send('No file data');
  }
});


// 업로드된 파일에 대한 정적 경로 제공
app.use('/uploads', express.static(uploadDir));

app.post('/api/one-to-one', (req, res) => {
  const data = req.body;
  const receiver = data.receiver;
  const user = data.user;
  const message = data.message;
  const room = data.room;

  console.log(data);
  console.log(app.locals.users);

  if (app.locals.users[room]) {
    // 사용자 배열에서 receiver와 user 객체 찾기
    const receiverSocket = app.locals.users[room].find(u => u.username === receiver);
    const userSocket = app.locals.users[room].find(u => u.username === user);
  
    if (receiverSocket && userSocket) {
      // 해당 소켓 ID로 메시지 전송
      app.locals.io.to(receiverSocket.id).emit('chat message', { user, message });
      app.locals.io.to(userSocket.id).emit('chat message', { user, message });
      res.status(200).send('Message sent to ' + receiver);
    } else {
      res.status(404).send('Receiver or user not connected');
    }
  } else {
    res.status(404).send('Room not found');
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