package bgu.spl.net.impl.BGRSServer;

import java.nio.file.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
	private static Database database;
	private ConcurrentHashMap<Integer, Course> coursesMap;
	private ConcurrentHashMap<String, User> usersMap;
	private ArrayList<String> loginUsers;


	//to prevent user from creating new Database
	private Database() {
		//getting the path to the current folder
		Path currentFolder = Paths.get("");
		String coursesFilePath = currentFolder.toAbsolutePath().toString() + "/Courses.txt";
		//initializing the database with the Courses.txt file given 
		initialize(coursesFilePath);
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Database getInstance() {
		if(database == null) {
			database = new Database();
		}
		return database;
	}
	
	/**
	 * loades the courses from the file path specified 
	 * into the Database, returns true if successful.
	 */
	boolean initialize(String coursesFilePath) {
		this.loginUsers = new ArrayList<String>();
		this.coursesMap = new ConcurrentHashMap<Integer, Course>();
		this.usersMap = new ConcurrentHashMap<String, User>();
		File coursesFile = new File(coursesFilePath);
		try {
			Scanner scan = new Scanner(coursesFile);
			int i = 1;
			while (scan.hasNext()) {
				Course course = new Course(i,scan.nextLine());
				i = i + 1;
				this.coursesMap.put(course.getCoursenumber(), course);
			}
			scan.close();
		} catch (FileNotFoundException e) {
			return false;
		}
		return false;
	}
	
	//check if the user name is for an admin
	public boolean isAdmin(String userName) {
		return this.usersMap.get(userName).isAdmin();
	}
	
	//return true if the user name already exists in the Database
	public boolean exist(String userName) {
		return this.usersMap.containsKey(userName);
	}
	//this method register new admin to the database
	public String adminRegister(String userName, String password) {
		if(exist(userName)) {
			return "1301\n";
		}
		Admin admin = new Admin(userName, password);
		this.usersMap.put(userName, admin);
		return "1201\n";
	}
	
	//this method register new student to the database
	public String studentRegister(String userName, String password) {
		if(this.usersMap.containsKey(userName)) {
			return "1302\n";
		}
		Student student = new Student(userName, password);
		this.usersMap.put(userName, student);
		return "1202\n";
	}
	
	//this method verify the user details and log in the user 
	public boolean loginUser(String userName, String password) {
		if(this.usersMap.containsKey(userName)) {
			//System.out.println("Database - loginUser | username identified in database");
			//System.out.println("Database - loginUser | password in database: "+usersMap.get(userName).getPW()+" submitted password: "+password);
			if(this.usersMap.get(userName).isPassword(password)&&!this.usersMap.get(userName).isLogin()) {
				this.usersMap.get(userName).loginUser();
				this.loginUsers.add(userName);
				return true;
			}
		}
		return false;
	}
	
	//logout the user received
	public String logout(String userName) {
		if(this.loginUsers.contains(userName)) {
			this.loginUsers.remove(userName);
			this.usersMap.get(userName).logoutUser();
			return "1204\n";
		}
		else {
			return "1304\n";
		}
	}
	
	//try to register a student to a course
	public String courseReg(String student, int courseNum) {
		User user = this.usersMap.get(student);
		Course course = this.coursesMap.get(courseNum);
		if(course !=null && user != null) {
			ArrayList<Integer> studentsKdams = ((Student)user).getRegisteredCourses();
			ArrayList<Integer> courseKdams = course.getKdamCourses();
			if(pass(studentsKdams,courseKdams)) {
				if(course.addStudent(student)) {
					((Student)user).addCourse(course);
					return "1205\n";
				}
			}	
		}
		return "1305\n";
	}
	
	//return true if the student completed the kdam requirements for the course
	public boolean pass(ArrayList<Integer> studentsKdams, ArrayList<Integer> courseKdams) {
		for(Integer kdam:courseKdams) {
			if(!studentsKdams.contains(kdam)) {
				return false;
			}
		}
		return true;
	}
	
	//checks if the student completed the kdam requirements for the course
	public String kdamCheck(String userName, int course) {
		Course c = this.coursesMap.get(course);
		if(c != null) {
		    ArrayList<Integer> kdamCourses = c.getKdamCourses();
		    return "1206"+kdamCourses.toString()+"$"+ "\n";    
		}
		return "1306\n";
	}
	
	//returns course details
	public String courseStat(String userName, int course) {
		if(this.coursesMap.containsKey(course) && isAdmin(userName) && this.loginUsers.contains(userName)) {
			return "1207" +  this.coursesMap.get(course).courseStat() + "\n";
		}
		else {
			return "1307\n";
		}
	}
	
	//return students details
	public String studentStat(String user, String student) {
		User s = this.usersMap.get(student);
		if(s!=null && !s.isAdmin()) {
			return "1208" + ((Student)(s)).studentStat() + "\n";
		}
		else {
			return "1308\n";
		}	
	}
	
	// return an appropriate message if the student is registered to the course
	public String isRegistered(String userName, int course) {
		if(this.loginUsers.contains(userName)) {
			Student s = ((Student)this.usersMap.get(userName));
			Course c = this.coursesMap.get(course);
			if(c != null && s.isRegistered(c)) {
				return "1209REGISTERED\n";
			}
		}
		return "1209NOT REGISTERED\n";
	}
	
	//unregister the student from the course
	public String unregister(String userName, int course) {
		Course c = this.coursesMap.get(course);
		if(c!=null && c.unregister(userName)) {
			((Student)this.usersMap.get(userName)).unregister(c);
			return "1210\n";
		}
		else {
			return "1310\n";
		}
	}
	
	//return the courses of the student
	public String myCourses(String userName) {
		User user = this.usersMap.get(userName);
		if(user != null && !user.isAdmin() && user.isLogin()) {
			return "1211" + ((Student)user).myCourses()+ "$\n";
		}
		else {
			return "1311\n";
		}
	}
}
