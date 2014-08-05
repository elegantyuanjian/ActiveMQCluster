/**
 * 
 */
package iie.logcabin.clustercontroller.config;

/**
 * 负责与Broker通信的服务器端的配置信息
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class BrokerServerConfig {

	private int brokerServerPort;
	private String channelKey;
	private Long heartBeatTime;
	private int virtualNode;
	
	
	public int getBrokerServerPort() {
		return brokerServerPort;
	}
	public void setBrokerServerPort(int brokerServerPort) {
		this.brokerServerPort = brokerServerPort;
	}
	
	public String getChannelKey() {
		return channelKey;
	}
	public void setChannelKey(String channelKey) {
		this.channelKey = channelKey;
	}
	public Long getHeartBeatTime() {
		return heartBeatTime;
	}
	public void setHeartBeatTime(Long heartBeatTime) {
		this.heartBeatTime = heartBeatTime;
	}
	public int getVirtualNode() {
		return virtualNode;
	}
	public void setVirtualNode(int virtualNode) {
		this.virtualNode = virtualNode;
	}
	
	public String toString(){
		return this.brokerServerPort+this.channelKey+this.heartBeatTime+this.virtualNode;
	}
}
