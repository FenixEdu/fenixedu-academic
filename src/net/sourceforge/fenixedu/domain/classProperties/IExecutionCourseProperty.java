package net.sourceforge.fenixedu.domain.classProperties;

import net.sourceforge.fenixedu.domain.IExecutionCourse;

/**
 * @author David Santos in Apr 7, 2004
 */

public interface IExecutionCourseProperty extends IGeneralClassProperty {
    public IExecutionCourse getExecutionCourse();

    public void setExecutionCourse(IExecutionCourse executionCourse);
}