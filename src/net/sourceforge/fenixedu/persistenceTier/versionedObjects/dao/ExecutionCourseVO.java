/*
 * Created on Jun 1, 2005
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularSemester;
import net.sourceforge.fenixedu.domain.ICurricularYear;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class ExecutionCourseVO extends VersionedObjectsBase implements IPersistentExecutionCourse {

    public List readByCurricularYearAndExecutionPeriodAndExecutionDegree(Integer anoCurricular,
            Integer executionPeriodSemestre, String degreeCurricularPlanName, String degreeSigla,
            Integer executionPeriodID) throws ExcepcaoPersistencia {

        final IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodID);
        final List<IExecutionCourse> executionCourses = executionPeriod.getAssociatedExecutionCourses();
        final List result = new ArrayList();
        for (final IExecutionCourse executionCourse : executionCourses) {
            final List<ICurricularCourse> curricularCourses = executionCourse
                    .getAssociatedCurricularCourses();
            if (hasCurricularCourseScopeAndDegreeCurricularPlan(curricularCourses,
                    degreeCurricularPlanName, degreeSigla, anoCurricular)) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    private boolean hasCurricularCourseScopeAndDegreeCurricularPlan(
            List<ICurricularCourse> curricularCourses, String degreeCurricularPlanName,
            String degreeSigla, Integer anoCurricular) {
        for (final ICurricularCourse curricularCourse : curricularCourses) {
            final IDegreeCurricularPlan degreeCurricularPlan = curricularCourse
                    .getDegreeCurricularPlan();
            final IDegree degree = degreeCurricularPlan.getDegree();
            if (degreeCurricularPlan.getName().equals(degreeCurricularPlanName)
                    && degree.getSigla().equals(degreeSigla)) {
                final List<ICurricularCourseScope> curricularCourseScopes = curricularCourse.getScopes();
                for (final ICurricularCourseScope curricularCourseScope : curricularCourseScopes) {
                    final ICurricularSemester curricularSemester = curricularCourseScope
                            .getCurricularSemester();
                    final ICurricularYear curricularYear = curricularSemester.getCurricularYear();
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

        final IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodID);
        final List<IExecutionCourse> executionCourses = executionPeriod.getAssociatedExecutionCourses();
        final List result = new ArrayList();
        for (final IExecutionCourse executionCourse : executionCourses) {
            final List<ICurricularCourse> curricularCourses = executionCourse
                    .getAssociatedCurricularCourses();
            if (hasDegreeCurricularPlan(curricularCourses, curricularPlanName, degreeSigla)) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    private boolean hasDegreeCurricularPlan(List<ICurricularCourse> curricularCourses,
            String curricularPlanName, String degreeSigla) {
        for (final ICurricularCourse curricularCourse : curricularCourses) {
            final IDegreeCurricularPlan degreeCurricularPlan = curricularCourse
                    .getDegreeCurricularPlan();
            final IDegree degree = degreeCurricularPlan.getDegree();
            if (degreeCurricularPlan.getName().equals(curricularPlanName)
                    && degree.getSigla().equals(degreeSigla)) {
                return true;
            }
        }
        return false;
    }

    public IExecutionCourse readByExecutionCourseInitialsAndExecutionPeriodId(String courseInitials,
            Integer executionPeriodId) throws ExcepcaoPersistencia {

        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodId);

        if (executionPeriod != null) {
            List<IExecutionCourse> executionCourses = executionPeriod.getAssociatedExecutionCourses();
            for (IExecutionCourse course : executionCourses) {
                if (course.getSigla().equals(courseInitials))
                    return course;
            }
        }
        return null;
    }

    public List readByExecutionPeriod(Integer executionPeriodID, DegreeType curso)
            throws ExcepcaoPersistencia {
        
        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodID);
        List executionCourses = new ArrayList();
        
        if (executionPeriod != null) {
            List<IExecutionCourse> executionCourses1 = executionPeriod.getAssociatedExecutionCourses();
            for (IExecutionCourse course : executionCourses1) {
                List<ICurricularCourse> curricularCourses = course.getAssociatedCurricularCourses();
                for (ICurricularCourse curricularCourse : curricularCourses) {
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

    public List readByExecutionPeriodAndExecutionDegreeAndCurricularYearAndName(
            Integer executionPeriodID, Integer degreeCurricularPlanID, Integer curricularYearID,
            String executionCourseName) throws ExcepcaoPersistencia {

        final IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodID);
        final List<IExecutionCourse> executionCourses = executionPeriod.getAssociatedExecutionCourses();
        final List result = new ArrayList();
        for (final IExecutionCourse executionCourse : executionCourses) {
            boolean addExecutionCourse = true;

            if (executionCourseName != null && !executionCourseName.equals("")
                    && !executionCourse.getNome().matches(executionCourseName.replaceAll("%", "*"))) {
                addExecutionCourse = false;
            }

            final List<ICurricularCourse> curricularCourses = executionCourse
                    .getAssociatedCurricularCourses();
            if (degreeCurricularPlanID != null
                    && !hasDegreeCurricularPlan(curricularCourses, degreeCurricularPlanID)) {
                addExecutionCourse = false;
            }

            if (curricularYearID != null
                    && !hasCurricularCourseYear(curricularCourses, curricularYearID)) {
                addExecutionCourse = false;
            }

            if (addExecutionCourse) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    private boolean hasCurricularCourseYear(List<ICurricularCourse> curricularCourses,
            Integer curricularYearID) {
        for (final ICurricularCourse curricularCourse : curricularCourses) {
            final List<ICurricularCourseScope> curricularCourseScopes = curricularCourse.getScopes();
            for (final ICurricularCourseScope curricularCourseScope : curricularCourseScopes) {
                final ICurricularSemester curricularSemester = curricularCourseScope
                        .getCurricularSemester();
                final ICurricularYear curricularYear = curricularSemester.getCurricularYear();
                if (curricularYear.getIdInternal().equals(curricularYearID)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasDegreeCurricularPlan(List<ICurricularCourse> curricularCourses,
            Integer degreeCurricularPlanID) {
        for (final ICurricularCourse curricularCourse : curricularCourses) {
            final IDegreeCurricularPlan degreeCurricularPlan = curricularCourse
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
        ICurricularCourse curricularCourse = (ICurricularCourse) readByOID(CurricularCourse.class,
                curricularCourseID);

        if (curricularCourse != null) {
            List<IExecutionCourse> executionCourses = curricularCourse.getAssociatedExecutionCourses();

            for (IExecutionCourse executionCourse : executionCourses) {
                if (executionCourse.getExecutionPeriod().getIdInternal().equals(executionPeriodID))
                    executionCoursesAux.add(executionCourse);
            }
        }
        return executionCoursesAux;
    }

    public List readByDegreeCurricularPlanAndExecutionYear(Integer degreeCurricularPlanID,
            Integer executionYearID) throws ExcepcaoPersistencia {

        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID);

        List<ICurricularCourse> curricularCourses = degreeCurricularPlan.getCurricularCourses();

        List executionCourses = new ArrayList();
        for (ICurricularCourse curricularCourse : curricularCourses) {
            List<IExecutionCourse> executionCoursesAux = curricularCourse
                    .getAssociatedExecutionCourses();
            for (IExecutionCourse executionCourse : executionCoursesAux) {
                if (executionCourse.getExecutionPeriod().getExecutionYear().getIdInternal().equals(executionYearID))
                    executionCourses.add(executionCourse);
            }
        }

        return executionCourses;
    }

    public List readByExecutionDegreeAndExecutionPeriod(Integer degreeCurricularPlanID,
            Integer executionPeriodID) throws ExcepcaoPersistencia {

        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID);

        List<ICurricularCourse> curricularCourses = degreeCurricularPlan.getCurricularCourses();

        List executionCourses = new ArrayList();
        for (ICurricularCourse curricularCourse : curricularCourses) {
            List<IExecutionCourse> executionCoursesAux = curricularCourse
                    .getAssociatedExecutionCourses();
            for (IExecutionCourse executionCourse : executionCoursesAux) {
                if (executionCourse.getExecutionPeriod().getIdInternal().equals(executionPeriodID))
                    executionCourses.add(executionCourse);
            }
        }

        return executionCourses;
    }

    public List readByExecutionCourseIds(List<Integer> executionCourseIds) throws ExcepcaoPersistencia {

        List executionCourses = new ArrayList();

        for (Integer executionCourseID : executionCourseIds) {
            IExecutionCourse executionCourse = (IExecutionCourse) readByOID(ExecutionCourse.class,
                    executionCourseID);
            executionCourses.add(executionCourse);
        }

        return executionCourses;
    }

    public List readByExecutionPeriodWithNoCurricularCourses(Integer executionPeriodID)
            throws ExcepcaoPersistencia {
        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodID);

        List executionCoursesFinal = new ArrayList();
        if (executionPeriod != null) {
            List<IExecutionCourse> executionCourses = executionPeriod.getAssociatedExecutionCourses();
            for (IExecutionCourse executionCourse : executionCourses) {
                if (executionCourse.getAssociatedCurricularCourses().isEmpty())
                    executionCoursesFinal.add(executionCourse);
            }
        }
        return executionCoursesFinal;
    }

    public List readByCurricularYearAndAllExecutionPeriodAndExecutionDegree(Integer curricularYear,
            Integer executionPeriodID, String degreeCurricularPlanName,
            String degreeSigla) throws ExcepcaoPersistencia {
        
        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodID);

        List executionCourses = new ArrayList();
        if (executionPeriod != null) {
            List<IExecutionDegree> executionDegrees = executionPeriod.getExecutionYear()
                    .getExecutionDegrees();

            for (IExecutionDegree degree : executionDegrees) {
                if (degree.getDegreeCurricularPlan().getName().equals(degreeCurricularPlanName)
                        && degree.getDegreeCurricularPlan().getDegree().getSigla().equals(degreeSigla)) {
                    List<ICurricularCourse> curricularCourses = degree.getDegreeCurricularPlan()
                            .getCurricularCourses();
                    for (ICurricularCourse course : curricularCourses) {
                        List<ICurricularCourseScope> scopes = course.getScopes();
                        for (ICurricularCourseScope courseScope : scopes) {
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
            List<IExecutionDegree> executionDegreeList) throws ExcepcaoPersistencia {
        IExecutionPeriod executionPeriod = (IExecutionPeriod) readByOID(ExecutionPeriod.class,
                executionPeriodID);
        List executionCourses = new ArrayList();

        List curricularPlanNames = new ArrayList();
        List degreeSiglas = new ArrayList();
        for (IExecutionDegree executionDegree : executionDegreeList) {
            curricularPlanNames.add(executionDegree.getDegreeCurricularPlan().getName());
            degreeSiglas.add(executionDegree.getDegreeCurricularPlan().getDegree().getSigla());
        }

        if (executionPeriod != null) {
            List<IExecutionDegree> executionDegrees = executionPeriod.getExecutionYear()
                    .getExecutionDegrees();
            for (IExecutionDegree degree : executionDegrees) {
                if (curricularPlanNames.contains(degree.getDegreeCurricularPlan().getName())
                        && degreeSiglas
                                .contains(degree.getDegreeCurricularPlan().getDegree().getSigla())) {
                    List<ICurricularCourse> curricularCourses = degree.getDegreeCurricularPlan()
                            .getCurricularCourses();
                    for (ICurricularCourse course : curricularCourses) {
                        List<ICurricularCourseScope> scopes = course.getScopes();
                        for (ICurricularCourseScope courseScope : scopes) {
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
