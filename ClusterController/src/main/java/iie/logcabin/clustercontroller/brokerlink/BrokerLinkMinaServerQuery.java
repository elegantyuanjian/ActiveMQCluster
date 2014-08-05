/**
 * 
 */
package iie.logcabin.clustercontroller.brokerlink;

import iie.logcabin.clustercontroller.config.ApplicationConfigReader;
import iie.logcabin.clustercontroller.config.BrokerServerConfig;
import iie.logcabin.clustercontroller.crypto.CryptoFactory;
import iie.logcabin.clustercontroller.protocol.BrokerServerProtocol;
import iie.logcabin.clustercontroller.type.ControllerType;
import iie.logcabin.clustercontroller.type.ServerType;

import java.util.Iterator;
import javax.crypto.SealedObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;


/**
 * Controller定时查询Broker信息  
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version activemq_balance 2014-02-01/01version 
 */
public class BrokerLinkMinaServerQuery extends Thread 
{	
	private BrokerLinkMinaServer brokerLinkMinaServer = BrokerLinkMinaServer.getBrokerLinkMinaServer();
	private ApplicationConfigReader reader = ApplicationConfigReader.getApplicationConfigReader();
	private BrokerServerConfig brokerServerConfig = reader.getBrokerServerConfig();
	
	public void run()
	{
		while(true)
		{
			BrokerServerProtocol controllerBean = new BrokerServerProtocol();
			//设置查询内容
			controllerBean.setRequestType(ControllerType.QUERYINFO);
			
			java.util.Vector<IoSession> sessionVector = brokerLinkMinaServer.getIoSessionVector();
			
			if(sessionVector.isEmpty()!=true)
			{
				Iterator<IoSession> iter = sessionVector.iterator();
				IoSession session;
				while (iter.hasNext()) 
				{ 
					session =(IoSession)iter.next();//对对象进行加密
					CryptoFactory factory = new CryptoFactory(ServerType.BROKERSERVER);
					SealedObject sealedObject =  factory.encryption(controllerBean);							
					session.write(sealedObject);
					log.debug(session.getRemoteAddress().toString());
					
			    }
			}
			
			try 
			{
				Thread.sleep(brokerServerConfig.getHeartBeatTime());//
			} catch (InterruptedException e)
			{
				log.debug("查询线程休眠被中断:"+e.getMessage());
			}
		}
	}
	
	private Log log = LogFactory.getLog(BrokerLinkMinaServerQuery.class);
	
}
