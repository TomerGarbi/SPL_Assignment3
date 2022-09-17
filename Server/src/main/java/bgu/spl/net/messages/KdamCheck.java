package bgu.spl.net.messages;

import bgu.spl.net.impl.BGRSServer.Database;

public class KdamCheck implements Message{
	/*
	 * print the kdam course list of the course received
	 */
	
	private Database db = Database.getInstance();
	private String userName;
	
	public KdamCheck(String userName) {
		this.userName = userName;
	}
	
	@Override
	public String action(String input) {
		return this.db.kdamCheck(this.userName, Integer.parseInt(input.substring(2,input.length()-1)));
	}

}