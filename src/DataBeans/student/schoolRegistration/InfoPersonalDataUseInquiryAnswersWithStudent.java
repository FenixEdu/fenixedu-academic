/*
 * Created on Aug 15, 2004
 *
 */
package DataBeans.student.schoolRegistration;

import DataBeans.InfoStudent;
import Dominio.student.IPersonalDataUseInquiryAnswers;

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