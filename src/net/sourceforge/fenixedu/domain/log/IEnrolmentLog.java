/*
 * Created on Nov 4, 2004
 */
package net.sourceforge.fenixedu.domain.log;

import java.util.Date;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.util.EnrolmentAction;

/**
 * @author nmgo
 * @author lmre
 */
public interface IEnrolmentLog extends IDomainObject{
    /**
     * @return Returns the action.
     */
    public abstract EnrolmentAction getAction();

    /**
     * @param action The action to set.
     */
    public abstract void setAction(EnrolmentAction action);

    /**
     * @return Returns the curricularCourse.
     */
    public abstract ICurricularCourse getCurricularCourse();

    /**
     * @param curricularCourse The curricularCourse to set.
     */
    public abstract void setCurricularCourse(ICurricularCourse curricularCourse);

    /**
     * @return Returns the date.
     */
    public abstract Date getDate();

    /**
     * @param date The date to set.
     */
    public abstract void setDate(Date date);

    /**
     * @return Returns the student.
     */
    public abstract IStudent getStudent();

    /**
     * @param student The student to set.
     */
    public abstract void setStudent(IStudent student);
}