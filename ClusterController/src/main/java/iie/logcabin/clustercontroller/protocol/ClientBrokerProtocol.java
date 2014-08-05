package iie.logcabin.clustercontroller.protocol;

import java.io.Serializable;
import java.util.Date;

/**
 * Client与Broker的映射关系
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class ClientBrokerProtocol implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	private String brokerIP;//消息中间件的
	private String clientIP;
	private String requestType;
	private String channelName;
	
	private Date dateTime;
	
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getBrokerIP() {
		return brokerIP;
	}
	public void setBrokerIP(String brokerIP) {
		this.brokerIP = brokerIP;
	}
	public String getClientIP() {
		return clientIP;
	}
	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	
	
	
}
