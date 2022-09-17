package bgu.spl.net.messages;

import bgu.spl.net.impl.BGRSServer.Database;

public class IsRegistered implements Message{
	/*
	 * prints REGISTERED if the student registered to the course
	 * otherwise print NOT REGISTERD
	 */
	
	private Database db = Database.getInstance();
	private String userName;
	
	public IsRegistered(String userName) {
		this.userName = userName;
	}
	@Override
	public String action(String input) {
		return db.isRegistered(userName, Integer.parseInt(input.substring(2,input.length()-1)));
	}
}
