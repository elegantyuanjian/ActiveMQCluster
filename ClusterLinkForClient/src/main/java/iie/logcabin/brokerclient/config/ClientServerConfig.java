/**
 * 
 */
package iie.logcabin.brokerclient.config;

/**
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class ClientServerConfig {
	
	private int clientServerPort;
	private String clientServerIP;
	private String channelKey;
	
	public int getClientServerPort() {
		return clientServerPort;
	}
	public void setClientServerPort(int clientServerPort) {
		this.clientServerPort = clientServerPort;
	}
	public String getChannelKey() {
		return channelKey;
	}
	public void setChannelKey(String channelKey) {
		this.channelKey = channelKey;
	}
	public String getClientServerIP() {
		return clientServerIP;
	}
	public void setClientServerIP(String clientServerIP) {
		this.clientServerIP = clientServerIP;
	}
	
}
