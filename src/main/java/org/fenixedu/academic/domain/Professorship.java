/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;

import pt.ist.fenixframework.Atomic;

/**
 * @author João Mota
 */
public class Professorship extends Professorship_Base {

    public static final Comparator<Professorship> COMPARATOR_BY_PERSON_NAME =
            new BeanComparator("person.name", Collator.getInstance());

    public static final String PROFESSORSHIP_CREATED = "academic.professorship.created";

    public Professorship() {
        super();
        setRootDomainObject(Bennu.getInstance());
        new ProfessorshipPermissions(this);
    }

    public boolean belongsToExecutionInterval(ExecutionInterval executionInterval) {
        return this.getExecutionCourse().getExecutionInterval().equals(executionInterval);
    }

    /**
     * @deprecated Use {@link #belongsToExecutionInterval(ExecutionInterval)
     */
    @Deprecated
    public boolean belongsToExecutionPeriod(ExecutionInterval executionInterval) {
        return this.getExecutionCourse().getExecutionInterval().equals(executionInterval);
    }

    @Atomic
    public static Professorship create(Boolean responsibleFor, ExecutionCourse executionCourse, Person person) {

        Objects.requireNonNull(responsibleFor);
        Objects.requireNonNull(executionCourse);
        Objects.requireNonNull(person);

        if (executionCourse.getProfessorshipsSet().stream().anyMatch(p -> person.equals(p.getPerson()))) {
            throw new DomainException("error.teacher.already.associated.to.professorship");
        }

        Professorship professorShip = new Professorship();
        professorShip.setExecutionCourse(executionCourse);
        professorShip.setPerson(person);
        professorShip.setCreator(Authenticate.getUser().getPerson());

        professorShip.setResponsibleFor(responsibleFor);

        if (person.getTeacher() != null) {
            executionCourse.getAssociatedSummariesSet().stream()
                    .filter(s -> s.getTeacher() != null && s.getTeacher().equals(person.getTeacher()))
                    .forEach(s -> s.moveFromTeacherToProfessorship(professorShip));
        }

        Signal.emit(PROFESSORSHIP_CREATED, new DomainObjectEvent<>(professorShip));
        ProfessorshipManagementLog.createLog(professorShip.getExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.professorship.added", professorShip.getPerson().getPresentationName(),
                professorShip.getExecutionCourse().getNome(), professorShip.getExecutionCourse().getDegreePresentationString());
        return professorShip;
    }

    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        ProfessorshipManagementLog.createLog(getExecutionCourse(), Bundle.MESSAGING, "log.executionCourse.professorship.removed",
                getPerson().getPresentationName(), getExecutionCourse().getNome(),
                getExecutionCourse().getDegreePresentationString());
        setExecutionCourse(null);
        setPerson(null);
        if (super.getPermissions() != null) {
            getPermissions().delete();
        }
        setRootDomainObject(null);
        setCreator(null);
        deleteDomainObject();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getAssociatedSummariesSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.remove.professorship.hasAnyAssociatedSummaries"));
        }
        if (!getAssociatedShiftProfessorshipSet().isEmpty()) {
            blockers.add(
                    BundleUtil.getString(Bundle.APPLICATION, "error.remove.professorship.hasAnyAssociatedShiftProfessorship"));
        }
    }

    public boolean isDeletable() {
        return getDeletionBlockers().isEmpty();
    }

    public static List<Professorship> readByDegreeCurricularPlanAndExecutionYear(DegreeCurricularPlan degreeCurricularPlan,
            ExecutionYear executionYear) {

        Set<Professorship> professorships = new HashSet<Professorship>();
        for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
            for (ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionYear(executionYear)) {
                professorships.addAll(executionCourse.getProfessorshipsSet());
            }
        }
        return new ArrayList<Professorship>(professorships);
    }

