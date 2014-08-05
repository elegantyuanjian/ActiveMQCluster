package iie.logcabin.clustercontroller.crypto;

import iie.logcabin.clustercontroller.config.ApplicationConfigReader;
import iie.logcabin.clustercontroller.type.ServerType;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 对通信协议进行加解密
 * @author 罗元剑
 * @email elegantyuanjian@gmail.com
 * @version AQBalancer 2014-02-01/01version
 */
public class CryptoFactory {
	
	private  String keyValue = "";
	
	public CryptoFactory(String serverType)
	{
		ApplicationConfigReader configReader = ApplicationConfigReader.getApplicationConfigReader();	
		
		if(serverType.equals(ServerType.BROKERSERVER))
		{
			keyValue = configReader.getBrokerServerConfig().getChannelKey();
		}
		else if(serverType.equals(ServerType.CLIENTSERVER))
		{			
			keyValue = configReader.getClientServerConfig().getChannelKey();
		}
		else
		{
			log.error("没有对应的服务类型");
		}
	}
	
	/**
	 * 解密 把SealedObject对象解密成Object
	 * @param Object
	 * @return CommandProtocal
	 */
	public Object decryption(SealedObject sealedObject) 
	{
		Key key = getKey(keyValue.getBytes());//keyGenerator.generateKey();		
		try {
			Object object= sealedObject.getObject(key);
			return object;
		}
		catch (InvalidKeyException e) {
			log.error("对Protocal协议解密时，发生错误"+e.getMessage().toString());
			throw new RuntimeException(e);
		} 
		catch (NoSuchAlgorithmException e){
			log.error("对Protocal协议解密时，发生错误"+e.getMessage().toString());
			throw new RuntimeException(e);
		}
		
		catch (IOException e){
			log.error("对Protocal协议解密时，发生错误"+e.getMessage().toString());
			throw new RuntimeException(e);
		} 
		catch (ClassNotFoundException e) {
			log.error("对Protocal协议解密时，发生错误"+e.getMessage().toString());
			throw new RuntimeException(e);
		}		
	}
	
	/**
	 * 加密 把Object对象加密为SealObject对象
	 * @param Object 
	 * @return SealedObject 
	 */
	public  SealedObject encryption(Object object){
		Key key = getKey(keyValue.getBytes());//keyGenerator.generateKey();
	     
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("DES");
			 try {
				cipher.init(Cipher.ENCRYPT_MODE, key);
			} catch (InvalidKeyException e) {
				log.error("对Protocal协议加密时，发生错误"+e.getMessage().toString());
				throw new RuntimeException(e);
			}
			SealedObject sealObject;
			try {
				sealObject = new SealedObject((Serializable) object,cipher);
				return  sealObject;
			} catch (IllegalBlockSizeException e) {
				log.error("对Protocal协议加密时，发生错误"+e.getMessage().toString());
				throw new RuntimeException(e);
			} catch (IOException e) {
				log.error("对Protocal协议加密时，发生错误"+e.getMessage().toString());
				throw new RuntimeException(e);
			}
			
		
		} catch (NoSuchAlgorithmException e) {
			log.error("对Protocal协议加密时，发生错误"+e.getMessage().toString());
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			log.error("对Protocal协议加密时，发生错误"+e.getMessage().toString());
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 把bit流转变为Key
	 * @param arrBTmp bit流
	 * @return Key 秘钥
	 */
	private  Key getKey(byte[] arrBTmp)
	 {
		  // 创建一个空的8位字节数组（默认值为0）   
		  byte[] arrB = new byte[8];

		  // 将原始字节数组转换为8位   
		  for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
		  arrB[i] = arrBTmp[i];
		  }
		  // 生成密钥   
		  Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
		  return key;
	}
	
	private Log log = LogFactory.getLog(CryptoFactory.class);
}
