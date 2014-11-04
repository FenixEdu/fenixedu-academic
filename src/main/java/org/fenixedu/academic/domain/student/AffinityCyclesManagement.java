/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import pt.ist.fenixframework.Atomic;

public class AffinityCyclesManagement {
    private StudentCurricularPlan studentCurricularPlan;

    public AffinityCyclesManagement(final StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    private StudentCurricularPlan getStudentCurricularPlan() {
        return this.studentCurricularPlan;
    }

    public Registration enrol(final CycleCourseGroup cycleCourseGroup) {
        return separateSecondCycle();
    }

    protected Registration separateSecondCycle() {
        return new SeparationCyclesManagement().separateSecondCycle(getStudentCurricularPlan());
    }

    @Atomic
    public void createCycleOrRepeateSeparate() {

        if (studentCurricularPlan.isActive() && canSeparate()) {

            if (studentAlreadyHasNewRegistration() && canRepeateSeparate()) {
                // Repeating separate
                new SeparationCyclesManagement().createNewSecondCycle(studentCurricularPlan);
            } else {
                // Separating student
                new SeparationCyclesManagement().separateSecondCycle(studentCurricularPlan);
            }

        } else if (studentCurricularPlan.hasRegistration() && getRegistration().isConcluded() && canRepeateSeparate()) {
            new SeparationCyclesManagement().createNewSecondCycle(studentCurricularPlan);
        }
    }

    private Registration getRegistration() {
        return studentCurricularPlan.getRegistration();
    }

    private boolean canSeparate() {
        return hasFirstCycleConcluded() && hasExternalSecondCycle();
    }

    private boolean hasFirstCycleConcluded() {
        final CycleCurriculumGroup firstCycle = studentCurricularPlan.getFirstCycle();
        return firstCycle != null && firstCycle.isConcluded();
    }

    private boolean hasExternalSecondCycle() {
        final CycleCurriculumGroup secondCycle = studentCurricularPlan.getSecondCycle();
        return secondCycle != null && secondCycle.isExternal() && secondCycle.hasAnyCurriculumLines();
    }

    private boolean studentAlreadyHasNewRegistration() {
        final Student student = getRegistration().getStudent();
        return student.hasRegistrationFor(studentCurricularPlan.getSecondCycle().getDegreeCurricularPlanOfDegreeModule());
    }

    private boolean canRepeateSeparate() {
        return hasFirstCycleConcluded() && hasExternalSecondCycleAndNewRegistration();
    }

    private boolean hasExternalSecondCycleAndNewRegistration() {
        final CycleCurriculumGroup secondCycle = studentCurricularPlan.getSecondCycle();
        if (secondCycle != null && secondCycle.isExternal() && secondCycle.hasAnyCurriculumLines()) {
            final Student student = getRegistration().getStudent();
            return student.hasActiveRegistrationFor(secondCycle.getDegreeCurricularPlanOfDegreeModule());
        }
        return false;
    }

}