//    public static List<Professorship> readByDegreeCurricularPlanAndExecutionYearAndBasic(
//            DegreeCurricularPlan degreeCurricularPlan, ExecutionYear executionYear, Boolean basic) {
//
//        Set<Professorship> professorships = new HashSet<Professorship>();
//        for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
//            if (curricularCourse.getBasic().equals(basic)) {
//                for (ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionYear(executionYear)) {
//                    professorships.addAll(executionCourse.getProfessorshipsSet());
//                }
//            }
//        }
//        return new ArrayList<Professorship>(professorships);
//    }

    public static List<Professorship> readByDegreeCurricularPlanAndExecutionPeriod(DegreeCurricularPlan degreeCurricularPlan,
            ExecutionInterval executionInterval) {

        Set<Professorship> professorships = new HashSet<Professorship>();
        for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
            for (ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionPeriod(executionInterval)) {
                professorships.addAll(executionCourse.getProfessorshipsSet());
            }
        }
        return new ArrayList<Professorship>(professorships);
    }

//    public static List<Professorship> readByDegreeCurricularPlansAndExecutionYearAndBasic(
//            List<DegreeCurricularPlan> degreeCurricularPlans, ExecutionYear executionYear, Boolean basic) {
//
//        Set<Professorship> professorships = new HashSet<Professorship>();
//        for (DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
//            for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
//                if (curricularCourse.getBasic() == null || curricularCourse.getBasic().equals(basic)) {
//                    if (executionYear != null) {
//                        for (ExecutionCourse executionCourse : curricularCourse
//                                .getExecutionCoursesByExecutionYear(executionYear)) {
//                            professorships.addAll(executionCourse.getProfessorshipsSet());
//                        }
//                    } else {
//                        for (ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
//                            professorships.addAll(executionCourse.getProfessorshipsSet());
//                        }
//                    }
//                }
//            }
//        }
//        return new ArrayList<Professorship>(professorships);
//    }

    public static List<Professorship> readByDegreeCurricularPlansAndExecutionYear(
            List<DegreeCurricularPlan> degreeCurricularPlans, ExecutionYear executionYear) {

        Set<Professorship> professorships = new HashSet<Professorship>();
        for (DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
            for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
                if (executionYear != null) {
                    for (ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionYear(executionYear)) {
                        professorships.addAll(executionCourse.getProfessorshipsSet());
                    }
                } else {
                    for (ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
                        professorships.addAll(executionCourse.getProfessorshipsSet());
                    }
                }
            }
        }
        return new ArrayList<Professorship>(professorships);
    }

    public Teacher getTeacher() {
        return getPerson().getTeacher();
    }

    public void setTeacher(Teacher teacher) {
        setPerson(teacher.getPerson());
    }

    public boolean isResponsibleFor() {
        return getResponsibleFor().booleanValue();
    }

    public void setResponsibleFor(boolean responsibleFor) {
        super.setResponsibleFor(responsibleFor);
    }

    @Override
    public void setResponsibleFor(Boolean responsibleFor) {
        if (responsibleFor == null) {
            responsibleFor = Boolean.FALSE;
        }
        super.setResponsibleFor(responsibleFor);
    }

    public boolean hasTeacher() {
        return getPerson() != null && getPerson().getTeacher() != null;
    }

    public void removeTeacher() {
        setPerson(null);
    }

    public String getDegreeSiglas() {
        Set<String> degreeSiglas = new HashSet<String>();
        for (CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCoursesSet()) {
            degreeSiglas.add(curricularCourse.getDegreeCurricularPlan().getDegree().getSigla());
        }
        return StringUtils.join(degreeSiglas, ", ");
    }

    public String getDegreePlanNames() {
        Set<String> degreeSiglas = new HashSet<String>();
        for (CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCoursesSet()) {
            degreeSiglas.add(curricularCourse.getDegreeCurricularPlan().getName());
        }
        return StringUtils.join(degreeSiglas, ", ");
    }
}
