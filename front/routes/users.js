var express = require('express');
const session = require('express-session');
const axios = require('axios');
var router = express.Router();


//회원가입 페이지 이동
router.get('/join', (req, res) => {
  res.render('join');
})

//아이디 중복검사
router.post('/idchk', async (req, res) => {
  const {username} = req.body; // 클라이언트에서 보낸 username을 받아옴
  try {
    const response = await axios.post('http://localhost:8080/user/idchk', {username: username});
    // 받아온 응답에서 boolean 값을 확인하고 반환
    if (response.data) {
      // 아이디가 중복되는 경우
      res.json({exists: true});
    } else {
      // 아이디가 중복되지 않는 경우
      res.json({exists: false});
    }
  } catch (error) {
    console.error('Error during ID check:', error);
    res.status(500).send('서버 오류');
  }
});

//회원가입 프로세스
router.post('/join', async (req, res) => {
  const {username, password} = req.body;
  const axiosResponse = await axios.post('http://localhost:8080/user/join',
      {
        username: username
        , password: password
      })
  if (axiosResponse.data !== null) {
    //req.session.username = username; // 세션에 사용자명 저장
    res.redirect('/');
  } else {
    res.send("회원가입 실패");
  }
})

// 로그인 프로세스
router.post('/login', async (req, res) => {
  const {username, password} = req.body;
  const axiosResponse = await axios.post('http://localhost:8080/user/login',
      {
        username: username
        , password: password
      });
  if (axiosResponse.data !== null) {
    req.session.username = axiosResponse.data;
    res.redirect('/chat');
  } else {
    res.redirect('/');
  }
})

//////////////////////////////////////////////////////////////////////////////////////////////

module.exports = router;
