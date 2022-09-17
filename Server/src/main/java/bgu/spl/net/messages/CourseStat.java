package bgu.spl.net.messages;

import bgu.spl.net.impl.BGRSServer.Database;

public class CourseStat implements Message{
	/*
	 *Admin message only
	 *return a string describing the course 
	 */
	
	private Database db = Database.getInstance();
	private String userName;
	
	public CourseStat(String userName) {
		this.userName = userName;
	}
	
	@Override
	public String action(String input) {
		return db.courseStat(this.userName, Integer.parseInt(input.substring(2, input.length()-1)));
	}
}
