/*
 * Created on Jan 21, 2004
 */
package Dominio.grant.contract;

import Dominio.IDomainObject;
import Dominio.ITeacher;

/**
 * @author pica
 * @author barbosa
 */
public interface IGrantProject extends IDomainObject
{
	public String getDesignation();
	public ITeacher getResponsibleTeacher();
	public Integer getNumber();
	
	public void setDesignation(String designation);
	public void setResponsibleTeacher(ITeacher responsibleTeacher);
	public void setNumber(Integer number);
}