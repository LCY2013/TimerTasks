package com.lcydream.project.timer.service;

public class PayService {

	private RegisterService registerService = new RegisterService();
	
	public boolean noticePay(String num){
		if(num!=null && "1".equals(num.trim())){
			return true; 
		}
		return false;
	}
	
	public String getString(){
		return "¸¸ÀàµÄ×Ö·û´®";
	}
	
	public RegisterService getRegisterService(){
		return registerService;
	}
	
}
