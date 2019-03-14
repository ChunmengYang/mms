package ycm.mms.util;

import java.security.MessageDigest;

/**
 * MD5工具类。
 * @author Chunmeng
 */
public class MD5Utils {
    
    public static String encode(String str){
    	String result = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes());
            byte[] b = md5.digest();
            int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			result = buf.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return result;
    }
}