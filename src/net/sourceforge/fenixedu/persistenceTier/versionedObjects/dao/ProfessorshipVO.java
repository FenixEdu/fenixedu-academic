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

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
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
        final Collection<ITeacher> teachers = readAll(Teacher.class);
        for (final ITeacher teacher : teachers) {
            if (teacher.getTeacherNumber().equals(teacherNumber))
                return teacher.getProfessorships();
        }
        return new ArrayList();
    }

    public List readByTeacher(final Integer teacherID) throws ExcepcaoPersistencia {
        final ITeacher teacher = (ITeacher) readByOID(Teacher.class, teacherID);
        return (teacher != null) ? teacher.getProfessorships() : new ArrayList();
    }

    public IProfessorship readByTeacherAndExecutionCourse(final Integer teacherID,
            final Integer executionCourseID) throws ExcepcaoPersistencia {

        final IExecutionCourse executionCourse = (IExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseID);
        if (executionCourse != null) {
            final List<IProfessorship> associatedProfessorships = executionCourse.getProfessorships();
            for (final IProfessorship professorship : associatedProfessorships) {
                if (professorship.getTeacher().getIdInternal().equals(teacherID))
                    return professorship;
            }
        }
        return null;
    }

    public List readByExecutionCourse(final Integer executionCourseID) throws ExcepcaoPersistencia {
        final IExecutionCourse executionCourse = (IExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseID);
        return (executionCourse != null) ? executionCourse.getProfessorships() : new ArrayList();
    }

    public List readByTeacherAndExecutionPeriod(final Integer teacherID, final Integer executionPeriodID)
            throws ExcepcaoPersistencia {

        final List<IProfessorship> result = new ArrayList<IProfessorship>();
        final ITeacher teacher = (ITeacher) readByOID(Teacher.class, teacherID);
        if (teacher != null) {
            final List<IProfessorship> associatedProfessorships = teacher.getProfessorships();
            for (final IProfessorship professorship : associatedProfessorships) {
                if (professorship.getExecutionCourse().getExecutionPeriod().getIdInternal().equals(
                        executionPeriodID))
                    result.add(professorship);
            }
        }
        return result;
    }

    public List readByTeacherAndExecutionYear(final Integer teacherID, final Integer executionYearID)
            throws ExcepcaoPersistencia {

        final List<IProfessorship> result = new ArrayList<IProfessorship>();
        final ITeacher teacher = (ITeacher) readByOID(Teacher.class, teacherID);
        if (teacher != null) {
            final List<IProfessorship> associatedProfessorships = teacher.getProfessorships();
            for (final IProfessorship professorship : associatedProfessorships) {
                if (professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear()
                        .getIdInternal().equals(executionYearID))
                    result.add(professorship);
            }
        }
        return result;
    }

    public List readByDegreeCurricularPlansAndExecutionYearAndBasic(final List degreeCurricularPlanIDs,
            final Integer executionYearID, final Boolean basic) throws ExcepcaoPersistencia {

        final List<IProfessorship> result = new ArrayList<IProfessorship>();
        final Set<Integer> professorShipsIDs = new HashSet<Integer>();

        if (degreeCurricularPlanIDs != null) {
            for (final Integer degreeCurricularPlanID : (List<Integer>) degreeCurricularPlanIDs) {
                final IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) readByOID(
                        DegreeCurricularPlan.class, degreeCurricularPlanID);
                if (degreeCurricularPlan != null) {
                    final List<ICurricularCourse> curricularCourses = degreeCurricularPlan
                            .getCurricularCourses();
                    for (final ICurricularCourse curricularCourse : curricularCourses) {
                        if (curricularCourse.getBasic().equals(basic)) {
                            final List<IExecutionCourse> executionCourses = curricularCourse
                                    .getAssociatedExecutionCourses();
                            for (final IExecutionCourse executionCourse : executionCourses) {
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

        final List<IProfessorship> result = new ArrayList<IProfessorship>();
        final Set<Integer> professorShipsIDs = new HashSet<Integer>();

        final IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID);

        if (degreeCurricularPlan != null) {
            final List<ICurricularCourse> curricularCourses = degreeCurricularPlan
                    .getCurricularCourses();
            for (final ICurricularCourse curricularCourse : curricularCourses) {
                final List<IExecutionCourse> executionCourses = curricularCourse
                        .getAssociatedExecutionCourses();
                for (final IExecutionCourse executionCourse : executionCourses) {
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

        final List<IProfessorship> result = new ArrayList<IProfessorship>();
        final Set<Integer> professorShipsIDs = new HashSet<Integer>();

        final IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID);
        if (degreeCurricularPlan != null) {
            final List<ICurricularCourse> curricularCourses = degreeCurricularPlan
                    .getCurricularCourses();
            for (final ICurricularCourse curricularCourse : curricularCourses) {
                if (curricularCourse.getBasic().equals(basic)) {
                    final List<IExecutionCourse> executionCourses = curricularCourse
                            .getAssociatedExecutionCourses();
                    for (final IExecutionCourse executionCourse : executionCourses) {
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

        final List<IProfessorship> result = new ArrayList<IProfessorship>();
        final Set<Integer> professorShipsIDs = new HashSet<Integer>();

        if (degreeCurricularPlanIDs != null) {
            for (final Integer degreeCurricularPlanID : (List<Integer>) degreeCurricularPlanIDs) {
                final IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) readByOID(
                        DegreeCurricularPlan.class, degreeCurricularPlanID);
                if (degreeCurricularPlan != null) {
                    final List<ICurricularCourse> curricularCourses = degreeCurricularPlan
                            .getCurricularCourses();
                    for (final ICurricularCourse curricularCourse : curricularCourses) {
                        final List<IExecutionCourse> executionCourses = curricularCourse
                                .getAssociatedExecutionCourses();
                        for (final IExecutionCourse executionCourse : executionCourses) {
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

        final List<IProfessorship> result = new ArrayList<IProfessorship>();
        final Set<Integer> professorShipsIDs = new HashSet<Integer>();

        final IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID);
        if (degreeCurricularPlan != null) {
            final List<ICurricularCourse> curricularCourses = degreeCurricularPlan
                    .getCurricularCourses();
            for (final ICurricularCourse curricularCourse : curricularCourses) {
                final List<IExecutionCourse> executionCourses = curricularCourse
                        .getAssociatedExecutionCourses();
                for (final IExecutionCourse executionCourse : executionCourses) {
                    if (executionCourse.getExecutionPeriod().getIdInternal().equals(executionPeriodID)) {
                        addProfessorships(executionCourse, result, professorShipsIDs);
                    }
                }
            }
        }
        return result;
    }

    private void addProfessorships(final IExecutionCourse executionCourse, List<IProfessorship> result,
            Set<Integer> professorShipsIDs) {
        final List<IProfessorship> professorships = executionCourse.getProfessorships();
        for (final IProfessorship professorship : professorships) {
            if (!professorShipsIDs.contains((professorship.getIdInternal()))) {
                professorShipsIDs.add(professorship.getIdInternal());
                result.add(professorship);
            }
        }
    }
}