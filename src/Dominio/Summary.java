/*
 * Created on 21/Jul/2003
 *
 * 
 */
package Dominio;

import java.util.Calendar;
import java.util.Date;

import Util.TipoAula;

/**
 * @author João Mota
 * @author Susana Fernandes
 *
 * 21/Jul/2003
 * fenix-head
 * Dominio
 * 
 */
public class Summary extends DomainObject implements ISummary {
	private String title;
	private Calendar summaryDate;
	private Calendar summaryHour;
	private Date lastModifiedDate;
	private String summaryText;
	private TipoAula summaryType;
	private IExecutionCourse executionCourse;
	private Integer keyExecutionCourse;

	/**
	 * 
	 */
	public Summary() {}
	/**
	 * 
	 */
	public Summary(Integer summaryId) {
		setIdInternal(summaryId);

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
	public IExecutionCourse getExecutionCourse() {
		return executionCourse;
	}

	/**
	 * @param executionCourse
	 */
	public void setExecutionCourse(IExecutionCourse executionCourse) {
		this.executionCourse = executionCourse;
	}

	/**
	 * @return
	 */
	public Integer getKeyExecutionCourse() {
		return keyExecutionCourse;
	}

	/**
	 * @param keyExecutionCourse
	 */
	public void setKeyExecutionCourse(Integer keyExecutionCourse) {
		this.keyExecutionCourse = keyExecutionCourse;
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
	 * @param obj
	 */
	public boolean compareTo(Object obj) {
		boolean resultado = false;
		if (obj instanceof ISummary) {
			ISummary summary = (ISummary) obj;

			resultado =
				(summary != null)
					&& this.getExecutionCourse().equals(
						summary.getExecutionCourse())
					&& this.getSummaryDate().equals(summary.getSummaryDate())
					&& this.getSummaryHour().equals(summary.getSummaryHour())
					&& this.getSummaryText().equals(summary.getSummaryText())
					&& this.getTitle().equals(summary.getTitle());
		}
		return resultado;
	}
}
