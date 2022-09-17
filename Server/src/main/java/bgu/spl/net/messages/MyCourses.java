package bgu.spl.net.messages;

import bgu.spl.net.impl.BGRSServer.Database;

public class MyCourses implements Message{
	/*
	 * prints a list of the courses that the user is registered to
	 */
	
	private Database db = Database.getInstance();
	private String userName;
	
	public MyCourses(String userName) {
		this.userName = userName;
	}
	@Override
	public String action(String input) {
		return this.db.myCourses(userName);
	}
}
