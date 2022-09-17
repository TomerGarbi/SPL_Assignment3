package bgu.spl.net.messages;

import bgu.spl.net.impl.BGRSServer.Database;

public class Logout implements Message{
	/*
	 * logout the user from the system
	 */
	
	private Database db = Database.getInstance();
	private String userName;
	
	public Logout(String username) {
		this.userName = username;
	}
	@Override
	public String action(String input) {
		
		return this.db.logout(this.userName);
	}

}
