package iie.logcabin.clustercontroller.clientlink;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 服务器端接受客户端发送来的请求
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class ClientLinkServerListenerThread extends Thread {
	private boolean closed = false;

	private ServerSocket clientServer = ClientLinkServer.ClientLinkServer.getServerSocket();
	
	public  void requesTerminal()
	{
		this.closed = true;
		this.interrupt();
	}
	
	@Override
	public void run() 
	{
		while(!closed)
		{
			try 
			{
				Socket client = clientServer.accept();
				Thread thread = new ClientLinkServerHandlerThread(client);
				thread.start();
				
			} 
			catch (IOException e) 
			{
				log.error("服务器端接受客户端时出现异常，错误信息为："+e.getMessage());
			}
		}
	}
	
	private Log log = LogFactory.getLog(ClientLinkServerListenerThread.class);
}
