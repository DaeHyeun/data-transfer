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
  const { username, password } = req.body;

  try {
    // 로그인 요청
    const loginResult = await axios.post('http://localhost:8080/user/login', {
      username: username,
      password: password
    });

    // 로그인 성공 시
    if (loginResult.data !== null) {
      // 로그인한 사용자의 세션에 username 저장
      req.session.username = loginResult.data; // 여기서 loginResult.data.username을 사용해야 합니다

      // 유저 목록 요청
      const userList = await axios.post('http://localhost:8080/user/getUserList');

      // userList가 null이 아닐 경우, chat 페이지로 리다이렉트하면서 userList 전달
      if (userList.data !== null) {
        // chat 페이지로 렌더링하면서 유저 목록과 username 전달
        res.render('chat', {
          username: req.session.username,  // 로그인한 사용자 정보
          userList: userList.data          // 유저 목록
        });
      } else {
        res.redirect('/'); // 유저 목록이 없으면 로그인 페이지로 리다이렉트
      }
    } else {
      res.redirect('/'); // 로그인 실패 시 로그인 페이지로 리다이렉트
    }
  } catch (error) {
    console.error('Login or user list fetch error:', error);
    res.status(500).send('Server error');
  }
})


//////////////////////////////////////////////////////////////////////////////////////////////

module.exports = router;
