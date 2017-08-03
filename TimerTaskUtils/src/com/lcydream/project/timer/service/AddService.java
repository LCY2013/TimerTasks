package com.lcydream.project.timer.service;

import com.lcydream.project.timer.TimerPools;
import com.lcydream.project.timer.TimerSchle;

public class AddService extends PayService{
	
	public void printStr(int i,String str){
		//TimerPools.refreshThree(TimerPools.getTimerSchleByName("num"),new Date(new Date().getTime()+1000),TimerPools.getTimerSchleByName("num").getNextExecuteTime());
		//System.out.println(new Date(TimerPools.getTimerSchleByName("num").getNextExecuteTime()));
		TimerSchle ts = TimerPools.getTimerSchleByName("num");
		/*if(ts.getNowCount() == 1){
			TimerPools.removeTimerSchleByClass(ts);
		}*/
		getRegisterService().noticeRegisterService(str);
		//System.out.println("随机数是:"+i+"  序号是:"+str+"父类的字符串是:"+getString());
	}

}
