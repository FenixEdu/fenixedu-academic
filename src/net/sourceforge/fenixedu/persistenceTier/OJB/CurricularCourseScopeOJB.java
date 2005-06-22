package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
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

    public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(
			Integer curricularCourseId, Integer curricularSemesterId, Integer branchId)
    		throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourseKey", curricularCourseId);
        criteria.addEqualTo("curricularSemesterKey", curricularSemesterId);
        criteria.addEqualTo("branchKey", branchId);
        return (ICurricularCourseScope) queryObject(CurricularCourseScope.class, criteria);

    }

    public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndEndDate(
			Integer curricularCourseId, Integer curricularSemesterId, Integer branchId,
			Calendar endDate) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourseKey", curricularCourseId);
        criteria.addEqualTo("curricularSemesterKey", curricularSemesterId);
        criteria.addEqualTo("branchKey", branchId);
        if (endDate == null) {
            criteria.addIsNull("endDate");
        } else {
            criteria.addEqualTo("endDate", endDate);
        }
        return (ICurricularCourseScope) queryObject(CurricularCourseScope.class, criteria);

    }

    public List readActiveCurricularCourseScopesByCurricularCourse(Integer curricularCourseId)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourseKey", curricularCourseId);
        crit.addIsNull("endDate");
        List result = queryList(CurricularCourseScope.class, crit);
        return result;
    }

    public List readActiveCurricularCourseScopesByDegreeCurricularPlanId(
            final Integer degreeCurricularPlanId) throws ExcepcaoPersistencia {
        final Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.degreeCurricularPlanKey", degreeCurricularPlanId);
        crit.addIsNull("endDate");
        final List result = queryList(CurricularCourseScope.class, crit);
        return result;
    }

    public List readCurricularCourseScopesByCurricularCourseInExecutionPeriod(
            Integer curricularCourseId, Date beginDate, Date endDate)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourseKey", curricularCourseId);
        crit.addLessThan("beginDate", endDate);

        Criteria crit3 = new Criteria();
        crit3.addIsNull("endDate");

        Criteria crit4 = new Criteria();
        crit4.addGreaterThan("endDate", beginDate);

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
        crit.addLessThan("beginDate", endDate);

        Criteria crit3 = new Criteria();
        crit3.addIsNull("endDate");

        Criteria crit4 = new Criteria();
        crit4.addGreaterThan("endDate", beginDate);

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
        crit.addLessThan("beginDate", endDate);

        Criteria crit3 = new Criteria();
        crit3.addIsNull("endDate");

        Criteria crit4 = new Criteria();
        crit4.addGreaterThan("endDate", beginDate);

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
        crit.addLessThan("beginDate", endDate);

        Criteria crit3 = new Criteria();
        crit3.addIsNull("endDate");

        Criteria crit4 = new Criteria();
        crit4.addGreaterThan("endDate", beginDate);

        crit3.addOrCriteria(crit4);
        crit.addAndCriteria(crit3);
        List result = queryList(CurricularCourseScope.class, crit);

        return result;
    }
}