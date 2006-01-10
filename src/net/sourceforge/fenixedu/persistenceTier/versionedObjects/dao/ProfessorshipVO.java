/*
 * Created on May 20, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author jdnf
 *  
 */
public class ProfessorshipVO extends VersionedObjectsBase implements IPersistentProfessorship {

    public List readByTeacherNumber(final Integer teacherNumber) throws ExcepcaoPersistencia {
        final Collection<Teacher> teachers = readAll(Teacher.class);
        for (final Teacher teacher : teachers) {
            if (teacher.getTeacherNumber().equals(teacherNumber))
                return teacher.getProfessorships();
        }
        return new ArrayList();
    }

    public List readByTeacher(final Integer teacherID) throws ExcepcaoPersistencia {
        final Teacher teacher = (Teacher) readByOID(Teacher.class, teacherID);
        return (teacher != null) ? teacher.getProfessorships() : new ArrayList();
    }

    public Professorship readByTeacherAndExecutionCourse(final Integer teacherID,
            final Integer executionCourseID) throws ExcepcaoPersistencia {

        final ExecutionCourse executionCourse = (ExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseID);
        if (executionCourse != null) {
            final List<Professorship> associatedProfessorships = executionCourse.getProfessorships();
            for (final Professorship professorship : associatedProfessorships) {
                if (professorship.getTeacher().getIdInternal().equals(teacherID))
                    return professorship;
            }
        }
        return null;
    }

    public List readByExecutionCourse(final Integer executionCourseID) throws ExcepcaoPersistencia {
        final ExecutionCourse executionCourse = (ExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseID);
        return (executionCourse != null) ? executionCourse.getProfessorships() : new ArrayList();
    }

    public List readByTeacherAndExecutionPeriod(final Integer teacherID, final Integer executionPeriodID)
            throws ExcepcaoPersistencia {

        final List<Professorship> result = new ArrayList<Professorship>();
        final Teacher teacher = (Teacher) readByOID(Teacher.class, teacherID);
        if (teacher != null) {
            final List<Professorship> associatedProfessorships = teacher.getProfessorships();
            for (final Professorship professorship : associatedProfessorships) {
                if (professorship.getExecutionCourse().getExecutionPeriod().getIdInternal().equals(
                        executionPeriodID))
                    result.add(professorship);
            }
        }
        return result;
    }

    public List readByTeacherAndExecutionYear(final Integer teacherID, final Integer executionYearID)
            throws ExcepcaoPersistencia {

        final List<Professorship> result = new ArrayList<Professorship>();
        final Teacher teacher = (Teacher) readByOID(Teacher.class, teacherID);
        if (teacher != null) {
            final List<Professorship> associatedProfessorships = teacher.getProfessorships();
            for (final Professorship professorship : associatedProfessorships) {
                if (professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear()
                        .getIdInternal().equals(executionYearID))
                    result.add(professorship);
            }
        }
        return result;
    }

