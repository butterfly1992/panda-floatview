package fv.adv.tool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;

import com.mbl.base64.BackAES;

import fv.adv.pojo.Param;

/**
 * 工具类
 * @author Administrator
 *
 */
public class Utils {

	public static int PANDA_ADV_TIMEFLAG = 2;
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public static Logger log = Logger.getLogger("Utils");

	public static String getID(String tem_name) {
		String id = "";
		int nums = 0;
		for (int i = 0; i < tem_name.length(); i++) {
			int num = (int) tem_name.charAt(i);// 返回指定索引处的char值
			nums += num;
		}
		for (int i = 0; i < 4; i++) {
			id = id + new Random().nextInt(9);
		}
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyyMMddHHmmss");
		String ctime = format.format(new Date());
		id = ctime + nums + id;
		return id;
	}

	/**
	 * null检测
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNULL(String str) {
		if (str == null || str.length() <= 0 || str.equals("null")
				|| str.equals("NULL")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 转码
	 * 
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static String toIso(String str) throws IOException {
		str = new String(str.getBytes("UTF-8"), "iso-8859-1");
		return str;
	}

	public static String toUtf(String str) throws IOException {
		if (str == null)
			return "";
		str = new String(str.getBytes("iso-8859-1"), "UTF-8");
		return str;
	}

	public static String toGB(String str) throws IOException {
		str = new String(str.getBytes("iso-8859-1"), "GB2312");
		return str;
	}

	/**
	 * 获得hashcode
	 * 
	 * @param keyword
	 * @return
	 */
	public static int Change(String keyword) {
		int nums = 0;
		for (int i = 0; i < keyword.length(); i++) {
			int num = (int) keyword.charAt(i);// 返回指定索引处的char值
			nums += num;
		}
		return nums;
	}

	/**
	 * 检测生肖等
	 * 
	 * @param item
	 * @param para
	 * @param len
	 * @return
	 */
	public static String checkItem(String item, String para, int len) {
		return checkItem("item", item, para, len);
	}

	public static String checkItem(String paraItem, String item, String para,
			int len) {
		String msg = "";
		if (isNULL(item)) {
			msg = paraItem + "(" + para + ")参数不可以为空";
		} else if (!item.matches("^\\d+$")) {
			msg = paraItem + "(" + para + ")参数不正确";
		} else {
			int name_num = Integer.parseInt(item);
			if (name_num > len || name_num <= 0) {
				msg = paraItem + "(" + para + ")参数不正确";
			}
		}
		return msg;
	}

	/**
	 * 检测日期
	 * 
	 * @param nm
	 * @return
	 */
	public static String checkDate(String nm) {
		String reg = "^[0-9]{4}-[0-1]{0,1}[0-9]{1}-[0-3]{0,1}[0-9]{1}$";
		if (isNULL(nm)) {
			return "(日期)参数不可以为空";
		}
		if (!nm.matches(reg)) {
			return "(日期)格式不正确，应为xxxx-x-x";
		}
		return "";
	}

	/**
	 * 获取是对应格式的当天日期
	 * 
	 * @return
	 */
	public static String DateTime(String mate) {
		// yyyyMMdd hh:mm:ss
		SimpleDateFormat format = new SimpleDateFormat(mate);
		Date date = new Date();
		String s = format.format(date);
		return s;
	}

	/**
	 * 获取对应格式的昨天日期
	 * 
	 * @param mate
	 * @return
	 */
	public static String getYesterday(String mate) {
		// yyyyMMdd hh:mm:ss
		SimpleDateFormat format = new SimpleDateFormat(mate);
		Date date = new Date(new Date().getTime() - 1000 * 60 * 60 * 24);
		String s = format.format(date);
		return s;
	}

	public static String DateTime() {
		// yyyy-MM-dd
		Date date = new Date();
		String s = format.format(date);
		return s;
	}

	/**
	 * 获取是对应格式的日期
	 * 
	 * @return
	 */
	public static String DateTime(String mate, String date) {
		// yyyy-M-d
		SimpleDateFormat format = new SimpleDateFormat(mate);
		Date d = null;
		try {
			d = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
		String s = format.format(d);
		return s;
	}

	/**
	 * 获取是本月的第几周
	 * 
	 * @return
	 */
	public static int getWeek() {
		Calendar c = Calendar.getInstance();
		int week = c.get(Calendar.WEEK_OF_MONTH);
		return week;
	}

	/**
	 * 获致是本周的今天周几, 1代表星期天...7代表星期六
	 * 
	 * @return
	 */
	public static String getTodayOfWeek() {
		String[] weeks = { "日", "一", "二", "三", "四", "五", "六" };
		Calendar c = Calendar.getInstance();
		int day = c.get(Calendar.DAY_OF_WEEK);
		return "星期" + weeks[day - 1];// day=1,表示星期日
	}

	/**
	 * 获致是本周的第明天星期几, 1代表星期天...7代表星期六
	 * 
	 * @return
	 */
	public static String getTomorrowOfWeek() {
		String[] weeks = { "", "一", "二", "三", "四", "五", "六", "日" };
		Calendar c = Calendar.getInstance();
		int day = c.get(Calendar.DAY_OF_WEEK);// day=1,表示星期日
		return "星期" + weeks[day];
	}

	/**
	 * 字符串转码
	 * 
	 * @return
	 */
	public static String formatStr(String str) {
		String strs = "";
		try {
			strs = new String(str.getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return strs;
	}

	/**
	 * 获取字符串的MD5码
	 **/
	public static String getMD5(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

	public static String getCodeid(String str) {
		return (str.hashCode() + "").replace("-", "");
	}


	/**
	 * 获取根据用户的手机卡串码指定用户的
	 **/
	public static Param analyzeImsi(Param param) {
		String imsi = param.getImsi();
		// 判断是何种运营商 0:所有 1:移动 2:联通 3:电信 4:铁通
		Integer carrieroperator = 0;
		if (imsi == null) {
			param.setImsi("0imsi");
		} else {
			String mnc = imsi.substring(0, 5);
			if ("46000".equals(mnc) || "46002".equals(mnc)
					|| "46007".equals(mnc)) {
				// 是移动用户
				carrieroperator = 1;
			} else if ("46001".equals(mnc) || "46006".equals(mnc)) {
				// 是联通用户
				carrieroperator = 2;
			} else if ("46003".equals(mnc) || "46005".equals(mnc)) {
				// 是电信用户
				carrieroperator = 3;
			} else if ("46020".equals(mnc)) {
				// 是铁通用户
				carrieroperator = 0;
			}  
		}
		param.setOperation(carrieroperator);
		return param;
	}

	public static void main(String[] aa) {
		Param p = new Param();
		p.setImsi("460018887609049");
		analyzeImsi(p);
		// Param a = analyzeImsi();
	}
	/**
	 * 解析路径获得参数
	 * 
	 * @param 加密内容
	 * @param 应用id
	 * @param 用户设备串码
	 * @return
	 */
	public static String jiexiUrl(String key, String appid, String imei) {
		String decryptString = null;
		try {
			String skey = "sp_" + appid.substring(0, 8) + imei.substring(imei.length() - 3, imei.length());
			decryptString = BackAES.decrypt(key, skey, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decryptString;
	}

}
