// app.js
const express = require('express');
const session = require('express-session');
const axios = require('axios');
const app = express();
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// 미들웨어 설정 (폼 데이터 처리)
app.use(express.urlencoded({extended: true}));

// express-session 설정
app.use(session({
    secret: 'your-secret-key',  // 세션을 암호화할 비밀 키
    resave: false,              // 요청마다 세션을 다시 저장할지 여부
    saveUninitialized: true,    // 세션을 초기화되지 않은 채로 저장할지 여부
    cookie: {secure: false}   // 보안을 위한 옵션 (HTTPS를 사용할 경우 true로 설정)
}));
// 정적 파일 제공 (예: 스타일시트, 이미지 등)
app.use(express.static('public'));
app.use(express.json());

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// EJS 설정
app.set('view engine', 'ejs');
app.set('views', __dirname + '/views');

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// 홈 페이지 라우트
var indexRouter = require('./routes/index');
app.use('/', indexRouter);
//user 라우트
var userRouter = require('./routes/users');
app.use('/user', userRouter);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//app.locals.io.emit('message', `<strong>${req.session.username}:</strong> 님이 입장했습니다.`);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

module.exports = app;  // app 객체를 내보냅니다.
