package iie.logcabin.clustercontroller.jettyserver;

import iie.logcabin.clustercontroller.brokerlink.BrokerLinkMinaServerListener;
import iie.logcabin.clustercontroller.brokerlink.BrokerLinkMinaServerQuery;
import iie.logcabin.clustercontroller.clientlink.ClientLinkServerListenerThread;


/**
 * 系统启动
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class JettyServerStart {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 new BrokerLinkMinaServerListener().start();
		 new BrokerLinkMinaServerQuery().start();
		 
		 //new ClientLinkMinaServerListener().start();
		 new ClientLinkServerListenerThread().start();
		 
		  JettyCustomServer server = new JettyCustomServer(
	                "./jetty/etc/jetty.xml", "/AQBalancer");
	        server.startServer();
	    //  System.out.println("test");
	     
	}

}





