/*
 * Created on 6/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

import Util.StudentType;

/**
 * @author dcs-rjao
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IStudentGroupInfo {
	public abstract StudentType getStudentType();
	public abstract void setStudentType(StudentType studentType);
	public abstract Integer getMaxCoursesToEnrol();
	public abstract Integer getMaxNACToEnrol();
	public abstract Integer getMinCoursesToEnrol();
	public abstract void setMaxCoursesToEnrol(Integer maxCoursesToEnrol);
	public abstract void setMaxNACToEnrol(Integer maxNACToEnrol);
	public abstract void setMinCoursesToEnrol(Integer minCoursesToEnrol);
}