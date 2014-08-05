package iie.logcabin.clustercontroller.brokerlink;

import java.net.InetSocketAddress;

import iie.logcabin.clustercontroller.container.BrokerContainer;
import iie.logcabin.clustercontroller.crypto.CryptoFactory;
import iie.logcabin.clustercontroller.protocol.BrokerProtocol;
import iie.logcabin.clustercontroller.type.ServerType;

import javax.crypto.SealedObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/** 
 * 当控制器和Broker之间联系通信后，apache mina框架对请求的处理
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class BrokerLinkMinaServerHanlder   extends IoHandlerAdapter{

	private Log log = LogFactory.getLog(BrokerLinkMinaServerHanlder.class);
	private BrokerContainer brokerContainer = BrokerContainer.getBrokerContainer();
	
	/**当Mina捕获通信异常时*/
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		log.error("监控服务器发生异常,客户端网络IP为:"+session.getRemoteAddress()+"异常情况为:"+cause.getMessage()+cause.getStackTrace());
		InetSocketAddress socketAddress = (InetSocketAddress)session.getRemoteAddress();
		this.brokerContainer.remove(socketAddress.getAddress().getHostAddress());
		//发现某一个地址连接错误，需要从容器里面，把相应的值删除
		
	}

	/**当Mina获取客户端消息时*/
	@Override
	public void messageReceived(IoSession session, Object message)	throws Exception {
		//把控制协议插队协议队列中去
		SealedObject sealedObject = (SealedObject)message;
		//BrokerBeanCrypto brokerBeanCrypto = new BrokerBeanCrypto();
		CryptoFactory factory = new CryptoFactory(ServerType.BROKERSERVER);
		BrokerProtocol brokerProtocol = (BrokerProtocol)factory.decryption(sealedObject);
		log.debug(brokerProtocol.toString());
		this.brokerContainer.add(brokerProtocol);//把信息加入到container中
	}

	/**当Mina发送回复消息成功时*/
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
	}
	
	/**Mina会话结束*/
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		log.debug("会话关闭:"+session.getRemoteAddress().toString());
		//输出相应的信息
		InetSocketAddress socketAddress = (InetSocketAddress)session.getRemoteAddress();
		this.brokerContainer.remove(socketAddress.getAddress().getHostAddress());
	}

	/*****************当Mina会话连接成功*************************/
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		log.debug("会话创建:"+session.getRemoteAddress().toString());
		//相关IP的
	}
	
	/*****************当Mina会话连接打开*************************/
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		log.debug("会话打开:"+session.getRemoteAddress().toString());
		
	}	
}
