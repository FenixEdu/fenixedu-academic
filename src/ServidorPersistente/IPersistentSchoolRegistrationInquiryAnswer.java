/*
 * Created on Jul 12, 2004
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IStudent;
import Dominio.student.SchoolRegistrationInquiryAnswer;


/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IPersistentSchoolRegistrationInquiryAnswer extends IPersistentObject{
    
    public void delete(SchoolRegistrationInquiryAnswer inquiryAnswer) throws ExcepcaoPersistencia;

    public List readAnswersByStudent(IStudent student) throws ExcepcaoPersistencia;

}
