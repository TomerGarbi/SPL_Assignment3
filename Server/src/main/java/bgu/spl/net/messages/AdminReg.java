package bgu.spl.net.messages;

import bgu.spl.net.impl.BGRSServer.Database;

public class AdminReg implements Message {
	//create new admin level user
	//login state must be logged out
	//add new admin user to database
	//make sure submitted username isn't taken
	private Database db = Database.getInstance();
	
	@Override
	public String action(String input) {
		//parse input for username and password
		String username = input.substring(2,input.indexOf(" ",2));
		String password = input.substring(input.indexOf(" ",input.indexOf(" ",2))+1,input.length()-1);

		if(!db.exist(username)) {
			db.adminRegister(username, password);
			//return ACK
			return "1201\n";
		}
		//return Error
		return "1301\n";
	}
}