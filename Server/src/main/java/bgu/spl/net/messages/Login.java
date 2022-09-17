package bgu.spl.net.messages;

import bgu.spl.net.impl.BGRSServer.Database;

public class Login implements Message{
	/*
	 *try to login the user with the user name and password received 
	 * return ACK if succeeded
	 */
	
	private Database db = Database.getInstance();
	private String user;
	private String pw;
	private boolean loggedin = false;
	private String result = "1303\n";
	@Override
	public String action(String input) {
		return result;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getPw() {
		return pw;
	}
	
	public void attemptLogin(String input) {
		//try to login with username and password
		user = input.substring(2,input.indexOf(" ",2));
		pw = input.substring(input.indexOf(" ",input.indexOf(" ",2))+1,input.length()-1);
		if(db.loginUser(this.user, this.pw)) {
			result = "1203\n";
			this.loggedin = true;
		}
	}
	
	public int loginType() {
		if(loggedin) {
			if(db.isAdmin(user)) {
				return 1;
			}
			return 2;
		}
		return 0;
	}
}
