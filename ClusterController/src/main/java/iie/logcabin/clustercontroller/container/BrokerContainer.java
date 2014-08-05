package iie.logcabin.clustercontroller.container;
import iie.logcabin.clustercontroller.consistencyhash.ConsistencyHash;
import iie.logcabin.clustercontroller.protocol.BrokerProtocol;

import java.util.Iterator;
import java.util.Vector;


/**
 * 存储BrokerCluster集群中的Broker的基本信息，即BrokerProtocol
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class BrokerContainer {
	
	private static  BrokerContainer brokerContainer;
	private Vector<BrokerProtocol> brokerBeanVector;
	private ConsistencyHash consistencyHash= ConsistencyHash.getConsistencyHash();
	
	public static BrokerContainer getBrokerContainer(){
		if(brokerContainer==null){
			synchronized(BrokerContainer.class){
				if(brokerContainer==null){
					brokerContainer = new BrokerContainer();
				}
			}
		}
		return brokerContainer;
	}
	
	private BrokerContainer(){
		this.brokerBeanVector = new Vector<BrokerProtocol>();
	}
	
	/**
	 * 1.如果是新的BrokerProtocol信息，增加到容器中,同时把这个容器的brokerIP加入到consistencyHash中
	 * 2.如果容器中已经有了BrokerProtocol的信息，更新容器中的值
	 * @param brokerBean 
	 */
	public synchronized void add(BrokerProtocol brokerProtocol){		
		if(this.brokerBeanVector.isEmpty()==true){
			this.brokerBeanVector.add(brokerProtocol);
			consistencyHash.addServerNode(brokerProtocol.getBrokerIP());
		}
		else{
			Iterator<BrokerProtocol> iterator =	brokerBeanVector.iterator();
			Boolean update = false ;
			while(iterator.hasNext()){
				BrokerProtocol tempBrokerProtocol = iterator.next();
				//如果新来的broker,和已有的broker不一样的话，添加或者更新数据
				if(tempBrokerProtocol.getBrokerIP().equals(brokerProtocol.getBrokerIP()))
				{
					this.brokerBeanVector.removeElement(tempBrokerProtocol);
					this.brokerBeanVector.add(brokerProtocol);
					//不做更新
					//consistencyHash.addServerNode(brokerBean.getBrokerIP());
					update = true ;
					break;
				}				
			}
			
			if(update==false){
				this.brokerBeanVector.add(brokerProtocol);
				consistencyHash.addServerNode(brokerProtocol.getBrokerIP());
			}
		}
	}
	
	/**
	 * 当Broker停止服务时，控制器Controller会捕获相应的异常
	 * 在捕获异常时，把异常IP对应的信息BrokerProtocol从brokerContainer和consistencyHash中删除 
	 * @param brokerIP
	 */
	public synchronized void remove(String brokerIP)
	{
		if(this.brokerBeanVector.isEmpty()!=true){
			Iterator<BrokerProtocol> iterator =	brokerBeanVector.iterator();
			while(iterator.hasNext()){
				BrokerProtocol brokerProtocol = iterator.next();
				if(brokerProtocol.getBrokerIP().equals(brokerIP)){
					this.brokerBeanVector.remove(brokerProtocol);
					this.consistencyHash.removeServerNode(brokerProtocol.getBrokerIP());
					break;
				}
			}
		}
	}
	
	public Vector<BrokerProtocol> getBrokerProtocolVector(){
		return this.brokerBeanVector;
	}

}
