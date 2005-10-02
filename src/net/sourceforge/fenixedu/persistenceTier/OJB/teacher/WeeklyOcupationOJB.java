package net.sourceforge.fenixedu.persistenceTier.OJB.teacher;

import net.sourceforge.fenixedu.domain.teacher.IWeeklyOcupation;
import net.sourceforge.fenixedu.domain.teacher.WeeklyOcupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentWeeklyOcupation;

import org.apache.ojb.broker.query.Criteria;


public class WeeklyOcupationOJB extends PersistentObjectOJB implements IPersistentWeeklyOcupation {

    public WeeklyOcupationOJB() {
        super();
    }

    public IWeeklyOcupation readByTeacherId(Integer teacherId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacherId);
        return (IWeeklyOcupation) queryObject(WeeklyOcupation.class, criteria);
    }

}