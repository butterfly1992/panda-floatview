package fv.adv.service.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.danga.MemCached.MemCachedClient;

import fv.adv.mapper.ReqMapper;
import fv.adv.pojo.Param;
import fv.adv.pojo.Soft;
import fv.adv.service.ReqService;
import fv.adv.tool.IP;
import fv.adv.tool.MemcacheUtil;
import fv.adv.tool.Utils;

/**
 * 请求实现类
 * 
 * @author Administrator
 *
 */
@Repository(value = "reqservice")
@Transactional
public class ReqServiceImpl implements ReqService {

	private ReqMapper reqMapper;
	private MemCachedClient mcc = MemcacheUtil.mcc;

	/**
	 * 正式id请求
	 */
	@Override
	public List<Object> getResult(Param param) {
		// TODO Auto-generated method stub
		List objs = null;
		Utils.log.info("【imei：" + param.getImei() + "；imsi：" + param.getImsi() + "；appid：" + param.getAppid() + "；sdkv："
				+ param.getGysdkv() + "】");
		// 1.初始化每天的请求次数
		int[] fvtimes = initTimes(param);
		if (fvtimes == null)
			return null;
		// 2.根据时间对应判断是否请求过
		int hour = Integer.parseInt(Utils.DateTime("HH"));// 获取当前请求时间
		if (hour >= 0 && hour < 9 && fvtimes[0] == 0) {// 第一个时间段
			objs = fvsoft(param, 0, fvtimes);
		} else if (hour >= 9 && hour < 17 && fvtimes[1] == 0) {// 第二个时间段
			objs = fvsoft(param, 1, fvtimes);
		} else if (hour >= 17 && hour <= 23 && fvtimes[2] == 0) {// 第三个时间段
			objs = fvsoft(param, 2, fvtimes);
		} else {
			return null;
		}
		return objs;
	}

	/**
	 * 获取产品集合
	 * 
	 * @param param
	 * @param flag
	 * @param fvtimes
	 * @return
	 */
	private List fvsoft(Param param, int flag, int[] fvtimes) {
		// TODO Auto-generated method stub
		param = Utils.analyzeImsi(param);
		String imsi = Utils.isNULL(param.getImsi()) ? "0imsi" : param.getImsi();
		int code;
		List<Soft> resSofts = null;
		if (mcc.get("nlscr_area_" + param.getIp()) != null) {// 判断之前ip是否记录过
			param.setArea(mcc.get("nlscr_area_" + param.getIp()).toString());
		} else {
			IP.load(null);// 加载IP类
			String area = "";
			String city = Arrays.toString(IP.find(param.getIp()));// 获取ip解析信息
			try {
				area = city.substring(3, 5);
			} catch (Exception e) {
				// TODO: handle exception
				Utils.log.info("ip:" + param.getIp() + ";city:" + city + "】");
				return null;
			}
			if (area.length() > 0) {
				param.setArea(area);// 根据ip解析出用户地址
				mcc.set("nlscr_area_" + param.getIp(), param.getArea());// 将ip存储
			}
		}
		if (mcc.get("fv_req_code" + param.getImei() + imsi) == null) {// 首次请求,肯定能够提取到优先级
			param.setCode(0);// 优先级大于0的产品
			param.setLimit(4);// 每次提取4个
			List<Soft> softs = reqMapper.getResult(param);
			code = softs.get(softs.size() - 1).getCode();
			mcc.set("fv_req_code" + param.getImei() + imsi, code);// 设置优先级
			resSofts = softs;
		} else {
			code = (Integer) mcc.get("fv_req_code" + param.getImei() + imsi);
			param.setCode(code);// 优先级大于0的产品
			param.setLimit(4);// 每次提取4个
			List<Soft> softs = reqMapper.getResult(param);
			if (softs != null) {
				if (softs.size() < 4) {// 不为空或者提取到4个产品
					param.setCode(0);// 优先级大于0的产品(循环从头开始)
					// 判断第二次是否为循环来还是补足剩余的
					int rest = (softs == null) ? 4 : (4 - softs.size());
					param.setLimit(rest);
					List<Soft> softrest = reqMapper.getResult(param);
					softs.addAll(softrest);
				}
				code = softs.get(softs.size() - 1).getCode();
				mcc.set("fv_req_code" + param.getImei() + imsi, code);
			}
			resSofts = softs;
		}
		fvtimes[flag] = 1;
		Date expiryd = getDefinedDateTime(23, 59, 59, 0);
		mcc.set("fv_req" + param.getImei() + imsi+param.getAppid(), fvtimes, expiryd);
		Utils.log.info("【H:" + Integer.parseInt(Utils.DateTime("HH")) + "；result:" + resSofts.size() + "；Flag：『"
				+ fvtimes[0] + ";" + fvtimes[1] + ";" + fvtimes[2] + "』;nextC：" + (code + 1) + "】");

		if (param.getArea() != null) {// 判断之前是否解析过ip地址
			Utils.log.info(
					"【ip：" + param.getIp() + "；area：" + param.getArea() + ";operation：" + param.getOperation() + "】");
			String[] area = IP.aa;
			for (int i = 0; i < area.length; i++) {// 遍历匹配ip地址的编号
				if (area[i].contains(param.getArea())) {
					param.setLimit(i + 1);
					break;
				}
			}
			if (param.getLimit() >= 0) {
				param.setTime(Utils.DateTime());
				flag = reqMapper.updateIpArea(param);
				if (flag != 1) {
					reqMapper.insertIpArea(param);
				}
			}
		}
		return resSofts;
	}

