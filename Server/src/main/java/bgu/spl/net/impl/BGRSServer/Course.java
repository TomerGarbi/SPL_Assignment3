package bgu.spl.net.impl.BGRSServer;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Course {
	/*
	 * this class create a course and holds the information about the course
	 */
	
	private int order;
	private int courseNumber;
	private String courseName;
	private ArrayList<Integer> kdamCourses;
	private int totalSeats;
	private int seatsAvailable;
	private String[] studentsList;
	private int studentsIndex;
	private ReadWriteLock lock = new ReentrantReadWriteLock() ;

	//constructor
	public Course(int order, String s) {
		//get the course number
		int i = s.indexOf('|');
		this.courseNumber = Integer.parseInt(s.substring(0,i));
		//get the course name
		s = s.substring(i+1);
		i = s.indexOf('|');
		this.courseName = s.substring(0,i);
		//get the kdam courses for this course
		s = s.substring(i+1);
		i = s.indexOf('|');
		this.kdamCourses = new ArrayList<Integer>();
		String list = s.substring(1,i);
		if(list.length()>2) {
			int begin = 0;
			int find = 0;
			while(list.charAt(find)!=']') {
				if(list.charAt(find)==',') {
					int kdam = Integer.parseInt(list.substring(begin,find));
					begin = find + 1;
					this.kdamCourses.add(kdam);
				}
				find = find +1;
			}
			int kdam = Integer.parseInt(list.substring(begin,find));
			this.kdamCourses.add(kdam);
		}
		//get total seats in this course
		s = s.substring(i+1);
		this.totalSeats = Integer.parseInt(s);
		this.studentsList = new String[this.totalSeats];
		this.studentsIndex = -1;
		this.seatsAvailable = this.totalSeats;
		this.order = order;
		}
	
	//return the course number
	public int getCoursenumber() {
		this.lock.readLock().lock();
		try{
			return this.courseNumber;
		}finally {
			this.lock.readLock().unlock();
		}
	}
	
	//add student to the course
	public boolean addStudent(String s) {
		this.lock.writeLock().lock();
		try {
			if(this.seatsAvailable == 0) {
				return false;
			}
			for(int i=0 ; i<=this.studentsIndex; i++) {
				if(s.equals(this.studentsList[i])) {
					return false;
				}
			}
			if(this.studentsIndex == -1) {
				this.studentsList[0] = s;
				this.studentsIndex = this.studentsIndex + 1;
				this.seatsAvailable = this .seatsAvailable - 1;
				return true;
			}
			
			else {
				this.studentsIndex = this.studentsIndex + 1;
				this.studentsList[this.studentsIndex] = s;
				Arrays.sort(this.studentsList, 0, this.studentsIndex+1);
				this.seatsAvailable = this .seatsAvailable - 1;
				return true;
			}
			
		}
		finally {
			this.lock.writeLock().unlock();
			}	
		}
	
	//return a string describing the course
	public String courseStat() {
		this.lock.readLock().lock();
		try{
			String output = "";
			output += "Course: (" + this.courseNumber + ") " + this.courseName + "$";
			output += "Seats Available: " + this.seatsAvailable+"/" + this.totalSeats + "$";
			output += "Students Registered: " + this.studentsList() + "$";
			return output ;
		}finally {
			this.lock.readLock().unlock();
		}
		
	}
	
	//return true if unregister process is done 
	public boolean unregister(String userName) {
		this.lock.writeLock().lock();
		try {
			int i = -1;
			for(int j=0;j<=this.studentsIndex;j++) {
				if(this.studentsList[j].equals(userName)){
					i = j;
					break;
				}
			}
			if(i !=-1) {
				while(i<this.studentsIndex && this.studentsList[i + 1]!=null) {
					this.studentsList[i] = this.studentsList[i + 1];
					i = i + 1;
				}
				this.studentsList[this.studentsIndex] = null;
				this.studentsIndex = this.studentsIndex - 1;
				this.seatsAvailable = this.seatsAvailable + 1;
				return true;
			}
			else {
				return false;
			}
		}finally {
			this.lock.writeLock().unlock();
		}
		
	}
	
	//return the kdam courses list for this course
	 public ArrayList<Integer> getKdamCourses(){
		 this.lock.readLock().lock();
			try{
				return this.kdamCourses;
			}finally {
				this.lock.readLock().unlock();
			}
	 }
	
	 //return true if the student is registered to this course
	 public boolean isRegistered(String student) {
		 this.lock.readLock().lock();
			try{
				int i = Arrays.binarySearch(this.studentsList, student);
				return i != -1;
				
			}finally {
				this.lock.readLock().unlock();
			}
	 }
	 //return a string representing the list if students in this course
	 public String studentsList() {
		 if(this.studentsIndex == -1) {
			 return "[]";
		 }
		 String output = "[";
		 for(int i=0;i<=this.studentsIndex;i++) {
			 output = output + this.studentsList[i] + ", ";
		 }
		 output = output.substring(0,output.length()-2) + "]";
		 return output;
	 }
	 public int getOrder() {
		 return this.order;
	 }
	public String toString() {
		return ""+this.courseNumber;
	}
}
	

	



