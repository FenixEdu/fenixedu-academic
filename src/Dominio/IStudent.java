/*
 * IStudent.java
 *
 * Created on 28 of December 2002, 17:05
 */

package Dominio;

import Util.StudentState;
import Util.TipoCurso;

/**
 *
 * @author Ricardo Nortadas
 */

public interface IStudent {
	public Integer getNumber();
	public StudentState getState();
	public IPessoa getPerson();
	public IStudentKind getStudentKind();
	public TipoCurso getDegreeType();
	
	public void setNumber(Integer number);
	public void setState(StudentState state);
	public void setPerson(IPessoa person);
	public void setDegreeType(TipoCurso degreeType);	
	public void setStudentKind(IStudentKind studentKind);
}
