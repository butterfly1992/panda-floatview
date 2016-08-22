package fv.adv.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import fv.adv.pojo.Param;
import fv.adv.pojo.Req;

/**
 * 操作映射接口
 * 
 * @author Administrator
 * 
 */
public interface OperMapper {

	/**
	 * 操作记录
	 * 
	 * @param param
	 * @return
	 */
	public int oper(Param param);

	/**
	 * 加入活跃用户记录
	 * 
	 * @param param
	 * @return
	 */
	public int recordaddUser(Param param);

	/**
	 * 更新用户活跃记录
	 * 
	 * @param param
	 * @return
	 */
	public int recordupdUser(Param param);

	/**
	 * 根据产品索引查出产品id
	 */
	@Select("SELECT id FROM zy_soft WHERE wareindex =#{sindex}")
	public String getsid(String sindex);

	/**
	 * 根据用户查出相对应的操作索引记录
	 * 
	 * @param param
	 * @return
	 */
	@Select("SELECT imei,imsi, IFNULL(CLICKINDEX,'') CLICKINDEX,IFNULL(DOWNINDEX,'') DOWNINDEX,IFNULL(SETUPINDEX,'')SETUPINDEX,TIME FROM fv_ori_req  WHERE imei =#{imei} and imsi=#{imsi} order by time limit 0,1")
	public Req getIndexs(Param param);

	/**
	 * 插入新用户索引记录
	 * 
	 * @param param
	 * @return
	 */
	@Insert(" INSERT INTO fv_ori_req(imei,imsi,CLICKINDEX,TIME) VALUES (#{imei},#{imsi},#{sindex},#{time});")
	public int insertcIndex(Param param);
	/**
	 * 插入新用户索引记录
	 * 
	 * @param param
	 * @return
	 */
	@Insert(" INSERT INTO fv_ori_req(imei,imsi,DOWNINDEX,TIME) VALUES (#{imei},#{imsi},#{sindex},#{time});")
	public int insertdIndex(Param param);

	/**
	 * 更新过期用户索引记录
	 * 
	 * @param param
	 * @return
	 */
	@Update("UPDATE fv_ori_req SET CLICKINDEX=#{sindex},DOWNINDEX=',', time=#{time} where imei=#{imei} and imsi=#{imsi}")
	public int updateTimeoutIndex(Param param);// 更新过期索引

	/**
	 * 更新未过期用户索引记录
	 * 
	 * @param req
	 * @return
	 */
	public int updateNooverIndex(Req req);// 更新未过期的索引

	public int updateOper(Param param);// 更新产品操作

	public int insertOper(Param param);// 添加产品操作
}
