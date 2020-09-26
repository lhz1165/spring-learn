<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<%
	ServletContext servletContext = request.getServletContext();
	String msg =(String) servletContext.getAttribute("msg");
%>
<h1>${message}</h1>
<%=msg%>
