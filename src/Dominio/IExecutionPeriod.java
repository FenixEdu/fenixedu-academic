package Dominio;

import Util.PeriodState;


/**
 * Created on 11/Fev/2003
 * @author João Mota
 * ciapl 
 * Dominio
 * 
 */
public interface IExecutionPeriod {
	public String getName();
	public IExecutionYear getExecutionYear();
	public void setExecutionYear(IExecutionYear executionYear);
	public void setName(String name);
	
	void setState(PeriodState newState);
	PeriodState getState();
}
