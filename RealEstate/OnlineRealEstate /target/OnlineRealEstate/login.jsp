<%@ page import="com.orems.model.AbstractUser" %>
<html>
<head><title>Login</title></head>
<body>
<h2>Login</h2>

<form action="auth" method="post">
    <input type="hidden" name="action" value="login">

    Username: <input type="text" name="username"><br>
    Password: <input type="password" name="password"><br>

    <button type="submit">Login</button>
</form>


<p><a href="register.jsp">Register</a></p>

<!-- Display error message -->
<%
    if (request.getAttribute("error") != null) {
        out.println("<p style='color:red'>" + request.getAttribute("error") + "</p>");
    }
%>

</body>
</html>
