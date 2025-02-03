const socket = io();
let username = $('#hiddenUsername').val(); // 서버에서 전달한 username을 가져옴
let toSend = [];
$('ul li').not('[data-user="all"]').each(function() {
    toSend.push($(this).data('user'));
});


// 메시지 전송 함수
function sendMessage() {
    const message = document.getElementById('message-input').value;

    if (message) {
        // 서버로 메시지 전송
        const data = {
            sender: username,   // 사용자 이름
            message: message,     // 전송할 메시지
            receiverList : toSend
        };

        fetch('/chat/message', {
            method: 'POST',             // POST 요청
            headers: {
                'Content-Type': 'application/json',  // 요청 본문 타입 지정
            },
            body: JSON.stringify(data)  // 데이터를 JSON 형식으로 변환하여 전송
        })
            .then(response => response.json())  // 서버 응답을 JSON으로 파싱
            .then(data => {
                socket.emit('chat',{toSend : toSend , username: data.sender, message:data.message})//{toSend, username, message}
                document.getElementById('message-input').value = '';  // 입력창 초기화
            })
            .catch(error => {
                console.error('Error:', error);  // 에러 처리
            });

        // document.getElementById('message-input').value = '';  // 입력창 초기화
    }
}

$('#user-list li').not('[data-user="all"]').each(function() {
    console.log($(this).text()); // 각각의 li 항목을 콘솔에 출력
});

//보낼 사용자 선택
$('li').on('click', function() {
    const user = $(this).data('user');  // 클릭된 li의 data-user 값을 가져옴
    if(user == 'all') {
        // 'all'을 클릭하면 전체 사용자 목록을 제외한 사용자들만 추가
        toSend = []
        // 'all'을 제외한 모든 li 항목을 콘솔에 출력하여 확인
        $('ul li').not('[data-user="all"]').each(function() {
            toSend.push($(this).data('user'));
        });
        $('#titlebar').text('전체 채팅');

    } else {
        // 개별 사용자가 클릭되면 해당 사용자와 본인(username)을 'toSend'에 추가
        toSend = [username, user];
        $('#titlebar').text(user);
    }

    //채팅 불러오기 tosend로 구분하면 될듯?? 3명 이상이면 전체
    savedMessage(toSend);

});

function savedMessage (tosend){
    // 서버로 메시지 전송
    const data = {
        tosend: tosend
    };

    fetch('/chat/savedMessage', {
        method: 'POST',             // POST 요청
        headers: {
            'Content-Type': 'application/json',  // 요청 본문 타입 지정
        },
        body: JSON.stringify(data)  // 데이터를 JSON 형식으로 변환하여 전송
    })
        .then(response => response.json())  // 서버 응답을 JSON으로 파싱
        .then(data => {
            if(data.length == 0){
                $('#chat-box').empty();
            }
            data.forEach(e=>{
                tosend = e.receiveLsit;
                $('#chat-box').empty();
                socket.emit('chat',{toSend : [username] , username: e.sender, message: e.message})//{toSend, username, message}
            })  ;
        })
        .catch(error => {
            console.error('Error:', error);  // 에러 처리
        });

    document.getElementById('message-input').value = '';  // 입력창 초기화
}

// 버튼 클릭 시 메시지 전송
document.getElementById('send-btn').addEventListener('click', sendMessage);

// Enter 키로 메시지 전송
document.getElementById('message-input').addEventListener('keydown', (event) => {
    if (event.key === 'Enter') {
        event.preventDefault(); // Enter 키 기본 동작 방지 (새 줄 추가 방지)
        sendMessage();  // 메시지 전송
    }
});

// 서버로부터 받은 메시지 처리(타인이 보낸 메세지)
socket.on(username, (msg) => {
    const chatBox = document.getElementById('chat-box');
    const messageDiv = document.createElement('div');
    messageDiv.innerHTML = msg; // 메시지 HTML 삽입
    chatBox.appendChild(messageDiv);
    chatBox.scrollTop = chatBox.scrollHeight;  // 스크롤 맨 아래로
});

savedMessage(toSend);