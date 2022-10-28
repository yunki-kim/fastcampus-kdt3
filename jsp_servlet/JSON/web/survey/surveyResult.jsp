<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>JSP</title>
    <link href="../css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
<header>
    <jsp:include page="../layout/header.jsp"/>
</header>

<%--<%--%>
<%--    --%>
<%--    String uId = "";--%>
<%--    if (session.getAttribute("SESSION_ID") != null) {--%>
<%--        uId = (String) session.getAttribute("SESSION_ID");--%>
<%--    } else {--%>
<%--        response.sendRedirect("../non-login/login.jsp");--%>
<%--    }--%>
<%--%>--%>

<%@ include file="../login/loginFilter.jsp"%>

<%
    request.setCharacterEncoding("UTF-8");

    String season = "";
    if (request.getParameter("season") != null) {
        season = request.getParameter("season");
    }

    String fruit = "";
    if (request.getParameter("fruit") != null) {
        fruit = request.getParameter("fruit");
    }
%>

<main>
    <h1>SURVEY RESULT</h1>
    <div>User Id => "<%=uId%>"</div>
    <div>
        <p>Selected Season => <%=season%>
        </p>
        <p>Selected Fruit => <%=fruit%>
        </p>
    </div>

    <div></div>
</main>

<footer class="main__nav__next">
    <jsp:include page="../layout/footer.jsp"/>
</footer>
</body>
</html>
