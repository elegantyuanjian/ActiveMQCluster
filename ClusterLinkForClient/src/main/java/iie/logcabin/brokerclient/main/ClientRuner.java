package iie.logcabin.brokerclient.main;

import iie.logcabin.brokerclient.clusterlink.ControllerLink;
import iie.logcabin.brokerclient.type.ClientRequestType;
import iie.logcabin.brokerclient.type.TransportProtocolType;

public class ClientRuner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	//	ControllerLinkMinaClient client = new ControllerLinkMinaClient();
		ControllerLink link = new ControllerLink();
		try
		{
			String url = link.getConnectURL(TransportProtocolType.tcp,ClientRequestType.MESSAGERECEIVER,"HelloWorld");
			System.err.println(url);
		}
		catch(Exception msg)
		{
			System.out.println(msg.getMessage());
		}
	}
}
