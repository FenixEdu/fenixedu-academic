/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.teacher;

import java.util.List;

import net.sourceforge.fenixedu.domain.CareerType;
import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentCareer;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class CareerOJB extends PersistentObjectOJB implements IPersistentCareer {

    public CareerOJB() {
        super();
    }

    public List readAllByTeacherIdAndCareerType(Integer teacherId, CareerType careerType)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacherId);
        if (careerType != null) {
            if (careerType.equals(CareerType.PROFESSIONAL)) {
                criteria.addEqualTo("ojbConcreteClass", ProfessionalCareer.class.getName());
            } else if (careerType.equals(CareerType.TEACHING)) {
                criteria.addEqualTo("ojbConcreteClass", TeachingCareer.class.getName());
            }
        }
        List careers = queryList(Career.class, criteria);

        return careers;
    }
}