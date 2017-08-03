package com.lcydream.project.timer.service;

public class RegisterService {

	public boolean noticeRegisterService(String num){
		if(num!=null && "1".equals(num.trim())){
			System.out.println("true");
			return true;
		}
		System.out.println("false");
		return false;
	}
}
