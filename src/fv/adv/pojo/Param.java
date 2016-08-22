package fv.adv.pojo;

import org.apache.ibatis.type.Alias;

/**
 * sdk发送路径的参数类
 * 
 * @author Administrator
 * 
 */
@Alias("Param")
public class Param {
	private String imei;// 手机串码
	private String imsi;// 手机卡串码
	private String appid;// 应用id;
	private String gysdkv;// sdk版本
	private Integer operation;// 运营商
	private Integer code;// 产品优先级
	private Integer limit;// 提取产品的数量

	// 记录活跃用户
	private String time;

	// 产品操作的相关信息
	private String sid;
	private String sindex;
	private Integer oper;

	
	//ip信息+区域
	private String ip;
	private String area;
	
	//加密密钥key
	private String key;
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getGysdkv() {
		return gysdkv;
	}

	public void setGysdkv(String gysdkv) {
		this.gysdkv = gysdkv;
	}

	public Integer getOperation() {
		return operation;
	}

	public void setOperation(Integer operation) {
		this.operation = operation;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSindex() {
		return sindex;
	}

	public void setSindex(String sindex) {
		this.sindex = sindex;
	}

	public Integer getOper() {
		return oper;
	}

	public void setOper(Integer oper) {
		this.oper = oper;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
