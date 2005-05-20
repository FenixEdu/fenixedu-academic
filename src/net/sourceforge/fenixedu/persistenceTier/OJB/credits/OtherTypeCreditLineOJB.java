package net.sourceforge.fenixedu.persistenceTier.OJB.credits;

import java.util.List;

import net.sourceforge.fenixedu.domain.credits.OtherTypeCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentOtherTypeCreditLine;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author jpvl
 */
public class OtherTypeCreditLineOJB extends PersistentObjectOJB implements
        IPersistentOtherTypeCreditLine {

    public List readByTeacherAndExecutionPeriod(Integer teacherId, Integer executionPeriodId)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacherId);
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriodId);
        return queryList(OtherTypeCreditLine.class, criteria);
    }

}