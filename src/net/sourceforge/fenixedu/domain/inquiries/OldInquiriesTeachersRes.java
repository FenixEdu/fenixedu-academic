/*
 * Created on Nov 17, 2004
 *
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class OldInquiriesTeachersRes extends OldInquiriesTeachersRes_Base {
    private IDegree degree;
    private IExecutionPeriod executionPeriod;
	private ITeacher teacher;

    /**
     * @return Returns the degree.
     */
    public IDegree getDegree() {
        return degree;
    }
    /**
     * @param degree The degree to set.
     */
    public void setDegree(IDegree degree) {
        this.degree = degree;
    }

    /**
     * @return Returns the executionPeriod.
     */
    public IExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }

    /**
     * @param executionPeriod The executionPeriod to set.
     */
    public void setExecutionPeriod(IExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

    /**
     * @return Returns the teacher.
     */
    public ITeacher getTeacher() {
        return teacher;
    }

    /**
     * @param teacher The teacher to set.
     */
    public void setTeacher(ITeacher teacher) {
        this.teacher = teacher;
    }

}
