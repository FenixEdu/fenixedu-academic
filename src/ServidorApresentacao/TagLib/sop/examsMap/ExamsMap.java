/*
 * Created on Apr 3, 2003
 *
 */
package ServidorApresentacao.TagLib.sop.examsMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoExam;
import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionCourse;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
public class ExamsMap {

	private List days;

	public ExamsMap(InfoExamsMap infoExamsMap) {
		Calendar firstDayOfSeason = infoExamsMap.getStartSeason1();
		Calendar lastDayOfSeason = infoExamsMap.getEndSeason2();

		days = new ArrayList();
		if (firstDayOfSeason.get(Calendar.YEAR) != lastDayOfSeason.get(Calendar.YEAR)) {
			for (int day = firstDayOfSeason.get(Calendar.DAY_OF_YEAR);
				 day < makeLastDayOfYear(firstDayOfSeason).get(Calendar.DAY_OF_YEAR);
				 day++) {
				 Calendar tempDayToAdd = makeDay(firstDayOfSeason,
				 	day - firstDayOfSeason.get(Calendar.DAY_OF_YEAR)); 
				days.add(new DayOfMap(tempDayToAdd, findExams(tempDayToAdd, infoExamsMap.getExecutionCourses())));
						                        // invoke method to determine list of exams for this day
			}
			
			firstDayOfSeason = makeFirstDayOfYear(lastDayOfSeason);
		}

		for (int day = firstDayOfSeason.get(Calendar.DAY_OF_YEAR);
			 day < lastDayOfSeason.get(Calendar.DAY_OF_YEAR);
			 day++) {
			Calendar tempDayToAdd = makeDay(firstDayOfSeason,
				day - firstDayOfSeason.get(Calendar.DAY_OF_YEAR));
			days.add(new DayOfMap(tempDayToAdd, null));
											 // invoke method to determine list of exams for this day
		}

	}

	private class DayOfMap {
		private Calendar day;
		private List exams;
		
		public DayOfMap(Calendar day, List exams) {
			setDay(day);
			setExams(exams);
		}

		public Calendar getDay() {
			return day;
		}

		public List getExams() {
			return exams;
		}

		public void setDay(Calendar calendar) {
			day = calendar;
		}

		public void setExams(List list) {
			exams = list;
		}
	}


	private List findExams(Calendar day, List executionCourses) {
		List result = new ArrayList();

		for (int i = 0; i < executionCourses.size(); i++) {
			InfoExecutionCourse infoExecutionCourse =
				(InfoExecutionCourse) executionCourses.get(i);
			List infoExams = infoExecutionCourse.getAssociatedInfoExams();
			
			for (int j = 0; j < infoExams.size(); j++) {
				InfoExam infoExam = (InfoExam) infoExams.get(j);

				// TODO : if day is the same then add it to the list 				
			}
		}

		return result;
	}


// ------------------------------------------------------------------------------------------
// --- Utils Para Manupulação de Datas ------------------------------------------------------ 

