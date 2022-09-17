package bgu.spl.net.messages;

import bgu.spl.net.impl.BGRSServer.Database;

public class StudentStat implements Message{
	/*
	 * Admin message only
	 * prints the data of the student received
	 */
	private Database db = Database.getInstance();
	private String username;
	
	public StudentStat(String userName) {
		this.username = userName;
	}
	
	@Override
	public String action(String input) {
		return this.db.studentStat(this.username,input.substring(2,input.length()-1));
	}
}
