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
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.services.teacher.professorship.ResponsibleForValidator;
import org.fenixedu.academic.service.services.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import org.fenixedu.academic.service.services.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;

/**
 * @author João Mota
 */
public class Professorship extends Professorship_Base {

    public static final Comparator<Professorship> COMPARATOR_BY_PERSON_NAME = new BeanComparator("person.name",
            Collator.getInstance());

    public Professorship() {
        super();
        setRootDomainObject(Bennu.getInstance());
        new ProfessorshipPermissions(this);
    }

    public boolean belongsToExecutionPeriod(ExecutionSemester executionSemester) {
        return this.getExecutionCourse().getExecutionPeriod().equals(executionSemester);
    }

    public static Professorship create(Boolean responsibleFor, ExecutionCourse executionCourse, Teacher teacher, Double hours)
            throws MaxResponsibleForExceed, InvalidCategory {

        for (final Professorship otherProfessorship : executionCourse.getProfessorshipsSet()) {
            if (teacher == otherProfessorship.getTeacher()) {
                throw new DomainException("error.teacher.already.associated.to.professorship");
            }
        }

        if (responsibleFor == null || executionCourse == null || teacher == null) {
            throw new NullPointerException();
        }

        Professorship professorShip = new Professorship();
        professorShip.setHours((hours == null) ? new Double(0.0) : hours);
        professorShip.setExecutionCourse(executionCourse);
        professorShip.setPerson(teacher.getPerson());
        professorShip.setCreator(AccessControl.getPerson());

        professorShip.setResponsibleFor(responsibleFor);
        executionCourse.moveSummariesFromTeacherToProfessorship(teacher, professorShip);

        return professorShip;
    }

    @Atomic
    public static Professorship create(Boolean responsibleFor, ExecutionCourse executionCourse, Person person, Double hours)
            throws MaxResponsibleForExceed, InvalidCategory {

        for (final Professorship otherProfessorship : executionCourse.getProfessorshipsSet()) {
            if (person == otherProfessorship.getPerson()) {
                throw new DomainException("error.teacher.already.associated.to.professorship");
            }
        }

        if (responsibleFor == null || executionCourse == null || person == null) {
            throw new NullPointerException();
        }

        Professorship professorShip = new Professorship();
        professorShip.setHours((hours == null) ? new Double(0.0) : hours);
        professorShip.setExecutionCourse(executionCourse);
        professorShip.setPerson(person);
        professorShip.setCreator(AccessControl.getPerson());

        if (responsibleFor.booleanValue() && professorShip.getPerson().getTeacher() != null) {
            ResponsibleForValidator.getInstance().validateResponsibleForList(professorShip.getPerson().getTeacher(),
                    professorShip.getExecutionCourse(), professorShip);
            professorShip.setResponsibleFor(Boolean.TRUE);
        } else {
            professorShip.setResponsibleFor(Boolean.FALSE);
        }
        if (person.getTeacher() != null) {
            executionCourse.moveSummariesFromTeacherToProfessorship(person.getTeacher(), professorShip);
        }

        ProfessorshipManagementLog.createLog(professorShip.getExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.professorship.added", professorShip.getPerson().getPresentationName(), professorShip
                        .getExecutionCourse().getNome(), professorShip.getExecutionCourse().getDegreePresentationString());
        return professorShip;
    }

    public void delete() {
        if (isDeletable()) {
            ProfessorshipManagementLog.createLog(getExecutionCourse(), Bundle.MESSAGING,
                    "log.executionCourse.professorship.removed", getPerson().getPresentationName(), getExecutionCourse()
                            .getNome(), getExecutionCourse().getDegreePresentationString());
            setExecutionCourse(null);
            setPerson(null);
            if (super.getPermissions() != null) {
                getPermissions().delete();
            }
            setRootDomainObject(null);
            setCreator(null);
            deleteDomainObject();
        }
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getAssociatedSummariesSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.remove.professorship.hasAnyAssociatedSummaries"));
        }
        if (!getAssociatedShiftProfessorshipSet().isEmpty()) {
            blockers.add(BundleUtil
                    .getString(Bundle.APPLICATION, "error.remove.professorship.hasAnyAssociatedShiftProfessorship"));
        }
    }

    public boolean isDeletable() {
        return getDeletionBlockers().isEmpty();
    }

    public boolean isResponsibleFor() {
        return getResponsibleFor().booleanValue();
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

    public static List<Professorship> readByDegreeCurricularPlanAndExecutionYearAndBasic(
            DegreeCurricularPlan degreeCurricularPlan, ExecutionYear executionYear, Boolean basic) {

        Set<Professorship> professorships = new HashSet<Professorship>();
        for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
            if (curricularCourse.getBasic().equals(basic)) {
                for (ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionYear(executionYear)) {
                    professorships.addAll(executionCourse.getProfessorshipsSet());
                }
            }
        }
        return new ArrayList<Professorship>(professorships);
    }

    public static List<Professorship> readByDegreeCurricularPlanAndExecutionPeriod(DegreeCurricularPlan degreeCurricularPlan,
            ExecutionSemester executionSemester) {

        Set<Professorship> professorships = new HashSet<Professorship>();
        for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
            for (ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester)) {
                professorships.addAll(executionCourse.getProfessorshipsSet());
            }
        }
        return new ArrayList<Professorship>(professorships);
    }

    public static List<Professorship> readByDegreeCurricularPlansAndExecutionYearAndBasic(
            List<DegreeCurricularPlan> degreeCurricularPlans, ExecutionYear executionYear, Boolean basic) {

        Set<Professorship> professorships = new HashSet<Professorship>();
        for (DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
            for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
                if (curricularCourse.getBasic() == null || curricularCourse.getBasic().equals(basic)) {
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
        }
        return new ArrayList<Professorship>(professorships);
    }

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
