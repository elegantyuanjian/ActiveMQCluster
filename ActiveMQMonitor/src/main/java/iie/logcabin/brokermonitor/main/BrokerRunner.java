package iie.logcabin.brokermonitor.main;



import iie.logcabin.brokermonitor.brokerinfo.BrokerBeanFactory;
import iie.logcabin.brokermonitor.controllerlink.ControllerLinkMinaClient;
import iie.logcabin.clustercontroller.protocol.BrokerProtocol;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 程序运行
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class BrokerRunner {
	
	public static void main(String args[])
	{
		try
		{
			ControllerLinkMinaClient client = ControllerLinkMinaClient.getBrokerLinkMinaClient();
			if(client.connectServer())
			{
				if(client!=null)
				{
					BrokerProtocol brokerProtocol  = new BrokerProtocol();
					BrokerBeanFactory brokerBeanFactory = new BrokerBeanFactory();	
					brokerProtocol = brokerBeanFactory.getBrokerBean();				
					brokerBeanFactory.getHashTable();					
					client.Send(brokerProtocol);					
					log.debug(brokerProtocol.toString());
				}
			}
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
			log.error("客户端无法获取与服务器端的session");
		}
	}
	
	private static Log  log  = LogFactory.getLog(BrokerRunner.class);
}
