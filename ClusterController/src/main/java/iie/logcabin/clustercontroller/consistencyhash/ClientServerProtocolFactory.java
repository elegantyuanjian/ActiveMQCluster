/**
 * 
 */
package iie.logcabin.clustercontroller.consistencyhash;



import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import iie.logcabin.clustercontroller.config.ApplicationConfigReader;
import iie.logcabin.clustercontroller.container.ClientBrokerContainer;
import iie.logcabin.clustercontroller.protocol.ClientBrokerProtocol;
import iie.logcabin.clustercontroller.protocol.ServerResponseProtocolBuf;
import iie.logcabin.clustercontroller.protocol.ClientRequestProtocolBuf.ClientRequestProtocol;
import iie.logcabin.clustercontroller.protocol.ServerResponseProtocolBuf.ServerResponseProtocol;
import iie.logcabin.clustercontroller.type.TransportProtocolType;

/**  
 * 更加Client请求的队列名和协议类型，返回客户端的请求
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version 
 */
public class ClientServerProtocolFactory {
	private ApplicationConfigReader configReader = ApplicationConfigReader.getApplicationConfigReader();
	private ConsistencyHash consistencyHash = ConsistencyHash.getConsistencyHash();
	private Log log = LogFactory.getLog(ClientServerProtocolFactory.class);
	private ClientBrokerContainer clientBrokerContainer = ClientBrokerContainer.getBrokerContainer();
	
	public ServerResponseProtocol getClientServerProtocol(ClientRequestProtocol clientRequestProtocol,String clientRequestIP)
	{
		ServerResponseProtocolBuf.ServerResponseProtocol.Builder builder = ServerResponseProtocolBuf.ServerResponseProtocol.newBuilder();
		String uri = this.configReader.getTransportURI(clientRequestProtocol.getTransportProtocol());
		log.debug(uri);
		String brokerIP = consistencyHash.getServerNode(clientRequestProtocol.getChannelName());
		if(brokerIP==null){
			
			builder.setTransportProtocol(TransportProtocolType.nobroker);
			builder.setTransportUrl("集群中Broker没有打开");
		}
		else{
			uri = uri.replaceAll("0.0.0.0", brokerIP);
			
			builder.setTransportProtocol(clientRequestProtocol.getTransportProtocol());
			builder.setTransportUrl(uri);
			//log.debug(clientServerProtocol.toString());
			
			ClientBrokerProtocol clientBrokerProtocol = new ClientBrokerProtocol();
			clientBrokerProtocol.setBrokerIP(brokerIP);
			clientBrokerProtocol.setChannelName(clientRequestProtocol.getChannelName());
			
			clientBrokerProtocol.setDateTime(new Date());
			clientBrokerProtocol.setRequestType(clientRequestProtocol.getRequestType());
			
			clientBrokerProtocol.setClientIP(clientRequestIP);
			
			this.clientBrokerContainer.add(clientBrokerProtocol);
		}
		ServerResponseProtocolBuf.ServerResponseProtocol serverResponseProtocol = builder.build();	
		return serverResponseProtocol;
	}
}
