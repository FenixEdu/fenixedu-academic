package Dominio;

import java.util.Date;

import Util.DegreeState;

/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public interface IDegreeCurricularPlan {

	public String getName();
	public ICurso getDegree();
	public DegreeState getState();
	public Date getEndDate();
	public Date getInitialDate();

	public void setName(String name);
	public void setDegree(ICurso curso);
	public void setState(DegreeState state);
	public void setEndDate(Date endDate);
	public void setInitialDate(Date initialDate);
}