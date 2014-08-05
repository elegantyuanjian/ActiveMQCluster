package iie.logcabin.clustercontroller.clientlink;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import iie.logcabin.clustercontroller.consistencyhash.ClientServerProtocolFactory;
import iie.logcabin.clustercontroller.protocol.*;

/**
 * 接受客户端的请求
 * 通过请求，返回客户端的请求
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class ClientLinkServerHandlerThread extends Thread{
	private Socket client;
	
	public ClientLinkServerHandlerThread(Socket client)
	{
		this.client = client;
	}
	private static byte[] clientRequestMessage = new byte[1024];
	
	@Override 
	public void run(){
		try {
			DataInputStream input = new DataInputStream(client.getInputStream());
			/**length为读取message的数量**/
			int length = input.read(clientRequestMessage);
			/**请求协议不会大于1024**/
			
			byte[] realRequestMessage = new byte[length];
			for(int i = 0 ; i < length ; i++)
			{
				realRequestMessage[i] = clientRequestMessage[i];
			}
			
			ClientRequestProtocolBuf.ClientRequestProtocol clientRequestProtocol = ClientRequestProtocolBuf.ClientRequestProtocol.parseFrom(realRequestMessage);
			log.debug(clientRequestProtocol.toString());
			
			ClientServerProtocolFactory protocolFactory = new ClientServerProtocolFactory();
			InetSocketAddress clientRequestAddress = (InetSocketAddress)client.getRemoteSocketAddress();
			String clientRequestIP= clientRequestAddress.getAddress().getHostName();
			log.debug(clientRequestIP);
			ServerResponseProtocolBuf.ServerResponseProtocol clientSerProBuf = protocolFactory.getClientServerProtocol(clientRequestProtocol,clientRequestIP);
				
			log.debug(clientSerProBuf.toString());
			
			DataOutputStream out = new DataOutputStream(client.getOutputStream());			
			byte[] buf = clientSerProBuf.toByteArray();
            /**翻译消息中间件与消息中间件通信的地址**/ 
            out.write(buf);
            out.flush();
            out.close();
            
            input.close();
            
			
		} catch (IOException e) {
			log.error("服务器端在接受客户端数据时发生异常,异常信息为："+e.getMessage());		
		}
	}
	
	private Log log =  LogFactory.getLog(ClientLinkServerHandlerThread.class);
}
