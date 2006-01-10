/*
 * Created on Jul 12, 2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.student.PersonalDataUseInquiryAnswers;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IPersistentPersonalDataUseInquiryAnswers extends IPersistentObject {

    public void delete(PersonalDataUseInquiryAnswers inquiryAnswer) throws ExcepcaoPersistencia;

    public PersonalDataUseInquiryAnswers readAnswersByStudent(Student student)
            throws ExcepcaoPersistencia;

}