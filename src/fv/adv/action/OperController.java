package fv.adv.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fv.adv.pojo.Param;
import fv.adv.service.OperService;
import fv.adv.tool.Utils;
import fv.adv.tool.Variable;

/**
 * 操作控制类
 * 
 * @author Administrator
 * 
 */
@Controller
public class OperController {

	@Resource(name = "operservice")
	private OperService operservice;

	@RequestMapping(value = "/oper", method = { RequestMethod.POST })
	public @ResponseBody Object oper(Param param) {// 操作统计
		if (Utils.isNULL(param.getAppid()) || Utils.isNULL(param.getImei()) || Utils.isNULL(param.getImsi())
				|| Utils.isNULL(param.getSindex()) || param.getOper() <= 0 || Utils.isNULL(param.getGysdkv())) {
			Utils.log.error("[appid为" + param.getAppid() + "；imei为" + param.getImei() + "；imsi为" + param.getImsi()
					+ ";产品索引：" + param.getSindex() + ";操作：" + param.getOper() + "；版本为" + param.getGysdkv() + ";]");
			return Variable.errorJson;
		}
		if (param.getImsi().indexOf("00000") >= 0 || param.getImei().indexOf("000000") >= 0 || Variable.errorImsi.contains(param.getImsi())) {//错误的串码
			Utils.log.info("Mobile moni： 『" + param.getOper() + "imei:" + param.getImei() + ";imsi：" + param.getImsi()
					+ ";appid:" + param.getAppid() + "』");
			return Variable.errorJson;
		}
		if (Variable.testId.contains(param.getAppid())) {// 测试id
			return Variable.errorJson;
		}
		if (Utils.isNULL(param.getSid())) {// 查询产品id
			param.setSid(operservice.getsid(param.getSindex()));
			Utils.log.info("other：" + param.getSid() + ";apid:" + param.getAppid() + ";");
		}

		Utils.log.info("『 操作：" + param.getOper() + "；appid:" + param.getAppid() + "；imei为" + param.getImei() + "；imsi为"
				+ param.getImsi() + "；sid:" + param.getSid() + ";』" + ";产品索引：" + param.getSindex() + "；版本："
				+ param.getGysdkv());

		return operservice.oper(param);
	}

	@RequestMapping(value = "/actuser", method = { RequestMethod.POST })
	public @ResponseBody Object recordUser(Param param) {// 记录活跃用户(需要imei，imsi，appid，gysdkv,
															// time,times)
		if (Utils.isNULL(param.getAppid()) || Utils.isNULL(param.getImei()) || Utils.isNULL(param.getImsi())
				|| Utils.isNULL(param.getGysdkv())) {
			Utils.log.error("[appid为" + param.getAppid() + "；imei为" + param.getImei() + "；imsi为" + param.getImsi()
					+ "；版本为" + param.getGysdkv() + ";]");
			return Variable.errorJson;
		}
		if (Variable.testId.contains(param.getAppid())) {
			return Variable.errorJson;
		}
		param.setTime(Utils.DateTime());
		String res = operservice.recordUser(param) > 0 ? Variable.correntJson : Variable.errorJson;
		// int res = operservice.oper(param);
		return res;
	}

	@RequestMapping(value = "/ksipmeed", method = { RequestMethod.POST })
	public @ResponseBody Object jiamoper(Param param) {// 操作统计
		if (Utils.isNULL(param.getAppid()) || Utils.isNULL(param.getImei()) || Utils.isNULL(param.getImsi())
				|| Utils.isNULL(param.getKey())) {
			Utils.log.error("[appid为" + param.getAppid() + "；imei为" + param.getImei() + "；imsi为" + param.getImsi()
					+ ";key " + param.getKey() + ";]");
			return Variable.errorJson;
		}
		if (param.getImsi().indexOf("00000") >= 0 || param.getImei().indexOf("000000") >= 0 || Variable.errorImsi.contains(param.getImsi())) {//错误的串码
			Utils.log.info("Mobile moni： 『" + param.getOper() + "imei:" + param.getImei() + ";imsi：" + param.getImsi()
					+ ";appid:" + param.getAppid() + "』");
			return Variable.errorJson;
		}
		if (Variable.testId.contains(param.getAppid())) {// 测试id
			return Variable.errorJson;
		}

		String keycont = Utils.jiexiUrl(param.getKey(), param.getAppid(), param.getImei());// 获取解密内容
		if (keycont == null) {// 解密失败
			Utils.log.error("JieXiException : " + param.getKey() + ";appid:" + param.getAppid() + ";imei:"
					+ param.getImei() + "；");
			return Variable.errorJson;
		}
		String[] moreparam = keycont.split(";");
		param.setSindex(moreparam[0]);
		param.setOper(Integer.parseInt((moreparam[1])));
		param.setGysdkv(moreparam[2]);

		if (Utils.isNULL(param.getSid())) {// 查询产品id
			param.setSid(operservice.getsid(param.getSindex()));
			Utils.log.info("other：" + param.getSid() + ";apid:" + param.getAppid() + ";");
		}

		Utils.log.info("『 操作：" + param.getOper() + "；appid:" + param.getAppid() + "；imei为" + param.getImei() + "；imsi为"
				+ param.getImsi() + "；sid:" + param.getSid() + ";』" + ";产品索引：" + param.getSindex() + "；版本："
				+ param.getGysdkv());
		return operservice.oper(param);
	}

}
