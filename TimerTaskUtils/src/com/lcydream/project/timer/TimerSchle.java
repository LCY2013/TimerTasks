package com.lcydream.project.timer;

import com.yinhai.common.util.LOGUtil;
import com.yinhai.simpay.his.orthopedics.common.constant.I_81OrthopedicsConstant;
import com.yinhai.simpay.his.orthopedics.common.enums.TagerType;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * 定时器实现类
 * @author LuoChunYun
 *
 */
public class TimerSchle extends TimerTask{

	private Class<?> className;  //执行定时器类名
	private String classPath;    //定时器的类路径
	private String methodName;   //定时器执行的方法名
	private Object[] arguments;  //执行方法的参数
	private Object returnResult; //执行方法的返回值
	private String timerName;    //获取定时器的名字
	private int timerFrequency=5;  //定时器最大定时次数
	private int nowCount=0;      //当前定时器执行次数
	private boolean issuspend=false; //设置定时器使用的标识，如果定时的次数到了最大数就为true
	private boolean isSingle=false;  //设置定时器的定时规则:false表示循环定时，true表示单次定时
	private String targerSupper= TagerType.SUPPER_ORDINARY.name();  //设置定时对象执行目标的来源（目前支持Spring容器和无依赖的对象）默认支持没有依赖的对象反射
	private Map<Integer,Integer> executeTimeMap = null;  //自定义定时任务每次执行时间
	private Map<Integer,Integer> defaultExecuTimeMap = null;  //该用户没有自定义时，使用默认的定时任务执行时间
	private boolean isCancle=false;     //标识改定时任务是否取消
	private boolean isFourTimer=true;   //标识是不规律的定时执行

	/**
	 *
	 * @param timerName 定时器名字
	 * @param classPath 定时器执行对象的全路径
	 * @param methodName 定时器执行对象的方法名
	 * @param arguments 定时器执行对象的参数
	 */
	public TimerSchle(String timerName,String classPath,String methodName,Object[] arguments) {
		this.classPath = classPath;
		this.methodName = methodName;
		this.arguments = arguments;
		this.timerName = timerName;
	}

	/**
	 *
	 * @param timerName 定时器名字
	 * @param className 定时器执行对象的类
	 * @param methodName 定时器执行对象的方法名称
	 * @param arguments 定时器执行对象的参数
	 */
	public TimerSchle(String timerName,Class<?> className,String methodName,Object[] arguments) {
		this.className = className;
		this.methodName = methodName;
		this.arguments = arguments;
		this.timerName = timerName;
	}

	/**
	 * 获取定时器剩余的执行次数
	 * @return
	 */
	public int getTimerFrequencyExecutor(){
		if(timerFrequency > nowCount){
			return timerFrequency - nowCount;
		}
		return 0;
	}



