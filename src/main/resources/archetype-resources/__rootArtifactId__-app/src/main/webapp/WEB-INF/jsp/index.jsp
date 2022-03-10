<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" isErrorPage="false" %>
<%@ include file="include.jspf" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="keywords" content="freedom, democracy, science">
        <base href="<c:url value='/' />" ><%-- when context is ROOT, ${pageContext.request.contextPath} output '//' --%>
        <meta charset="UTF-8">
        <title>JSP TEMPLATE TITLE</title>
        <!-- [if lt IE 9]>

        <![endif]-->
    </head>
    <body>
<%--
#if($framework.contains('websocket'))
--%>
        <div id="chat-box">
            <button id="close">关闭</button>
            <div id="message-box">
                <div id="receive-box"></div>
            </div>
            <div id="send-box">
                <textarea type="text" id="msg-to-send"></textarea>
                <button id="send-btn">发送</button>
            </div>
        </div>
<%--
#end
--%>
        <noscript style="align: center; color: hsla(1,1,1,0.4);"><fmt:message key="JAVASCRIPT_DISABLED_WARNING" /></noscript>
    </body>
<%--
#if($framework.contains('websocket'))
--%>
    <script type="application/javascript">
        const msgBox = document.getElementById("msg-to-send")
        const sendBtn = document.getElementById("send-btn")
        const close = document.getElementById("close")
        const receiveBox = document.getElementById("receive-box")

        // 创建一个webSocket对象
        const ws = new WebSocket("ws://127.0.0.1:8080/wss")
        ws.onopen = e => {
            // 连接后监听
            console.log(`WebSocket 连接状态： \${ws.readyState}`)
        }

        ws.onmessage = data => {
            // 当服务端返回数据的时候，放到页面里
            receiveBox.innerHTML += `<p>\${data.data}</p>`
            receiveBox.scrollTo({
                top: receiveBox.scrollHeight,
                behavior: "smooth"
            })
        }

        ws.onclose = data => {
            // 监听连接关闭
            console.log("WebSocket连接已关闭")
            console.log(data);
        }

        sendBtn.onclick = () => {
            // 点击发送按钮，将数据发送给服务端
            ws.send(msgBox.value)
        }
        close.onclick = () => {
            // 客户端主动关闭连接
            ws.close()
        }
    </script>
<%--
#end
--%>
</html>