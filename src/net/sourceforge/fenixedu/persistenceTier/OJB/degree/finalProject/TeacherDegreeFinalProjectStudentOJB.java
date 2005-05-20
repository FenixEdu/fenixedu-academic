package net.sourceforge.fenixedu.persistenceTier.OJB.degree.finalProject;

import java.util.List;

import net.sourceforge.fenixedu.domain.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author jpvl
 */
public class TeacherDegreeFinalProjectStudentOJB extends PersistentObjectOJB implements
        IPersistentTeacherDegreeFinalProjectStudent {

    public List readByTeacherAndExecutionPeriod(Integer teacherId, Integer executionPeriodId)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacherId);
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriodId);
        return queryList(TeacherDegreeFinalProjectStudent.class, criteria);
    }

    public ITeacherDegreeFinalProjectStudent readByUnique(Integer teacherId, Integer executionPeriodId,
            Integer studentId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacherId);
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriodId);
        criteria.addEqualTo("student.idInternal", studentId);
        return (ITeacherDegreeFinalProjectStudent) queryObject(TeacherDegreeFinalProjectStudent.class,
                criteria);
    }

    public List readByStudentAndExecutionPeriod(Integer studentId, Integer executionPeriodId)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("student.idInternal", studentId);
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriodId);
        return queryList(TeacherDegreeFinalProjectStudent.class, criteria);
    }

}
