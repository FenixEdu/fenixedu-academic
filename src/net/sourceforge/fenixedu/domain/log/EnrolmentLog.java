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
public class EnrolmentLog extends DomainObject implements IEnrolmentLog{

    private Date date;
    
    private EnrolmentAction action;
    
    private IStudent student;
    
    private Integer keyStudent;
    
    private ICurricularCourse curricularCourse;
    
    private Integer keyCurricularCourse;
    
    
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
        this.date = date;
        this.action = action;
        this.student = student;
        this.keyStudent = keyStudent;
        this.curricularCourse = curricularCourse;
        this.keyCurricularCourse = keyCurricularCourse;
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
     * @return Returns the date.
     */
    public Date getDate() {
        return date;
    }
    /**
     * @param date The date to set.
     */
    public void setDate(Date date) {
        this.date = date;
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
    /**
     * @return Returns the keyCurricularCourse.
     */
    public Integer getKeyCurricularCourse() {
        return keyCurricularCourse;
    }
    /**
     * @param keyCurricularCourse The keyCurricularCourse to set.
     */
    public void setKeyCurricularCourse(Integer keyCurricularCourse) {
        this.keyCurricularCourse = keyCurricularCourse;
    }
    /**
     * @return Returns the keyStudent.
     */
    public Integer getKeyStudent() {
        return keyStudent;
    }
    /**
     * @param keyStudent The keyStudent to set.
     */
    public void setKeyStudent(Integer keyStudent) {
        this.keyStudent = keyStudent;
    }
}
