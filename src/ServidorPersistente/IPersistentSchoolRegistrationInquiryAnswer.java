/*
 * Created on Jul 12, 2004
 *
 */
package ServidorPersistente;

import Dominio.student.ISchoolRegistrationInquiryAnswer;
import Dominio.student.SchoolRegistrationInquiryAnswer;


/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IPersistentSchoolRegistrationInquiryAnswer extends IPersistentObject{
    
    public void delete(SchoolRegistrationInquiryAnswer inquiryAnswer) throws ExcepcaoPersistencia;

    public ISchoolRegistrationInquiryAnswer readAnswersByStudentNumber(Integer studentNumber) throws ExcepcaoPersistencia;

}
