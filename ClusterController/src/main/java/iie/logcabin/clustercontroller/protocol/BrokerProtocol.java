package iie.logcabin.clustercontroller.protocol;

import java.io.Serializable;
/**
 * BrokerCluster向ClusterController传输的协议
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */

public class BrokerProtocol implements Serializable
{
	private static final long serialVersionUID = 1L;
		
	private String brokerIP;
	private String brokerPort;//监控端口
	
	private String brokerName;//消息中间件的名称
	
	//Queue消费者的数目
	private Long totalQueueConsumer;
	
	//Queue生成者的数目
	private Long totalQueueProducer;
	
	//Topic消费者的数目
	private Long totalTopicConsumer;
	
	//Topic生成者的数目
	private Long totalTopicProducer;
	
	private String dateTime;//发送消息的时间
	

	public String getBrokerName() {
		return brokerName;
	}

	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}


	public Long getTotalQueueConsumer() {
		return totalQueueConsumer;
	}

	public void setTotalQueueConsumer(Long totalQueueConsumer) {
		this.totalQueueConsumer = totalQueueConsumer;
	}

	public Long getTotalQueueProducer() {
		return totalQueueProducer;
	}

	public void setTotalQueueProducer(Long totalQueueProducer) {
		this.totalQueueProducer = totalQueueProducer;
	}

	public Long getTotalTopicConsumer() {
		return totalTopicConsumer;
	}

	public void setTotalTopicConsumer(Long totalTopicConsumer) {
		this.totalTopicConsumer = totalTopicConsumer;
	}

	public Long getTotalTopicProducer() {
		return totalTopicProducer;
	}

	public void setTotalTopicProducer(Long totalTopicProducer) {
		this.totalTopicProducer = totalTopicProducer;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public String getBrokerIP() {
		return brokerIP;
	}

	public void setBrokerIP(String brokerIP) {
		this.brokerIP = brokerIP;
	}

	public String getBrokerPort() {
		return brokerPort;
	}

	public void setBrokerPort(String brokerPort) {
		this.brokerPort = brokerPort;
	}
	
	public String toString(){
		return this.brokerName + this.totalQueueConsumer + this.totalQueueProducer + this.totalTopicConsumer + this.totalTopicProducer;
	}
}
