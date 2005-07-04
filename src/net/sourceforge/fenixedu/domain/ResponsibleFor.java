package net.sourceforge.fenixedu.domain;

/**
 * @author João Mota
 * 
 *  
 */
public class ResponsibleFor extends ResponsibleFor_Base {

    public ResponsibleFor() {
    }

    public ResponsibleFor(ITeacher teacher, IExecutionCourse executionCourse) {
        setTeacher(teacher);
        setExecutionCourse(executionCourse);
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IResponsibleFor) {
            IResponsibleFor responsibleFor = (IResponsibleFor) obj;
            result = getIdInternal().equals(responsibleFor.getIdInternal());
        }
        return result;
    }
}