	@Override
	public void run() {
		try {
			if(nowCount < timerFrequency){
				System.out.println("<----------定时器"+this.timerName+"开始执行----------"+"定时池中的定时器数量是:"+TimerPools.getTimerPoolSize()+"---------->");
				this.nowCount++;
				if(classPath!=null && !"".equals(classPath.trim())){
					this.className = Class.forName(classPath);
					invoTimerTask(this.className);
				}else if((classPath==null || "".equals(classPath.trim())) && className!=null){
					invoTimerTask(className);
				}else{
					throw new RuntimeException("<-----------执行定时器的对象入参不合法，请先检查----------->");
				}
				if(!isSingle && !isCancle && isFourTimer){//如果是自定义循环定时就执行他的循环定时时间
					//刷新定时器对象
					TimerPools.refreshThree(this,new Date(this.getNextExecuteTime()),this.getNextExecuteTime());
				}
			}else{
				System.out.println("<-----------定时器"+this.timerName+"定时任务完成----------->");
				this.cancel(); //达到最大的定时次数，取消定时器
				this.issuspend = true;  //定时器的最大次数达到
				TimerPools.removeTimerSchleByClass(this); //将定时器从池中移除
			}
			//判断这个执行的定时器是否是单次执行
			if(isSingle){
				//将该定时器从定时池中移除
				TimerPools.removeTimerSchleByClass(this);
				System.out.println("<-------------------单次定时器"+this.timerName+"已经成功执行------------------->");
			}
			if(!isSingle&&isCancle){
				System.out.println("<-------------------循环定时器"+this.timerName+"已经成功执行------------------->");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void invoTimerTask(Class<?> clazz) throws Exception{
		//得到方法
		Method methlist[] = clazz.getDeclaredMethods();
		int i = 0;
		for (; i < methlist.length; i++) {
			Method m = methlist[i];
			if(m.getName()!=null && this.methodName!=null
					&& !"".equals(this.methodName)
					&& m.getName().equals(this.methodName)){
				LOGUtil.i(LOGUtil.Level.INFO,"************************************************************************");
				LOGUtil.i(LOGUtil.Level.INFO,"*                           定时器"+this.timerName+"执行的方法开始执行                                                                  *");
				LOGUtil.i(LOGUtil.Level.INFO,"*                                                                      *");
				Object obj = null;
				if(this.targerSupper.equals(TagerType.SUPPER_SPRING.name())) {  //支持Spring依赖注入
					WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
					String sName = clazz.getSimpleName();
					char[] chars=new char[1];
					chars[0]=sName.charAt(0);
					String temp=new String(chars);
					sName = sName.replaceFirst(temp, temp.toLowerCase());
					if(wac != null) {
						Object wacBean = null;
						try {
							wacBean = wac.getBean(sName);
						}catch (Exception e){
							LOGUtil.i(LOGUtil.Level.ERROR,"Spring中没有找到"+sName);
						}
						Object wacBeanByType = null;
						try {
							wacBeanByType = wac.getBean(clazz);
						}catch (Exception e){
							LOGUtil.i(LOGUtil.Level.ERROR,"Spring中没有找到"+clazz.getSimpleName()+"类型");
						}
						if(wacBean != null) {
							//执行方法
							obj = m.invoke(wacBean, this.arguments);
						}else if(wacBeanByType != null){
							//执行方法
							obj = m.invoke(wacBeanByType, this.arguments);
						}else{
							LOGUtil.i(LOGUtil.Level.ERROR,"请检查Spring容器中是否有"+sName+"对象");
							throw new RuntimeException("无效的执行对象"+sName);
						}
					}else{
						LOGUtil.i(LOGUtil.Level.ERROR,"未获取到Spring的上下文，请在有效的Spring环境中使用。");
					}
					//System.out.println("从Spring中获取对象");
				}else{   //普通对象执行
					//获取改类对象
					Object object = clazz.newInstance();
					obj = m.invoke(object,this.arguments);
				}
				LOGUtil.i(LOGUtil.Level.INFO,"*                                                                      *");
				LOGUtil.i(LOGUtil.Level.INFO,"*                           定时器执行的方法结束执行                                                                   *");
				LOGUtil.i(LOGUtil.Level.INFO,"*************************************************************************\n\n");
				//设置返回值
				if(obj != null){
					setReturnResult(obj);
					LOGUtil.i(LOGUtil.Level.INFO,"********************定时器执行的方法<"+this.methodName+">的返回值***********************");
					LOGUtil.i(LOGUtil.Level.INFO,"<------------定时任务返回值------------>"+obj);
					LOGUtil.i(LOGUtil.Level.INFO,"********************定时器执行的方法<"+this.methodName+">的返回值***********************\n\n");
				}
				break;
			}
		}
		if(i==methlist.length){
			LOGUtil.i(LOGUtil.Level.INFO,"<-------------------定时器执行时未找到有效的方法------------------->");
			TimerPools.removeTimerSchleByClass(this);
		}
		LOGUtil.i(LOGUtil.Level.INFO,"<-------------------定时池中定时器的数量是:"+TimerPools.getTimerPoolSize()+"------------------->");
	}

	/**
	 * copy参数
	 * @param them
	 */
	public void copyToMe(TimerSchle them){
		this.setTimerFrequency(them.getTimerFrequency());
		this.setNowCount(them.getNowCount());
		this.setIssuspend(them.isIssuspend());
		this.setTargerSupper(them.getTagerType());
		if(them.getExecuteTimeMap() != null){
			this.setExecuteTimeMap(them.getExecuteTimeMap());
		}
		if(them.getDefaultExecuTimeMap() != null){
			this.defaultExecuTimeMap = them.getDefaultExecuTimeMap();
		}
	}

	/**
	 * 设置定时对象执行目标的来源
	 */
	public void setTargerSupper(TagerType tagerType){
		this.targerSupper = tagerType.name();
	}

	public TagerType getTagerType(){
		if(this.targerSupper.equals(TagerType.SUPPER_SPRING.name())){
			return TagerType.SUPPER_SPRING;
		}else if(this.targerSupper.equals(TagerType.SUPPER_ORDINARY.name())){
			return TagerType.SUPPER_ORDINARY;
		}
		return null;
	}

	public Object getReturnResult() {
		return returnResult;
	}

	public void setReturnResult(Object returnResult) {
		this.returnResult = returnResult;
	}

	public String getTimerName() {
		return timerName;
	}

	public void setTimerName(String timerName) {
		this.timerName = timerName;
	}

	public void setTimerFrequency(int timerFrequency) {
		this.timerFrequency = timerFrequency;
	}

	public void setNowCount(int nowCount) {
		this.nowCount = nowCount;
	}

	public void setClassName(Class<?> className) {
		this.className = className;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public boolean isIssuspend() {
		return issuspend;
	}


	public String getMethodName() {
		return methodName;
	}

	public Object[] getArguments() {
		return arguments;
	}



	public Class<?> getClassName() {
		return className;
	}



	public String getClassPath() {
		return classPath;
	}

	public int getTimerFrequency() {
		return timerFrequency;
	}

	public int getNowCount() {
		return nowCount;
	}

	public void setIssuspend(boolean issuspend) {
		this.issuspend = issuspend;
	}

	public boolean isSingle() {
		return isSingle;
	}

	public void setSingle(boolean isSingle) {
		this.isSingle = isSingle;
	}

	public Map<Integer, Integer> getExecuteTimeMap() {
		return executeTimeMap;
	}



	public void setFourTimer(boolean isFourTimer) {
		this.isFourTimer = isFourTimer;
	}

	public boolean isCancle() {
		return isCancle;
	}

	public void setCancle(boolean isCancle) {
		this.isCancle = isCancle;
	}

	public Map<Integer, Integer> getDefaultExecuTimeMap() {
		return defaultExecuTimeMap;
	}

	public long getNextExecuteTime(){
		if(this.executeTimeMap != null){
			if(this.nowCount<this.timerFrequency){
				return this.executeTimeMap.get((this.nowCount+1))+new Date().getTime();
			}else{
				return new Date().getTime()+1000;
			}
		}else if(this.defaultExecuTimeMap != null){
			if(this.nowCount<this.timerFrequency){
				return this.defaultExecuTimeMap.get((this.nowCount+1))+new Date().getTime();
			}else{
				return 0;
			}
		}else{
			System.out.println("没有初始化定时任务时间，使用默认的定时任务时间，改定时任务不能初始化了");
			//初始化默认的定时器执行时间
			this.defaultExecuTimeMap = new HashMap<Integer, Integer>();
			this.defaultExecuTimeMap.put(1, 1000*30);
			this.defaultExecuTimeMap.put(2, 1000*60*1);
			this.defaultExecuTimeMap.put(3, 1000*60*2);
			this.defaultExecuTimeMap.put(4, 1000*60*5);
			this.defaultExecuTimeMap.put(5, 1000*60*10);
			this.timerFrequency = this.defaultExecuTimeMap.size();
			return this.defaultExecuTimeMap.get(this.getNowCount()+1)+new Date().getTime();
		}
	}

	public void setExecuteTimeMap(Map<Integer, Integer> executeTimeMap) {
		if(this.executeTimeMap != null || this.defaultExecuTimeMap != null){
			throw new RuntimeException(this.timerName+"--》该定时器的任务时间已经被设置过或者默认任务时间已经使用过，不能重复设置");
		}
		if(executeTimeMap == null){
			throw new RuntimeException("设置定时任务对象executeTimeMap不能为空");
		}
		//首先判断给的执行时间是否合理
		for(int i = 1;i<=executeTimeMap.size();i++){
			if(executeTimeMap.get(i) == null){
				throw new RuntimeException("定时参数不合法,定时的序号不连续"+executeTimeMap.toString());
			}
		}
		this.timerFrequency = executeTimeMap.size();
		this.executeTimeMap = executeTimeMap;
	}



}
