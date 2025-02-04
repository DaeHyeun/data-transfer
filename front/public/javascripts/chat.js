const socket = io(); //소켓
let username = $('#hiddenUsername').val(); // 서버에서 전달한 username을 가져옴
let toSend = []; //메세지 보낼 목록(자신포함)
// 시작은 전체 채팅으로 시작한다. 이전에 있던 기록들 불러오기
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
    }
}

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

//채팅 내역 불러오기
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
savedMessage(toSend);// 이전 채팅 내역 불러오기

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
    toSend.forEach(function(e){
        if(msg.includes(e)){
            const chatBox = document.getElementById('chat-box');
            const messageDiv = document.createElement('div');
            messageDiv.innerHTML = msg; // 메시지 HTML 삽입
            chatBox.appendChild(messageDiv);
            chatBox.scrollTop = chatBox.scrollHeight;  // 스크롤 맨 아래로
        }
    });
});






//첨부파일 전송
var fileItem = document.getElementById('chat-section');

fileItem.ondragover = function(e) {
    e.preventDefault(); // 이 부분이 없으면 ondrop 이벤트가 발생하지 않습니다.
};

fileItem.ondrop = function(e) {
    e.preventDefault(); // 이 부분이 없으면 파일을 브라우저 실행해버립니다.
    var data = e.dataTransfer;
    if (data.items) { // DataTransferItemList 객체 사용
        for (var i = 0; i < data.items.length; i++) { // DataTransferItem 객체 사용
            if (data.items[i].kind == "file") {
                var file = data.items[i].getAsFile();
                uploadFile(toSend, file);
            }
        }
    } else { // File API 사용
        for (var i = 0; i < data.files.length; i++) {
            alert(data.files[i].name);
        }
    }
};

function uploadFile (tosend, file){
    const formData = new FormData();  // FormData 객체 생성
    formData.append("tosend", JSON.stringify(tosend));  // 배열을 JSON 문자열로 변환해서 첨부
    formData.append("file", file);  // 파일 추가

    fetch('/upload', {
        method: 'POST',
        body: formData  // FormData를 본문에 추가
    })
        .then(response => response.json())  // 서버 응답을 JSON으로 파싱
        .then(data => {
            console.log("Success response data: " + data.message);
        })
        .catch(error => {
            console.error('Error:', error);  // 에러 처리
        });
}