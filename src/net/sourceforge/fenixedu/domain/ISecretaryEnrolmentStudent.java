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
public interface ISecretaryEnrolmentStudent {
    /**
     * @return Returns the reasonDescription.
     */
    public abstract String getReasonDescription();

    /**
     * @param reasonDescription The reasonDescription to set.
     */
    public abstract void setReasonDescription(String reasonDescription);

    /**
     * @return Returns the reasonType.
     */
    public abstract SecretaryEnrolmentStudentReason getReasonType();

    /**
     * @param reasonType The reasonType to set.
     */
    public abstract void setReasonType(SecretaryEnrolmentStudentReason reasonType);

    /**
     * @return Returns the student.
     */
    public abstract IStudent getStudent();

    /**
     * @param student The student to set.
     */
    public abstract void setStudent(IStudent student);
    
    /**
     * @return Returns the keyStudent.
     */
    public abstract Integer getKeyStudent();
    /**
     * @param keyStudent The keyStudent to set.
     */
    public abstract void setKeyStudent(Integer keyStudent);
}