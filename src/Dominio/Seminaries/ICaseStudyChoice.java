/*
 * Created on 29/Jul/2003, 14:19:43
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package Dominio.Seminaries;

import Dominio.IDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 29/Jul/2003, 14:19:43
 * 
 */
public interface ICaseStudyChoice extends IDomainObject
{
	public abstract Integer getOrder();

    /**
	 * @param integer
	 */
	public abstract void setOrder(Integer integer);
	/**
	 * @return
	 */
	public abstract Integer getCandidacyIdInternal();
	/**
	 * @return
	 */
	public abstract Integer getCaseStudyIdInternal();
	/**
	 * @param integer
	 */
	public abstract void setCandidacyIdInternal(Integer integer);
	/**
	 * @param integer
	 */
	public abstract void setCaseStudyIdInternal(Integer integer);
}