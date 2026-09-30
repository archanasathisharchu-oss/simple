<%@ page contentType="text/html; charset=UTF-8" %>

<%
    String currentUser = (String) session.getAttribute("username");

    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Simple Chat</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            font-family: Arial, sans-serif;
            background: #e8f0ff;

            display: flex;
            justify-content: center;
            align-items: center;
        }

        .container {
            width: 90%;
            max-width: 1000px;
            height: 85vh;

            display: flex;

            background: #e8f0ff;

            border-radius: 30px;

            box-shadow:
                15px 15px 30px #b8c5d8,
                -15px -15px 30px white;

            overflow: hidden;
        }

        /* LEFT */

        .sidebar {
            width: 32%;
            padding: 25px;

            border-right: 1px solid #ccd7e5;
        }

        .sidebar h2 {
            margin-top: 0;
            color: #263238;
        }

        .logged {
            color: #78909c;
            margin-bottom: 20px;
        }

        #userList {
            display: flex;
            flex-direction: column;
            gap: 12px;
        }

        .user {
            display: flex;
            align-items: center;
            gap: 12px;

            padding: 12px;

            border-radius: 18px;

            cursor: pointer;

            box-shadow:
                5px 5px 10px #bcc8d9,
                -5px -5px 10px white;
        }

        .user:hover {
            transform: translateY(-2px);
        }

        .avatar {
            width: 42px;
            height: 42px;

            border-radius: 50%;

            display: flex;
            align-items: center;
            justify-content: center;

            background: #5c9ded;
            color: white;
            font-weight: bold;
        }

        /* RIGHT */

        .chat {
            width: 68%;

            display: flex;
            flex-direction: column;
        }

        .header {
            padding: 22px;

            font-size: 20px;
            font-weight: bold;

            color: #263238;

            border-bottom: 1px solid #ccd7e5;
        }

        #messages {
            flex: 1;
            padding: 25px;
            overflow-y: auto;
        }

        .welcome {
            text-align: center;
            margin-top: 100px;
            color: #90a4ae;
        }

        .message-area {
            padding: 20px;

            border-top: 1px solid #ccd7e5;

            display: flex;
            gap: 10px;
        }

        #messageInput {
            flex: 1;

            padding: 14px;

            border: none;
            outline: none;

            border-radius: 18px;

            background: #e8f0ff;

            box-shadow:
                inset 5px 5px 10px #bcc8d9,
                inset -5px -5px 10px white;
        }

        .send {
            width: 90px;

            border: none;
            border-radius: 18px;

            background: #5c9ded;

            color: white;
            font-weight: bold;

            cursor: pointer;
        }

        .send:active {
            transform: translateY(2px);
        }

        .sent {
            text-align: right;
            margin-bottom: 12px;
        }

        .sent span {
            display: inline-block;

            padding: 10px 15px;

            background: #5c9ded;
            color: white;

            border-radius: 18px 18px 4px 18px;

            max-width: 70%;
        }

        .error {
            color: red;
            padding: 10px;
        }

    </style>
</head>

<body>

<div class="container">

    <!-- USER LIST -->

    <div class="sidebar">

        <h2>💬 Chats</h2>

        <div class="logged">
            Logged in as:
            <b><%= currentUser %></b>
        </div>

        <div id="userList">
            Loading users...
        </div>

    </div>


    <!-- CHAT AREA -->

    <div class="chat">

        <div class="header" id="chatHeader">
            Select a user
        </div>

        <div id="messages">

            <div class="welcome">
                Select a user to start chatting
            </div>

        </div>

        <div class="message-area">

            <input
                type="text"
                id="messageInput"
                placeholder="Type a message..."
            >

            <button class="send" onclick="sendMessage()">
                Send
            </button>

        </div>

    </div>

</div>


<script>

let selectedUser = null;


/* LOAD USERS */

function loadUsers() {

    fetch("${pageContext.request.contextPath}/UsersServlet")

    .then(function(response) {

        if (!response.ok) {
            throw new Error("UsersServlet error: " + response.status);
        }

        return response.text();
    })

    .then(function(data) {

        document.getElementById("userList").innerHTML = data;

    })

    .catch(function(error) {

        console.error(error);

        document.getElementById("userList").innerHTML =
            "<div class='error'>Unable to load users</div>";
    });
}


/* SELECT USER */

function selectUser(username) {

    selectedUser = username;

    document.getElementById("chatHeader").innerText =
        "Chat with " + username;

    document.getElementById("messages").innerHTML =
        "<div class='welcome'>" +
        "Start chatting with <b>" +
        username +
        "</b>" +
        "</div>";
}


/* SEND MESSAGE */

function sendMessage() {

    if (selectedUser === null) {

        alert("Please select a user first.");
        return;
    }

    const input =
        document.getElementById("messageInput");

    const message = input.value.trim();

    if (message === "") {
        return;
    }


    const params = new URLSearchParams();

    params.append("receiver", selectedUser);
    params.append("message", message);


    fetch("${pageContext.request.contextPath}/SendMessageServlet", {

        method: "POST",

        headers: {
            "Content-Type":
                "application/x-www-form-urlencoded"
        },

        body: params.toString()

    })

    .then(function(response) {

        if (!response.ok) {

            return response.text().then(function(text) {
                throw new Error(
                    "Server error " +
                    response.status +
                    ": " +
                    text
                );
            });

        }

        return response.json();

    })

    .then(function(data) {

        if (data.ok) {

            const messages =
                document.getElementById("messages");


            const div =
                document.createElement("div");

            div.className = "sent";


            const span =
                document.createElement("span");

            span.textContent = message;


            div.appendChild(span);

            messages.appendChild(div);


            input.value = "";

            messages.scrollTop =
                messages.scrollHeight;
        }

    })

    .catch(function(error) {

        console.error(error);

        alert("Message could not be sent.");

    });
}


/* ENTER KEY */

document.getElementById("messageInput")
    .addEventListener("keydown", function(event) {

        if (event.key === "Enter") {

            event.preventDefault();

            sendMessage();
        }

    });


/* START */

loadUsers();

</script>

</body>
</html>