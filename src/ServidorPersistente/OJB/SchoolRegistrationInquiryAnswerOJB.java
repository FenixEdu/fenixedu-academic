/*
 * Created on Jul 12, 2004
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IStudent;
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

    public List readAnswersByStudent(IStudent student) throws ExcepcaoPersistencia {
        
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent",student.getIdInternal());
        return queryList(SchoolRegistrationInquiryAnswer.class,criteria);
    }

}
