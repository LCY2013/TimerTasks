package com.lcydream.project.timer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

/**
 * 定时器池
 * @author LuoChunYun
 *
 */
public class TimerPools {

	//定义一个Timer执行器
	private static Timer timer = null;

	//定义默认的定时器池的大小
	private static int size = 10000;

	//定义一个定时器池
	private static List<TimerSchle> timerPool = new ArrayList<TimerSchle>();

	//私有化构造方法
	private TimerPools(){}
	
	//添加第一种定时器任务(设定指定任务task在指定时间time执行 schedule(TimerTask task, Date time))
		public synchronized static void addTimerOne(TimerSchle timerSchle,Date date){
			if(timerPool.size()<size){
				//timer.schedule(timerSchle,new Date(new Date().getTime()+5000));
				if(date.getTime() > new Date().getTime()) {
					//判断这个名字的定时器是否存在
					TimerSchle schle = getTimerSchleByName(timerSchle.getTimerName());
					if(schle != null ){
						timerPool.remove(schle);
						schle=null;
					}
					//此方法设置定时器是单次定时
					timerSchle.setSingle(true);
					timerPool.add(timerSchle);
					Timer timer = TimerPools.getTimer();
					if(timer!=null) {
						if(date.getTime() < new Date().getTime()) {
							timer.schedule(timerSchle, new Date(new Date().getTime()+1000));
						}else{
							timer.schedule(timerSchle, date);
						}
					}
				}
			}else{
				throw new RuntimeException("<--------------定时池数量超过了限制-------------->");
			}
		}

		//添加第二种定时器任务(设定指定任务task在指定延迟delay后进行固定延迟peroid的执行 schedule(TimerTask task, long delay, long period))
		public synchronized static void addTimerTwo(TimerSchle timerSchle,long delay, long period){
			if(timerPool.size()<size){
				if(period>0) {
					TimerSchle schle = getTimerSchleByName(timerSchle.getTimerName());
					if(schle != null ){
						timerPool.remove(schle);
						schle=null;
					}
					timerSchle.setFourTimer(false);  //设置为规律的定时
					timerPool.add(timerSchle);
					Timer timer = TimerPools.getTimer();
					if(timer != null){
						timer.schedule(timerSchle, delay ,period);
					}

				}
			}else{
				throw new RuntimeException("<--------------定时池数量超过了限制-------------->");
			}
		}

		//添加第三种定时器任务(设定指定任务task在指定延迟delay后进行固定频率peroid的执行)
		//scheduleAtFixedRate(TimerTask task, long delay, long period)
		public synchronized static void addTimerThree(TimerSchle timerSchle,long delay, long period){
			if(timerPool.size()<size){
				if(period>0) {
					TimerSchle schle = getTimerSchleByName(timerSchle.getTimerName());
					if(schle != null ){
						timerPool.remove(schle);
						schle=null;
					}
					timerSchle.setFourTimer(false);  //设置为规律的定时
					timerPool.add(timerSchle);
					Timer timer = TimerPools.getTimer();
					if(timer != null){
						timer.scheduleAtFixedRate(timerSchle, delay ,period);
					}
				}
			}else{
				throw new RuntimeException("<--------------定时池数量超过了限制-------------->");
			}
		}

		//添加第四种定时器任务(安排指定的任务task在指定的时间firstTime开始进行重复的固定速率period执行)
		//Timer.scheduleAtFixedRate(TimerTask task,Date firstTime,long period)
		public synchronized static void addTimerFour(TimerSchle timerSchle,Date firstTime,long period){
			if(timerPool.size()<size){
				if(period>0) {
					TimerSchle schle = getTimerSchleByName(timerSchle.getTimerName());
					if(schle != null ){
						timerPool.remove(schle);
						schle=null;
					}
					timerPool.add(timerSchle);
					Timer timer = TimerPools.getTimer();
					if(timer != null){
						if(firstTime.getTime() < new Date().getTime()){
							timer.scheduleAtFixedRate(timerSchle, new Date(new Date().getTime()+10) ,period);
						}else{
							timer.scheduleAtFixedRate(timerSchle, firstTime ,period);
						}
					}

				}
			}else{
				throw new RuntimeException("<--------------定时池数量超过了限制-------------->");
			}
		}

		//根据定时器的名字获取定时器对象
		public synchronized static TimerSchle getTimerSchleByName(String timerSchleName){
			for(TimerSchle ts : timerPool){
				if(timerSchleName!=null &&
						!"".equals(timerSchleName.trim())
						&&ts.getTimerName().equals(timerSchleName)){
					return ts;
				}
			}
			return null;
		}

