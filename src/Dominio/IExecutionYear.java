package Dominio;

import fileSuport.INode;
import Util.PeriodState;

/**
 * Created on 11/Fev/2003
 * @author João Mota
 * ciapl 
 * Dominio
 * 
 */
public interface IExecutionYear extends IDomainObject,INode {
	public String getYear();
	public void setYear(String year);
	void setState (PeriodState state);
	PeriodState getState();

}
