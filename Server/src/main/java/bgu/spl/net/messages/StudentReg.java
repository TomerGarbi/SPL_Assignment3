package bgu.spl.net.messages;

import bgu.spl.net.impl.BGRSServer.Database;

public class StudentReg implements Message {
	//create new student level user
	//login state must be logged out
	//add new student user to database
	//make sure submitted user name isn't taken
	private Database db = Database.getInstance();
	
	@Override
	public String action(String input) {
		//parse input for user name and password
		String username = input.substring(2,input.indexOf(" ",2));
		String password = input.substring(input.indexOf(" ",input.indexOf(" ",2))+1,input.length()-1);
		if(!db.exist(username)) {
			db.studentRegister(username, password);
			//return ACK
			return "1202\n";
		}
		//return Error
		return "1302\n";
	}
}
