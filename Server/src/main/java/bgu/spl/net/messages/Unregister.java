package bgu.spl.net.messages;

import bgu.spl.net.impl.BGRSServer.Database;

public class Unregister implements Message{
	/*
	 * unregister a student from the course received
	 */
	
	private Database db = Database.getInstance();
	private String userName;
	
	public Unregister(String userName) {
		this.userName = userName;
	}
	@Override
	public String action(String input) {
		int courseNum = Integer.parseInt(input.substring(2,input.length()-1));
		return this.db.unregister(this.userName, courseNum);
	}
}
