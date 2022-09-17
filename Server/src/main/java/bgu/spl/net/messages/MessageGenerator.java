package bgu.spl.net.messages;

public class MessageGenerator {
	//creates different message objects
	public String genAdminRegister(String user, String pw) {
		return "01"+user+"0"+pw+"0";
	}
	public String genStudentRegister(String user, String pw) {
		return "02"+user+"0"+pw+"0";
	}
	public String genLogin(String user, String pw) {
		return "03"+user+"0"+pw+"0";
	}
	public String genLogout() {
		return "04";
	}
	public String genCourseRegister(int num) {
		return "05"+num;
	}
	public String genKdamCheck(int num) {
		return "06"+num;
	}
	public String genCourseStat(int num) {
		return "07"+num;
	}
	public String genStudentStat(String user) {
		return "08"+user+"0";
	}
	public String genIsRegistered(int num) {
		return "09"+num;
	}
	public String genUnregister(int num) {
		return "10"+num;
	}
	public String genMyCourses() {
		return "11";
	}
	public String genACK(int num, String msg) {
		return "12"+num+msg+"0";
	}
	public String genError(int num) {
		return "13"+num;
	}
}
