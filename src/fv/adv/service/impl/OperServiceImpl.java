package fv.adv.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fv.adv.mapper.OperMapper;
import fv.adv.pojo.Param;
import fv.adv.pojo.Req;
import fv.adv.service.OperService;
import fv.adv.tool.Utils;
import fv.adv.tool.Variable;

/**
 * 操作实现类
 * @author Administrator
 *
 */
@Repository(value = "operservice")
@Transactional
public class OperServiceImpl implements OperService {

	private OperMapper operMapper;
	private String time_flag = Variable.formater.format(System
			.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000);// 过期时间

	/* 1：点击产品，2：下载完成，3：安装完成 */
	@Override
	public Object oper(Param param) {// 产品操作
		// TODO Auto-generated method stub
		String time = Utils.DateTime();
		int oper = param.getOper();
		param.setTime(time);
		int flag = 0;
		if (oper == 1) {
			Req req = operMapper.getIndexs(param);
			if (req == null) {
				flag = operMapper.insertcIndex(param);
			} else {
				String cindex = req.getClickindex();
				String date_time = req.getTime();
				boolean isOutOfDate = false;
				if (time_flag.hashCode() >= date_time.hashCode()) {
					isOutOfDate = true;// 过期
				}
				if (isContain(cindex, param.getSindex()) && !isOutOfDate) {
					// 在有效期内的已经展示过
					return Variable.errorJson;
				}
				if (isOutOfDate) {// 过期，将存储过的索引除安装外清空，
					flag = operMapper.updateTimeoutIndex(param);
				} else {// 未过期，将索引拼接，时间用之前
					req.setClickindex(cindex.concat("," + param.getSindex()));
					flag = operMapper.updateNooverIndex(req);
				}
			}
			if (flag == 1) {// 更新操作
				flag = operMapper.updateOper(param);
				if (flag != 1) {// 插入操作
					flag = operMapper.insertOper(param);
				}
			} else {
				return Variable.errorJson;
			}

		} else if (oper == 2) {// down
			Req req = operMapper.getIndexs(param);
			if (req == null) {
				flag = operMapper.insertdIndex(param);
			} else {
				String dindex = req.getDownindex();
				if (isContain(dindex, param.getSindex())) {
					// 已经下载过
					return Variable.errorJson;
				}
				req.setDownindex(dindex.concat("," + param.getSindex()));
				flag = operMapper.updateNooverIndex(req);
			}
			if (flag == 1) {
				flag = operMapper.updateOper(param);
				if (flag != 1) {
					flag = operMapper.insertOper(param);
				}
			} else {
				return Variable.errorJson;
			}
		} else if (oper == 3) {// setup
			Req req = operMapper.getIndexs(param);
			if (req == null) {
				// 没有安装过
				return Variable.errorJson;
			} else {
				String dindex = req.getDownindex();
				String sindex = req.getSetupindex();
				if (isContain(sindex, param.getSindex())
						|| (!isContain(dindex, param.getSindex()))) {
					// 已经安装过,或尚未下载过
					return Variable.errorJson;
				}
				req.setSetupindex(sindex.concat("," + param.getSindex()));
				flag = operMapper.updateNooverIndex(req);
			}
			if (flag == 1) {
				flag = operMapper.updateOper(param);
				if (flag != 1) {
					Utils.log.error("没有安装记录过，更新失败:" + param.getAppid()
							+ ";imei" + param.getImei() + "』");
				}
			} else {
				return Variable.errorJson;
			}
		} else {
			return Variable.errorJson;
		}
		if (flag > 0) {
			return Variable.correntJson;
		} else
			return Variable.errorJson;
	}

	@Override
	public int recordUser(Param param) {
		// TODO Auto-generated method stub
		int res = operMapper.recordupdUser(param);
		if (res == 0) {
			res = operMapper.recordaddUser(param);
		}
		return res;
	}

	@Override
	public String getsid(String sindex) {
		// TODO Auto-generated method stub
		return operMapper.getsid(sindex);
	}

	@Autowired
	public void setOperMapper(OperMapper operMapper) {
		this.operMapper = operMapper;
	}

	public boolean isContain(String s, String c) {
		String a[] = s.split(",");
		for (int i = 0; i < a.length; i++) {
			if (a[i].equals(c)) {
				return true;
			}
		}
		return false;
	}
}
