<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <style type="text/css" media="screen">
        @import url('styles/sorttable.css');
        @import url('styles/type-settings.css');
        @import url('styles/site.css');
        @import url('styles/prettify.css');
    </style>
	
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script src="js/highcharts.js"></script>
	<script src="js/exporting.js"></script>
	<script src="js/grid.js"></script>
    <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="js/stomp.js"> </script>
 	<script type="text/javascript" src="js/serverinfo.js"></script>

  </head>
  
  <body> 
	ActiveMQ Balancer 简介<p>
	有四个部分组成，如下图所示:<p>
	<img  src="./images/architecture.png">
  </body>
</html>