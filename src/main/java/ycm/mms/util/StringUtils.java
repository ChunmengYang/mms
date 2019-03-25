package ycm.mms.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 字符串工具类。
 * @author Chunmeng
 */
public abstract class StringUtils {
	/**
     * 判断是否为允许的上传文件类型,true表示允许
     */
	public static boolean checkImageFileSuffix(String fileName) {
        //设置允许上传文件类型
        String suffixList = "jpg,png,jpeg";
        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".")
                + 1, fileName.length());
        if (suffixList.contains(suffix.trim().toLowerCase())) {
            return true;
        }
        return false;
    }


	/**
	* 获取url中的参数
	* @param url
	* @param name
	* @return String 
	 */
	public static String getParamByUrl(String url, String name) {
		String pattern = "(^|&)" + name + "=[a-zA-Z0-9]*(&{1})";
		//完整URL
		if(url.indexOf("://")!=-1) {
			pattern = "(\\?|&){1}#{0,1}" + name + "=[a-zA-Z0-9]*(&{1})";
		} else {
			//纯参数
			url += "&";
		}

		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(url);
		if (m.find()) {
			return m.group(0).split("=")[1].replace("&", "");
		} else {
			return null;
		}
	}
	
	/**
	 * 生产随机字符串
	 * @param length 字符串长度
	 * @return 生成的字符串
	 * **/
	public static String random(int length) {
		String str = "2345689ABCDEFGHJKLMNPQRSUVWXYZabcdefhikmnrstuvwxz";
		return RandomStringUtils.random(length, str);
	}
	
	/**
	 * 邮箱校验
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		try {
			// 正常邮箱
			// /^\w+((-\w)|(\.\w))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/
 
			// 个人邮箱
			// [\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+@[\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+\\.[\\w.\\\\+\\-\\*\\/\\=\\`\\~\\!\\#\\$\\%\\^\\&\\*\\{\\}\\|\\'\\_\\?]+
			
			// 含有特殊字符的个人邮箱和正常邮箱
			// /^[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+@[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+(\.[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+)+$/
			 
			// 范围更广的邮箱验证 
			// “/^[^@]+@.+\\..+$/”
			final String patternStr = "/^[^@]+@.+\\..+$/";
 
			final Pattern pattern = Pattern.compile(patternStr);
			final Matcher mat = pattern.matcher(email);
			return mat.matches();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
 
	/**
	 * 总结一下:虚拟运营商、数据卡、物联网、卫星通信、移动网络识别码都不作为正常使用的电话号码,所以需要验证的手机号如下:
	 * 130、131、132、133、134(0-8)、135、136、137、138、139 149
	 * 150、151、152、153、155、156、157、158、159 166、 173、175、176、177、178、
	 * 180、181、182、183、184、185、186、187、188、189 198、199
	 */
	private static final String REGEX_MOBILE = "(134[0-8]\\d{7})" + "|(" + "((13([0-3]|[5-9]))" + "|149"
			+ "|15([0-3]|[5-9])" + "|166" + "|17(3|[5-8])" + "|18[0-9]" + "|19[8-9]" + ")" + "\\d{8}" + ")";
 
	/**
	 * 手机号校验
	 * 
	 * @param tel
	 * @return
	 */
	public static boolean isMobile(String tel) {
		return Pattern.matches(REGEX_MOBILE, tel);
	}
	
	/**
	 * 字符串拼接
	 * @param buffer
	 * @param str
	 * @return
	 * ***/
	public static String concat(String ...str) {
		StringBuffer buffer = new StringBuffer();
		
		int length = str.length;
		for (int i = 0; i < length; i++) {
			buffer.append(str[i]);
        }
		return buffer.toString();
	}
}
