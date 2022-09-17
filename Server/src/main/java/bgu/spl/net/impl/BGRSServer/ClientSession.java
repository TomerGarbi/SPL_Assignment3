package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.messages.AdminReg;
import bgu.spl.net.messages.CourseRegister;
import bgu.spl.net.messages.CourseStat;
import bgu.spl.net.messages.ErrorAct;
import bgu.spl.net.messages.IsRegistered;
import bgu.spl.net.messages.KdamCheck;
import bgu.spl.net.messages.Login;
import bgu.spl.net.messages.Logout;
import bgu.spl.net.messages.Message;
import bgu.spl.net.messages.MyCourses;
import bgu.spl.net.messages.StudentReg;
import bgu.spl.net.messages.StudentStat;
import bgu.spl.net.messages.Unregister;

public class ClientSession {
	/*
	 * this class handling the messages received from the clients
	 */
	
	
	//0 - not logged in, 1 - admin logged in, 2 - student logged in
	private int loginState = 0;
	//login information kept by protocol
	private String username = null;
	public ClientSession() {
		//initially not logged in;
	}
	public String act(String msg) {
		String t = msg;
		int code = Integer.parseInt(t.substring(0,2));
		Message act = generateNewAction(code, msg);
		return act.action(msg);
	}
	public Message generateNewAction(int code, String msg) {
		//STUDENTREG received
		if(code == 1 && loginState == 0) {
			return new AdminReg();
		}
		//ADMINREG received
		if(code == 2 && loginState == 0) {
			return new StudentReg();
		}
		//LOGIN received
		if(code == 3 && loginState == 0) {
			Login login = new Login();
			login.attemptLogin(msg);
			if(login.loginType()!=0) {
	    		username = login.getUser();
	    		loginState = login.loginType();
			}
			return login;
		}
		//LOGOUT received
		if(code == 4 && loginState != 0) {
			try {
				return new Logout(this.username);
			} finally {
				loginState = 0;
				username = null;
			}
		}
		//COURSEREG received
		if(code == 5 && loginState == 2) {
			
			return new CourseRegister(username);
		}
		//KDANCHECK received
		if(code == 6  && loginState != 0) {
			return new KdamCheck(this.username);
		}
		//COURSESTAT received
		if(code == 7 && loginState == 1) {
			return new CourseStat(this.username);
		}
		//STUDENTSTAT received
		if(code == 8 && loginState == 1) {
			return new StudentStat(this.username);
		}
		//ISREGISTERED received
		if(code == 9 && loginState == 2) {
			return new IsRegistered(this.username);
		}
		//UNREGISTER received
		if(code == 10 && loginState == 2) {
			return new Unregister(this.username);
		}
		//MYCOURSES received
		if(code == 11 && loginState == 2) {
			//unfinished
			return new MyCourses(this.username);
		}
		//if opcode does not exist return an ERROR message 
		return new ErrorAct(msg.substring(0,2));
	}
}
