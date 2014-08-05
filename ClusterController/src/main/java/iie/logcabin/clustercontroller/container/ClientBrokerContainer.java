package iie.logcabin.clustercontroller.container;

import iie.logcabin.clustercontroller.protocol.ClientBrokerProtocol;
import iie.logcabin.clustercontroller.type.ClientRequestType;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClientBrokerContainer {
	
	private Log log = LogFactory.getLog(ClientBrokerContainer.class);
	private static  ClientBrokerContainer clientBrokerContainer;
	private Vector<ClientBrokerProtocol> clientBrokerProtocolVector;
	private int messageSender[];
	private int messageReceiver[];
	
	public static ClientBrokerContainer getBrokerContainer(){
		if(clientBrokerContainer==null){
			synchronized(ClientBrokerContainer.class){
				if(clientBrokerContainer==null){
					clientBrokerContainer = new ClientBrokerContainer();
				}
			}
		}
		return clientBrokerContainer;
	}
	
	private ClientBrokerContainer(){
		this.clientBrokerProtocolVector = new Vector<ClientBrokerProtocol>();
		messageSender = new int[24];
		messageReceiver = new int[24];
		for(int i=0 ; i< 24 ;i++){
			messageSender[i] = 0 ;
			messageReceiver[i] = 0 ;
		}
	}
	
	
	public void add(ClientBrokerProtocol clientBrokerProtocol){		
		this.clientBrokerProtocolVector.add(clientBrokerProtocol);
		log.debug(clientBrokerProtocol.getRequestType());
		if(clientBrokerProtocol.getRequestType().equals(ClientRequestType.MESSAGESENDER)){
			this.messageSender[clientBrokerProtocol.getDateTime().getHours()]++;
		}
		else{
			this.messageReceiver[clientBrokerProtocol.getDateTime().getHours()]++;
		}
	}
	
	
	
	public Vector<ClientBrokerProtocol> getBrokerProtocolVector(){
		return this.clientBrokerProtocolVector;
	}
	//private Log log=LogFactory.getLog(ClientLinkMinaServerListener.class);
//	private void getTimeRequestCount(){
//		
//		java.util.Iterator<ClientBrokerProtocol> iterator = this.clientBrokerProtocolVector.iterator();
//		while(iterator.hasNext()){
//			ClientBrokerProtocol clientBrokerProtocol = iterator.next();
//			Date date = clientBrokerProtocol.getDateTime();
//		
//			if(clientBrokerProtocol.getRequestType()==ClientRequestType.MESSAGESENDER){
//				this.messageSender[date.getHours()]++;
//			}else{
//				this.messageReceiver[date.getHours()]++;}
//		}
//	}
	
	public int[] getMessageSenderCount(){
		//getTimeRequestCount();
		return this.messageSender;
	}
	
	public int[] getMessageReceiverCount(){
		//getTimeRequestCount();
		return this.messageReceiver;
	}
}
