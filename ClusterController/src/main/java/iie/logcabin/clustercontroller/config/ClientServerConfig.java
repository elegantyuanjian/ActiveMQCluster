/**
 * 
 */
package iie.logcabin.clustercontroller.config;

/**
 * 负责与Client通信的服务器的配置信息
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class ClientServerConfig {
	private int producerServerPort;
	private String channelKey;
	
	public int getProducerServerPort() {
		return producerServerPort;
	}
	public void setProducerServerPort(int producerServerPort) {
		this.producerServerPort = producerServerPort;
	}
	public String getChannelKey() {
		return channelKey;
	}
	public void setChannelKey(String channelKey) {
		this.channelKey = channelKey;
	}
	
}
