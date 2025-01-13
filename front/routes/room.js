var express = require('express');
var router = express.Router();

router.get('/', function(req, res, next) {
  res.render('room', { title: '채팅방' });  // '채팅방 선택'으로 제목 변경
});

module.exports = router;  // 라우터 객체 내보내기
