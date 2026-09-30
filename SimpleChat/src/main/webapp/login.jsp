```jsp
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Simple Chat - Login</title>

    <style>

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;

            display: flex;
            justify-content: center;
            align-items: center;

            font-family: Arial, sans-serif;

            background: #e8f0ff;
        }

        /* Main Clay Card */
        .login-card {
            width: 380px;
            padding: 45px 40px;

            background: #e8f0ff;

            border-radius: 35px;

            box-shadow:
                15px 15px 30px rgba(163, 177, 198, 0.6),
                -15px -15px 30px rgba(255, 255, 255, 0.9);

            text-align: center;
        }

        /* Logo */
        .logo {
            width: 85px;
            height: 85px;

            margin: 0 auto 25px;

            display: flex;
            justify-content: center;
            align-items: center;

            border-radius: 28px;

            background: #e8f0ff;

            box-shadow:
                10px 10px 20px rgba(163, 177, 198, 0.6),
                -10px -10px 20px rgba(255, 255, 255, 0.9);

            font-size: 40px;
        }

        h1 {
            margin: 0;
            color: #263238;
            font-size: 28px;
            font-weight: 700;
        }

        .subtitle {
            margin-top: 10px;
            margin-bottom: 30px;

            color: #78909c;
            font-size: 14px;
        }

        /* Input */
        .input-box {
            margin-bottom: 25px;
        }

        input {
            width: 100%;
            padding: 16px 20px;

            border: none;
            outline: none;

            border-radius: 18px;

            background: #e8f0ff;

            color: #263238;
            font-size: 15px;

            box-shadow:
                inset 6px 6px 12px rgba(163, 177, 198, 0.55),
                inset -6px -6px 12px rgba(255, 255, 255, 0.9);
        }

        input::placeholder {
            color: #90a4ae;
        }

        /* Clay Button */
        button {
            width: 100%;
            padding: 16px;

            border: none;
            outline: none;

            border-radius: 18px;

            background: #5c9ded;

            color: white;

            font-size: 16px;
            font-weight: bold;

            cursor: pointer;

            box-shadow:
                8px 8px 16px rgba(163, 177, 198, 0.6),
                -6px -6px 14px rgba(255, 255, 255, 0.8);

            transition: 0.2s;
        }

        button:hover {
            transform: translateY(-2px);
        }

        button:active {
            transform: translateY(3px);

            box-shadow:
                inset 5px 5px 10px rgba(0,0,0,0.15),
                inset -5px -5px 10px rgba(255,255,255,0.2);
        }

        .footer {
            margin-top: 25px;
            color: #90a4ae;
            font-size: 12px;
        }

    </style>
</head>

<body>

    <div class="login-card">

        <div class="logo">
            💬
        </div>

        <h1>Simple Chat</h1>

        <div class="subtitle">
            Connect. Chat. Stay together.
        </div>

        <form action="${pageContext.request.contextPath}/LoginServlet"
              method="post">

            <div class="input-box">

                <input
                    type="text"
                    name="username"
                    placeholder="Enter your username"
                    autocomplete="off"
                    required>

            </div>

            <button type="submit">
                Enter Chat
            </button>

        </form>

        <div class="footer">
            Simple • Private • Connected
        </div>

    </div>

</body>
</html>
```
