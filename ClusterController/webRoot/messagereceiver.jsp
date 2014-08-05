<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="iie.logcabin.clustercontroller.container.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html >
<head>

<title>Highcharts - Basic line</title>

<script type="text/javascript" src="js/jquery.min.js"></script>


</head>
<body>


<script type="text/javascript">


(function($){ // encapsulate jQuery
<%
   ClientBrokerContainer clientBrokerContainer = ClientBrokerContainer.getBrokerContainer();
   int messageReceiver[] = clientBrokerContainer.getMessageReceiverCount();
  // int messageSender[] = clientBrokerContainer.getMessageSenderCount();
%>
   // var senderCount=new Array(24);
    var receiverCount =new Array(24);
  
   
    
 	<%for(int i = 0; i<24; i++){%>
       receiverCount[<%=i%>] = <%=messageReceiver[i]%>
      
    <%}%>
   
$(function () {
        $('#container').highcharts({
            title: {
                text: '日访问量记录',
                x: -20 //center
            },
            subtitle: {
                text: '2014-04-02',
                x: -20
            },
            xAxis: {
                categories: ['0', '1', '2', '3', '4', '5',
                    '6', '7', '8', '9', '10', '11','12', '13', '14', '15', '16', '17',
                    '18', '19', '20', '21', '22', '23']
            },
            yAxis: {
                title: {
                    text: 'Count (次数)'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                valueSuffix: '次'
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            },
            series: [ {
                name: '消息接受者',
                data: receiverCount
            }]
        });
    });
    

})(jQuery);
</script> 
</dic>
<div id="demo-content">
	<div style="margin: 5px">
	
	<script src="js/highcharts.js"></script>
    <script src="js/exporting.js"></script>

<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
	</div>	
</div>
</body>

</html>
