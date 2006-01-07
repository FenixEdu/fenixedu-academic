/*
 * Created on Jul 12, 2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.student.PersonalDataUseInquiryAnswers;
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

    public PersonalDataUseInquiryAnswers readAnswersByStudent(Student student)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", student.getIdInternal());
        return (PersonalDataUseInquiryAnswers) queryObject(PersonalDataUseInquiryAnswers.class,
                criteria);
    }

}