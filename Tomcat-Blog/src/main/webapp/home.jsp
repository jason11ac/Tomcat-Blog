<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%@ page import="java.io.IOException" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.Statement" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Post List</title>

    <style>
      #top {
        font-weight: bold;
      }
    </style>

</head>

<body>
  <form>
    <div>
      <button  type="submit" action="Open" name="NewPost">New Post</button>
      <br>
  </div>

<%
  Connection c = null;
  Statement  s = null;

  String user = (String) request.getAttribute("use");

  try {
    /* create an instance of a Connection object */
    c = DriverManager.getConnection("jdbc:mysql://localhost:3306/CS144", "cs144", "");

  } catch (SQLException ex) {
    //do nothing
  }

  try {
    //Print out home screen with data

    String display = "SELECT * FROM Posts WHERE username = ?";
    PreparedStatement prepared = c.prepareStatement(display);


    prepared.setString(1, user);

    ResultSet homepage = prepared.executeQuery();

%>
<div id="top">
  <span style="margin-left: 2em;">Title</span>
  <span style="margin-left: 4em;">Created</span>
  <span style="margin-left: 4em;">Modified</span>
</div>
<%

    while (homepage.next()) {
      String dtitle = homepage.getString("title");
      String dbody = homepage.getString("body");
      String modified = homepage.getString("modified");
      String created = homepage.getString("created");

      DateFormat dateFormat = new SimpleDateFormat("HH:mm MM/dd/yyyy");
      String created1 = created.substring(0, 16);
      String modified1 = modified.substring(0, 16);
%>
      <span style="margin-left: 1em;"><%= dtitle %></span>
      <span style="margin-left: 2.5em;"><%= created1 %></span>
      <span style="margin-left: 2.5em;"><%= modified1 %></span>
      <button type="submit" action="Open" name="Open">Open</button>
      <button type="submit" action="Delete" name="DeleteHome">Delete</button>
      <br>
<%
    }
  } catch (SQLException ex) {
    //do nothing
  }
%>

</form>

</body>
</html>
