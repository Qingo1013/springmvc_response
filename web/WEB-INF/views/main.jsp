<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2021/3/10
  Time: 19:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>TestTitle</title>
</head>
<%--<% String res=request.getAttribute("type").toString(); %>--%>
<body>
    test:${requestScope.type}<p></p>
    get type from session :${sessionScope.type}<p/>
    User:${requestScope.user}
</body>
</html>
