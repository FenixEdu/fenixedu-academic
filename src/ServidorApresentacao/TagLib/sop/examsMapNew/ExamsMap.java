/*
 * Created on Apr 3, 2003
 *
 */
package ServidorApresentacao.TagLib.sop.examsMapNew;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoExam;
import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoomExamsMap;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
public class ExamsMap {

	private List days;
	private List curricularYears;
	private List executionCourses;
	private InfoExecutionDegree infoExecutionDegree;
	private Calendar firstDayOfSeason;
	private Calendar lastDayOfSeason;
	private InfoExecutionPeriod infoExecutionPeriod;

	/**
		 * @param infoRoomExamsMap
		 */
	public ExamsMap(InfoRoomExamsMap infoRoomExamsMap) {
		Calendar firstDayOfSeason = infoRoomExamsMap.getStartSeason1();
		Calendar lastDayOfSeason = infoRoomExamsMap.getEndSeason2();
		this.firstDayOfSeason = infoRoomExamsMap.getStartSeason1();
		this.lastDayOfSeason = infoRoomExamsMap.getEndSeason2();

		days = new ArrayList();
		if (firstDayOfSeason.get(Calendar.YEAR)
			!= lastDayOfSeason.get(Calendar.YEAR)) {
			for (int day = firstDayOfSeason.get(Calendar.DAY_OF_YEAR);
				day
					< makeLastDayOfYear(firstDayOfSeason).get(
						Calendar.DAY_OF_YEAR);
				day++) {
				Calendar tempDayToAdd =
					makeDay(
						firstDayOfSeason,
						day - firstDayOfSeason.get(Calendar.DAY_OF_YEAR));
				if (tempDayToAdd.get(Calendar.DAY_OF_WEEK)
					!= Calendar.SUNDAY) {
					days.add(
						new ExamsMapSlot(
							tempDayToAdd,
							findExamsFromListOfExams(
								tempDayToAdd,
								infoRoomExamsMap.getExams())));
				}
			}
			firstDayOfSeason = makeFirstDayOfYear(lastDayOfSeason);
		}
		for (int day = firstDayOfSeason.get(Calendar.DAY_OF_YEAR);
			day < lastDayOfSeason.get(Calendar.DAY_OF_YEAR) + 1;
			day++) {
			Calendar tempDayToAdd =
				makeDay(
					firstDayOfSeason,
					day - firstDayOfSeason.get(Calendar.DAY_OF_YEAR));
			if (tempDayToAdd.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				days.add(
					new ExamsMapSlot(
						tempDayToAdd,
						findExamsFromListOfExams(
							tempDayToAdd,
							infoRoomExamsMap.getExams())));
			}
		}
	}

