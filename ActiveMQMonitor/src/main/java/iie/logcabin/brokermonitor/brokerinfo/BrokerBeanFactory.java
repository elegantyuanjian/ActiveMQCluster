package iie.logcabin.brokermonitor.brokerinfo;



import iie.logcabin.brokermonitor.config.ApplicationConfigReader;
import iie.logcabin.brokermonitor.controllerlink.ControllerLinkMinaClient;
import iie.logcabin.brokermonitor.type.TransportType;
import iie.logcabin.clustercontroller.protocol.BrokerProtocol;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.broker.jmx.TopicViewMBean;
import org.apache.activemq.web.RemoteJMXBrokerFacade;
import org.apache.activemq.web.config.SystemPropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 获取监控的ActiveMQ的信息
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */

public class BrokerBeanFactory 
{
	private Log log  = LogFactory.getLog(BrokerBeanFactory.class);
	private BrokerViewMBean brokerAdmin ;
	private RemoteJMXBrokerFacade remoteJmxBrokerFacade;
	private ControllerLinkMinaClient brokerClient = ControllerLinkMinaClient.getBrokerLinkMinaClient();
	
	public BrokerBeanFactory()
	{
		
		remoteJmxBrokerFacade  = new RemoteJMXBrokerFacade();		
		SystemPropertiesConfiguration configuration = new SystemPropertiesConfiguration();  
		System.setProperty("webconsole.jmx.url","service:jmx:rmi:///jndi/rmi://127.0.0.1:11099/jmxrmi");
		/**当两个broker在同一台电脑上运行的时候，这个监控端口11099就会有作用***/
		/**用来区分，到底是监控那一个broker的，设置在**/
		/**<managementContext createConnector="true" connectorPort="11098" />**/		
		/**同时，如果两台broker能够运行成功，那么他们的conf中的activemq.xml节点<transportConnector>的ip和端口组合不能相同**/
		/**在第一版本中，只支持一个ip对应一个broker**/
		/**同时连接路径，保存在控制器端，不由本地上传**/
		/**那么producer和comsumer获取的地址，就应该是ip和<transportConnector>节点的组合**/
		/**那么在brokerBean中的端指的时空broker的端口，例如上面的11099，而ip指的是broker所在物理机的ip**/
		remoteJmxBrokerFacade.setConfiguration(configuration);
		/**貌似11099是不好获取的了***/

//		ManagementContext context =this.remoteJmxBrokerFacade.getManagementContext();
//		log.debug("连接信息："+context.getConnectorPath()+context.getConnectorPort());
		
		//上面的代码会发生异常
	
		try 
		{
			brokerAdmin = remoteJmxBrokerFacade.getBrokerAdmin();
		} 
		catch (Exception e) {
			log.error("实例化BrokerAdmin时出错，错误信息为："+e.getMessage().toString());
			throw new RuntimeException(e);
		}	
	}
	
	public BrokerProtocol getBrokerBean()
	{
		BrokerProtocol brokerBean = new BrokerProtocol();
		try {
			brokerBean.setBrokerName(brokerAdmin.getBrokerName());//broker的名称
			
			brokerBean.setTotalQueueConsumer(getTotalQueueConsumer());
			brokerBean.setTotalQueueProducer(getTotalQueueProducer());
			brokerBean.setTotalTopicConsumer(getTotalTopicConsumer());
			brokerBean.setTotalTopicProducer(getTotalTopicProducer());
			
			brokerBean.setDateTime(getDataTime());
			
			InetSocketAddress socketAddress = (InetSocketAddress)this.brokerClient.getIOSession().getLocalAddress();
			//brokerBean.setUrl(socketAddress.getAddress()+":"+socketAddress.getPort());
			brokerBean.setBrokerIP(socketAddress.getAddress().getHostAddress());
			log.debug("BrokerIP="+brokerBean.getBrokerIP());
			brokerBean.setBrokerPort(ApplicationConfigReader.getApplicationConfigReader().getClusterControllerConfig().getMonitorPort());
			
			return brokerBean;
		}
		catch(Exception msg)
		{
			return brokerBean;
		}
	}
	
	
	public String getBrokerString(String type)
	{
		return brokerAdmin.getTransportConnectorByType(type); //根据type 获取相应的Url
	}
	
	public Hashtable<String,String> getHashTable()
	{
		
		Hashtable<String,String> connectorHashTable = new Hashtable<String,String>();
		for(String type: TransportType.transportType)
		{
			connectorHashTable.put(type, this.brokerAdmin.getTransportConnectorByType(type));
			log.debug(this.brokerAdmin.getTransportConnectorByType(type));
		}
		return connectorHashTable;
	}
	
	private String getDataTime()
	{
		Date date = new Date();
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        return formatter.format(date);		
	}
	
	private long getTotalQueueConsumer()
	{
		long totalCount = 0 ;
		try {
			Collection<QueueViewMBean> queueCollection = remoteJmxBrokerFacade.getQueues();
			//this.remoteJmxBrokerFacade.getc
			for(QueueViewMBean viewMBean : queueCollection)
			{
				totalCount += viewMBean.getConsumerCount();
			}
			return totalCount ;
		} catch (Exception e) {
			log.error("在初始化QueueConsumerCount时出错");
			return 0 ;
		}
	}
	
	private long getTotalQueueProducer()
	{
		long totalCount = 0 ;
		try {
			Collection<QueueViewMBean> queueCollection = remoteJmxBrokerFacade.getQueues();
			
			for(QueueViewMBean viewMBean : queueCollection)
			{
				totalCount += viewMBean.getProducerCount();
			}
			return totalCount ;
		} catch (Exception e) {
			log.error("在初始化QueueProducerCount时出错");
			return 0 ;
		}
	}
	
	private long getTotalTopicConsumer()
	{
		long totalCount = 0 ;
		try
		{
			Collection<TopicViewMBean> topicCollection = remoteJmxBrokerFacade.getTopics();
			for(TopicViewMBean viewMBean : topicCollection)
			{
				totalCount += viewMBean.getConsumerCount();
			}
			return totalCount;
		}
		catch(Exception e)
		{
			log.error("在初始化TopicConsumerCount");
			return 0;
		}
	}
	
	private long getTotalTopicProducer()
	{
		long totalCount = 0 ;
		try
		{
			Collection<TopicViewMBean> topicCollection = remoteJmxBrokerFacade.getTopics();
			for(TopicViewMBean viewMBean : topicCollection)
			{
				totalCount += viewMBean.getProducerCount();
			}
			return totalCount;
		}
		catch(Exception e)
		{
			log.error("在初始化TopicConsumerCount");
			return 0;
		}
	}
}
