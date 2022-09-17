package bgu.spl.net.impl.BGRSServer;
import java.util.Comparator;


public class CourseComparator implements Comparator<Course> {
	/*
	 * this is a comparator between two courses
	 * check which course appear earlier in the courses file according to order
	 */

	public CourseComparator() {
		
	}

	@Override
	public int compare(Course arg0, Course arg1) {
		if(arg0.getOrder()<arg1.getOrder()) {
			return -1;
		}
		return 1;
	}

}
