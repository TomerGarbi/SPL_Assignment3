package bgu.spl.net.messages;

import bgu.spl.net.impl.BGRSServer.Database;

public class CourseRegister implements Message{
	/*
	 * try to register the client to the course 
	 * return ACK if succeeded, ERROR otherwise
	 */
	
	private String user;
	private Database db = Database.getInstance();
	
	public CourseRegister(String submitted_user) {
		user = submitted_user;
	}
	
	@Override
	public String action(String input) {
		int courseNum = Integer.parseInt(input.substring(2,input.length()-1));
		return db.courseReg(this.user, courseNum);
	}

}
