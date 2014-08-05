package iie.logcabin.brokermonitor.config;

/**
 * 连接ClusterController的基本信息
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class ClusterControllerConfig {
	
	private int clusterControllerPort;
	private String channelKey;
	private String clusterControllerIP;
	private Long  heartBeatTime;
	private String monitorPort;
	
	public String getMonitorPort() {
		return monitorPort;
	}
	public void setMonitorPort(String monitorPort) {
		this.monitorPort = monitorPort;
	}
		
	public Long getHeartBeatTime() {
		return heartBeatTime;
	}
	public void setHeartBeatTime(Long heartBeatTime) {
		this.heartBeatTime = heartBeatTime;
	}

	public String getChannelKey() {
		return channelKey;
	}
	public void setChannelKey(String channelKey) {
		this.channelKey = channelKey;
	}
	
	public int getClusterControllerPort() {
		return clusterControllerPort;
	}
	public void setClusterControllerPort(int clusterControllerPort) {
		this.clusterControllerPort = clusterControllerPort;
	}
	public String getClusterControllerIP() {
		return clusterControllerIP;
	}
	public void setClusterControllerIP(String clusterControllerIP) {
		this.clusterControllerIP = clusterControllerIP;
	}
	@Override
	public String toString() {
		return this.clusterControllerIP+":"+this.clusterControllerPort;
	}
	
	

	
}
