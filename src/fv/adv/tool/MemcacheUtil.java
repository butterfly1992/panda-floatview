package fv.adv.tool;


import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class MemcacheUtil {
	public static MemCachedClient mcc = new MemCachedClient();
	public static MemcacheUtil mem = new MemcacheUtil();
	static {
//		 String[] servers = { "192.168.0.18:22320" };
		 String[] servers = { "127.0.0.1:22120", "127.0.0.1:22121" };
		// 获取sock连接池的实例对象
		SockIOPool pool = SockIOPool.getInstance();
		pool.setFailover(true);
		// 设置服务器信息
		// 设置每个集群的权重
		Integer weights[] = { 5 };
		pool.setWeights(weights);
		pool.setServers(servers);
		// 设置初始连接数、最小和最大连接数以及最大处理时间
		pool.setInitConn(10);
		pool.setMinConn(5);
		pool.setMaxConn(150);
		pool.setMaxIdle(1000 * 60 * 60 * 6);
		// 设置主线程的睡眠时间
		pool.setMaintSleep(30);
		// 设置TCP的参数，连接超时等
		pool.setNagle(false);
		pool.setSocketTO(3000);
		pool.setSocketConnectTO(0);
		// 初始化连接池
		// TODO cache（2011-11-14） 初始化连接池
		pool.initialize();
		// 压缩设置，超过指定大小（单位为K）的数据都会被压缩
		// mcc.setCompressEnable(true);
		// mcc.setCompressThreshold(64 * 1024);
		mcc.setCompressEnable(true);
		mcc.setCompressThreshold(512 * 1024);
	}

	/**
	 * 保护型构造方法，不答应实例化！
	 * 
	 */
	protected MemcacheUtil() {
	}

	/**
	 * 获取唯一实例.
	 * 
	 * @return
	 */
	public static MemcacheUtil getInstance() {
		return mem;
	}

	public boolean add(String key, Object value) {
		mcc.set(key, value);
		return false;
	}

	// @SuppressWarnings("unchecked")
	public static void main(String[] arg) {
//		MemcacheUtil.mcc.set("aa", "cccc");
//		System.out.println(MemcacheUtil.mcc.get("aa"));
//		String imsi = "460016125937650";
//		String tablename = "aa_";
//		tablename += new BigDecimal(Double.parseDouble(imsi.substring(imsi
//				.length() - 4)) / 20).setScale(0, BigDecimal.ROUND_HALF_UP); 
		mcc.flushAll();
		System.out.println("清除完毕");
		// MemcacheUtil.mcc.flushAll();
	} 
}
