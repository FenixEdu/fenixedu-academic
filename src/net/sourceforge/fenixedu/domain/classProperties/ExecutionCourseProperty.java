package net.sourceforge.fenixedu.domain.classProperties;

import net.sourceforge.fenixedu.domain.IExecutionCourse;

/**
 * @author David Santos in Apr 7, 2004
 */

public class ExecutionCourseProperty extends GeneralClassProperty implements IExecutionCourseProperty {
    private Integer keyExecutionCourse;

    private IExecutionCourse executionCourse;

    public ExecutionCourseProperty() {
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IExecutionCourseProperty) {
            IExecutionCourseProperty o = (IExecutionCourseProperty) obj;
            resultado = (this.getName().equals(o.getName()) && this.getExecutionCourse().equals(
                    o.getExecutionCourse()));
        }
        return resultado;
    }

    public String toString() {
        return "name:[" + super.getName() + "]value:[" + super.getValue() + "]executionCourse:["
                + this.getExecutionCourse().toString() + "]";
    }

    /**
     * @return Returns the executionCourse.
     */
    public IExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    /**
     * @param executionCourse
     *            The executionCourse to set.
     */
    public void setExecutionCourse(IExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    /**
     * @return Returns the keyExecutionCourse.
     */
    public Integer getKeyExecutionCourse() {
        return keyExecutionCourse;
    }

    /**
     * @param keyExecutionCourse
     *            The keyExecutionCourse to set.
     */
    public void setKeyExecutionCourse(Integer keyExecutionCourse) {
        this.keyExecutionCourse = keyExecutionCourse;
    }
}