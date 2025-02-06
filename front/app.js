// app.js
const express = require('express');
const session = require('express-session');
const axios = require('axios');
const app = express();
const multer = require('multer');
const bodyParser = require('body-parser');
const { Blob } = require('buffer'); // Node.js에서 Blob을 사용하려면 buffer 패키지에서 가져와야 합니다.
const path = require('path');
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

// 'C:/Users/HCNC/Desktop/chatFile' 디렉토리에서 파일 제공
// 파일을 제공할 디렉토리 설정
const filesDirectory = 'C:/Users/HCNC/Desktop/chatFile';
app.use('/files', express.static(filesDirectory));


// 드래그된 데이터를 받는 엔드포인트
// Multer 설정 (파일 저장 방식 설정)
const storage = multer.memoryStorage();  // 파일을 메모리에 저장
const upload = multer({ storage: storage });  // 파일 업로드 미들웨어

// JSON 파싱을 위한 미들웨어
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// 드래그된 데이터를 받는 엔드포인트
app.post('/upload', upload.single('file'), async (req, res) => {
    const { tosend , sender} = req.body;
    const file = req.file; // multer로 업로드된 파일을 가져옴
    console.log(file.originalname);

    // Buffer를 Blob으로 변환
    const fileBlob = new Blob([file.buffer], { type: file.mimetype });
    // FormData 객체 생성
    const formData = new FormData();
    formData.append('tosend', tosend);  // tosend는 JSON 문자열로 보내기
    formData.append('file', fileBlob, file.originalname);  // 파일 데이터는 buffer로 처리
    formData.append("sender", sender );

    try {
        // fetch 요청 보내기
        const response = await fetch('http://localhost:8080/message/file', {
            method: 'POST',
            body: formData,  // FormData로 body 전송
        });

        // 서버 응답을 JSON으로 파싱
        const data = await response.json();

        // 응답을 클라이언트에 전달
        res.status(200).json(data);
    } catch (error) {
        // 에러 처리
        console.error(error);
        res.status(500).json({message: '서버 에러'});
    }
});

//파일 다운로드
app.post('/fileDownload', async (req,res)=>{
    const {fileName} = req.body;
    console.log("=======================");
    console.log(fileName);
    console.log("=======================");
    try {
        const response = await fetch('http://localhost:8080/message/downLoad', {
            method: 'post',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ fileName })
        });

        if (response.ok) {
            // .arrayBuffer()로 데이터를 받아온 후, Buffer로 변환
            const arrayBuffer = await response.arrayBuffer();
            const buffer = Buffer.from(arrayBuffer); // Buffer로 변환

            // 파일 다운로드를 위한 응답 헤더 설정
            res.setHeader('Content-Disposition', `attachment; filename=${fileName}`);
            res.setHeader('Content-Type', 'application/octet-stream');
            res.send(buffer); // 클라이언트로 파일 전송
        } else {
            res.status(response.status).send("파일 다운로드 실패");
        }
    } catch (error) {
        console.error(error);
        res.status(500).send("서버 오류");
    }
})

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
//chat 라우트
var chatRouter = require('./routes/chat');
const {post} = require("axios");
const {response} = require("express");
app.use('/chat', chatRouter);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

module.exports = app;  // app 객체를 내보냅니다.
