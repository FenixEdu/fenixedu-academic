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
 *
 * 21/Jul/2003
 * fenix-head
 * Dominio
 * 
 */
public interface ISummary extends IDomainObject {
	/**
	 * @return
	 */
	public abstract IExecutionCourse getExecutionCourse();
	/**
	 * @param executionCourse
	 */
	public abstract void setExecutionCourse(IExecutionCourse executionCourse);
	/**
	 * @return
	 */
	public abstract Integer getKeyExecutionCourse();
	/**
	 * @param keyExecutionCourse
	 */
	public abstract void setKeyExecutionCourse(Integer keyExecutionCourse);
	/**
	 * @return
	 */
	public abstract Date getLastModifiedDate();
	/**
	 * @param lastModifiedDate
	 */
	public abstract void setLastModifiedDate(Date lastModifiedDate);
	/**
	 * @return
	 */
	public abstract TipoAula getSummaryType();
	/**
	 * @param summaryType
	 */
	public abstract void setSummaryType(TipoAula summaryType);
	/**
	 * @return
	 */
	public abstract String getTitle();
	/**
	 * @param title
	 */
	public abstract void setTitle(String title);
	/**
	 * @return
	 */
	public abstract Calendar getSummaryDate();
	/**
	 * @param summaryDate
	 */
	public abstract void setSummaryDate(Calendar summaryDate);
	/**
	 * @return
	 */
	public abstract String getSummaryText();
	/**
	 * @param summaryText
	 */
	public abstract void setSummaryText(String summaryText);

	public Calendar getSummaryHour();

	/**
	 * @param summaryHour
	 */
	public void setSummaryHour(Calendar summaryHour);
	
	/**
	 *  @param summary
	 */
	public boolean compareTo(Object obj);
}