var express = require('express');
var router = express.Router();

// 기본 경로 '/'에 대한 GET 요청 처리
router.get('/', function(req, res, next) {
  res.render('index', { title: '채팅방 입장' });  // '채팅방 선택'으로 제목 변경
});

module.exports = router;  // 라우터 객체 내보내기
