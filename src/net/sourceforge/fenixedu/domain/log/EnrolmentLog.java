/*
 * Created on Nov 4, 2004
 */
package net.sourceforge.fenixedu.domain.log;

import java.util.Date;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.util.EnrolmentAction;

/**
 * @author nmgo
 * @author lmre
 */
public class EnrolmentLog extends EnrolmentLog_Base {

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
    public EnrolmentLog(Date date, EnrolmentAction action, IStudent student, Integer keyStudent,
            ICurricularCourse curricularCourse, Integer keyCurricularCourse) {
        this.setDate(date);
        this.setAction(action);
        this.setStudent(student);
        this.setKeyStudent(keyStudent);
        this.setCurricularCourse(curricularCourse);
        this.setKeyCurricularCourse(keyCurricularCourse);
    }

}
