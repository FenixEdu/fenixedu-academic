package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularSemester;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
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

    public void delete(ICurricularCourseScope scope) throws ExcepcaoPersistencia {
            super.delete(scope);
    }

    public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(
            ICurricularCourse curricularCourse, ICurricularSemester curricularSemester, IBranch branch)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
        criteria.addEqualTo("curricularSemesterKey", curricularSemester.getIdInternal());
        criteria.addEqualTo("branchKey", branch.getIdInternal());
        return (ICurricularCourseScope) queryObject(CurricularCourseScope.class, criteria);

    }

    public List readAll() throws ExcepcaoPersistencia {

        return queryList(CurricularCourseScope.class, new Criteria());
    }

    public List readCurricularCourseScopesByCurricularCourse(ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
        List result = queryList(CurricularCourseScope.class, crit);
        return result;
    }

    public List readByCurricularCourseAndSemesterAndBranch(ICurricularCourse curricularCourse,
            Integer semester, IBranch branch) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
        crit.addEqualTo("curricularSemester.semester", semester);
        crit.addEqualTo("branchKey", branch.getIdInternal());

        return queryList(CurricularCourseScope.class, crit);

    }

    public List readByCurricularCourseAndYearAndSemester(ICurricularCourse curricularCourse,
            Integer year, Integer semester) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
        crit.addEqualTo("curricularSemester.semester", semester);
        crit.addEqualTo("curricularSemester.curricularYear.year", year);

        return queryList(CurricularCourseScope.class, crit);
    }

    public List readByCurricularCourseAndYear(ICurricularCourse curricularCourse, Integer year)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
        crit.addEqualTo("curricularSemester.curricularYear.year", year);

        return queryList(CurricularCourseScope.class, crit);
    }

    public List readByCurricularCourseAndSemester(ICurricularCourse curricularCourse, Integer semester)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
        crit.addEqualTo("curricularSemester.semester", semester);

        return queryList(CurricularCourseScope.class, crit);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentCurricularCourseScope#readByCurricularCourse(Dominio.ICurricularCourse)
     */
    public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());

        return queryList(CurricularCourseScope.class, crit);

    }

    public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndEndDate(
            ICurricularCourse curricularCourse, ICurricularSemester curricularSemester, IBranch branch,
            Calendar endDate) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
        criteria.addEqualTo("curricularSemesterKey", curricularSemester.getIdInternal());
        criteria.addEqualTo("branchKey", branch.getIdInternal());
        if (endDate == null) {
            criteria.addIsNull("endDate");
        } else {
            criteria.addEqualTo("endDate", endDate);
        }
        return (ICurricularCourseScope) queryObject(CurricularCourseScope.class, criteria);

    }

    public List readActiveCurricularCourseScopesByCurricularCourse(ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
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
            ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
        crit.addLessThan("beginDate", executionPeriod.getEndDate());

        Criteria crit3 = new Criteria();
        crit3.addIsNull("endDate");

        Criteria crit4 = new Criteria();
        crit4.addGreaterThan("endDate", executionPeriod.getBeginDate());

        crit3.addOrCriteria(crit4);
        crit.addAndCriteria(crit3);

        List result = queryList(CurricularCourseScope.class, crit);
        return result;
    }

    public List readCurricularCourseScopesByDegreeCurricularPlanInExecutionYear(
            IDegreeCurricularPlan degreeCurricularPlan, IExecutionYear executionYear)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();

        crit
                .addEqualTo("curricularCourse.degreeCurricularPlanKey", degreeCurricularPlan
                        .getIdInternal());
        crit.addLessThan("beginDate", executionYear.getEndDate());

        Criteria crit3 = new Criteria();
        crit3.addIsNull("endDate");

        Criteria crit4 = new Criteria();
        crit4.addGreaterThan("endDate", executionYear.getBeginDate());

        crit3.addOrCriteria(crit4);
        crit.addAndCriteria(crit3);

        List result = queryList(CurricularCourseScope.class, crit);

        return result;
    }

    public List readCurricularCourseScopesByCurricularCourseInExecutionYear(
            ICurricularCourse curricularCourse, IExecutionYear executionYear)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        crit.addLessThan("beginDate", executionYear.getEndDate());

        Criteria crit3 = new Criteria();
        crit3.addIsNull("endDate");

        Criteria crit4 = new Criteria();
        crit4.addGreaterThan("endDate", executionYear.getBeginDate());

        crit3.addOrCriteria(crit4);
        crit.addAndCriteria(crit3);

        List result = queryList(CurricularCourseScope.class, crit);
        return result;
    }

    public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndBeginDate(
            ICurricularCourse curricularCourse, ICurricularSemester curricularSemester, IBranch branch,
            Calendar beginDate) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourseKey", curricularCourse.getIdInternal());
        criteria.addEqualTo("curricularSemesterKey", curricularSemester.getIdInternal());
        criteria.addEqualTo("branchKey", branch.getIdInternal());
        criteria.addEqualTo("beginDate", beginDate);
        return (ICurricularCourseScope) queryObject(CurricularCourseScope.class, criteria);

    }

    //added by Nuno Correia
    public List readByBranch(IBranch branch) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("branchKey", branch.getIdInternal());
        return queryList(CurricularCourseScope.class, criteria);
    }

    public List readActiveCurricularCourseScopesByDegreeCurricularPlanAndCurricularYear(
            IDegreeCurricularPlan degreeCurricularPlan, Integer curricularYear,
            IExecutionYear executionYear) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit
                .addEqualTo("curricularCourse.degreeCurricularPlanKey", degreeCurricularPlan
                        .getIdInternal());
        crit.addEqualTo("curricularSemester.curricularYear.year", curricularYear);
        crit.addLessThan("beginDate", executionYear.getEndDate());

        Criteria crit3 = new Criteria();
        crit3.addIsNull("endDate");

        Criteria crit4 = new Criteria();
        crit4.addGreaterThan("endDate", executionYear.getBeginDate());

        crit3.addOrCriteria(crit4);
        crit.addAndCriteria(crit3);
        List result = queryList(CurricularCourseScope.class, crit);

        return result;
    }

    public List readCurricularCourseScopesByDegreeCurricularPlanAndExecutionYear(
            IDegreeCurricularPlan degreeCurricularPlan, IExecutionYear executionYear)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();

        crit
                .addEqualTo("curricularCourse.degreeCurricularPlanKey", degreeCurricularPlan
                        .getIdInternal());

        List result = queryList(CurricularCourseScope.class, crit);

        return result;
    }

}