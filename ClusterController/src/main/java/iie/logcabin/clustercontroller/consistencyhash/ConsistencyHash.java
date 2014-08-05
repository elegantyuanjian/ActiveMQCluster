package iie.logcabin.clustercontroller.consistencyhash;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 资源分配，采用一致性Hash算法
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class ConsistencyHash {
	
	/**单例模式**/
	private static ConsistencyHash consistencyHash;	
	public static ConsistencyHash getConsistencyHash()
	{
		if(consistencyHash==null)
		{
			synchronized(ConsistencyHash.class)
			{
				if(consistencyHash==null){
					consistencyHash = new ConsistencyHash();
				}
			}
		}
		return consistencyHash;
	}
	
	private ConsistencyHash(){
		/**用TreeMap数据结构模拟一致性Hash算法的环**/
		circleNodes = new TreeMap<Long,Object>();
	}
	
	private TreeMap<Long,Object> circleNodes = null;
	/**防止访问倾斜**/
	private int duplicateNumber=3;
	
	/**
	 * 把新增加的Broker加入一致性Hash中的环性结构中
	 * @param brokerIP Broker服务器的IP地址 
	 * @return 添加成功，返回true
	 */
	public boolean addServerNode(String brokerIP)
	{
		if(brokerIP==null){
			return false;
		}
		for(int i = 0 ; i < duplicateNumber ; i++){
			long key = hash(computeMd5(brokerIP+i));
			circleNodes.put(key, (Object)brokerIP);
		}
		return true;
	}
	
	/**
	 * 把IP为brokerIP的Broker从一致性Hash中环线结构中删除
	 * @param brokerIP
	 * @return 删除成功，返回true
	 */
	public boolean removeServerNode(String brokerIP)
	{
		if(brokerIP==null){
			return false;
		}
		for(int i = 0 ; i < duplicateNumber ;i++){
			long key = hash(computeMd5(brokerIP+i));
			if(this.circleNodes.containsKey(key)){				
				this.circleNodes.remove(key);			
			}			
		}			
		return true;
	}
	

	/**
	 * 根据Client提供的队列名，返回对应的通信路径
	 * @param channelName 
	 * @return Broker的IP地址
	 */
	public String getServerNode(String channelName)
	{
		if(this.circleNodes.size()==0){
			return null;
			//throw new RuntimeException("目前Broker集群中没有Broker开启");
		}
		
		long key = hash(computeMd5(channelName+this.duplicateNumber));		
		SortedMap<Long, Object> tailMap=this.circleNodes.tailMap(key);
        if(tailMap.isEmpty()) {
                key = this.circleNodes.firstKey();
        } else {
                key = tailMap.firstKey();
        }
        String serverIp = (String)this.circleNodes.get(key);
        return serverIp;
	}
	
	/**
     * 根据2^32把节点分布到圆环上面。
     * @param digest
     * @param nTime
     * @return
     */
    private long hash(byte[] digest) {
    	
    	int nTime = 3;
                long rv = ((long) (digest[3+nTime*4] & 0xFF) << 24)
                                | ((long) (digest[2+nTime*4] & 0xFF) << 16)
                                | ((long) (digest[1+nTime*4] & 0xFF) << 8)
                                | (digest[0+nTime*4] & 0xFF);

                return rv & 0xffffffffL; /* Truncate to 32-bits */
          }

    /**
     * 计算MD5后的值
     * @param key Broker的IP地址
     * @return byte[]
     */
    private byte[] computeMd5(String key) {
                MessageDigest md5;
                try {
                        md5 = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException("不支持MD5算法", e);
                }
                md5.reset();
                byte[] keyBytes = null;
                try {
                        keyBytes = key.getBytes("UTF-8");
                }
                catch (UnsupportedEncodingException e) {
                        throw new RuntimeException("Unknown string :" + key, e);
                }

                md5.update(keyBytes);
                return md5.digest();
         }



	public int getDuplicateNumber() {
		return duplicateNumber;
	}
	
	public void setDuplicateNumber(int duplicateNumber) {
		this.duplicateNumber = duplicateNumber;
	}
    
}
