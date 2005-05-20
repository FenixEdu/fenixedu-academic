package net.sourceforge.fenixedu.persistenceTier.OJB.credits;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.credits.ManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentManagementPositionCreditLine;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author jpvl
 */
public class ManagementPositionCreditLineOJB extends PersistentObjectOJB implements
        IPersistentManagementPositionCreditLine {

    public List readByTeacherAndExecutionPeriod(Integer teacherId, Date executionPeriodBeginDate, Date executionPeriodEndDate)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("teacher.idInternal", teacherId);
        criteria.addGreaterThan("end", executionPeriodBeginDate);
        criteria.addLessThan("start", executionPeriodEndDate);

        return queryList(ManagementPositionCreditLine.class, criteria);
    }

    public List readByTeacher(Integer teacherId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addEqualTo("teacher.idInternal", teacherId);

        return queryList(ManagementPositionCreditLine.class, criteria);
    }

}