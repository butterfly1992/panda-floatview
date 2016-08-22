package fv.adv.service;

import fv.adv.pojo.Param;

/**
 * 操作服务类
 * 
 * @author Administrator
 * 
 */
public interface OperService {

	/**
	 * 产品操作
	 * 
	 * @param param
	 * @return
	 */
	public Object oper(Param param);

	/**
	 * 记录活跃用户
	 * 
	 * @param param
	 * @return
	 */
	public int recordUser(Param param);

	/**
	 * 查询产品id
	 * 
	 * @param sindex
	 * @return
	 */
	public String getsid(String sindex);
}
