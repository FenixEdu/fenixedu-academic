/*
 * Created on Jul 12, 2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.student.IPersonalDataUseInquiryAnswers;
import net.sourceforge.fenixedu.domain.student.PersonalDataUseInquiryAnswers;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPersonalDataUseInquiryAnswers;

import org.apache.ojb.broker.query.Criteria;

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