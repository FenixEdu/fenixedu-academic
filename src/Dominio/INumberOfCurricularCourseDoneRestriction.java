/*
 * Created on 2/Abr/2003 by jpvl
 *
 */
package Dominio;

/**
 * @author jpvl
 */
public interface INumberOfCurricularCourseDoneRestriction extends IRestriction {
	/**
	 * @return
	 */
	public abstract Integer getNumberOfCurricularCourseDone();
	/**
	 * @param integer
	 */
	public abstract void setNumberOfCurricularCourseDone(Integer numberOfCurricularCourseDone);
}