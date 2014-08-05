package iie.logcabin.clustercontroller.config;

import iie.logcabin.clustercontroller.type.ServerType;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Hashtable;

/**
 * 读取ServiceConfig.xml配置文件中的内容
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class ApplicationConfigReader {
	
	private static ApplicationConfigReader configReader;	
	private BrokerServerConfig brokerServerConfig;
	private ClientServerConfig clientServerConfig;
	
	private Hashtable<String,String> transportHashtable;

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
	
	public BrokerServerConfig getBrokerServerConfig() {
		return brokerServerConfig;
	}

	public ClientServerConfig getClientServerConfig() {
		return this.clientServerConfig;
	}
	
	/**
	 * 根据请求的协议类型，返回相应的通信地址
	 * @param transportProtocol 协议类型
	 * @return 通信地址
	 */
	public String getTransportURI(String transportProtocol){
		return (String) this.transportHashtable.get(transportProtocol);
	}
	
	private ApplicationConfigReader ()
	{
		this.brokerServerConfig = new BrokerServerConfig();
		this.clientServerConfig = new ClientServerConfig();
		this.transportHashtable = new Hashtable<String, String>();
		readApplicationConfig();
	}
	
	private void readApplicationConfig()
	{
		try
		 {	
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docummentBuilder = factory.newDocumentBuilder();      
	        //String projectPath = this.getClass().getResource("/").getPath().substring(1)+"SystemConfig.xml";	         
	        String projectPath = "SystemConfig.xml";
	        Document doc = docummentBuilder.parse(new File(projectPath));	        
	        Element elmtInfo = doc.getDocumentElement();	        
	        NodeList rootNodes = elmtInfo.getChildNodes();
	        for (int i = 0; i < rootNodes.getLength(); i++)
	        {
	        	Node rootNode = rootNodes.item(i);
	        	if(rootNode.getNodeType() == Node.ELEMENT_NODE)//mail
	        	{
	        		
	        		if(rootNode.getNodeName().equals(ServerType.BROKERSERVER))
	        		{
	        			this.readBrokerServerConfig(rootNode);
	        		}
	        		else if(rootNode.getNodeName().equals(ServerType.CLIENTSERVER))
	        		{
	        			this.readClientServerConfig(rootNode);
	        		}
	        		else if(rootNode.getNodeName().equals("transportConnectors")){
	        			this.readTransportProtocol(rootNode);
	        		}
	        	}           	
           }
		 }
		catch(Exception msg)
		{
			log.error("读取ServiceConfig.xml出错，错误信息为："+msg.getMessage());
			throw new RuntimeException(msg);
		}
	}
	
	private void readBrokerServerConfig(Node leafNode)
	{
		try
		{
			NodeList nodeList = leafNode.getChildNodes();
			for(int i = 0; i < nodeList.getLength(); i++)
		    {
				Node node = nodeList.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE)//mail
		        {	        	
					if(node.getNodeName().equals("port"))
					{
						this.brokerServerConfig.setBrokerServerPort(Integer.valueOf(node.getTextContent().toString()));
		        	}
		        	else if(node.getNodeName().equals("channelkey"))
		        	{
		        		this.brokerServerConfig.setChannelKey(node.getTextContent().toString());
		        	}
		        	else if(node.getNodeName().equals("heartbeattime"))
		        	{
		        		this.brokerServerConfig.setHeartBeatTime(Long.valueOf(node.getTextContent().toString()));
		        	}
		        	else if(node.getNodeName().equals("virtualnode"))
		        	{
		        		this.brokerServerConfig.setVirtualNode(Integer.valueOf(node.getTextContent().toString()));
		        	}
		        }           	
	        }
		}
		catch(Exception msg)
		{
			log.error("从配置文件serviceconfig.xml中读取Broker信息时发生错误，错误信息为："+msg.getMessage().toString());
			throw new RuntimeException(msg);
		}
	}
	
	private void readClientServerConfig(Node leafNode)
	{
		try
		{
			NodeList nodeList = leafNode.getChildNodes();
			for(int i = 0; i < nodeList.getLength(); i++)
		    {
				Node node = nodeList.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE)//mail
		        {	        	
					if(node.getNodeName().equals("port"))
					{
						this.clientServerConfig.setProducerServerPort(Integer.valueOf(node.getTextContent().toString()));
		        	}
		        	else if(node.getNodeName().equals("channelkey"))
		        	{
		        		this.clientServerConfig.setChannelKey(node.getTextContent().toString());
		        	}		        	
		        }           	
	        }
		}
		catch(Exception msg)
		{
			log.error("从配置文件serviceconfig.xml中读取Producer信息时发生错误，错误信息为："+msg.getMessage().toString());
			throw new RuntimeException(msg);
		}
	}
	
	private void readTransportProtocol(Node leafNode){
		try
		{
			NodeList nodeList = leafNode.getChildNodes();
			for(int i = 0; i < nodeList.getLength(); i++)
		    {
				Node node = nodeList.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE)
		        {	
					if(node.getNodeName().equals("transportConnector")){
						NamedNodeMap nameNodeMap = node.getAttributes();
						this.transportHashtable.put(nameNodeMap.getNamedItem("name").getNodeValue(),nameNodeMap.getNamedItem("uri").getNodeValue());
						//log.debug(nameNodeMap.getNamedItem("name").getNodeValue()+nameNodeMap.getNamedItem("uri").getNodeValue());
					}
		        }           	
	        }
		}
		catch(Exception msg)
		{
			log.error("从配置文件serviceconfig.xml中读取传输地址信息时发生错误，错误信息为："+msg.getMessage().toString());
			throw new RuntimeException(msg);
		}
	}
	
	private Log log = LogFactory.getLog(ApplicationConfigReader.class);
}
