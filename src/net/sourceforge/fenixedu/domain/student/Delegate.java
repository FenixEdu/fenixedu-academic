/*
 * Created on Feb 18, 2004
 *  
 */
package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.util.DelegateYearType;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class Delegate extends Delegate_Base {
    private IDegree degree;

    private IExecutionYear executionYear;

    private IStudent student;

    private DelegateYearType yearType;

    /**
     * @return Returns the degree.
     */
    public IDegree getDegree() {
        return degree;
    }

    /**
     * @param degree
     *            The degree to set.
     */
    public void setDegree(IDegree degree) {
        this.degree = degree;
    }

    /**
     * @return Returns the executionYear.
     */
    public IExecutionYear getExecutionYear() {
        return executionYear;
    }

    /**
     * @param executionYear
     *            The executionYear to set.
     */
    public void setExecutionYear(IExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    /**
     * @return Returns the student.
     */
    public IStudent getStudent() {
        return student;
    }

    /**
     * @param student
     *            The student to set.
     */
    public void setStudent(IStudent student) {
        this.student = student;
    }

    /**
     * @return Returns the type.
     */
    public DelegateYearType getYearType() {
        return yearType;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setYearType(DelegateYearType yearType) {
        this.yearType = yearType;
    }

}