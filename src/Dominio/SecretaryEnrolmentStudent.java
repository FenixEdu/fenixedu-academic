/*
 * Created on Feb 3, 2005
 *
 */
package Dominio;

import Util.SecretaryEnrolmentStudentReason;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 *
 */
public class SecretaryEnrolmentStudent extends DomainObject implements ISecretaryEnrolmentStudent {
    
    private IStudent student;
    
    private SecretaryEnrolmentStudentReason reasonType;
    
    private String reasonDescription;
    
    private Integer keyStudent;
    

    /**
     * @return Returns the reasonDescription.
     */
    public String getReasonDescription() {
        return reasonDescription;
    }
    /**
     * @param reasonDescription The reasonDescription to set.
     */
    public void setReasonDescription(String reasonDescription) {
        this.reasonDescription = reasonDescription;
    }
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
