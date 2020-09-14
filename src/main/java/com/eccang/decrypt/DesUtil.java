package com.eccang.decrypt;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.log4j.Logger;
import org.junit.Test;

import sun.misc.BASE64Decoder;


/**
 **************************************************************
 *                                                            *
 *                          深圳易仓科技                                                                                            * 
 *                    https://www.eccang.com/                 *
 *                                                            *
 **************************************************************
 * @Title：DesUtil.java
 * @Description:用一句话来描述这个类的作用
 * @Author: liaoziyang
 * @Date: 2020年9月8日上午9:30:44
 * @Version:1.0
 */
@SuppressWarnings("restriction")
public class DesUtil {

	static Logger LOG =Logger.getLogger(DesUtil.class); 
	
    private final static String DES = "DES";
    private final static String ENCODE = "utf-8";

    @Test
	public static void test() {
        try {
			System.err.println(decrypt("87IWwOm1rRTkJmtoYPFUs+EwTKTIHDaH2dVTmHA2h1c=", "mxITjtVqv"));
		} catch (IOException e) {
			LOG.error(e);
		} catch (Exception e) {
			LOG.error(e);
		}
    }
    
    /**
     * 
     * @MethodName: decrypt
     * @Description: 根据键值进行解密
     * @author liaoziyang
     * @param data
     * @param key
     * @return 返回解密后的密文
     * @throws IOException
     * @throws Exception String
     * @date 2020-09-08 09:20:00
     */
	public static String decrypt(String data, String key) throws IOException,
            Exception {
        if (data == null)
            return null;
        
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf, key.getBytes(ENCODE));
        
        return new String(bt, ENCODE);
    }

    
    /**
     * 
     * @MethodName: decrypt
     * @Description: 根据键值进行解密
     * @author liaoziyang
     * @param data
     * @param key
     * @return 解密后的字节数组
     * @throws Exception byte[]  加密键byte数组
     * @date 2020-09-08 09:21:28
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }
}