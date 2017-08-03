package com.lcydream.project.timer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

/**
 * ��ʱ����
 * @author LuoChunYun
 *
 */
public class TimerPools {

	//����һ��Timerִ����
	private static Timer timer = null;

	//����Ĭ�ϵĶ�ʱ���صĴ�С
	private static int size = 10000;

	//����һ����ʱ����
	private static List<TimerSchle> timerPool = new ArrayList<TimerSchle>();

	//˽�л����췽��
	private TimerPools(){}
	
	//��ӵ�һ�ֶ�ʱ������(�趨ָ������task��ָ��ʱ��timeִ�� schedule(TimerTask task, Date time))
		public synchronized static void addTimerOne(TimerSchle timerSchle,Date date){
			if(timerPool.size()<size){
				//timer.schedule(timerSchle,new Date(new Date().getTime()+5000));
				if(date.getTime() > new Date().getTime()) {
					//�ж�������ֵĶ�ʱ���Ƿ����
					TimerSchle schle = getTimerSchleByName(timerSchle.getTimerName());
					if(schle != null ){
						timerPool.remove(schle);
						schle=null;
					}
					//�˷������ö�ʱ���ǵ��ζ�ʱ
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
				throw new RuntimeException("<--------------��ʱ����������������-------------->");
			}
		}

		//��ӵڶ��ֶ�ʱ������(�趨ָ������task��ָ���ӳ�delay����й̶��ӳ�peroid��ִ�� schedule(TimerTask task, long delay, long period))
		public synchronized static void addTimerTwo(TimerSchle timerSchle,long delay, long period){
			if(timerPool.size()<size){
				if(period>0) {
					TimerSchle schle = getTimerSchleByName(timerSchle.getTimerName());
					if(schle != null ){
						timerPool.remove(schle);
						schle=null;
					}
					timerSchle.setFourTimer(false);  //����Ϊ���ɵĶ�ʱ
					timerPool.add(timerSchle);
					Timer timer = TimerPools.getTimer();
					if(timer != null){
						timer.schedule(timerSchle, delay ,period);
					}

				}
			}else{
				throw new RuntimeException("<--------------��ʱ����������������-------------->");
			}
		}

		//��ӵ����ֶ�ʱ������(�趨ָ������task��ָ���ӳ�delay����й̶�Ƶ��peroid��ִ��)
		//scheduleAtFixedRate(TimerTask task, long delay, long period)
		public synchronized static void addTimerThree(TimerSchle timerSchle,long delay, long period){
			if(timerPool.size()<size){
				if(period>0) {
					TimerSchle schle = getTimerSchleByName(timerSchle.getTimerName());
					if(schle != null ){
						timerPool.remove(schle);
						schle=null;
					}
					timerSchle.setFourTimer(false);  //����Ϊ���ɵĶ�ʱ
					timerPool.add(timerSchle);
					Timer timer = TimerPools.getTimer();
					if(timer != null){
						timer.scheduleAtFixedRate(timerSchle, delay ,period);
					}
				}
			}else{
				throw new RuntimeException("<--------------��ʱ����������������-------------->");
			}
		}

		//��ӵ����ֶ�ʱ������(����ָ��������task��ָ����ʱ��firstTime��ʼ�����ظ��Ĺ̶�����periodִ��)
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
				throw new RuntimeException("<--------------��ʱ����������������-------------->");
			}
		}

		//���ݶ�ʱ�������ֻ�ȡ��ʱ������
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

		//���ݶ�ʱ�������ֻ��߶�ʱ���������쳣��ʱ��
		public synchronized static void removeTimerSchleByName(String timerSchleName){
			TimerSchle ts = getTimerSchleByName(timerSchleName);
			timerPool.remove(ts);
			//�Ƴ�����ȡ����ʱ��
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

		//���ݶ�ʱ��������Ƴ��Ķ�ʱ��
		public synchronized static void removeTimerSchleByClass(TimerSchle timerSchle){
			//�Ƴ�����ȡ����ʱ��
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

		//�޸Ķ�ʱ����ˢ�¶�ʱ�ض���
		public synchronized static void refreshOne(TimerSchle timerSchle,Date date){
			if(timerSchle != null){
				if(date.getTime() > new Date().getTime()){
					TimerSchle ts = new TimerSchle(timerSchle.getTimerName(),timerSchle.getClassName(),timerSchle.getMethodName(),timerSchle.getArguments());
					ts.copyToMe(timerSchle);
					//ȡ����ʱ����ִ��
					timerSchle.cancel();
					//�Ƚ���ʱ������ȡ��
					timerPool.remove(timerSchle);
					timer.purge();
					//��ʶ����GC����
					timerSchle=null;
					addTimerOne(ts, date);
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					System.out.println("<------------��ʱ��"+ts.getTimerName()+"ˢ�³ɹ�***********ˢ��ʱ����:"+formatter.format(new Date())+"---------->\n\n");
				}else{
					throw new RuntimeException("<--------------��ʱ��ʱ�䲻�������ö�ʱ����ʱ��ǰ��-------------->");
				}
			}else{
				throw new RuntimeException("<--------------δ��ȡ����Ч�Ķ�ʱ����-------------->");
			}
		}

		//�޸Ķ�ʱ����ˢ�¶�ʱ�ض���
		public synchronized static void refreshTwo(TimerSchle timerSchle,long delay, long period){
			if(timerSchle != null){
				if(delay>0 && period>0){
					TimerSchle ts = new TimerSchle(timerSchle.getTimerName(),timerSchle.getClassName(),timerSchle.getMethodName(),timerSchle.getArguments());
					ts.copyToMe(timerSchle);
					//ȡ����ʱ����ִ��
					timerSchle.cancel();
					//�Ƚ���ʱ������ȡ��
					timerPool.remove(timerSchle);
					//��ʶ����GC����
					timerSchle=null;
					addTimerTwo(ts, delay, period);
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					System.out.println("<------------��ʱ��"+ts.getTimerName()+"ˢ�³ɹ�***********ˢ��ʱ����:"+formatter.format(new Date())+"---------->");
				}else{
					throw new RuntimeException("<--------------��ʱ��ʱ�䲻��ΪС�������-------------->");
				}
			}else{
				throw new RuntimeException("<--------------δ��ȡ����Ч�Ķ�ʱ����-------------->");
			}
		}

		public synchronized static void refreshThree(TimerSchle timerSchle,Date firstTime,long period){
			if(timerSchle != null){
				if(period < 0){
					throw new RuntimeException("��ʱ��ʱ�䲻��ΪС�������");
				}
				TimerSchle ts = new TimerSchle(timerSchle.getTimerName(),timerSchle.getClassName(),timerSchle.getMethodName(),timerSchle.getArguments());
				ts.copyToMe(timerSchle);
				//ȡ����ʱ����ִ��
				timerSchle.cancel();
				//�Ƚ���ʱ������ȡ��
				timerPool.remove(timerSchle);
				//��ʶ����GC����
				timerSchle=null;
				addTimerFour(ts, firstTime, ts.getNextExecuteTime());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				System.out.println("<------------��ʱ��"+ts.getTimerName()+"ˢ�³ɹ�***********ˢ��ʱ����:"+formatter.format(new Date())+"---------->");
			}else{
				throw new RuntimeException("<------------δ��ȡ����Ч�Ķ�ʱ����------------>");
			}
		}

		//��ȡ��ʱ��������ĸ���
		public synchronized static int getTimerPoolSize(){
			return timerPool.size();
		}

		//����ģʽ��ȡtimer
		public synchronized static Timer getTimer(){
			// ����ʵ����ʱ����жϣ���ʹ��ͬ������飬instance������nullʱ��ֱ�ӷ��ض����������Ч�ʣ�
			if(timer==null){
				//ͬ������飨����δ��ʼ��ʱ��ʹ��ͬ������飬��֤���̷߳���ʱ�����ڵ�һ�δ����󣬲����ظ���������
				synchronized (TimerPools.class) {
					//δ��ʼ�������ʼinstance����
					if(timer==null){
						timer = new Timer();
					}
				}
			}
			return timer;
		}

		//���ö�ʱ���صĴ�С
		public synchronized static void setTimerSize(int sizeLong){
			size = sizeLong;
		}
	}
