// app.js

const express = require('express');
const session = require('express-session');
const bodyParser = require('body-parser');
const app = express();
const axios = require('axios');


// 세션 미들웨어 설정
app.use(session({
  secret: 'your-secret-key',  // 세션 암호화에 사용되는 키
  resave: false,              // 세션이 변경되지 않더라도 저장할지 여부
  saveUninitialized: true     // 초기화되지 않은 세션도 저장할지 여부
}));



// EJS 설정
app.set('view engine', 'ejs');
app.set('views', './views');

let messages = [];  // 저장된 메시지 목록
// 요청 본문 파싱 미들웨어 설정
app.use(express.urlencoded({ extended: false }));  // x-www-form-urlencoded 형태의 데이터 파싱
app.use(express.json());  // JSON 형태의 데이터 파싱
app.use(bodyParser.json()); // JSON 데이터를 파싱

// 기본 라우트 설정
app.get('/', (req, res) => {
  res.render('index')
});

//소켓 테스트
app.get('/test',(req,res) => {
  res.render('test')
})

// 아이디 입력 후 채팅 화면으로 리다이렉트
app.post('/chat', (req, res) => {
  const userId = req.body.userId;
  // 입력된 아이디를 세션에 저장
  req.session.userId = userId;
  sessionId = userId;

  if (userId) {
    res.render('chat', { userId: userId, messages: messages});
  } else {
    res.redirect('/');  // 세션에 아이디가 없으면 다시 아이디 입력 페이지로 리다이렉트
  }
});

// 메시지를 받는 API
app.post('/message', async (req, res) => {
  const { result, sessionId } = req.body;

  console.log("app.js");
  console.log("Received result:", result);
  console.log("Received sessionId:", sessionId);

  try {
    // 세션에 userId 저장
    req.session.userId = sessionId;

    // 외부 서버에서 응답을 받기
    const response = await axios.post('http://localhost:8080/multi/getMessage', {
      sessionId: sessionId
    }, {
      headers: {
        'Content-Type': 'application/json', // 요청이 JSON 데이터임을 명시
      },
      responseType: 'json'  // 응답 형식이 JSON임을 명시
    });

    messages = response.data.messages;  // 얕은 복사
    console.log(messages)
    res.render('chat', { userId: sessionId, messages: messages});
  } catch (error) {
    console.error("Error sending data to external server:", error);
    if (!res.headersSent) {  // 이미 응답을 보낸 경우를 방지
      return res.status(500).send("Error sending data");
    }
  }
});




app.listen(3000, () => {
  console.log('Server is running on http://localhost:3000');
});
