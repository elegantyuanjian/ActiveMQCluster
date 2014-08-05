package iie.logcabin.brokerclient.clusterlink;

import iie.logcabin.brokerclient.config.ApplicationConfigReader;
import iie.logcabin.brokerclient.config.ClientServerConfig;
import iie.logcabin.clustercontroller.protocol.ClientRequestProtocolBuf;
import iie.logcabin.clustercontroller.protocol.ServerResponseProtocolBuf;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class ControllerLink {
	private ApplicationConfigReader cfgReader = ApplicationConfigReader.getApplicationConfigReader();
	
	private ClientServerConfig clientServerConfig = cfgReader.getClientServerConfig();
	
	
	/**
	 * 客户端通过使用Socket远程连接 获取与分布式集群的通信地址
	 * @param transportProtocol 通信协议类型
	 * @param channelName 通道名，如果该通道名应该保持唯一性
	 * @param requestType 是消息的生产者，还是消息的消费者
	 * @return 返回与分布式集群的通信地址
	 */
	public String getConnectURL(String transportProtocol,String requestType, String channelName )
	{
		Socket socket = null ;
		try
		{
			socket = new Socket(clientServerConfig.getClientServerIP(),clientServerConfig.getClientServerPort());
			
						
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			ClientRequestProtocolBuf.ClientRequestProtocol.Builder builder = ClientRequestProtocolBuf.ClientRequestProtocol.newBuilder();
			builder.setTransportProtocol(transportProtocol);
			builder.setChannelName(channelName);
			builder.setRequestType(requestType);
			
			
			ClientRequestProtocolBuf.ClientRequestProtocol requestProtocol = builder.build();
			
			byte[] requestByte = requestProtocol.toByteArray();
			output.write(requestByte);
			output.flush();
			//output.close();
			
			DataInputStream input = new DataInputStream(socket.getInputStream());
			byte[] resultByte = new byte[1024];
			int length = input.read(resultByte);
			byte[] protocolByte = new byte[length];
			for(int i = 0 ; i < length ; i++)
			{
				protocolByte[i] = resultByte[i];
			}
			
			ServerResponseProtocolBuf.ServerResponseProtocol resultProtocol = ServerResponseProtocolBuf.ServerResponseProtocol.parseFrom(protocolByte);
			
			output.close();
			input.close();
			
			return resultProtocol.getTransportUrl();
			
		} 
		catch (UnknownHostException e) 
		{
			throw new RuntimeException("控制器服务器主机名未知，错误信息为："+e.getMessage());
		} catch (IOException e) 
		{
			throw new RuntimeException("IO读写异常，异常信息为："+e.getMessage());
		}		
	}
}
