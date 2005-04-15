package net.sourceforge.fenixedu.domain;

/**
 * @author João Mota
 * 
 *  
 */
public class ResponsibleFor extends ResponsibleFor_Base implements IResponsibleFor {
    protected ITeacher teacher;

    protected IExecutionCourse executionCourse;

    /**
     *  
     */
    public ResponsibleFor() {
    }

    public ResponsibleFor(ITeacher teacher, IExecutionCourse executionCourse) {
        setTeacher(teacher);
        setExecutionCourse(executionCourse);
    }

    /**
     * @return IDisciplinaExecucao
     */
    public IExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    /**
     * @return ITeacher
     */
    public ITeacher getTeacher() {
        return teacher;
    }

    /**
     * Sets the executionCourse.
     * 
     * @param executionCourse
     *            The executionCourse to set
     */
    public void setExecutionCourse(IExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    /**
     * Sets the teacher.
     * 
     * @param teacher
     *            The teacher to set
     */
    public void setTeacher(ITeacher teacher) {
        this.teacher = teacher;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IResponsibleFor) {
            IResponsibleFor responsibleFor = (IResponsibleFor) obj;
            result = getTeacher().equals(responsibleFor.getTeacher());
            result &= getExecutionCourse().equals(responsibleFor.getExecutionCourse());
        }
        return result;
    }
}