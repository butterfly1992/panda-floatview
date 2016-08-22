package fv.adv.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fv.adv.pojo.Param;
import fv.adv.service.ReqService;
import fv.adv.tool.MemcacheUtil;
import fv.adv.tool.Utils;
import fv.adv.tool.Variable;
import user.agent.tool.Platform;

/**
 * 请求控制类
 * 
 * @author Administrator
 * 
 */
@Controller
public class ReqController {

	@Resource(name = "reqservice")
	private ReqService reqservice;

	@RequestMapping(value = "/req", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody Object showlist(Param param, HttpServletRequest request) {// 验证查询
		List<Object> obs = null;
		// 一系列参数验证后
		if (Utils.isNULL(param.getAppid()) || Utils.isNULL(param.getImei()) || Utils.isNULL(param.getGysdkv())) {
			Utils.log.error(
					"[appid为" + param.getAppid() + "；imei为" + param.getImei() + "；版本为" + param.getGysdkv() + ";]");
			return Variable.errorJson;
		}

		Utils.log.info("-------------req_start------------------------------------------------------------");
		// 测试id
		if (Variable.testId.contains(param.getAppid())) {
			obs = reqservice.getTestResult(param);
			if (obs == null) {
				return Variable.errorJson;
			} else
				return obs;
		}
		// 判断是否为黑名单上的appid,拦截用户请求ip
		if (Variable.blacklistId.contains(param.getAppid())) {
			String ip = request.getHeader("x-forwarded-for");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			Utils.log.info("【appid：" + Variable.blacklistId + ",ip:" + ip + "】");
			obs = reqservice.getTestResult(param);
			if (obs == null) {
				return Variable.errorJson;
			} else
				return obs;
		}
		// 正式用户开始请求
		try {
			if (reqservice.isSwitch(param)) {// 判断开关是否打开
				param.setIp(Platform.findIp(request));// 分析用户ip地址
				obs = reqservice.getResult(param);
				if (obs == null) {
					Utils.log.info("『result：null;』");
					return Variable.errorJson;
				} else
					Utils.log.info("result:『" + obs.size() + "』");
				return obs;
			} else {
				Utils.log.error("『appid:" + param.getAppid() + ";switch:close』");
				return Variable.switcherror;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return Variable.errorJson;
	}

	@RequestMapping(value = "/clearmem", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody Object deleteme(Param param) {// 删除memcached用户数据
		if (Utils.isNULL(param.getImei())) {
			Utils.log.error(" imei为null");
			return Variable.errorJson;
		}
		String imsi = Utils.isNULL(param.getImsi()) ? "0imsi" : param.getImsi();
		boolean res = false;
		if (MemcacheUtil.mcc.keyExists("fv_req" + param.getImei() + imsi)) {
			res = MemcacheUtil.mcc.delete("fv_req" + param.getImei() + imsi);
		}
		if (param.getOper() != null) {
			if (param.getOper() == -1) {
				res = reqservice.delReq(param);
			}
		}
		if (res) {
			return Variable.correntJson;
		} else
			return Variable.errorJson;
	}

	@RequestMapping(value = "/adswitch", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody Object adswitch(Param param) {// 验证开关状态，
		if (Utils.isNULL(param.getAppid()) || Utils.isNULL(param.getGysdkv())) {
			Utils.log.error("[appid为" + param.getAppid() + "；gysdkv:" + param.getGysdkv() + ";]");
			return Variable.errorJson;
		}
		// 测试id，开关直接打开
		if (Variable.testId.contains(param.getAppid())) {
			return Variable.correntJson;
		}
		Utils.log.info("[swiid:" + param.getAppid() + "；swidkv:" + param.getGysdkv() + ";]");
		if (reqservice.isSwitch(param)) {// 开关打开
			return Variable.correntJson;
		} else
			return Variable.errorJson;
	}
}
