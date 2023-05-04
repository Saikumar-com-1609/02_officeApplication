package com.ait.service;

import com.ait.bindingclasses.LoginForm;
import com.ait.bindingclasses.SignupForm;
import com.ait.bindingclasses.UnlockForm;

public interface UserService {
	
	public boolean signUp(SignupForm signup);
	
	public boolean unlock(UnlockForm unlock);
	
	public String login(LoginForm login);
	
	public boolean forgotPwd(String email);

}
