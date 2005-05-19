/*
 * Created on May 18, 2005
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
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author jdnf
 *  
 */
public class ResponsibleForVO extends VersionedObjectsBase implements IPersistentResponsibleFor {

    public List readByTeacher(Integer teacherNumber) throws ExcepcaoPersistencia {
        final Collection<ITeacher> teachers = readAll(Teacher.class);
        for (final ITeacher teacher : teachers) {
            if (teacher.getTeacherNumber().equals(teacherNumber))
                return teacher.getAssociatedResponsibles();
        }
        return new ArrayList();
    }

    public IResponsibleFor readByTeacherAndExecutionCourse(Integer teacherID, Integer executionCourseID)
            throws ExcepcaoPersistencia {

        final IExecutionCourse executionCourse = (IExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseID);
        final List<IResponsibleFor> responsibleTeachers = executionCourse.getResponsibleTeachers();
        for (IResponsibleFor responsibleFor : responsibleTeachers) {
            if (responsibleFor.getTeacher().getIdInternal().equals(teacherID))
                return responsibleFor;
        }
        return null;
    }

    public List readByExecutionCourse(Integer executionCourseID) throws ExcepcaoPersistencia {
        final IExecutionCourse executionCourse = (IExecutionCourse) readByOID(ExecutionCourse.class,
                executionCourseID);
        return (executionCourse != null) ? executionCourse.getResponsibleTeachers() : new ArrayList();
    }

    public List readByTeacherAndExecutionPeriod(Integer teacherID, Integer executionPeriodID)
            throws ExcepcaoPersistencia {

        final List<IResponsibleFor> responsibleTeachers = new ArrayList<IResponsibleFor>();
        final ITeacher teacher = (ITeacher) readByOID(Teacher.class, teacherID);

        if (teacher != null) {
            final Collection<IResponsibleFor> associatedResponsibles = teacher
                    .getAssociatedResponsibles();
            for (final IResponsibleFor responsibleFor : associatedResponsibles) {
                if (responsibleFor.getExecutionCourse().getExecutionPeriod().getIdInternal().equals(
                        executionPeriodID))
                    responsibleTeachers.add(responsibleFor);
            }
        }
        return responsibleTeachers;
    }

    public List readByTeacherAndExecutionYear(Integer teacherID, Integer executionYearID)
            throws ExcepcaoPersistencia {

        final List<IResponsibleFor> responsibleTeachers = new ArrayList<IResponsibleFor>();
        final ITeacher teacher = (ITeacher) readByOID(Teacher.class, teacherID);

        if (teacher != null) {
            final Collection<IResponsibleFor> associatedResponsibles = teacher
                    .getAssociatedResponsibles();
            for (final IResponsibleFor responsibleFor : associatedResponsibles) {
                if (responsibleFor.getExecutionCourse().getExecutionPeriod().getExecutionYear()
                        .getIdInternal().equals(executionYearID))
                    responsibleTeachers.add(responsibleFor);
            }
        }
        return responsibleTeachers;
    }

    public List readByTeacherAndExecutionCourseIds(Integer teacherID, List executionCourseIds)
            throws ExcepcaoPersistencia {

        final List<IResponsibleFor> responsibleTeachers = new ArrayList<IResponsibleFor>();
        final ITeacher teacher = (ITeacher) readByOID(Teacher.class, teacherID);

        if (teacher != null) {
            final List<IResponsibleFor> associatedResponsibles = teacher.getAssociatedResponsibles();
            for (final IResponsibleFor responsibleFor : associatedResponsibles) {
                if (executionCourseIds.contains(responsibleFor.getExecutionCourse().getIdInternal()))
                    responsibleTeachers.add(responsibleFor);
            }
        }
        return responsibleTeachers;
    }

    public List readByExecutionDegree(Integer degreeCurricularPlanID, Integer executionYearID)
            throws ExcepcaoPersistencia {

        final List<IResponsibleFor> result = new ArrayList<IResponsibleFor>();
        final Set<Integer> responsibleTeacherIDs = new HashSet<Integer>();

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
                        final List<IResponsibleFor> responsibleFors = executionCourse
                                .getResponsibleTeachers();
                        for (final IResponsibleFor responsibleFor : responsibleFors) {
                            if (!responsibleTeacherIDs.contains(responsibleFor.getIdInternal())) {
                                responsibleTeacherIDs.add(responsibleFor.getIdInternal());
                                result.add(responsibleFor);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
