package fv.adv.service;

import java.util.List;

import fv.adv.pojo.Param;

/**
 * 请求服务类
 * @author Administrator
 *
 */
public interface ReqService {

	/**
	 * 测试id请求数据
	 * @param param
	 * @return
	 */
	public List<Object> getTestResult(Param param);

	/**
	 * 广告开关的验证
	 * @param param
	 * @return
	 */
	public boolean isSwitch(Param param);

	/**
	 * 正式id的请求
	 * @param param
	 * @return
	 */
	public List<Object> getResult(Param param);

	/**
	 * 内测时，根据用户删除相对应用户的产品索引记录
	 * @param param
	 * @return
	 */
	public boolean delReq(Param param);

}