	/**
	 * 初始化用户每天的请求次数
	 * 
	 * @param param
	 * @return
	 */
	private int[] initTimes(Param param) {
		// TODO Auto-generated method stub
		int[] times = null;
		String imsi = Utils.isNULL(param.getImsi()) ? "0imsi" : param.getImsi();
		if (mcc.get("fv_req" + param.getImei() + imsi+param.getAppid()) == null) {
			times = new int[3];
			/** 额外加的测试活跃用户量 */
			int actuser = 0;
			if (!imsi.equals("0imsi")) {// 有效用户
				if (mcc.get("fv_actout_" + param.getAppid()) == null) {
					actuser = 1;
				} else {
					actuser = Integer.parseInt(mcc.get("fv_actout_" + param.getAppid()).toString()) + 1;// 获取用户数
				}
				mcc.set("fv_actout_" + param.getAppid(), actuser, getDefinedDateTime(23, 59, 59, 0));
				Utils.log.info("[fvTappid：" + param.getAppid() + "，Tactuser:" + actuser + "]");
			} else {// 无sim卡用户
				if (mcc.get("fv_factout_" + param.getAppid()) == null) {
					actuser = 1;
				} else {
					actuser = Integer.parseInt(mcc.get("fv_factout_" + param.getAppid()).toString()) + 1;// 获取用户数
				}
				mcc.set("fv_factout_" + param.getAppid(), actuser, getDefinedDateTime(23, 59, 59, 0));
				Utils.log.info("[fvFappid：" + param.getAppid() + "，Factuser:" + actuser + "]");
			}
			/** 额外加的测试活跃用户量结束 */
		} else {
			times = (int[]) mcc.get("fv_req" + param.getImei() + imsi+param.getAppid());
			Utils.log.info("Chance：" + times[0] + ";" + times[1] + ";" + times[2] + "】");
		}
		return times;
	}

	/**
	 * 测试id的请求
	 */
	@Override
	public List getTestResult(Param param) {
		// TODO Auto-generated method stub
		if (mcc.get("fv_t_req" + param.getImei()) == null) {// 首次请求,肯定能够提取到优先级
			param.setCode(0);// 优先级大于0的产品
			param.setLimit(4);// 每次提取4个
			List<Soft> softs = reqMapper.getTestResult(param);
			int code = softs.get(softs.size() - 1).getCode();
			mcc.set("fv_t_req" + param.getImei(), code);// 设置优先级
			return softs;
		} else {
			int code = (Integer) mcc.get("fv_t_req" + param.getImei());
			param.setCode(code);// 优先级大于0的产品
			param.setLimit(4);// 每次提取4个
			List<Soft> softs = reqMapper.getTestResult(param);
			if (softs != null) {
				if (softs.size() < 4) {// 不为空或者提取到4个产品
					param.setCode(0);// 优先级大于0的产品(循环从头开始)
					// 判断第二次是否为循环来还是补足剩余的
					int rest = (softs == null) ? 4 : (4 - softs.size());
					param.setLimit(rest);
					List<Soft> softrest = reqMapper.getTestResult(param);
					softs.addAll(softrest);
				}
				code = softs.get(softs.size() - 1).getCode();
				mcc.set("fv_t_req" + param.getImei(), code);
			}
			return softs;
		}
	}

	@Autowired
	public void setReqMapper(ReqMapper reqMapper) {
		this.reqMapper = reqMapper;
	}

	/**
	 * 广告开关验证
	 */
	@Override
	public boolean isSwitch(Param param) {
		// TODO Auto-generated method stub
		Param p = reqMapper.isSwitch(param);
		boolean result = (p == null) ? false : true;
		return result;
	}

	public Date getDefinedDateTime(int hour, int minute, int second, int milliSecond) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, milliSecond);
		Date date = new Date(cal.getTimeInMillis());
		return date;
	}

	/**
	 * 内测时删除用户的产品索引记录
	 */
	@Override
	public boolean delReq(Param param) {
		// TODO Auto-generated method stub
		if (reqMapper.delReq(param) > 0)
			return true;
		else
			return false;
	}
}
