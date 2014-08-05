package iie.logcabin.clustercontroller.clientlink;

import iie.logcabin.clustercontroller.config.ApplicationConfigReader;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 使用Enum方式实现单例方式 简洁 高效
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */

public enum ClientLinkServer 
{
	
	ClientLinkServer;
	/**从配置文件中读取端口号**/
	private final int serverPort = ApplicationConfigReader.getApplicationConfigReader().getClientServerConfig().getProducerServerPort();
	private ServerSocket socketServer ;
	
	private ClientLinkServer(){
		try {
			log.debug("实例化1");
			socketServer = new ServerSocket(this.serverPort);
		} catch (IOException e) {
			log.error("ClientLinkServer 启动失败，失败原因为"+e.getMessage());
		}
	}
	
	public ServerSocket getServerSocket(){
		return socketServer;
	}
	
	private Log log = LogFactory.getLog(ClientLinkServer.class);
	
}
