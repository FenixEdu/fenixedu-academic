package net.sourceforge.fenixedu.persistenceTier.OJB.teacher;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.ITeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.teacher.IWeeklyOcupation;
import net.sourceforge.fenixedu.domain.teacher.WeeklyOcupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentTeacherPersonalExpectation;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentWeeklyOcupation;

import org.apache.ojb.broker.query.Criteria;

/**
 * 
 * @author naat
 *
 */
public class TeacherPersonalExpectationOJB extends PersistentObjectOJB implements IPersistentTeacherPersonalExpectation {

    /**
     *  
     */
    public TeacherPersonalExpectationOJB() {
        super();
    }

}