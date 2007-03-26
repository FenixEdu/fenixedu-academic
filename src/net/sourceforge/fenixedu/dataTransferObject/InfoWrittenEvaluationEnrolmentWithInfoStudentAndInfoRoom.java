/*
 * Created on 13/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.space.OldRoom;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoWrittenEvaluationEnrolmentWithInfoStudentAndInfoRoom extends InfoWrittenEvaluationEnrolment {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain(Dominio.DomainObject)
     */
    public void copyFromDomain(WrittenEvaluationEnrolment writtenEvaluationEnrolment) {
        super.copyFromDomain(writtenEvaluationEnrolment);
        if (writtenEvaluationEnrolment != null) {
            setInfoStudent(InfoStudent.newInfoFromDomain(writtenEvaluationEnrolment.getStudent()));
            setInfoRoom(InfoRoom.newInfoFromDomain((OldRoom) writtenEvaluationEnrolment.getRoom()));
        }
    }

    public static InfoWrittenEvaluationEnrolment newInfoFromDomain(WrittenEvaluationEnrolment writtenEvaluationEnrolment) {
        InfoWrittenEvaluationEnrolmentWithInfoStudentAndInfoRoom infoWrittenEvaluationEnrolment = null;
        if (writtenEvaluationEnrolment != null) {
            infoWrittenEvaluationEnrolment = new InfoWrittenEvaluationEnrolmentWithInfoStudentAndInfoRoom();
            infoWrittenEvaluationEnrolment.copyFromDomain(writtenEvaluationEnrolment);
        }
        return infoWrittenEvaluationEnrolment;
    }
}