var express = require('express');
const session = require('express-session');
const axios = require('axios');
var router = express.Router();



// 클라이언트에서 /message 경로로 POST 요청을 받을 때
router.post('/message', async (req, res) => {
    const {sender, message, receiverList} = req.body;
    try {
        // 서버로 메시지를 보내는 요청
        const response = await fetch('http://localhost:8080/message/sendMessage', {
            method: 'POST',  // POST 요청
            headers: {
                'Content-Type': 'application/json',  // JSON 데이터 형식
            },
            body: JSON.stringify({sender, message, receiverList})  // 클라이언트에서 받은 데이터
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

router.post('/savedMessage', async (req, res) => {
    const {sender, message, tosend} = req.body;
    try {
        // 서버로 메시지를 보내는 요청
        const response = await fetch('http://localhost:8080/message/savedMessage', {
            method: 'POST',  // POST 요청
            headers: {
                'Content-Type': 'application/json',  // JSON 데이터 형식
            },
            body: JSON.stringify({sender, message, receiverList: tosend})  // 클라이언트에서 받은 데이터
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
})
module.exports = router;
