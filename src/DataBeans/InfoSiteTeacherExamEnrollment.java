/*
 * Created on 29/Mai/2003
 *
 * 
 */
package DataBeans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author João Mota
 *
 */
public class InfoSiteTeacherExamEnrollment implements ISiteComponent {
	private InfoExam infoExam;
	private InfoExamEnrollment infoExamEnrollment;

	/**
	 * 
	 */
	public InfoSiteTeacherExamEnrollment() {

	}
	public InfoSiteTeacherExamEnrollment(
		InfoExam infoExam,
		InfoExamEnrollment infoExamEnrollment) {
		setInfoExam(infoExam);
		setInfoExamEnrollment(infoExamEnrollment);
	}
	/**
	 * @return
	 */
	public InfoExam getInfoExam() {
		return infoExam;
	}

	/**
	 * @return
	 */
	public InfoExamEnrollment getInfoExamEnrollment() {
		return infoExamEnrollment;
	}

	/**
	 * @param exam
	 */
	public void setInfoExam(InfoExam exam) {
		infoExam = exam;
	}

	/**
	 * @param enrollment
	 */
	public void setInfoExamEnrollment(InfoExamEnrollment enrollment) {
		infoExamEnrollment = enrollment;
	}

	public List getDays() {
		List days = new ArrayList();
		int i = 1;
		while (i < 32) {
			days.add(new Integer(i));
			i++;
		}
		return days;
	}

	public List getMonths() {
		List month = new ArrayList();
		int i = 1;
		while (i < 13) {
			month.add(new Integer(i));
			i++;
		}
		return month;
	}
	public List getYears() {

		List years = new ArrayList();
		Calendar calendar = Calendar.getInstance();
		years.add(new Integer(calendar.getTime().getYear()+ 1900));
		years.add(new Integer(calendar.getTime().getYear() + 1901));
		return years;
	}
	public List getHours() {
		List hours = new ArrayList();
		int i = 0;
		while (i < 24) {
			hours.add(formatTime(new Integer(i).toString()));
			i++;
		}
		return hours;
	}
	public List getMinutes() {
		List minutes = new ArrayList();
		minutes.add("00");
		minutes.add("30");
		return minutes;
	}
	
	private String formatTime(String time){
			String result= time;
			if (result.length()==1){
				result="0"+result;
			}
			return result;
		}
}
