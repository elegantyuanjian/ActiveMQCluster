package iie.logcabin.clustercontroller.protocol;

import java.io.Serializable;

public class BrokerServerProtocol implements Serializable{	
	
	private static final long serialVersionUID = 4819947869358631848L;
	
	private int requestType ;

	public int getRequestType() 
	{
		return requestType;
	}

	public void setRequestType(int requestType) 
	{
		this.requestType = requestType;
	}
}
