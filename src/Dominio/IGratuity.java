/*
 * IGratuity
 *
 * Created on 28 of December 2002, 10:02
 */

/**
 *
 * @author Nuno Nunes & Joana Mota
 */

package Dominio;

import java.util.Date;

import Util.GratuityState;
import Util.State;

public interface IGratuity extends IDomainObject {

	public void setState(State state);
	public void setGratuityState(GratuityState gratuityState);
	public void setDate(Date date);
	public void setRemarks(String remarks);
	public void setStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan);
	
	
	public State getState();
	public GratuityState getGratuityState();
	public Date getDate();
	public String getRemarks();
	public IStudentCurricularPlan getStudentCurricularPlan();
	
}
