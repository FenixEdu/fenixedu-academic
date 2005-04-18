/*
 * Created on Feb 3, 2005
 *
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.SecretaryEnrolmentStudentReason;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 *
 */
public class SecretaryEnrolmentStudent extends SecretaryEnrolmentStudent_Base {
    
    private IStudent student;
    
    private SecretaryEnrolmentStudentReason reasonType;
    
    /**
     * @return Returns the reasonType.
     */
    public SecretaryEnrolmentStudentReason getReasonType() {
        return reasonType;
    }
    /**
     * @param reasonType The reasonType to set.
     */
    public void setReasonType(SecretaryEnrolmentStudentReason reasonType) {
        this.reasonType = reasonType;
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
