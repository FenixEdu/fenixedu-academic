/*
 * Created on Aug 15, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.student.schoolRegistration;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.student.IPersonalDataUseInquiryAnswers;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class InfoPersonalDataUseInquiryAnswersWithStudent extends InfoPersonalDataUseInquiryAnswers {

    public void copyFromDomain(IPersonalDataUseInquiryAnswers personalDataUseInquiryAnswers) {
        super.copyFromDomain(personalDataUseInquiryAnswers);
        if (personalDataUseInquiryAnswers != null) {
            setInfoStudent(InfoStudent.newInfoFromDomain(personalDataUseInquiryAnswers.getStudent()));
        }
    }

    public static InfoPersonalDataUseInquiryAnswers newInfoFromDomain(
            IPersonalDataUseInquiryAnswers personalDataUseInquiryAnswers) {
        InfoPersonalDataUseInquiryAnswers infoPersonalDataUseInquiryAnswers = null;
        if (personalDataUseInquiryAnswers != null) {
            infoPersonalDataUseInquiryAnswers = new InfoPersonalDataUseInquiryAnswers();
            infoPersonalDataUseInquiryAnswers.copyFromDomain(personalDataUseInquiryAnswers);
        }
        return infoPersonalDataUseInquiryAnswers;
    }
}