package iie.logcabin.brokermonitor.config;

import iie.logcabin.brokermonitor.type.ServerType;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 读取系统配置文件
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class ApplicationConfigReader {
	
	private static ApplicationConfigReader configReader;	
	private ClusterControllerConfig clusterControllerConfig;
	
	public static ApplicationConfigReader getApplicationConfigReader()
	{
		if(configReader==null)
		{
			synchronized(ApplicationConfigReader.class)
			{
				if(configReader==null)
				{
					configReader = new ApplicationConfigReader();
				}
			}
		}
		return configReader;
	}
	
	public ClusterControllerConfig getClusterControllerConfig()
	{
		return this.clusterControllerConfig;
	}
	
	private ApplicationConfigReader ()
	{			
		this.clusterControllerConfig = new ClusterControllerConfig();
		readApplicationConfig();
	}
	
	private void readApplicationConfig()
	{
		try
		 {	
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docummentBuilder = factory.newDocumentBuilder();      
	       // String projectPath = this.getClass().getResource("/").getPath().substring(1)+"SystemConfig.xml";	         
	        String projectPath = "SystemConfig.xml";	         
		       
	        Document doc = docummentBuilder.parse(new File(projectPath));	        
	        Element elmtInfo = doc.getDocumentElement();
	        NodeList rootNodes = elmtInfo.getChildNodes();
	        
	        for (int i = 0; i < rootNodes.getLength(); i++)
	        {
	        	Node rootNode = rootNodes.item(i);
	        	
	        	if(rootNode.getNodeType() == Node.ELEMENT_NODE)//mail
	        	{
	        		log.error(rootNode.getNodeName().toString());
	        		if(rootNode.getNodeName().equals(ServerType.CONTROLLERSERVER))
	        		{
	        			this.readBrokerConfig(rootNode);
	        		}
	        	}           	
           }
		 }
		catch(Exception msg)
		{
			log.error("读取serviceconfig.xml出错，错误信息为："+msg.getMessage());
			throw new RuntimeException(msg);
		}
	}
	
	private void readBrokerConfig(Node leafNode)
	{
		try
		{
			NodeList nodeList = leafNode.getChildNodes();
			for(int i = 0; i < nodeList.getLength(); i++)
		    {
				Node node = nodeList.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE)
		        {	        
					
					if(node.getNodeName().equals("port"))
					{
						this.clusterControllerConfig.setClusterControllerPort(Integer.valueOf(node.getTextContent().toString()));
		        	}
		        	else if(node.getNodeName().equals("channelkey"))
		        	{
		        		this.clusterControllerConfig.setChannelKey(node.getTextContent().toString());
		        	}
		        	else if(node.getNodeName().equals("ip"))
		        	{
		        		this.clusterControllerConfig.setClusterControllerIP(node.getTextContent().toString());
		        	}
		        	else if(node.getNodeName().equals("heartbeattime"))
		        	{
		        		this.clusterControllerConfig.setHeartBeatTime(Long.valueOf(node.getTextContent().toString()));
		        	}
		        	else if(node.getNodeName().equals("monitorport")){
		        		
		        		this.clusterControllerConfig.setMonitorPort(node.getTextContent().toString());
		        	}
		        }           	
	        }
			log.debug(this.clusterControllerConfig.toString());
		}
		catch(Exception msg)
		{
			log.error("从配置文件serviceconfig.xml中读取Broker信息时发生错误，错误信息为："+msg.getMessage().toString());
			throw new RuntimeException(msg);
		}
	}
	private Log log = LogFactory.getLog(ApplicationConfigReader.class);
}
