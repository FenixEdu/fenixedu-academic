package Dominio.classProperties;

import Dominio.IExecutionCourse;

/**
 * @author David Santos in Apr 7, 2004
 */

public interface IExecutionCourseProperty extends IGeneralClassProperty
{
	public IExecutionCourse getExecutionCourse();
	public void setExecutionCourse(IExecutionCourse executionCourse);
}