package fv.adv.pojo;

import org.apache.ibatis.type.Alias;

/**
 * 返回产品数据
 * @author Administrator
 *
 */
@Alias("Soft")
public class Soft {
	private String id;
	private String name;
	private String apkurl;
	private String wareindex;
	private String pck;
	private String icon;
	private Integer code;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	} 
	public String getApkurl() {
		return apkurl;
	}
	public void setApkurl(String apkurl) {
		this.apkurl = apkurl;
	}
	public String getWareindex() {
		return wareindex;
	}
	public void setWareindex(String wareindex) {
		this.wareindex = wareindex;
	}
	public String getPck() {
		return pck;
	}
	public void setPck(String pck) {
		this.pck = pck;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	 
}
