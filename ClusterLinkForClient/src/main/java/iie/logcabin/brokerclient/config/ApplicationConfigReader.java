package iie.logcabin.brokerclient.config;


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
 * 系统配置文件
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class ApplicationConfigReader {
	
	private static ApplicationConfigReader reader;
	private ClientServerConfig clientServerConfig;

	public static ApplicationConfigReader getApplicationConfigReader()	{
		if(reader==null)
		{
			synchronized(ApplicationConfigReader.class)
			{
				if(reader==null)
				{
					reader = new ApplicationConfigReader();
				}
			}
		}
		return reader;
	}
	
	public ClientServerConfig getClientServerConfig() {
		return clientServerConfig;
	}

	
	private ApplicationConfigReader ()	{
		this.clientServerConfig = new ClientServerConfig();		
		readApplicationConfig();
	}
	
	private void readApplicationConfig()
	{
		try
		 {	
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docummentBuilder = factory.newDocumentBuilder();      
	       // String projectPath  = this.getClass().getResource("/").getPath().substring(1)+"ClusterLinkForClient.xml";	         
	        
	        String projectPath="ClusterLinkForClient.xml";
	        Document doc = docummentBuilder.parse(new File(projectPath));	        
	        Element elmtInfo = doc.getDocumentElement();	        
	        NodeList rootNodes = elmtInfo.getChildNodes();
	        for (int i = 0; i < rootNodes.getLength(); i++)
	        {
	        	Node rootNode = rootNodes.item(i);
	        	if(rootNode.getNodeType() == Node.ELEMENT_NODE)
	        	{	        		
	        		if(rootNode.getNodeName().equals("controllerServer"))
	        		{	        		
	        			this.readClientServerConfig(rootNode);
	        		}
	        	}           	
           }
		 }
		catch(Exception msg)
		{
			log.error("读取serviceconfig.xml出错，错误信息为："+msg.getMessage());
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
						this.clientServerConfig.setClientServerPort(Integer.valueOf(node.getTextContent().toString()));
		        	}
		        	else if(node.getNodeName().equals("channelkey"))
		        	{
		        		this.clientServerConfig.setChannelKey(node.getTextContent().toString());
		        	}		
		        	else if(node.getNodeName().equals("ip")){
		        		this.clientServerConfig.setClientServerIP(node.getTextContent().toString());
		        	}
		        }           	
	        }
		}
		catch(Exception msg)
		{
			log.error("从配置文件serviceconfig.xml中读取Producer信息时发生错误，错误信息为："+msg.getMessage().toString());
		}
	}
	
	private Log log = LogFactory.getLog(ApplicationConfigReader.class);
}
