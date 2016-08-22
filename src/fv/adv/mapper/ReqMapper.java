package fv.adv.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import fv.adv.pojo.Param;
import fv.adv.pojo.Soft;

/**
 * 请求映射接口
 * 
 * @author Administrator
 * 
 */
public interface ReqMapper {

	/**
	 * 返回测试id的请求信息
	 * 
	 * @param param
	 * @return
	 */
	List<Soft> getTestResult(Param param);

	/**
	 * 验证开关是否打开
	 * 
	 * @param param
	 * @return
	 */
	@Select("select id from zy_app where id=#{appid} and fv_open_status=1")
	// fv_open_status
	@Results({ @Result(column = "id", property = "appid") })
	public Param isSwitch(Param param);

	/**
	 * 正式id的请求
	 * 
	 * @param param
	 * @return
	 */
	List<Soft> getResult(Param param);

	/**
	 * 删除
	 * 
	 * @param param
	 * @return
	 */
	@Delete("DELETE FROM fv_ori_req WHERE imei=#{imei} and imsi=#{imsi} ")
	public int delReq(Param param);
	
	@Update("UPDATE zy_appori_area SET times=times+1 WHERE appid=#{appid} and time=#{time} and area=#{limit} ")
	public int updateIpArea(Param param);
	@Insert("INSERT INTO zy_appori_area VALUES(#{appid},#{time},#{limit},1);")
	public int insertIpArea(Param param);//插入app地区分布方法
}
