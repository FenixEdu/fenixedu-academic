package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author dcs-rjao
 * 24/Mar/2003
 * 
 */

public class CurricularCourseScopeOJB extends PersistentObjectOJB implements
        IPersistentCurricularCourseScope {

    public CurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(
			Integer curricularCourseId, Integer curricularSemesterId, Integer branchId)
    		throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourseKey", curricularCourseId);
        criteria.addEqualTo("curricularSemesterKey", curricularSemesterId);
        criteria.addEqualTo("branchKey", branchId);
        return (CurricularCourseScope) queryObject(CurricularCourseScope.class, criteria);

    }

    public CurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndEndDate(
			Integer curricularCourseId, Integer curricularSemesterId, Integer branchId,
			Calendar endDate) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourseKey", curricularCourseId);
        criteria.addEqualTo("curricularSemesterKey", curricularSemesterId);
        criteria.addEqualTo("branchKey", branchId);
        if (endDate == null) {
            criteria.addIsNull("end");
        } else {
            criteria.addEqualTo("end", endDate.getTime());
        }
        return (CurricularCourseScope) queryObject(CurricularCourseScope.class, criteria);

    }

    public List readActiveCurricularCourseScopesByCurricularCourse(Integer curricularCourseId)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourseKey", curricularCourseId);
        crit.addIsNull("end");
        List result = queryList(CurricularCourseScope.class, crit);
        return result;
    }

    public List readActiveCurricularCourseScopesByDegreeCurricularPlanId(
            final Integer degreeCurricularPlanId) throws ExcepcaoPersistencia {
        final Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.degreeCurricularPlanKey", degreeCurricularPlanId);
        crit.addIsNull("end");
        final List result = queryList(CurricularCourseScope.class, crit);
        return result;
    }

    public List readCurricularCourseScopesByCurricularCourseInExecutionPeriod(
            Integer curricularCourseId, Date beginDate, Date endDate)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourseKey", curricularCourseId);
        crit.addLessThan("begin", endDate);

        Criteria crit3 = new Criteria();
        crit3.addIsNull("end");

        Criteria crit4 = new Criteria();
        crit4.addGreaterThan("end", beginDate);

        crit3.addOrCriteria(crit4);
        crit.addAndCriteria(crit3);

        List result = queryList(CurricularCourseScope.class, crit);
        return result;
    }

    public List readCurricularCourseScopesByDegreeCurricularPlanInExecutionYear(
            Integer degreeCurricularPlanId, Date beginDate, Date endDate)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();

        crit.addEqualTo("curricularCourse.degreeCurricularPlanKey", degreeCurricularPlanId);
        crit.addLessThan("begin", endDate);

        Criteria crit3 = new Criteria();
        crit3.addIsNull("end");

        Criteria crit4 = new Criteria();
        crit4.addGreaterThan("end", beginDate);

        crit3.addOrCriteria(crit4);
        crit.addAndCriteria(crit3);

        List result = queryList(CurricularCourseScope.class, crit);

        return result;
    }

    public List readCurricularCourseScopesByCurricularCourseInExecutionYear(
            Integer curricularCourseId, Date beginDate, Date endDate)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.idInternal", curricularCourseId);
        crit.addLessThan("begin", endDate);

        Criteria crit3 = new Criteria();
        crit3.addIsNull("end");

        Criteria crit4 = new Criteria();
        crit4.addGreaterThan("end", beginDate);

        crit3.addOrCriteria(crit4);
        crit.addAndCriteria(crit3);

        List result = queryList(CurricularCourseScope.class, crit);
        return result;
    }

    public List readActiveCurricularCourseScopesByDegreeCurricularPlanAndCurricularYear(
			Integer degreeCurricularPlanId, Integer curricularYear, Date beginDate, Date endDate) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.degreeCurricularPlanKey", degreeCurricularPlanId);
        crit.addEqualTo("curricularSemester.curricularYear.year", curricularYear);
        crit.addLessThan("begin", endDate);

        Criteria crit3 = new Criteria();
        crit3.addIsNull("end");

        Criteria crit4 = new Criteria();
        crit4.addGreaterThan("end", beginDate);

        crit3.addOrCriteria(crit4);
        crit.addAndCriteria(crit3);
        List result = queryList(CurricularCourseScope.class, crit);

        return result;
    }
}