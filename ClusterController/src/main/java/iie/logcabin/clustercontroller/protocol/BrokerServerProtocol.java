package iie.logcabin.clustercontroller.protocol;

import java.io.Serializable;
/**
 * ClusterController向BrokerCluster传输的协议
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class BrokerServerProtocol implements Serializable{
	
	
	private static final long serialVersionUID = 4819947869358631848L;
	
	private int requestType ;

	public int getRequestType() {
		return requestType;
	}

	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}
}
