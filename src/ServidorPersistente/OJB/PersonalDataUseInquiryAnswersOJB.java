/*
 * Created on Jul 12, 2004
 *
 */
package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IStudent;
import Dominio.student.IPersonalDataUseInquiryAnswers;
import Dominio.student.PersonalDataUseInquiryAnswers;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPersonalDataUseInquiryAnswers;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class PersonalDataUseInquiryAnswersOJB extends PersistentObjectOJB implements
        IPersistentPersonalDataUseInquiryAnswers {

    public void delete(PersonalDataUseInquiryAnswers inquiryAnswer) throws ExcepcaoPersistencia {
        super.delete(inquiryAnswer);
    }

    public IPersonalDataUseInquiryAnswers readAnswersByStudent(IStudent student)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", student.getIdInternal());
        return (IPersonalDataUseInquiryAnswers) queryObject(PersonalDataUseInquiryAnswers.class,
                criteria);
    }

}