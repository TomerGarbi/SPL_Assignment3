package bgu.spl.net.impl.BGRSServer;


public class Admin extends User{
	/*
	 * This class create a new admin when registered and holds the information of him/her 
	 */

	public Admin(String userName, String password) {
		super (userName,password);
	}
	
	@Override
	public boolean isAdmin() {
		return true;
	}
	

}
