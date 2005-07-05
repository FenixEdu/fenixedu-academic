package net.sourceforge.fenixedu.domain.classProperties;



/**
 * @author David Santos in Apr 7, 2004
 */

public class ExecutionCourseProperty extends ExecutionCourseProperty_Base {

    public boolean equals(Object obj) {
        if (obj instanceof IExecutionCourseProperty) {
            final IExecutionCourseProperty executionCourseProperty = (IExecutionCourseProperty) obj;
            return this.getIdInternal().equals(executionCourseProperty.getIdInternal());
        }
        return false;
    }

    public String toString() {
        return "name:[" + super.getName() + "]value:[" + super.getValue() + "]executionCourse:["
                + this.getExecutionCourse().toString() + "]";
    }

}