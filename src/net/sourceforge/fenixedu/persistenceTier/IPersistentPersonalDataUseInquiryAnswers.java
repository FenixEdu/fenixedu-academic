/*
 * Created on Jul 12, 2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.student.IPersonalDataUseInquiryAnswers;
import net.sourceforge.fenixedu.domain.student.PersonalDataUseInquiryAnswers;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IPersistentPersonalDataUseInquiryAnswers extends IPersistentObject {

    public void delete(PersonalDataUseInquiryAnswers inquiryAnswer) throws ExcepcaoPersistencia;

    public IPersonalDataUseInquiryAnswers readAnswersByStudent(IStudent student)
            throws ExcepcaoPersistencia;

}