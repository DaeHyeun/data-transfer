// app.js
const express = require('express');
const session = require('express-session');
const axios = require('axios');
const app = express();

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// 미들웨어 설정 (폼 데이터 처리)
app.use(express.urlencoded({ extended: true }));

// express-session 설정
app.use(session({
  secret: 'your-secret-key',  // 세션을 암호화할 비밀 키
  resave: false,              // 요청마다 세션을 다시 저장할지 여부
  saveUninitialized: true,    // 세션을 초기화되지 않은 채로 저장할지 여부
  cookie: { secure: false }   // 보안을 위한 옵션 (HTTPS를 사용할 경우 true로 설정)
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
app.get('/', (req, res) => {
  res.render('index');
});

//회원가입 페이지 이동
app.get('/join', (req,res)=>{
    res.render('join');
})

//아이디 중복검사
app.post('/idchk', async (req, res) => {
    const { username } = req.body; // 클라이언트에서 보낸 username을 받아옴
    try {
        const response = await axios.post('http://localhost:8080/user/idchk', { username :username});
        // 받아온 응답에서 boolean 값을 확인하고 반환
        if (response.data) {
            // 아이디가 중복되는 경우
            res.json({ exists: true });
        } else {
            // 아이디가 중복되지 않는 경우
            res.json({ exists: false });
        }
    } catch (error) {
        console.error('Error during ID check:', error);
        res.status(500).send('서버 오류');
    }
});

//회원가입 프로세스
app.post('/join',async (req,res)=>{
    const {username , password} = req.body;
    const axiosResponse = await axios.post('http://localhost:8080/user/join',
        {
            username : username
            , password : password
        })
    if (axiosResponse.data !==null){
        req.session.username = username; // 세션에 사용자명 저장
        res.redirect('/');
    }else {
        res.send("회원가입 실패");
    }
})

// 로그인 프로세스





/*

// 사용자 아이디 생성 POST 라우트
app.post('/chat', async (req, res) => {
  const username = req.body.username; // 폼에서 전송한 데이터의 이름은 'username'
  console.log(username);

 try {// 아이디 스프링 부트에 전달
         // Spring 서버에 사용자 ID 확인 요청
   const response = await axios.post('http://localhost:8080/user/idchk', { username: username });
         // idchk 응답이 "success"이면 채팅 페이지로 이동
         if (!response.data) {
             //로그인 성공시 세션에 이름 넣고 chat으로 페이지 이동(get)
             req.session.username = username; // 세션에 사용자명 저장
             res.redirect('/chat');
         } else {
             // 실패 시 처리 (예: 메시지 출력 또는 다른 페이지로 리디렉션)
             res.send('아이디가 유효하지 않습니다.');
         }
     } catch (error) {
         console.error('Error during ID check:', error);
         res.status(500).send('서버 오류');
     }
});
*/



// 채팅 페이지로 이동 (로그인 후 이동)
app.get('/chat', (req, res) => {
  // 로그인된 사용자가 없으면 로그인 페이지로 리다이렉션
  if (!req.session.username) {// express 세션에 아이디 없으면 로그인화면
    return res.redirect('/');
  }
  // 로그인된 사용자명과 함께 채팅 페이지 렌더링
  app.locals.io.emit('message', `<strong>${req.session.username}:</strong> 님이 입장했습니다.`);
  res.render('chat', { username: req.session.username });
});

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

module.exports = app;  // app 객체를 내보냅니다.
