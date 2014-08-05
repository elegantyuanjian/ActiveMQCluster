<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="iie.logcabin.clustercontroller.container.*" %>
<%@page import="iie.logcabin.clustercontroller.protocol.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'brokercluster.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  <div id="contentdiv">
  <!-- 获取 BToB映射关系 的基本信息 -->
  <table   style="width:100%" >
	<caption><font ><b>消息中间件集群情况</b></font></caption>
	<tr >
		<td >消息中间件对应的IP	</td>
		<td >Queue生产者的数目	</td>
		<td >Queue消费者的数目	</td>
		<td >Topic生产者的数目	</td>
		<td >Topic消费者的数目	</td>
	</tr>
  <% 
  	BrokerContainer brokerContainer = BrokerContainer.getBrokerContainer();
  	Vector<BrokerProtocol> brokerProtocolVector = brokerContainer.getBrokerProtocolVector();
  	Iterator<BrokerProtocol> iterator = brokerProtocolVector.iterator();
  	BrokerProtocol brokerProtocol;
  			while (iterator.hasNext())
			{
				brokerProtocol =iterator.next();
					
	%>
		<tr >
			<td ><%=brokerProtocol.getBrokerIP()%> </td>
			<td ><%=brokerProtocol.getTotalQueueProducer()%></td>
			<td ><%=brokerProtocol.getTotalQueueConsumer()%></td>
			<td ><%=brokerProtocol.getTotalTopicProducer()%></td>			
			<td ><%=brokerProtocol.getTotalTopicConsumer()%></td>
			
		</tr>
	<%
			}
	%>
	<tr>
	<td colspan="5"><a onClick="return insertDiv()">添加新的连接关系</a></td>
	</tr>
	</table>
	
  </div>
  </body>
</html>
