package bgu.spl.net.impl.BGRSServer;



public class User {
	/*
	 * this class holds the information of the users in the database
	 * user can be admin or a student
	 */
	private String userName;
	private String password;
	private boolean login;

	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
		this.login = false;
	}
	
	//override by the student/admin. return true if the user is admin
	public boolean isAdmin() {
		System.out.println("NEVER USE THIS METHOD!");
		return false;
	}
	
	//return true if the password received is the user's password
	public boolean isPassword(String password) {
		return this.password.compareTo(password)==0;
	}
	
	//return true if the user is login
	public boolean isLogin() {
		return this.login;
	}
	
	//updates the login state of this user when he login
	public void loginUser() {
		this.login = true;
	}
	
	//updates the login state of this user when he logout
	public void logoutUser() {
		this.login = false;
	}
	
	//return the name of the student
	public String getName() {
		return this.userName;
	}
	
	//return the password of the student
	public String getPW() {
		return this.password;
	}
}
