package com.lcydream.project.timer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.lcydream.project.timer.service.AddService;

public class TimerTaskMain {

	public static void main(String[] args) throws InterruptedException {
		//Timer timer = new Timer();
		/*Timer timer = TimerPools.getTimer();
		TimerSchle ts1 = new TimerSchle("TimerSchle1",PayService.class, "noticePay", new Object[]{"2"});
		TimerPools.addTimer(ts1);
		System.out.println(TimerPools.getTimerPoolSize());
		TimerSchle ts2 = new TimerSchle("TimerSchle2",RegisterService.class, "noticeRegisterService", new Object[]{"1"});
		TimerPools.addTimer(ts2);
		System.out.println(TimerPools.getTimerPoolSize());
		timer.schedule(ts1,new Date(new Date().getTime()+5000));//��dateΪ������ָ��ĳ��ʱ���ִ���߳�
		timer.schedule(ts2,new Date(new Date().getTime()+10000));*/
		/*Map map=Thread.getAllStackTraces(); //Ҳ����map<Thread, StackTraceElement[]>  
        System.out.println("��ǰ�߳�����"+map.size());  
        Iterator it=map.keySet().iterator();  
        while (it.hasNext()) {  
            Thread t=(Thread) it.next(); //  
            System.out.println(t.getName());  
        }*/  
//		TimerSchle timerSchle = TimerPools.getTimerSchleByName("TimerSchle1");
		//timerSchle.cancel();
		//TimerPools.removeTimerSchleByClass(timerSchle);
		//TimerPools.removeTimerSchleByName("TimerSchle1");
//		TimerSchle timerSchle2 = TimerPools.getTimerSchleByName("TimerSchle2");
		//timerSchle2.cancel();
		//TimerPools.removeTimerSchleByClass(timerSchle2);
		//TimerPools.removeTimerSchleByName("TimerSchle2");
		/*if(TimerPools.getTimerPoolSize() == 0){
			timer.cancel();
		}*/
		
		//System.out.println(TimerPools.getTimerPoolSize());
		
		/*for(int i=0;i<10000;i++){
			//�½���ʱ��ʵ����
	        TimerSchle ts = new TimerSchle("num:"+(i+1),AddService.class,
	                "printStr",new Object[]{i,i+""});
	        //��ȡ�ϴ�ִ�е�ʱ��
	        int lastTime = ts.getLastTime();
	        //�õ���ζ�ʱ��ʱ��
	        int nowTime = lastTime+1000;
	        //���ö�ʱ����ʱ��
	        Date timerDate = new Date(new Date().getTime()+ nowTime);
	        //����ʱ����ӵ���ʱ������
	        TimerPools.addTimerTwo(ts,1000,1000);
	        //TimerPools.addTimerOne(ts,new Date(new Date().getTime()+1000));
		}*/
		
		/*������ִ������
		 * TimerSchle ts = new TimerSchle("num",AddService.class,
                "printStr",new Object[]{100,"luochunyun"});
		TimerPools.addTimerOne(ts, new Date(new Date().getTime()+2000));*/
		
		/*TimerSchle timerSchle = TimerPools.getTimerSchleByName("num");
		int i = new Random(10).nextInt()+1;
		timerSchle.setArguments(new Object[]{i});
		try {
			TimerPools.refreshOne(timerSchle, new Date(new Date().getTime()+1000));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}*/
		
		//���̲߳����޸�ͬһ����ʱ��
		/*TimerSchle ts = new TimerSchle("num",AddService.class,
                "printStr",new Object[]{100});
		TimerPools.addTimerOne(ts, new Date(new Date().getTime()+1000));
		TimerSchle ts1 = new TimerSchle("num1",AddService.class,
                "printStr",new Object[]{100});
		TimerPools.addTimerOne(ts1, new Date(new Date().getTime()+1000));
		for(int i=0;i<1000;i++){
			int k = i+1;
			new Thread(new Runnable() {
				@Override
				public void run() {
					TimerSchle timerSchle = TimerPools.getTimerSchleByName("num"); //���ö�ʱ��
					int i = (int)(Math.random() * 10) + 1;
					timerSchle.setArguments(new Object[]{i,k+""});  //�޸Ķ�ʱ������
					TimerPools.refreshOne(timerSchle, new Date(new Date().getTime()+1000));  //ˢ�������ʱ��
					System.out.println("���ڶ�ʱ�صĴ�С��:------>"+TimerPools.getTimerPoolSize());
				}
			}).start();
		}*/
		
		    /*ѭ����������*/
		    TimerSchle ts = new TimerSchle("num",AddService.class,
			        "printStr",new Object[]{100,"1"});
		    Map<Integer, Integer> defaultExecuTimeMap = new HashMap<Integer, Integer>();
		    defaultExecuTimeMap.put(1, 1000*2);
			defaultExecuTimeMap.put(2, 1000*3);
			defaultExecuTimeMap.put(3, 1000*7);
			defaultExecuTimeMap.put(4, 1000*8);
			defaultExecuTimeMap.put(5, 1000*9);
			defaultExecuTimeMap.put(6, 1000*10);
			defaultExecuTimeMap.put(7, 1000*2);
			ts.setExecuteTimeMap(defaultExecuTimeMap);
			TimerPools.addTimerFour(ts, new Date(ts.getNextExecuteTime()),ts.getNextExecuteTime());
			//TimerPools.addTimerTwo(ts, 2000, 2000);
			
			//Thread.currentThread().sleep(1000);
			//ts.cancel();
			/*TimerSchle ts1 = new TimerSchle("num1",AddService.class,
			        "printStr",new Object[]{100});
			TimerPools.addTimerOne(ts1, new Date(new Date().getTime()+1000));*/
			/*for(int i=0;i<1000;i++){
				int k = i+1;
				new Thread(new Runnable() {
					@Override
					public void run() {
						TimerSchle timerSchle = TimerPools.getTimerSchleByName("num"); //���ö�ʱ��
						int i = (int)(Math.random() * 10) + 1;
						timerSchle.setArguments(new Object[]{i,k+""});  //�޸Ķ�ʱ������
						TimerPools.refreshOne(timerSchle, new Date(new Date().getTime()+1000));  //ˢ�������ʱ��
						System.out.println("���ڶ�ʱ�صĴ�С��:------>"+TimerPools.getTimerPoolSize());
					}
				}).start();
			}*/
		
		/*TimerSchle ts = new TimerSchle("num",AddService.class,
                "printStr",new Object[]{100,"��ʼ����ʱ��"});
		//֧��spring��Ŀ��Դ
		ts.setTargerSupper(TagerType.SUPPER_SPRING);
		//TimerPools.addTimerOne(ts, new Date(new Date().getTime()+1000));
		TimerPools.addTimerTwo(ts, 0,2000);*/
		//TimerPools.addTimerThree(ts, 2000,1000);
		//TimerPools.addTimerFour(ts, new Date(new Date().getTime()+2000),1000);
		//Thread current = Thread.currentThread();  
		//current.sleep(1000);
		/*TimerSchle timerSchle = TimerPools.getTimerSchleByName("num"); //���ö�ʱ��
		if(timerSchle!=null){
			int i = (int)(Math.random() * 10) + 1;
			timerSchle.setArguments(new Object[]{i,i+""});  //�޸Ķ�ʱ������
			TimerPools.refreshThree(timerSchle, new Date(new Date().getTime()+1000),2000);
			//System.out.println("����������:"+TimerPools.getTimerSchleByName("num").getMethodName());
		}*/
		//ˢ�������ʱ��
        
		/*Timer timer = TimerPools.getTimer();
		//�½���ʱ��ʵ����
        TimerSchle ts = new TimerSchle("num:"+10,AddService.class,
                "cancelLockNumber",new Object[]{10});
        //���ö�ʱ����ʱ��
        Date timerDate = new Date(new Date().getTime()+ 1000);
        //timer.schedule(ts, timerDate);
        TimerPools.addTimerOne(ts,timerDate);
        
      //�½���ʱ��ʵ����
        TimerSchle ts1 = new TimerSchle("num:"+10,AddService.class,
                "cancelLockNumber",new Object[]{10});
        //���ö�ʱ����ʱ��
        Date timerDate1 = new Date(new Date().getTime()+ 2000);
        //timer.schedule(ts1, timerDate1);
        TimerPools.addTimerOne(ts1,timerDate1);*/
	}
	
}
