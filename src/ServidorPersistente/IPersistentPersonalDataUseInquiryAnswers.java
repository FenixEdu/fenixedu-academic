/*
 * Created on Jul 12, 2004
 *
 */
package ServidorPersistente;

import Dominio.IStudent;
import Dominio.student.IPersonalDataUseInquiryAnswers;
import Dominio.student.PersonalDataUseInquiryAnswers;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IPersistentPersonalDataUseInquiryAnswers extends IPersistentObject {

    public void delete(PersonalDataUseInquiryAnswers inquiryAnswer) throws ExcepcaoPersistencia;

    public IPersonalDataUseInquiryAnswers readAnswersByStudent(IStudent student)
            throws ExcepcaoPersistencia;

}