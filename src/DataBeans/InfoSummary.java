/*
 * Created on 21/Jul/2003
 *
 * 
 */
package DataBeans;

import java.util.Calendar;
import java.util.Date;

import DataBeans.comparators.CalendarDateComparator;
import DataBeans.comparators.CalendarHourComparator;
import Util.TipoAula;

/**
 * @author João Mota
 * @author Susana Fernandes
 *
 * 21/Jul/2003
 * fenix-head
 * DataBeans
 * 
 */
public class InfoSummary extends InfoObject implements Comparable {
	private String title;
	private Calendar summaryDate;
	private Calendar summaryHour;
	private Date lastModifiedDate;
	private String summaryText;
	private TipoAula summaryType;
	private InfoExecutionCourse infoExecutionCourse;

	/**
	 * @return
	 */
	public InfoExecutionCourse getInfoExecutionCourse() {
		return infoExecutionCourse;
	}

	/**
	 * @param infoExecutionCourse
	 */
	public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
		this.infoExecutionCourse = infoExecutionCourse;
	}

	/**
	 * @return
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * @param lastModifiedDate
	 */
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * @return
	 */
	public Calendar getSummaryDate() {
		return summaryDate;
	}

	/**
	 * @param summaryDate
	 */
	public void setSummaryDate(Calendar summaryDate) {
		this.summaryDate = summaryDate;
	}

	/**
	 * @return
	 */
	public Calendar getSummaryHour() {
		return summaryHour;
	}

	/**
	 * @param summaryHour
	 */
	public void setSummaryHour(Calendar summaryHour) {
		this.summaryHour = summaryHour;
	}

	/**
	 * @return
	 */
	public String getSummaryText() {
		return summaryText;
	}

	/**
	 * @param summaryText
	 */
	public void setSummaryText(String summaryText) {
		this.summaryText = summaryText;
	}

	/**
	 * @return
	 */
	public TipoAula getSummaryType() {
		return summaryType;
	}

	/**
	 * @param summaryType
	 */
	public void setSummaryType(TipoAula summaryType) {
		this.summaryType = summaryType;
	}

	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummaryDateFormatted() {
		String result = "";
		Calendar date = getSummaryDate();
		result += date.get(Calendar.DAY_OF_MONTH);
		result += "/";
		result += date.get(Calendar.MONTH) + 1;
		result += "/";
		result += date.get(Calendar.YEAR);
		return result;
	}

	public String getSummaryHourFormatted() {
		String result = "";
		Calendar date = getSummaryHour();
		result += date.get(Calendar.HOUR_OF_DAY);
		result += ":";
		if (date.get(Calendar.MINUTE) < 10) {
			result += "0";
		}
		result += date.get(Calendar.MINUTE);

		return result;
	}

	public String getSummaryTypeFormatted() {
		String result = "";
		int value = getSummaryType().getTipo().intValue();
		switch (value) {
			case 1 :
				return "Teórica";
			case 2 :
				return "Prática";
			case 3 :
				return "Teórico-Prática";
			case 4 :
				return "Laboratorial";
		}
		return result;
	}

	public String getLastModifiedDateFormatted() {
		String result = "";
		Date date = getLastModifiedDate();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		result += calendar.get(Calendar.DAY_OF_MONTH);
		result += "/";
		result += calendar.get(Calendar.MONTH) + 1;
		result += "/";
		result += calendar.get(Calendar.YEAR);
		result += " ";
		result += calendar.get(Calendar.HOUR_OF_DAY);
		result += ":";
		if (calendar.get(Calendar.MINUTE)<10){
			result+="0";
		}
		result += calendar.get(Calendar.MINUTE);

		return result;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoSummary) {
			InfoSummary infoSummary = (InfoSummary) obj;
			result = getIdInternal().equals(infoSummary.getIdInternal());
			result =
				result
					|| (getInfoExecutionCourse()
						.equals(infoSummary.getInfoExecutionCourse())
						&& getSummaryDate().get(Calendar.DAY_OF_MONTH)
							== infoSummary.getSummaryDate().get(
								Calendar.DAY_OF_MONTH)
						&& getSummaryDate().get(Calendar.MONTH)
							== infoSummary.getSummaryDate().get(Calendar.MONTH)
						&& getSummaryDate().get(Calendar.YEAR)
							== infoSummary.getSummaryDate().get(Calendar.YEAR)
						&& getSummaryHour().get(Calendar.HOUR_OF_DAY)
							== infoSummary.getSummaryHour().get(
								Calendar.HOUR_OF_DAY)
						&& getSummaryHour().get(Calendar.MINUTE)
							== infoSummary.getSummaryHour().get(Calendar.MINUTE));
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object arg0) {
		InfoSummary infoSummary = (InfoSummary) arg0;
		CalendarDateComparator dateComparator = new CalendarDateComparator();
		CalendarHourComparator hourComparator = new CalendarHourComparator();
		if (getSummaryType().compareTo(infoSummary.getSummaryType()) == 0) {
			if (dateComparator
				.compare(getSummaryDate(), infoSummary.getSummaryDate())
				== 0) {
				return hourComparator.compare(
					getSummaryHour(),
					infoSummary.getSummaryHour());
			} else {
				return dateComparator.compare(
					getSummaryDate(),
					infoSummary.getSummaryDate());
			}
		} else {
			return getSummaryType().compareTo(infoSummary.getSummaryType());
		}

	}

}