	public ExamsMap(InfoExamsMap infoExamsMap) {
		this.firstDayOfSeason = infoExamsMap.getStartSeason1();
		this.lastDayOfSeason = infoExamsMap.getEndSeason2();

		setInfoExecutionPeriod(infoExamsMap.getInfoExecutionPeriod());
		setInfoExecutionDegree(infoExamsMap.getInfoExecutionDegree());
		
		Calendar firstDayOfSeason = infoExamsMap.getStartSeason1();
		Calendar lastDayOfSeason = infoExamsMap.getEndSeason2();

		curricularYears = infoExamsMap.getCurricularYears();
		executionCourses = infoExamsMap.getExecutionCourses();

		days = new ArrayList();
		if (firstDayOfSeason.get(Calendar.YEAR)
			!= lastDayOfSeason.get(Calendar.YEAR)) {
			for (int day = firstDayOfSeason.get(Calendar.DAY_OF_YEAR);
				day
					< makeLastDayOfYear(firstDayOfSeason).get(
						Calendar.DAY_OF_YEAR);
				day++) {
				Calendar tempDayToAdd =
					makeDay(
						firstDayOfSeason,
						day - firstDayOfSeason.get(Calendar.DAY_OF_YEAR));
				if (tempDayToAdd.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
					days.add(
						new ExamsMapSlot(
							tempDayToAdd,
							findExams(
								tempDayToAdd,
								infoExamsMap.getExecutionCourses())));
			}

			firstDayOfSeason = makeFirstDayOfYear(lastDayOfSeason);
		}

		for (int day = firstDayOfSeason.get(Calendar.DAY_OF_YEAR);
			day < lastDayOfSeason.get(Calendar.DAY_OF_YEAR) + 1;
			day++) {
			Calendar tempDayToAdd =
				makeDay(
					firstDayOfSeason,
					day - firstDayOfSeason.get(Calendar.DAY_OF_YEAR));
			if (tempDayToAdd.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
				days.add(
					new ExamsMapSlot(
						tempDayToAdd,
						findExams(
							tempDayToAdd,
							infoExamsMap.getExecutionCourses())));
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

				if (sameDayAsExam(day, infoExam)) {
					infoExam.setInfoExecutionCourse(infoExecutionCourse);
					result.add(infoExam);
				}
			}
		}

		return result;
	}

	private List findExamsFromListOfExams(Calendar day, List infoExams) {
		List result = new ArrayList();

		for (int j = 0; j < infoExams.size(); j++) {
			InfoExam infoExam = (InfoExam) infoExams.get(j);

			if (sameDayAsExam(day, infoExam)) {
				result.add(infoExam);
			}
		}

		return result;
	}

	private boolean sameDayAsExam(Calendar day, InfoExam infoExam) {
		return day.get(Calendar.YEAR) == infoExam.getDay().get(Calendar.YEAR)
			&& day.get(Calendar.MONTH) == infoExam.getDay().get(Calendar.MONTH)
			&& day.get(Calendar.DAY_OF_MONTH)
				== infoExam.getDay().get(Calendar.DAY_OF_MONTH);
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
			dayOfPreviousYear.set(
				Calendar.DAY_OF_MONTH,
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
		if ((year % 400) == 0) {
			daysInYear = 366;
		} else if ((year % 4) == 0 && (year % 100) != 0) {
			daysInYear = 366;
		}

		if (anyDay.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			return anyDay;
		if (anyDay.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			return anyDay;

		int daysToGoForward =
			Calendar.SATURDAY - anyDay.get(Calendar.DAY_OF_WEEK);
		if (anyDay.get(Calendar.DAY_OF_YEAR) > daysInYear - 6) {
			// Monday is parte of Previous year
			Calendar dayOfPreviousYear = Calendar.getInstance();
			dayOfPreviousYear.set(Calendar.YEAR, anyDay.get(Calendar.YEAR) + 1);
			dayOfPreviousYear.set(Calendar.MONTH, Calendar.JANUARY);
			dayOfPreviousYear.set(
				Calendar.DAY_OF_MONTH,
				daysToGoForward
					- daysInYear
					+ anyDay.get(Calendar.DAY_OF_YEAR));
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
		result.set(
			Calendar.DAY_OF_YEAR,
			dayToCopy.get(Calendar.DAY_OF_YEAR) + offset);
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
		if ((year % 400) == 0) {
			daysInYear = 366;
		} else if ((year % 4) == 0 && (year % 100) != 0) {
			daysInYear = 366;
		}

		return daysInYear;
	}

	/**
	 * @return
	 */
	public List getDays() {
		return days;
	}

	/**
	 * @return
	 */
	public List getCurricularYears() {
		return curricularYears;
	}

	/**
	 * @param list
	 */
	private void setCurricularYears(List list) {
		curricularYears = list;
	}
	
	/**
	 * @return
	 */
	public List getExecutionCourses() {
		return executionCourses;
	}

	/**
	 * @param list
	 */
	private void setExecutionCourses(List list) {
		executionCourses = list;
	}
	
	/**
	 * @return Returns the infoExecutionDegree.
	 */
	public InfoExecutionDegree getInfoExecutionDegree() {
		return infoExecutionDegree;
	}

	/**
	 * @param infoExecutionDegree The infoExecutionDegree to set.
	 */
	public void setInfoExecutionDegree(InfoExecutionDegree infoExecutionDegree) {
		this.infoExecutionDegree = infoExecutionDegree;
	}

	/**
	 * @return Returns the firstDayOfSeason.
	 */
	public Calendar getFirstDayOfSeason() {
		return firstDayOfSeason;
	}

	/**
	 * @param firstDayOfSeason The firstDayOfSeason to set.
	 */
	public void setFirstDayOfSeason(Calendar firstDayOfSeason) {
		this.firstDayOfSeason = firstDayOfSeason;
	}

	/**
	 * @return Returns the lastDayOfSeason.
	 */
	public Calendar getLastDayOfSeason() {
		return lastDayOfSeason;
	}

	/**
	 * @param lastDayOfSeason The lastDayOfSeason to set.
	 */
	public void setLastDayOfSeason(Calendar lastDayOfSeason) {
		this.lastDayOfSeason = lastDayOfSeason;
	}

    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return infoExecutionPeriod;
	}
	    
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

}