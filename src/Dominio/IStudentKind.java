/*
 * Created on 6/Mai/2003
 *
 */
package Dominio;

import Util.StudentType;

/**
 * @author dcs-rjao
 *
 */
public interface IStudentKind extends IDomainObject{
	public abstract StudentType getStudentType();
	public abstract void setStudentType(StudentType studentType);
	public abstract Integer getMaxCoursesToEnrol();
	public abstract Integer getMaxNACToEnrol();
	public abstract Integer getMinCoursesToEnrol();
	public abstract void setMaxCoursesToEnrol(Integer maxCoursesToEnrol);
	public abstract void setMaxNACToEnrol(Integer maxNACToEnrol);
	public abstract void setMinCoursesToEnrol(Integer minCoursesToEnrol);
}