    public List readByDegreeCurricularPlansAndExecutionYearAndBasic(final List degreeCurricularPlanIDs,
            final Integer executionYearID, final Boolean basic) throws ExcepcaoPersistencia {

        final List<Professorship> result = new ArrayList<Professorship>();
        final Set<Integer> professorShipsIDs = new HashSet<Integer>();

        if (degreeCurricularPlanIDs != null) {
            for (final Integer degreeCurricularPlanID : (List<Integer>) degreeCurricularPlanIDs) {
                final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) readByOID(
                        DegreeCurricularPlan.class, degreeCurricularPlanID);
                if (degreeCurricularPlan != null) {
                    final List<CurricularCourse> curricularCourses = degreeCurricularPlan
                            .getCurricularCourses();
                    for (final CurricularCourse curricularCourse : curricularCourses) {
                        if (curricularCourse.getBasic().equals(basic)) {
                            final List<ExecutionCourse> executionCourses = curricularCourse
                                    .getAssociatedExecutionCourses();
                            for (final ExecutionCourse executionCourse : executionCourses) {
                                if (executionYearID != null) {
                                    if (executionCourse.getExecutionPeriod().getExecutionYear()
                                            .getIdInternal().equals(executionYearID)) {
                                        addProfessorships(executionCourse, result, professorShipsIDs);
                                    }
                                } else {
                                    addProfessorships(executionCourse, result, professorShipsIDs);
                                }

                            }
                        }
                    }

                }
            }
        }
        return result;
    }

    public List readByDegreeCurricularPlanAndExecutionYear(final Integer degreeCurricularPlanID,
            final Integer executionYearID) throws ExcepcaoPersistencia {

        final List<Professorship> result = new ArrayList<Professorship>();
        final Set<Integer> professorShipsIDs = new HashSet<Integer>();

        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID);

        if (degreeCurricularPlan != null) {
            final List<CurricularCourse> curricularCourses = degreeCurricularPlan
                    .getCurricularCourses();
            for (final CurricularCourse curricularCourse : curricularCourses) {
                final List<ExecutionCourse> executionCourses = curricularCourse
                        .getAssociatedExecutionCourses();
                for (final ExecutionCourse executionCourse : executionCourses) {
                    if (executionCourse.getExecutionPeriod().getExecutionYear().getIdInternal().equals(
                            executionYearID)) {
                        addProfessorships(executionCourse, result, professorShipsIDs);
                    }
                }
            }
        }
        return result;
    }

    public List readByDegreeCurricularPlanAndBasic(final Integer degreeCurricularPlanID,
            final Integer executionYearID, final Boolean basic) throws ExcepcaoPersistencia {

        final List<Professorship> result = new ArrayList<Professorship>();
        final Set<Integer> professorShipsIDs = new HashSet<Integer>();

        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID);
        if (degreeCurricularPlan != null) {
            final List<CurricularCourse> curricularCourses = degreeCurricularPlan
                    .getCurricularCourses();
            for (final CurricularCourse curricularCourse : curricularCourses) {
                if (curricularCourse.getBasic().equals(basic)) {
                    final List<ExecutionCourse> executionCourses = curricularCourse
                            .getAssociatedExecutionCourses();
                    for (final ExecutionCourse executionCourse : executionCourses) {
                        if (executionCourse.getExecutionPeriod().getExecutionYear().getIdInternal()
                                .equals(executionYearID)) {
                            addProfessorships(executionCourse, result, professorShipsIDs);
                        }
                    }
                }
            }
        }

        return result;
    }

    public List readByDegreeCurricularPlansAndExecutionYear(final List degreeCurricularPlanIDs,
            final Integer executionYearID) throws ExcepcaoPersistencia {

        final List<Professorship> result = new ArrayList<Professorship>();
        final Set<Integer> professorShipsIDs = new HashSet<Integer>();

        if (degreeCurricularPlanIDs != null) {
            for (final Integer degreeCurricularPlanID : (List<Integer>) degreeCurricularPlanIDs) {
                final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) readByOID(
                        DegreeCurricularPlan.class, degreeCurricularPlanID);
                if (degreeCurricularPlan != null) {
                    final List<CurricularCourse> curricularCourses = degreeCurricularPlan
                            .getCurricularCourses();
                    for (final CurricularCourse curricularCourse : curricularCourses) {
                        final List<ExecutionCourse> executionCourses = curricularCourse
                                .getAssociatedExecutionCourses();
                        for (final ExecutionCourse executionCourse : executionCourses) {
                            if (executionYearID != null) {
                                if (executionCourse.getExecutionPeriod().getExecutionYear()
                                        .getIdInternal().equals(executionYearID)) {
                                    addProfessorships(executionCourse, result, professorShipsIDs);
                                }
                            } else {
                                addProfessorships(executionCourse, result, professorShipsIDs);
                            }

                        }
                    }

                }
            }
        }
        return result;
    }

    public List readByDegreeCurricularPlanAndExecutionPeriod(final Integer degreeCurricularPlanID,
            final Integer executionPeriodID) throws ExcepcaoPersistencia {

        final List<Professorship> result = new ArrayList<Professorship>();
        final Set<Integer> professorShipsIDs = new HashSet<Integer>();

        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID);
        if (degreeCurricularPlan != null) {
            final List<CurricularCourse> curricularCourses = degreeCurricularPlan
                    .getCurricularCourses();
            for (final CurricularCourse curricularCourse : curricularCourses) {
                final List<ExecutionCourse> executionCourses = curricularCourse
                        .getAssociatedExecutionCourses();
                for (final ExecutionCourse executionCourse : executionCourses) {
                    if (executionCourse.getExecutionPeriod().getIdInternal().equals(executionPeriodID)) {
                        addProfessorships(executionCourse, result, professorShipsIDs);
                    }
                }
            }
        }
        return result;
    }

    private void addProfessorships(final ExecutionCourse executionCourse, List<Professorship> result,
            Set<Integer> professorShipsIDs) {
        final List<Professorship> professorships = executionCourse.getProfessorships();
        for (final Professorship professorship : professorships) {
            if (!professorShipsIDs.contains((professorship.getIdInternal()))) {
                professorShipsIDs.add(professorship.getIdInternal());
                result.add(professorship);
            }
        }
    }
}