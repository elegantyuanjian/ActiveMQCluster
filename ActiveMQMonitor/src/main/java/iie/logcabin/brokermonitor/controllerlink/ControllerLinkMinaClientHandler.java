/**
 * 
 */
package iie.logcabin.brokermonitor.controllerlink;

import iie.logcabin.brokermonitor.brokerinfo.BrokerBeanFactory;
import iie.logcabin.brokermonitor.crypto.CryptoFactory;
import iie.logcabin.brokermonitor.type.ControllerType;
import iie.logcabin.brokermonitor.type.ServerType;
import iie.logcabin.clustercontroller.protocol.BrokerProtocol;
import iie.logcabin.clustercontroller.protocol.BrokerServerProtocol;

import javax.crypto.SealedObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * 处理Controller发送过来的请求
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class ControllerLinkMinaClientHandler  extends IoHandlerAdapter
{
	private Log log  = LogFactory.getLog(ControllerLinkMinaClientHandler.class);
	//private BrokerLinkMinaClient client = BrokerLinkMinaClient.getBrokerLinkMinaClient();
	// 当一个客端端连结到服务器后

	public void sessionOpened(IoSession session) throws Exception {
		log.debug(session.getLocalAddress());
	}
	 
	// 当一个客户端关闭时
	@SuppressWarnings("deprecation")
	@Override
	public void sessionClosed(IoSession session) 
	{
		log.fatal("和无服务器断开"+session.getRemoteAddress());		
		ControllerLinkMinaClient client = ControllerLinkMinaClient.getBrokerLinkMinaClient();
		//session.close();
		//session.close(true);
		session.close();
		
		client.connectServer();
	}
	 
	// 当服务器端发送的消息到达时:
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
	    	
		SealedObject sealedObject = (SealedObject)message; 
		CryptoFactory cryptoFactory = new CryptoFactory(ServerType.CONTROLLERSERVER);
		BrokerServerProtocol controllerBean = (BrokerServerProtocol)cryptoFactory.decryption(sealedObject);
		log.debug(controllerBean.getRequestType());
		if(controllerBean.getRequestType()==ControllerType.QUERYINFO)
		{
			//这里应该有个判断
			//当收到控制器发过来的查询信息时，返回当前的信息
			BrokerBeanFactory brokerBeanfactory= new BrokerBeanFactory();			
			BrokerProtocol brokerBean = brokerBeanfactory.getBrokerBean();
			session.write(cryptoFactory.encryption(brokerBean));
			log.debug("broker信息发送成功");
		}
	}
	    
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception 
	{
	    	log.fatal("与客服端通信时，发生异常"+session.getRemoteAddress());
	}
}
