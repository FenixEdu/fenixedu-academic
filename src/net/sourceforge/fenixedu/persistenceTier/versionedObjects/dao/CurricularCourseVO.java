package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class CurricularCourseVO extends VersionedObjectsBase implements IPersistentCurricularCourse {

    public List readByCurricularStage(CurricularStage curricularStage) throws ExcepcaoPersistencia {
        List<CurricularCourse> result = new ArrayList<CurricularCourse>();
        for (CurricularCourse cc : (List<CurricularCourse>) readAll(CurricularCourse.class)) {
            if (cc.getCurricularStage().equals(curricularStage)) {
                result.add(cc);
            }
        }
        return result;
    }
    
    public List readCurricularCoursesByDegreeCurricularPlan(final String name, final String degreeName,
            final String degreeSigla) throws ExcepcaoPersistencia {

        final Collection curricularCourses = readByCurricularStage(CurricularStage.OLD);

        return (List) CollectionUtils.select(curricularCourses, new Predicate() {
            public boolean evaluate(Object obj) {
                String nameEval = ((CurricularCourse) obj).getDegreeCurricularPlan().getName();
                String degreeNameEval = ((CurricularCourse) obj).getDegreeCurricularPlan().getDegree()
                        .getNome();
                String degreeSiglaEval = ((CurricularCourse) obj).getDegreeCurricularPlan().getDegree()
                        .getSigla();

                return (nameEval.equals(name) && degreeNameEval.equals(degreeName) && degreeSiglaEval
                        .equals(degreeSigla));
            }
        });
    }

    public List readCurricularCoursesByDegreeCurricularPlanAndBasicAttribute(
            final Integer degreeCurricularPlanKey, final Boolean basic) throws ExcepcaoPersistencia {

        final Collection curricularCourses = readByCurricularStage(CurricularStage.OLD);

        return (List) CollectionUtils.select(curricularCourses, new Predicate() {
            public boolean evaluate(Object obj) {
                return ((CurricularCourse) obj).getDegreeCurricularPlan().getIdInternal().equals(
                        degreeCurricularPlanKey)
                        && ((CurricularCourse) obj).getBasic().equals(basic);
            }
        });
    }

    public List readbyCourseNameAndDegreeCurricularPlan(final String curricularCourseName,
            final Integer degreeCurricularPlanKey) throws ExcepcaoPersistencia {

        final Collection curricularCourses = readByCurricularStage(CurricularStage.OLD);

        return (List) CollectionUtils.select(curricularCourses, new Predicate() {
            public boolean evaluate(Object obj) {
                return ((CurricularCourse) obj).getName().equals(curricularCourseName)
                        && ((CurricularCourse) obj).getDegreeCurricularPlan().getIdInternal().equals(
                                degreeCurricularPlanKey);
            }
        });
    }

    public List readExecutedCurricularCoursesByDegreeAndExecutionYear(final Integer degreeKey,
            final Integer executionYearKey) throws ExcepcaoPersistencia {

        final Collection curricularCourses = readByCurricularStage(CurricularStage.OLD);

        return (List) CollectionUtils.select(curricularCourses, new Predicate() {
            public boolean evaluate(Object obj) {
                List executionCourses = ((CurricularCourse) obj).getAssociatedExecutionCourses();

                if (((CurricularCourse) obj).getDegreeCurricularPlan().getDegree().getIdInternal()
                        .equals(degreeKey)
                        && ((CurricularCourse) obj).getDegreeCurricularPlan().getState().equals(
                                DegreeCurricularPlanState.ACTIVE)) {

                    for (Iterator iterator = executionCourses.iterator(); iterator.hasNext();) {
                        if (((ExecutionCourse) iterator.next()).getExecutionPeriod().getExecutionYear()
                                .getIdInternal().equals(executionYearKey))
                            return true;

                    }
                }
                return false;

            }
        });
    }

    public List readExecutedCurricularCoursesByDegreeAndYearAndExecutionYear(final Integer degreeKey,
            final Integer year, final Integer executionYearKey) throws ExcepcaoPersistencia {

        final Collection curricularCourses = readByCurricularStage(CurricularStage.OLD);

        return (List) CollectionUtils.select(curricularCourses, new Predicate() {
            public boolean evaluate(Object obj) {
                List executionCourses = ((CurricularCourse) obj).getAssociatedExecutionCourses();
                List scopes = ((CurricularCourse) obj).getScopes();

                if (((CurricularCourse) obj).getDegreeCurricularPlan().getDegree().getIdInternal()
                        .equals(degreeKey)
                        && ((CurricularCourse) obj).getDegreeCurricularPlan().getState().equals(
                                DegreeCurricularPlanState.ACTIVE)) {

                    for (Iterator iterator = executionCourses.iterator(); iterator.hasNext();) {
                        if (((ExecutionCourse) iterator.next()).getExecutionPeriod().getExecutionYear()
                                .getIdInternal().equals(executionYearKey))

                            for (Iterator iteratorScope = scopes.iterator(); iteratorScope.hasNext();) {
                                if (((CurricularCourseScope) iterator.next()).getCurricularSemester()
                                        .getCurricularYear().getYear().equals(year))
                                    return true;
                            }
                    }
                }
                return false;
            }
        });
    }

    public CurricularCourse readCurricularCourseByDegreeCurricularPlanAndNameAndCode(
            final Integer degreeCurricularPlanId, final String name, final String code)
            throws ExcepcaoPersistencia {

        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanId);

        if (degreeCurricularPlan != null) {
            List<CurricularCourse> curricularCourses = degreeCurricularPlan.getCurricularCourses();
            for (final CurricularCourse curricularCourse : curricularCourses) {
                if (curricularCourse.getName().equals(name) && curricularCourse.getCode().equals(code)) {
                    return curricularCourse;
                }
            }
        }
        return null;
    }

    public List readbyCourseCodeAndDegreeCurricularPlan(final String curricularCourseCode,
            final Integer degreeCurricularPlanID) throws ExcepcaoPersistencia {

        final List<CurricularCourse> result = new ArrayList<CurricularCourse>();
        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID);

        if (degreeCurricularPlan != null) {
            List<CurricularCourse> curricularCourses = degreeCurricularPlan.getCurricularCourses();
            for (final CurricularCourse curricularCourse : curricularCourses) {
                if (curricularCourse.getCode().equals(curricularCourseCode)) {
                    result.add(curricularCourse);
                }
            }
        }
        return result;
    }
}
