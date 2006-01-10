/*
 * Created on Jun 1, 2005
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class ExecutionCourseVO extends VersionedObjectsBase implements IPersistentExecutionCourse {

    public List readByCurricularYearAndExecutionPeriodAndExecutionDegree(Integer anoCurricular,
            Integer executionPeriodSemestre, String degreeCurricularPlanName, String degreeSigla,
            Integer executionPeriodID) throws ExcepcaoPersistencia {

        final ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodID);
        final List<ExecutionCourse> executionCourses = executionPeriod.getAssociatedExecutionCourses();
        final List result = new ArrayList();
        for (final ExecutionCourse executionCourse : executionCourses) {
            final List<CurricularCourse> curricularCourses = executionCourse
                    .getAssociatedCurricularCourses();
            if (hasCurricularCourseScopeAndDegreeCurricularPlan(curricularCourses,
                    degreeCurricularPlanName, degreeSigla, anoCurricular)) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    private boolean hasCurricularCourseScopeAndDegreeCurricularPlan(
            List<CurricularCourse> curricularCourses, String degreeCurricularPlanName,
            String degreeSigla, Integer anoCurricular) {
        for (final CurricularCourse curricularCourse : curricularCourses) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse
                    .getDegreeCurricularPlan();
            final Degree degree = degreeCurricularPlan.getDegree();
            if (degreeCurricularPlan.getName().equals(degreeCurricularPlanName)
                    && degree.getSigla().equals(degreeSigla)) {
                final List<CurricularCourseScope> curricularCourseScopes = curricularCourse.getScopes();
                for (final CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
                    final CurricularSemester curricularSemester = curricularCourseScope
                            .getCurricularSemester();
                    final CurricularYear curricularYear = curricularSemester.getCurricularYear();
                    if (curricularYear.getYear().equals(anoCurricular)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public List readByExecutionPeriodAndExecutionDegree(Integer executionPeriodID,
            String curricularPlanName, String degreeSigla) throws ExcepcaoPersistencia {

        final ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodID);
        final List<ExecutionCourse> executionCourses = executionPeriod.getAssociatedExecutionCourses();
        final List result = new ArrayList();
        for (final ExecutionCourse executionCourse : executionCourses) {
            final List<CurricularCourse> curricularCourses = executionCourse
                    .getAssociatedCurricularCourses();
            if (hasDegreeCurricularPlan(curricularCourses, curricularPlanName, degreeSigla)) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    private boolean hasDegreeCurricularPlan(List<CurricularCourse> curricularCourses,
            String curricularPlanName, String degreeSigla) {
        for (final CurricularCourse curricularCourse : curricularCourses) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse
                    .getDegreeCurricularPlan();
            final Degree degree = degreeCurricularPlan.getDegree();
            if (degreeCurricularPlan.getName().equals(curricularPlanName)
                    && degree.getSigla().equals(degreeSigla)) {
                return true;
            }
        }
        return false;
    }

    public ExecutionCourse readByExecutionCourseInitialsAndExecutionPeriodId(String courseInitials,
            Integer executionPeriodId) throws ExcepcaoPersistencia {

        ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodId);

        if (executionPeriod != null) {
            List<ExecutionCourse> executionCourses = executionPeriod.getAssociatedExecutionCourses();
            for (ExecutionCourse course : executionCourses) {
                if (course.getSigla().equals(courseInitials))
                    return course;
            }
        }
        return null;
    }

    public List readByExecutionPeriod(Integer executionPeriodID, DegreeType curso)
            throws ExcepcaoPersistencia {

        ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodID);
        List executionCourses = new ArrayList();

        if (executionPeriod != null) {
            List<ExecutionCourse> executionCourses1 = executionPeriod.getAssociatedExecutionCourses();
            for (ExecutionCourse course : executionCourses1) {
                List<CurricularCourse> curricularCourses = course.getAssociatedCurricularCourses();
                for (CurricularCourse curricularCourse : curricularCourses) {
                    if (curricularCourse.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(
                            curso)) {
                        executionCourses.add(course);
                        break;
                    }
                }
            }
        }
        return executionCourses;
    }

    public List readByExecutionPeriod(Integer executionPeriodID) throws ExcepcaoPersistencia {

        ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodID);

        if (executionPeriod != null) {
            return executionPeriod.getAssociatedExecutionCourses();
        }

        return null;
    }

    public List readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(
            Integer executionPeriodID, Integer degreeCurricularPlanID, Integer curricularYearID,
            String executionCourseName, Integer semester) throws ExcepcaoPersistencia {

        final ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodID);
        final List<ExecutionCourse> executionCourses = executionPeriod.getAssociatedExecutionCourses();
        final List result = new ArrayList();
        for (final ExecutionCourse executionCourse : executionCourses) {
            boolean addExecutionCourse = true;

            if (executionCourseName != null && !executionCourseName.equals("")
                    && !executionCourse.getNome().matches(executionCourseName.replaceAll("%", "*"))) {
                addExecutionCourse = false;
            }

            final List<CurricularCourse> curricularCourses = executionCourse
                    .getAssociatedCurricularCourses();
            if (degreeCurricularPlanID != null
                    && !hasDegreeCurricularPlan(curricularCourses, degreeCurricularPlanID)) {
                addExecutionCourse = false;
            }

            if (!hasCurricularCourseSemesterAndYear(curricularCourses, semester, curricularYearID)) {
                addExecutionCourse = false;
            }

            if (addExecutionCourse) {
                result.add(executionCourse);
            }
        }
        return result;
    }

private boolean hasCurricularCourseSemesterAndYear(List<CurricularCourse> curricularCourses,
            Integer semester, Integer curricularYearID) {
        for (final CurricularCourse curricularCourse : curricularCourses) {
            final List<CurricularCourseScope> curricularCourseScopes = curricularCourse.getScopes();
            for (final CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
                final CurricularSemester curricularSemester = curricularCourseScope
                        .getCurricularSemester();
                final CurricularYear curricularYear = curricularSemester.getCurricularYear();
                if (curricularSemester.getSemester().equals(semester)
                        && (curricularYearID == null || curricularYear.getIdInternal().equals(curricularYearID))) {
                    return true;
                }
            }
        }
        return false;
    }    private boolean hasDegreeCurricularPlan(List<CurricularCourse> curricularCourses,
            Integer degreeCurricularPlanID) {
        for (final CurricularCourse curricularCourse : curricularCourses) {
            final DegreeCurricularPlan degreeCurricularPlan = curricularCourse
                    .getDegreeCurricularPlan();
            if (degreeCurricularPlan.getIdInternal().equals(degreeCurricularPlanID)) {
                return true;
            }
        }
        return false;
    }

    public List readbyCurricularCourseAndExecutionPeriod(Integer curricularCourseID,
            Integer executionPeriodID) throws ExcepcaoPersistencia {

        List executionCoursesAux = new ArrayList();
        CurricularCourse curricularCourse = (CurricularCourse) readByOID(CurricularCourse.class,
                curricularCourseID);

        if (curricularCourse != null) {
            List<ExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCourses();

            for (ExecutionCourse executionCourse : executionCourses) {
                if (executionCourse.getExecutionPeriod().getIdInternal().equals(executionPeriodID))
                    executionCoursesAux.add(executionCourse);
            }
        }
        return executionCoursesAux;
    }

    public List readByDegreeCurricularPlanAndExecutionYear(Integer degreeCurricularPlanID,
            Integer executionYearID) throws ExcepcaoPersistencia {

        DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID);

        List<CurricularCourse> curricularCourses = degreeCurricularPlan.getCurricularCourses();

        List executionCourses = new ArrayList();
        for (CurricularCourse curricularCourse : curricularCourses) {
            List<ExecutionCourse> executionCoursesAux = curricularCourse
                    .getAssociatedExecutionCourses();
            for (ExecutionCourse executionCourse : executionCoursesAux) {
                if (executionCourse.getExecutionPeriod().getExecutionYear().getIdInternal().equals(
                        executionYearID))
                    executionCourses.add(executionCourse);
            }
        }

        return executionCourses;
    }

    public List readByExecutionDegreeAndExecutionPeriod(Integer degreeCurricularPlanID,
            Integer executionPeriodID) throws ExcepcaoPersistencia {

        DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID);

        List<CurricularCourse> curricularCourses = degreeCurricularPlan.getCurricularCourses();

        List executionCourses = new ArrayList();
        for (CurricularCourse curricularCourse : curricularCourses) {
            List<ExecutionCourse> executionCoursesAux = curricularCourse
                    .getAssociatedExecutionCourses();
            for (ExecutionCourse executionCourse : executionCoursesAux) {
                if (executionCourse.getExecutionPeriod().getIdInternal().equals(executionPeriodID))
                    executionCourses.add(executionCourse);
            }
        }

        return executionCourses;
    }

    public List readByExecutionCourseIds(List<Integer> executionCourseIds) throws ExcepcaoPersistencia {

        List executionCourses = new ArrayList();

        for (Integer executionCourseID : executionCourseIds) {
            ExecutionCourse executionCourse = (ExecutionCourse) readByOID(ExecutionCourse.class,
                    executionCourseID);
            executionCourses.add(executionCourse);
        }

        return executionCourses;
    }

    public List readByExecutionPeriodWithNoCurricularCourses(Integer executionPeriodID)
            throws ExcepcaoPersistencia {
        ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodID);

        List executionCoursesFinal = new ArrayList();
        if (executionPeriod != null) {
            List<ExecutionCourse> executionCourses = executionPeriod.getAssociatedExecutionCourses();
            for (ExecutionCourse executionCourse : executionCourses) {
                if (executionCourse.getAssociatedCurricularCourses().isEmpty())
                    executionCoursesFinal.add(executionCourse);
            }
        }
        return executionCoursesFinal;
    }

    public List readByCurricularYearAndAllExecutionPeriodAndExecutionDegree(Integer curricularYear,
            Integer executionPeriodID, String degreeCurricularPlanName, String degreeSigla)
            throws ExcepcaoPersistencia {

        ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodID);

        List executionCourses = new ArrayList();
        if (executionPeriod != null) {
            List<ExecutionDegree> executionDegrees = executionPeriod.getExecutionYear()
                    .getExecutionDegrees();

            for (ExecutionDegree degree : executionDegrees) {
                if (degree.getDegreeCurricularPlan().getName().equals(degreeCurricularPlanName)
                        && degree.getDegreeCurricularPlan().getDegree().getSigla().equals(degreeSigla)) {
                    List<CurricularCourse> curricularCourses = degree.getDegreeCurricularPlan()
                            .getCurricularCourses();
                    for (CurricularCourse course : curricularCourses) {
                        List<CurricularCourseScope> scopes = course.getScopes();
                        for (CurricularCourseScope courseScope : scopes) {
                            if (courseScope.getCurricularSemester().getCurricularYear().getYear()
                                    .equals(curricularYear)) {
                                executionCourses.addAll(course.getAssociatedExecutionCourses());
                            }
                        }
                    }
                }
            }
        }
        return executionCourses;
    }

    public List readByCurricularYearAndExecutionPeriodAndExecutionDegreeList(Integer curricularYear,
            Integer executionPeriodID, Integer executionPeriodSemestre,
            List<ExecutionDegree> executionDegreeList) throws ExcepcaoPersistencia {
        ExecutionPeriod executionPeriod = (ExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodID);
        List executionCourses = new ArrayList();

        List curricularPlanNames = new ArrayList();
        List degreeSiglas = new ArrayList();
        for (ExecutionDegree executionDegree : executionDegreeList) {
            curricularPlanNames.add(executionDegree.getDegreeCurricularPlan().getName());
            degreeSiglas.add(executionDegree.getDegreeCurricularPlan().getDegree().getSigla());
        }

        if (executionPeriod != null) {
            List<ExecutionDegree> executionDegrees = executionPeriod.getExecutionYear()
                    .getExecutionDegrees();
            for (ExecutionDegree degree : executionDegrees) {
                if (curricularPlanNames.contains(degree.getDegreeCurricularPlan().getName())
                        && degreeSiglas
                                .contains(degree.getDegreeCurricularPlan().getDegree().getSigla())) {
                    List<CurricularCourse> curricularCourses = degree.getDegreeCurricularPlan()
                            .getCurricularCourses();
                    for (CurricularCourse course : curricularCourses) {
                        List<CurricularCourseScope> scopes = course.getScopes();
                        for (CurricularCourseScope courseScope : scopes) {
                            if (courseScope.getCurricularSemester().getCurricularYear().getYear()
                                    .equals(curricularYear)
                                    && courseScope.getCurricularSemester().getSemester().equals(
                                            executionPeriodSemestre)) {
                                executionCourses.addAll(course.getAssociatedExecutionCourses());
                            }
                        }
                    }
                }
            }
        }
        return executionCourses;
    }
}
