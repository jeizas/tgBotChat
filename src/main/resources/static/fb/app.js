let stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
        // $("#notice").html("");
    } else {
        // $("#conversation").hide();
    }
}

function connect() {
    var userId = $("#userId").val();
    var email = $("#email").val();
    var captchaCode = $("#captchaCode").val();
    if (userId === "") {
        alert("请填写用户ID！")
        return
    }
    if (email === "") {
        alert("请填写邮箱！")
        return;
    }
    if (captchaCode === "") {
        alert("请填写验证码！")
        return;
    }

    fetch('http://localhost:8080/api/check', {
        method: 'POST',
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({'userId': userId, 'email': email, 'captchaCode': captchaCode})
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.text();
        })
        .then(data => {
            console.log(data); // 处理响应数据
            var body = JSON.parse(data);
            if (body.code === 200) {
                wsConnect(body.data.uuid)
            } else {
                err(body.msg)
            }
        })
        .catch(error => {
            console.error('请求失败:', error);
            err("服务器繁忙")
        });
}

function err(msg) {
    alert(msg)
}

function wsConnect(uuid) {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        console.log('uuid: ' + uuid);
        showContent({'content': '<' + $("#userId").val() + '>: ' + '连接成功，等待客服赶来...', 'time': new Date()});
        //通过+from就可以指定我订阅的是我自己用户的信息
        stompClient.subscribe('/chat/single/' + uuid, function (result) {
            var data = JSON.parse(result.body)
            showContent({'content': '<' + data.userName + '>: ' + data.content, 'time': new Date(data.time)});
        });
        stompClient.subscribe('/chat/disconnect/' + uuid, function (result) {
            var data = JSON.parse(result.body)
            showContent({'content': '<' + data.userName + '>: ' + data.content, 'time': new Date(data.time)});
            disconnect()
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    //这里出了发送content信息外，还发送发送信息的用户信息，和接受信息的用户信息
    stompClient.send(
        "/app/message",
        {},
        JSON.stringify({'content': $("#content").val(), 'from': $("#userId").val()})
    );
    var userId = $("#userId").val()
    showContent({'content': '<' + userId + '>: ' + $("#content").val(), 'time': new Date().getTime()});
}

function showContent(body) {
    $("#notice").append("<tr><td>" + body.content + "</td> <td>" + new Date(body.time).toLocaleString() + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendName();
    });
});

function refreshCaptcha() {
    // 给图片URL添加随机参数，防止浏览器缓存
    var captchaImg = document.getElementById('captchaImg');
    captchaImg.src = '/api/captcha?rand=' + Math.random();
}


