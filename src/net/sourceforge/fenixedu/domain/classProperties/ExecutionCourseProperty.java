package net.sourceforge.fenixedu.domain.classProperties;


/**
 * @author David Santos in Apr 7, 2004
 */

public class ExecutionCourseProperty extends ExecutionCourseProperty_Base {

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

}