		//根据定时器的名字或者定时器的类型异常定时器
		public synchronized static void removeTimerSchleByName(String timerSchleName){
			TimerSchle ts = getTimerSchleByName(timerSchleName);
			timerPool.remove(ts);
			//移除并且取消定时器
			if(ts != null){
				ts.setCancle(true);
				timerPool.remove(ts);
				ts.cancel();
				ts=null;
			}
			/*if(timerPool.size() == 0){
				timer.cancel();
			}*/
		}

		//根据定时器类对象移除改定时器
		public synchronized static void removeTimerSchleByClass(TimerSchle timerSchle){
			//移除并且取消定时器
			if(timerSchle != null){
				timerSchle.setCancle(true);
				timerPool.remove(timerSchle);
				timerSchle.cancel();
				timerSchle=null;
			}
			/*if(timerPool.size() == 0){
				timer.cancel();
			}*/
		}

		//修改定时器后刷新定时池对象
		public synchronized static void refreshOne(TimerSchle timerSchle,Date date){
			if(timerSchle != null){
				if(date.getTime() > new Date().getTime()){
					TimerSchle ts = new TimerSchle(timerSchle.getTimerName(),timerSchle.getClassName(),timerSchle.getMethodName(),timerSchle.getArguments());
					ts.copyToMe(timerSchle);
					//取消定时器的执行
					timerSchle.cancel();
					//先将定时器任务取消
					timerPool.remove(timerSchle);
					timer.purge();
					//标识用于GC回收
					timerSchle=null;
					addTimerOne(ts, date);
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					System.out.println("<------------定时器"+ts.getTimerName()+"刷新成功***********刷新时间是:"+formatter.format(new Date())+"---------->\n\n");
				}else{
					throw new RuntimeException("<--------------定时的时间不能在设置定时器的时间前面-------------->");
				}
			}else{
				throw new RuntimeException("<--------------未获取到有效的定时任务-------------->");
			}
		}

		//修改定时器后刷新定时池对象
		public synchronized static void refreshTwo(TimerSchle timerSchle,long delay, long period){
			if(timerSchle != null){
				if(delay>0 && period>0){
					TimerSchle ts = new TimerSchle(timerSchle.getTimerName(),timerSchle.getClassName(),timerSchle.getMethodName(),timerSchle.getArguments());
					ts.copyToMe(timerSchle);
					//取消定时器的执行
					timerSchle.cancel();
					//先将定时器任务取消
					timerPool.remove(timerSchle);
					//标识用于GC回收
					timerSchle=null;
					addTimerTwo(ts, delay, period);
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					System.out.println("<------------定时器"+ts.getTimerName()+"刷新成功***********刷新时间是:"+formatter.format(new Date())+"---------->");
				}else{
					throw new RuntimeException("<--------------定时的时间不能为小于零的数-------------->");
				}
			}else{
				throw new RuntimeException("<--------------未获取到有效的定时任务-------------->");
			}
		}

		public synchronized static void refreshThree(TimerSchle timerSchle,Date firstTime,long period){
			if(timerSchle != null){
				if(period < 0){
					throw new RuntimeException("定时的时间不能为小于零的数");
				}
				TimerSchle ts = new TimerSchle(timerSchle.getTimerName(),timerSchle.getClassName(),timerSchle.getMethodName(),timerSchle.getArguments());
				ts.copyToMe(timerSchle);
				//取消定时器的执行
				timerSchle.cancel();
				//先将定时器任务取消
				timerPool.remove(timerSchle);
				//标识用于GC回收
				timerSchle=null;
				addTimerFour(ts, firstTime, ts.getNextExecuteTime());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				System.out.println("<------------定时器"+ts.getTimerName()+"刷新成功***********刷新时间是:"+formatter.format(new Date())+"---------->");
			}else{
				throw new RuntimeException("<------------未获取到有效的定时任务------------>");
			}
		}

		//获取定时器中任务的个数
		public synchronized static int getTimerPoolSize(){
			return timerPool.size();
		}

		//单粒模式获取timer
		public synchronized static Timer getTimer(){
			// 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
			if(timer==null){
				//同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
				synchronized (TimerPools.class) {
					//未初始化，则初始instance变量
					if(timer==null){
						timer = new Timer();
					}
				}
			}
			return timer;
		}

		//设置定时器池的大小
		public synchronized static void setTimerSize(int sizeLong){
			size = sizeLong;
		}
	}
