/*
 * Created on Nov 4, 2004
 */
package net.sourceforge.fenixedu.domain.log;

import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.util.EnrolmentAction;

/**
 * @author nmgo
 * @author lmre
 */
public class EnrolmentLog extends EnrolmentLog_Base {

    private EnrolmentAction action;
    
    private IStudent student;
    
    private ICurricularCourse curricularCourse;
    
    /**
     * 
     */
    public EnrolmentLog() {
    }
    
    
    /**
     * @param date
     * @param action
     * @param student
     * @param keyStudent
     * @param curricularCourse
     * @param keyCurricularCourse
     */
    public EnrolmentLog(Date date, EnrolmentAction action, IStudent student,
            Integer keyStudent, ICurricularCourse curricularCourse,
            Integer keyCurricularCourse) {
        this.setDate(date);
        this.action = action;
        this.student = student;
        this.setKeyStudent(keyStudent);
        this.curricularCourse = curricularCourse;
        this.setKeyCurricularCourse(keyCurricularCourse);
    }
    /**
     * @return Returns the action.
     */
    public EnrolmentAction getAction() {
        return action;
    }
    /**
     * @param action The action to set.
     */
    public void setAction(EnrolmentAction action) {
        this.action = action;
    }
    /**
     * @return Returns the curricularCourse.
     */
    public ICurricularCourse getCurricularCourse() {
        return curricularCourse;
    }
    /**
     * @param curricularCourse The curricularCourse to set.
     */
    public void setCurricularCourse(ICurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }
 
    /**
     * @return Returns the student.
     */
    public IStudent getStudent() {
        return student;
    }
    /**
     * @param student The student to set.
     */
    public void setStudent(IStudent student) {
        this.student = student;
    }
}
