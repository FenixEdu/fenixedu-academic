/*
 * Created on 13/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IWrittenEvaluationEnrolment;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoWrittenEvaluationEnrolmentWithInfoStudentAndInfoRoom extends InfoWrittenEvaluationEnrolment {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(IWrittenEvaluationEnrolment writtenEvaluationEnrolment) {
        super.copyFromDomain(writtenEvaluationEnrolment);
        if (writtenEvaluationEnrolment != null) {
            setInfoStudent(InfoStudentWithInfoPerson.newInfoFromDomain(writtenEvaluationEnrolment.getStudent()));
            setInfoRoom(InfoRoom.newInfoFromDomain(writtenEvaluationEnrolment.getRoom()));
        }
    }

    public static InfoWrittenEvaluationEnrolment newInfoFromDomain(IWrittenEvaluationEnrolment writtenEvaluationEnrolment) {
        InfoWrittenEvaluationEnrolmentWithInfoStudentAndInfoRoom infoWrittenEvaluationEnrolment = null;
        if (writtenEvaluationEnrolment != null) {
            infoWrittenEvaluationEnrolment = new InfoWrittenEvaluationEnrolmentWithInfoStudentAndInfoRoom();
            infoWrittenEvaluationEnrolment.copyFromDomain(writtenEvaluationEnrolment);
        }
        return infoWrittenEvaluationEnrolment;
    }
}