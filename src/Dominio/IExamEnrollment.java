/*
 * Created on 21/Mai/2003
 *
 */
package Dominio;

import java.util.Calendar;

/**
 * @author Tânia Nunes
 *
 */
public interface IExamEnrollment {
	/**
	 * @return
	 */
	public abstract Calendar getBeginDate();
	/**
	 * @return
	 */
	public abstract Calendar getEndDate();
	/**
	 * @return
	 */
	public abstract IExam getExam();
	/**
	 * @return
	 */
	public abstract Integer getKeyExam();
	/**
	 * @param calendar
	 */
	public abstract void setBeginDate(Calendar date);
	/**
	 * @param calendar
	 */
	public abstract void setEndDate(Calendar date);
	/**
	 * @param exam
	 */
	public abstract void setExam(IExam exam);
	/**
	 * @param integer
	 */
	public abstract void setKeyExam(Integer integer);
}