<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><!DOCTYPE html>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.lang.Integer" %>

<%@ page import="java.io.IOException" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.Statement" %>

<html>
  <head>
    <meta charset="UTF-8">
      <title>Edit Post</title>
    </head>
    <body>
      <div><h1>Edit Post</h1></div>
      <form>

        <%
        String dtitle = (String) request.getAttribute("dtitle");
        String dbody = (String) request.getAttribute("dbody");
        %>

        <div>
            <button  type="submit" action="save" name="Save" method="Post">Save</button>
            <button  type="submit" action="list" name="Close">Close</button>
            <button  type="submit" action="preview" name="Preview">Preview</button>
            <button  type="submit" action="delete" name="Delete" method="Post">Delete</button>
        </div>
        <div>
            <label for="title">Title</label>
            <input type="text" name="title" id="text" value="<%=dtitle%>">
        </div>
        <div>
            <label for="body">Body</label>
            <textarea name="body" style="height: 20rem;" id="body"><%=dbody%></textarea>
        </div>
    </form>

</body>
</html>
