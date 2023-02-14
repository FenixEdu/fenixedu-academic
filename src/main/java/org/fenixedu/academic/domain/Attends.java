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
/*
 * Attends.java
 *
 * Created on 20 de Outubro de 2002, 14:42
 */

package org.fenixedu.academic.domain;

import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

/**
 *
 * @author tfc130
 */
public class Attends extends Attends_Base {

    public static enum StudentAttendsStateType {
        ENROLED, NOT_ENROLED, IMPROVEMENT, SPECIAL_SEASON;

        public String getQualifiedName() {
            return StudentAttendsStateType.class.getSimpleName() + "." + name();
        }
    }

    public static final Comparator<Attends> COMPARATOR_BY_STUDENT_NUMBER = new Comparator<Attends>() {

        @Override
        public int compare(Attends attends1, Attends attends2) {
            final Integer n1 = attends1.getRegistration().getStudent().getNumber();
            final Integer n2 = attends2.getRegistration().getStudent().getNumber();
            int res = n1.compareTo(n2);
            return res != 0 ? res : DomainObjectUtil.COMPARATOR_BY_ID.compare(attends1, attends2);
        }
    };

    public static final Comparator<Attends> ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME = new Comparator<Attends>() {

        @Override
        public int compare(Attends o1, Attends o2) {
            final ExecutionCourse executionCourse1 = o1.getExecutionCourse();
            final ExecutionCourse executionCourse2 = o2.getExecutionCourse();
            final int c = Collator.getInstance().compare(executionCourse1.getNome(), executionCourse2.getNome());
            return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
        }

    };

    protected Attends() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public Attends(Registration registration, ExecutionCourse executionCourse) {
        this();
        setRegistration(registration);
        setDisciplinaExecucao(executionCourse);
    }

    public void delete() throws DomainException {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        setAluno(null);
        setDisciplinaExecucao(null);
        setEnrolment(null);

        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (hasAnyShiftEnrolments()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.attends.cant.delete"));
        }
        if (!getAssociatedMarksSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.attends.cant.delete.has.associated.marks"));
        }
    }

    public boolean hasAnyShiftEnrolments() {
        for (Shift shift : this.getExecutionCourse().getAssociatedShifts()) {
            if (shift.getStudentsSet().contains(this.getRegistration())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return true if attends has an enrolment and at least one enrolment evaluation for this attends execution interval
     */
    public boolean isValid() {
        final Enrolment enrolment = getEnrolment();
        final ExecutionInterval executionInterval = getExecutionInterval();

        return enrolment != null && !enrolment.isAnnulled() && (enrolment.getExecutionInterval() == executionInterval
                || enrolment.getEvaluationsSet().stream().anyMatch(ee -> ee.getExecutionInterval() == executionInterval));
    }

    public boolean isFor(final ExecutionInterval interval) {
        return getExecutionCourse().getExecutionInterval() == interval;
    }

    public boolean isFor(final ExecutionCourse executionCourse) {
        return getExecutionCourse() == executionCourse;
    }

    public boolean isFor(final ExecutionYear executionYear) {
        return getExecutionCourse().getExecutionYear() == executionYear;
    }

    public boolean isFor(final Shift shift) {
        return isFor(shift.getExecutionCourse());
    }

    public boolean isFor(final Student student) {
        return getRegistration().getStudent().equals(student);
    }

    public boolean isFor(final Registration registration) {
        return getRegistration().equals(registration);
    }

    @Override
    @Deprecated
    public Registration getAluno() {
        return getRegistration();
    }

    public Registration getRegistration() {
        return super.getAluno();
    }

    @Override
    @Deprecated
    public void setAluno(Registration registration) {
        setRegistration(registration);
    }

    public boolean hasRegistration() {
        return super.getAluno() != null;
    }

    public void setRegistration(final Registration registration) {
        if (hasRegistration() && registration != null && getRegistration() != registration) {
            getRegistration().changeShifts(this, registration);
        }
        super.setAluno(registration);
    }

    @Override
    @Deprecated
    public ExecutionCourse getDisciplinaExecucao() {
        return getExecutionCourse();
    }

    public ExecutionCourse getExecutionCourse() {
        return super.getDisciplinaExecucao();
    }

    public ExecutionInterval getExecutionInterval() {
        return getExecutionCourse().getExecutionPeriod();
    }

    @Deprecated
    public ExecutionInterval getExecutionPeriod() {
        return getExecutionCourse().getExecutionPeriod();
    }

    public ExecutionYear getExecutionYear() {
        return getExecutionInterval().getExecutionYear();
    }

    public StudentCurricularPlan getStudentCurricularPlanFromAttends() {
        final Enrolment enrolment = getEnrolment();
        return enrolment == null ? getRegistration().getLastStudentCurricularPlan() : enrolment.getStudentCurricularPlan();
    }

    public StudentAttendsStateType getAttendsStateType() {
        if (getEnrolment() == null) {
            return StudentAttendsStateType.NOT_ENROLED;
        }

        if (!getEnrolment().getExecutionInterval().equals(getExecutionInterval())
                && getEnrolment().hasImprovementFor(getExecutionInterval())) {
            return StudentAttendsStateType.IMPROVEMENT;
        }

        if (getEnrolment().isValid(getExecutionInterval())) {
            if (getEnrolment().hasSpecialSeason()) {
                return StudentAttendsStateType.SPECIAL_SEASON;
            }
            return StudentAttendsStateType.ENROLED;
        }

        return null;
    }

    public boolean hasExecutionCourseTo(final DegreeCurricularPlan degreeCurricularPlan) {
        for (final CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCoursesSet()) {
            if (degreeCurricularPlan.hasDegreeModule(curricularCourse)) {
                return true;
            }
        }
        return false;
    }

    boolean canMove(final StudentCurricularPlan from, final StudentCurricularPlan to) {
        if (getEnrolment() != null) {
            return !from.hasEnrolments(getEnrolment()) && to.hasEnrolments(getEnrolment());
        }
        return !getExecutionInterval().isBefore(to.getStartExecutionInterval());
    }

}
