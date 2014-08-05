
package iie.logcabin.brokermonitor.controllerlink;
import iie.logcabin.brokermonitor.config.ApplicationConfigReader;
import iie.logcabin.brokermonitor.crypto.CryptoFactory;
import iie.logcabin.brokermonitor.type.ServerType;
import iie.logcabin.clustercontroller.protocol.BrokerProtocol;


import java.net.InetSocketAddress;
import javax.crypto.SealedObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * 创建Broker与Controller之间的通信联系,使用Apache mina框架
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class ControllerLinkMinaClient {
	
	private static Log log = LogFactory.getLog(ControllerLinkMinaClient.class);
	private static ControllerLinkMinaClient controllerLinkMinaClient;	
	private ApplicationConfigReader configReader = ApplicationConfigReader.getApplicationConfigReader();
	private IoSession session;
	
	public static ControllerLinkMinaClient getBrokerLinkMinaClient()
	{
		if(controllerLinkMinaClient==null)
		{
			synchronized(ControllerLinkMinaClient.class)
			{
				if(controllerLinkMinaClient==null)
				{
					try{
						controllerLinkMinaClient= new ControllerLinkMinaClient();
					}
					catch(java.net.ConnectException msg){
						log.error("服务器没有开启，错误信息为："+msg.getMessage().toString());
						throw new RuntimeException(msg);
					}
					catch(Exception msg){
						log.error("客户端连接服务器是发生错误，错误信息为:"+msg.getMessage().toString());
						throw new RuntimeException(msg);
					}
				}
			}
		}
		return controllerLinkMinaClient;
	}
	
	private ConnectFuture connectFuture ;
	private NioSocketConnector connector ;
	
	private ControllerLinkMinaClient() throws java.net.ConnectException
	{
		connector = new NioSocketConnector(1);
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		ProtocolCodecFilter filter = new ProtocolCodecFilter(new ObjectSerializationCodecFactory());
		chain.addLast("objectFilter",filter);
		connector.setHandler(new ControllerLinkMinaClientHandler());		
	}
	
	/**
	 * 连接服务器，如果服务器没有开启，或者中途关闭，服务器将自动重连接
	 * @return
	 */
	public Boolean connectServer()
	{
		
		if(connector!=null){
			/**进行重连接的时候，这里需要把connector重新赋值为空，才能生效**/
			connector=null;
			connector = new NioSocketConnector(1);			
			//	connector = new NioSocketConnector()
			DefaultIoFilterChainBuilder chain = connector.getFilterChain();
			ProtocolCodecFilter filter = new ProtocolCodecFilter(new ObjectSerializationCodecFactory());
			chain.addLast("objectFilter",filter);
			connector.setHandler(new ControllerLinkMinaClientHandler());
		}
		if(this.connectFuture!=null){
			this.connectFuture = null;
		}
		if(this.session!=null){
			this.session = null;
		}
		
		connectFuture = connector.connect(new InetSocketAddress(this.configReader.getClusterControllerConfig().getClusterControllerIP(),
																this.configReader.getClusterControllerConfig().getClusterControllerPort()));		
		
		connectFuture.awaitUninterruptibly();		
		if(!this.connectFuture.isConnected())
		{
			log.debug("连接服务器中");
			try
			{
				Thread.sleep(configReader.getClusterControllerConfig().getHeartBeatTime());
			}
			catch(Exception msg)
			{
				log.error("线程休眠过程被打断"+msg.getMessage().toString());
			}
			return connectServer();
		}
		else
		{
			this.session = connectFuture.getSession();
			return true;
		}		
	}
	
	public IoSession getIOSession()
	{
		return this.session;
	}
	
	/**
	 * 客户端发送命令
	 * @param commandProtocal 命令
	 */
	public void Send(BrokerProtocol brokerBean)
	{
		CryptoFactory factory = new CryptoFactory(ServerType.CONTROLLERSERVER);
		SealedObject sealedObject = factory.encryption(brokerBean);
		this.session.write(sealedObject);		
	}	
}
