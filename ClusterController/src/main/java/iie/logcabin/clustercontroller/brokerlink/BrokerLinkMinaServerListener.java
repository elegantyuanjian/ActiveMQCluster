/**
 * 
 */
package iie.logcabin.clustercontroller.brokerlink;

import iie.logcabin.clustercontroller.config.ApplicationConfigReader;
import iie.logcabin.clustercontroller.config.BrokerServerConfig;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;

/**  
 * 负责监听Broker发送过来的消息
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version 
 */
public class BrokerLinkMinaServerListener extends Thread {
	
	private Log log=LogFactory.getLog(BrokerLinkMinaServerListener.class);
	private BrokerLinkMinaServer brokerLinkMinaServer = BrokerLinkMinaServer.getBrokerLinkMinaServer();
	private SocketAcceptor socketServer = brokerLinkMinaServer.getSocketAcceptor();
	private ApplicationConfigReader configReader = ApplicationConfigReader.getApplicationConfigReader();
	private BrokerServerConfig brokerServerConfig = configReader.getBrokerServerConfig();
	
	@Override
	public void run() {
		
		DefaultIoFilterChainBuilder chain = socketServer.getFilterChain();
         //设定这个过滤器将以对象为单位读取数据
        ProtocolCodecFilter filter= new ProtocolCodecFilter(new ObjectSerializationCodecFactory());
         chain.addLast("objectFilter",filter);
         //设定服务器消息处理器
        socketServer.setHandler(new BrokerLinkMinaServerHanlder());
         //服务器绑定的端口
        int bindPort = brokerServerConfig.getBrokerServerPort();
         //绑定端口，启动服务器
        try
        {
        	socketServer.bind(new InetSocketAddress(bindPort));
        } 
        catch (IOException msg) 
        {
        	log.error("实例化Controller与Broker之间通信,出错信息为："+msg.getMessage().toString());           
        	throw new RuntimeException(msg);
        }
        
        log.info("实例化控制器与消息中间件服务端之间的连接成功");
	}	
}