	private Calendar getPreviousMonday(Calendar anyDay) {
		if (anyDay.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			return anyDay;
		if (anyDay.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
			return anyDay;

		int daysToGoBack = anyDay.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
		if (anyDay.get(Calendar.DAY_OF_YEAR) < 6) {
			// Monday is parte of Previous year
			Calendar dayOfPreviousYear = Calendar.getInstance();
			dayOfPreviousYear.set(Calendar.YEAR, anyDay.get(Calendar.YEAR) - 1);
			dayOfPreviousYear.set(Calendar.MONTH, Calendar.DECEMBER);
			dayOfPreviousYear.set(Calendar.DAY_OF_MONTH,
				31 - daysToGoBack + anyDay.get(Calendar.DAY_OF_YEAR));
			dayOfPreviousYear.set(Calendar.HOUR_OF_DAY, 0);
			dayOfPreviousYear.set(Calendar.MINUTE, 0);
			dayOfPreviousYear.set(Calendar.SECOND, 0);
			return dayOfPreviousYear;
		} else {
			// Monday is parte of this year
			return makeDay(anyDay, 0 - daysToGoBack);
		}
	}

	private Calendar getNextSaturday(Calendar anyDay) {
		int year = anyDay.get(Calendar.YEAR);
		int daysInYear = 365;
		// In the Gregorian calendar, the following rules decides which years are leap years:
		//    1. Every year divisible by 4 is a leap year.
		//    2. But every year divisible by 100 is NOT a leap year
		//    3. Unless the year is also divisible by 400, then it is still a leap year. 
		if ((year % 400) ==  0) {
			daysInYear = 366;
		} else if ((year % 4) == 0 && (year % 100) != 0) {
			daysInYear = 366;
		}
		
		if (anyDay.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			return anyDay;
		if (anyDay.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			return anyDay;

		int daysToGoForward = Calendar.SATURDAY - anyDay.get(Calendar.DAY_OF_WEEK);
		// TODO : verify "anyDay.get(Calendar.DAY_OF_YEAR) >= daysInYear - 6"
		if (anyDay.get(Calendar.DAY_OF_YEAR) >= daysInYear - 6) {
			// Monday is parte of Previous year
			Calendar dayOfPreviousYear = Calendar.getInstance();
			dayOfPreviousYear.set(Calendar.YEAR, anyDay.get(Calendar.YEAR) + 1);
			dayOfPreviousYear.set(Calendar.MONTH, Calendar.JANUARY);
			dayOfPreviousYear.set(Calendar.DAY_OF_MONTH,
				daysToGoForward	- daysInYear + anyDay.get(Calendar.DAY_OF_YEAR));
			dayOfPreviousYear.set(Calendar.HOUR_OF_DAY, 0);
			dayOfPreviousYear.set(Calendar.MINUTE, 0);
			dayOfPreviousYear.set(Calendar.SECOND, 0);
			return dayOfPreviousYear;
		} else {
			// Monday is parte of this year
			return makeDay(anyDay, daysToGoForward);
		}
	}

	private Calendar makeFirstDayOfYear(Calendar someDayOfSameYear) {
		Calendar result = Calendar.getInstance();
		
		result.set(Calendar.YEAR, someDayOfSameYear.get(Calendar.YEAR));
		result.set(Calendar.MONTH, Calendar.JANUARY);
		result.set(Calendar.DAY_OF_MONTH, 1);
		result.set(Calendar.HOUR_OF_DAY, 0);
		result.set(Calendar.MINUTE, 0);
		result.set(Calendar.SECOND, 0);
		
		return result;
	}

	private Calendar makeLastDayOfYear(Calendar someDayOfSameYear) {
		Calendar result = Calendar.getInstance();
		
		result.set(Calendar.YEAR, someDayOfSameYear.get(Calendar.YEAR));
		result.set(Calendar.MONTH, Calendar.DECEMBER);
		result.set(Calendar.DAY_OF_MONTH, 31);
		result.set(Calendar.HOUR_OF_DAY, 0);
		result.set(Calendar.MINUTE, 0);
		result.set(Calendar.SECOND, 0);
		
		return result;
	}

	private Calendar makeDay(Calendar dayToCopy, int offset) {
		Calendar result = Calendar.getInstance();
		
		result.set(Calendar.YEAR, dayToCopy.get(Calendar.YEAR));
		result.set(Calendar.DAY_OF_YEAR, dayToCopy.get(Calendar.DAY_OF_YEAR) + offset);
		result.set(Calendar.HOUR_OF_DAY, 0);
		result.set(Calendar.MINUTE, 0);
		result.set(Calendar.SECOND, 0);
		
		return result;
	}
	
	private int getDaysInYear(int year) {
		int daysInYear = 365;

		// In the Gregorian calendar, the following rules decides which years are leap years:
		//    1. Every year divisible by 4 is a leap year.
		//    2. But every year divisible by 100 is NOT a leap year
		//    3. Unless the year is also divisible by 400, then it is still a leap year. 
		if ((year % 400) ==  0) {
			daysInYear = 366;
		} else if ((year % 4) == 0 && (year % 100) != 0) {
			daysInYear = 366;
		}

		return daysInYear;	
	}
	
}