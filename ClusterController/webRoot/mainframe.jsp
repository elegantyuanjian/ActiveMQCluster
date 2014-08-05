<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>后台监管系统</title>
    <style type="text/css" media="screen">
        @import url('styles/sorttable.css');
        @import url('styles/type-settings.css');
        @import url('styles/site.css');
        @import url('styles/prettify.css');
    </style>
    
        <script type='text/javascript' src='js/common.js'></script>
        <script type='text/javascript' src='js/css.js'></script>
        <script type='text/javascript' src='js/standardista-table-sorting.js'></script>
        <script type='text/javascript' src='js/prettify.js'></script>
        <script>addEvent(window, 'load', prettyPrint)</script>
</head>

<body>
<div class="white_box">
	<!-- 头部-->
    <div class="header">
        <div class="header_l">
            <div class="header_r">
            </div>
        </div>
    </div>
    <!--内容部分-->
    <div class="content">
        <div class="content_l">
            <div class="content_r">

                <div>

                    <!--标题 -->
                    <div id="asf_logo">
                        <div id="activemq_logo">
                         </div>
                    </div>

					<!--导航-->
                    <div class="top_red_bar">
                        <div id="site-breadcrumbs">
                            <a href="index.jsp"  title="Home" target="mainFrame">首页</a>
                            &#124;
                            <a href="messagesender.jsp" title="senderproxy" target="mainFrame">消息发送端</a>
                            &#124;
                            <a href="brokercluster.jsp" title="brokerproxy" target="mainFrame">消息中间件集群</a>
                            &#124;
                            <a href="messagereceiver.jsp" title="receiveproxy" target="mainFrame">消息接受端</a>
                            </div>
                        <div id="site-quicklinks"><P>
                            <a href="http://activemq.apache.org/support.html"
                               title="Get help and support using Apache ActiveMQ">Support</a></p>
                        </div>
                    </div>

                   <!--body部分-->
                    <div class="body-content"  style="width:80%; height:700px ;text-align:center">
                    	<iframe    style="width:100%; height:100% ;"  frameborder="0" name="mainFrame"  src="index.jsp"></iframe>  
                    </div>
                    
                    <div class="bottom_red_bar"></div>
                </div>
            </div>
        </div>
    </div>
    <!-- 底部 -->
    <div class="black_box">
        <div class="footer">
            <div class="footer_l">
                <div class="footer_r">
                    
                </div>
            </div>
        </div>
    </div>
</div>

<div class="design_attribution"></div>

</body>
</html>

