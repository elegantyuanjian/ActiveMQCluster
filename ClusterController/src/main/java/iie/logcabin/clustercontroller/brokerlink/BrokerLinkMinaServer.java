package iie.logcabin.clustercontroller.brokerlink;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * 创建消息中间件与控制器之间的通信联系,使用Apache mina框架
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class BrokerLinkMinaServer {
	
	private static BrokerLinkMinaServer brokerLinkMinaServer;
	
	public static BrokerLinkMinaServer getBrokerLinkMinaServer()
	{
		if(brokerLinkMinaServer==null)
		{
			synchronized(BrokerLinkMinaServer.class)
			{
				if(brokerLinkMinaServer==null)
				{
					brokerLinkMinaServer= new BrokerLinkMinaServer();
				}
			}
		}
		return brokerLinkMinaServer;
	}
	
	private SocketAcceptor socketServer;	
	private  BrokerLinkMinaServer()
	{
		socketServer = new NioSocketAcceptor();
	}
	
	
	public SocketAcceptor getSocketAcceptor()
	{
		return socketServer;
	}
	
	public Vector<IoSession> getIoSessionVector()
	{
		Vector<IoSession> ioSessionVector = new Vector<IoSession>();
		IoSession session; 
		Map<Long,IoSession> conMap = socketServer.getManagedSessions(); 
		Iterator<Long> iter = conMap.keySet().iterator(); 
		while (iter.hasNext()) { 
			Object key = iter.next();  
	        session = (IoSession)conMap.get(key);  
	        ioSessionVector.add(session);
		}  
		return ioSessionVector;
	}
}
