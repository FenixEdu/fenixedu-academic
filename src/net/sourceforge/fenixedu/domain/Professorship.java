package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author Jo�o Mota
 */
public class Professorship extends Professorship_Base implements ICreditsEventOriginator {

    public Professorship() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public boolean belongsToExecutionPeriod(ExecutionPeriod executionPeriod) {
        return this.getExecutionCourse().getExecutionPeriod().equals(executionPeriod);
    }

    public static Professorship create(Boolean responsibleFor, ExecutionCourse executionCourse,
            Teacher teacher, Double hours) throws MaxResponsibleForExceed, InvalidCategory {
        if (responsibleFor == null || executionCourse == null || teacher == null)
            throw new NullPointerException();

        Professorship professorShip = new Professorship();
        professorShip.setHours((hours == null) ? new Double(0.0) : hours);

        if (responsibleFor.booleanValue()) {
            ResponsibleForValidator.getInstance().validateResponsibleForList(teacher, executionCourse,
                    professorShip);
        }
        professorShip.setResponsibleFor(responsibleFor);
        professorShip.setExecutionCourse(executionCourse);
        professorShip.setTeacher(teacher);

        return professorShip;
    }

    public void delete() {
        if (hasAnyAssociatedSummaries() || hasAnyAssociatedShiftProfessorship()
                || hasAnySupportLessons() || hasAnyDegreeTeachingServices()
                || hasAnyTeacherMasterDegreeServices()) {
            throw new DomainException("error.remove.professorship");
        }
        this.removeExecutionCourse();
        this.removeTeacher();
        super.deleteDomainObject();
    }
    
    public boolean isResponsibleFor() {
        return getResponsibleFor().booleanValue();
    }

    public static List<Professorship> readByDegreeCurricularPlanAndExecutionYear(
            DegreeCurricularPlan degreeCurricularPlan, ExecutionYear executionYear) {

        Set<Professorship> professorships = new HashSet<Professorship>();
        for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
            for (ExecutionCourse executionCourse : curricularCourse
                    .getExecutionCoursesByExecutionYear(executionYear)) {
                professorships.addAll(executionCourse.getProfessorships());
            }
        }
        return new ArrayList(professorships);
    }

    public static List<Professorship> readByDegreeCurricularPlanAndExecutionYearAndBasic(
            DegreeCurricularPlan degreeCurricularPlan, ExecutionYear executionYear, Boolean basic) {

        Set<Professorship> professorships = new HashSet<Professorship>();
        for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
            if (curricularCourse.getBasic().equals(basic)) {
                for (ExecutionCourse executionCourse : curricularCourse
                        .getExecutionCoursesByExecutionYear(executionYear)) {
                    professorships.addAll(executionCourse.getProfessorships());
                }
            }
        }
        return new ArrayList(professorships);
    }

    public static List<Professorship> readByDegreeCurricularPlanAndExecutionPeriod(
            DegreeCurricularPlan degreeCurricularPlan, ExecutionPeriod executionPeriod) {

        Set<Professorship> professorships = new HashSet<Professorship>();
        for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
            for (ExecutionCourse executionCourse : curricularCourse
                    .getExecutionCoursesByExecutionPeriod(executionPeriod)) {
                professorships.addAll(executionCourse.getProfessorships());
            }
        }
        return new ArrayList(professorships);
    }

    public static List<Professorship> readByDegreeCurricularPlansAndExecutionYearAndBasic(
            List<DegreeCurricularPlan> degreeCurricularPlans, ExecutionYear executionYear, Boolean basic) {

        Set<Professorship> professorships = new HashSet<Professorship>();
        for (DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
            for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
                if (curricularCourse.getBasic().equals(basic)) {
                    if (executionYear != null) {
                        for (ExecutionCourse executionCourse : curricularCourse
                                .getExecutionCoursesByExecutionYear(executionYear)) {
                            professorships.addAll(executionCourse.getProfessorships());
                        }
                    } else {
                        for (ExecutionCourse executionCourse : curricularCourse
                                .getAssociatedExecutionCourses()) {
                            professorships.addAll(executionCourse.getProfessorships());
                        }
                    }
                }
            }
        }
        return new ArrayList(professorships);
    }

    public static List<Professorship> readByDegreeCurricularPlansAndExecutionYear(
            List<DegreeCurricularPlan> degreeCurricularPlans, ExecutionYear executionYear) {

        Set<Professorship> professorships = new HashSet<Professorship>();
        for (DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
            for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
                if (executionYear != null) {
                    for (ExecutionCourse executionCourse : curricularCourse
                            .getExecutionCoursesByExecutionYear(executionYear)) {
                        professorships.addAll(executionCourse.getProfessorships());
                    }
                } else {
                    for (ExecutionCourse executionCourse : curricularCourse
                            .getAssociatedExecutionCourses()) {
                        professorships.addAll(executionCourse.getProfessorships());
                    }
                }
            }
        }
        return new ArrayList(professorships);
    }
}
