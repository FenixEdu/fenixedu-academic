/*
 * Created on Jul 12, 2004
 *
 */
package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.student.ISchoolRegistrationInquiryAnswer;
import Dominio.student.SchoolRegistrationInquiryAnswer;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSchoolRegistrationInquiryAnswer;


/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class SchoolRegistrationInquiryAnswerOJB extends ObjectFenixOJB implements IPersistentSchoolRegistrationInquiryAnswer{ 
    
    public void delete(SchoolRegistrationInquiryAnswer inquiryAnswer) throws ExcepcaoPersistencia
    {
        super.delete(inquiryAnswer);
    }

    public ISchoolRegistrationInquiryAnswer readAnswersByStudentNumber(Integer studentNumber) throws ExcepcaoPersistencia {
        
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent",studentNumber);
        return (ISchoolRegistrationInquiryAnswer)queryObject(SchoolRegistrationInquiryAnswer.class,criteria);
    }

}
