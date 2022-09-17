package bgu.spl.net.impl.BGRSServer;


import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;



public class Student extends User  {
	/*
	 * this class create a new student when registered and holds the information of him/he
	 */
	
	private ArrayList<Course> coursesRegistered;
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	// constructor
	public Student(String userName,String password) {
		super(userName, password);
		this.coursesRegistered = new ArrayList<Course> ();
	}
	
	//return false because it is a student
	@Override
	public boolean isAdmin() {
		this.lock.readLock().lock();
		try{
			return false;
		}finally {
			this.lock.readLock().unlock();
		}
	}
	
	//return a string describing the student
	public String studentStat() {
		this.lock.readLock().lock();
		try{
			String output = "";
			output += "Student: " + this.getName() + "$";
			output += "Courses: " + this.myCourses() + "$";
			return output;
		}finally {
			this.lock.readLock().unlock();
		}
	}
	
	//return true if the student is registered to the course
	public boolean isRegistered(Course course) {
		this.lock.readLock().lock();
		try{
			if(this.coursesRegistered.contains(course)) {
				return true;
			}
			else {
				return false;
			}
		}finally {
			this.lock.readLock().unlock();
		}
	}
	
	//return a string of the courses this student is registered to
	public String myCourses() {
		this.lock.readLock().lock();
		try {
			if(this.coursesRegistered.size() == 0) {
				return "[]";
			}
			else{
				String output = "[";
				for(Course i:this.coursesRegistered) {
					output = output + i.getCoursenumber()+",";
				}
				output = output.substring(0,output.length()-1);
				output = output + "]";
				return output;
			}
		}finally {
			this.lock.readLock().unlock();
		}	
	}
	
	//returns the list of the courses
	public ArrayList<Integer> getRegisteredCourses(){
		this.lock.readLock().lock();
		try{
			ArrayList<Integer> output = new ArrayList<Integer>();
			for(Course c : this.coursesRegistered) {
				output.add(c.getCoursenumber());
			}
			return output;
		}finally {
			this.lock.readLock().unlock();
		}
	}
	
	//adds a course to this student
	public boolean addCourse(Course course) {
		this.lock.writeLock().lock();
		try {
			this.coursesRegistered.add(course);
			this.coursesRegistered.sort(new CourseComparator());
			return true;
		}finally{
			this.lock.writeLock().unlock();	
		}
	}
	
	//unregister the student from the course
	public boolean unregister(Course course) {
		this.coursesRegistered.remove(course);
		return true;
	}
	
}
