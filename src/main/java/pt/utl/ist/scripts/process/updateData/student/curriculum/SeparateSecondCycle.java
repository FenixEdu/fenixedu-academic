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
package pt.utl.ist.scripts.process.updateData.student.curriculum;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.SeparationCyclesManagement;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

@Task(englishTitle = "SeparateSecondCycle", readOnly = true)
public class SeparateSecondCycle extends CronTask {

    @Override
    public void runTask() {
        I18N.setLocale(new Locale("pt", "PT"));
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlans()) {
            getLogger().info("Processing DCP: " + degreeCurricularPlan.getName());

            for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlansSet()) {

                if (studentCurricularPlan.isActive() && canSeparate(studentCurricularPlan)) {

                    if (studentAlreadyHasNewRegistration(studentCurricularPlan) && canRepeateSeparate(studentCurricularPlan)) {
                        getLogger()
                                .info("1 - Repeating separate for: "
                                        + studentCurricularPlan.getRegistration().getStudent().getNumber());

                        separateByStudentNumberProcedure(studentCurricularPlan.getRegistration().getStudent(),
                                studentCurricularPlan.getDegreeCurricularPlan());
                    } else {

                        getLogger().info(
                                "Separating Student: " + studentCurricularPlan.getRegistration().getStudent().getNumber());
                        separateStudentProcedure(studentCurricularPlan);
                    }

                } else if (studentCurricularPlan.hasRegistration() && studentCurricularPlan.getRegistration().isConcluded()) {

                    if (canRepeateSeparate(studentCurricularPlan)) {
                        getLogger()
                                .info("2 - Repeating separate for: "
                                        + studentCurricularPlan.getRegistration().getStudent().getNumber());
                        separateByStudentNumberProcedure(studentCurricularPlan.getRegistration().getStudent(),
                                studentCurricularPlan.getDegreeCurricularPlan());
                    }
                }
            }
        }
    }

    private boolean studentAlreadyHasNewRegistration(final StudentCurricularPlan studentCurricularPlan) {
        final Student student = studentCurricularPlan.getRegistration().getStudent();
        return student.hasRegistrationFor(studentCurricularPlan.getSecondCycle().getDegreeCurricularPlanOfDegreeModule());
    }

    private boolean canSeparate(final StudentCurricularPlan studentCurricularPlan) {
        return hasFirstCycleConcluded(studentCurricularPlan) && hasExternalSecondCycle(studentCurricularPlan);
    }

    private boolean canRepeateSeparate(final StudentCurricularPlan studentCurricularPlan) {
        return hasFirstCycleConcluded(studentCurricularPlan) && hasExternalSecondCycleAndNewRegistration(studentCurricularPlan);
    }

    private List<DegreeCurricularPlan> getDegreeCurricularPlans() {
        return DegreeCurricularPlan.readByDegreeTypesAndState(new HashSet<DegreeType>() {
            {
                add(DegreeType.BOLONHA_DEGREE);
                add(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
            }
        }, DegreeCurricularPlanState.ACTIVE);
    }

    private boolean hasFirstCycleConcluded(final StudentCurricularPlan studentCurricularPlan) {
        final CycleCurriculumGroup firstCycle = studentCurricularPlan.getFirstCycle();
        return firstCycle != null && firstCycle.isConcluded();
    }

    private boolean hasExternalSecondCycle(final StudentCurricularPlan studentCurricularPlan) {
        final CycleCurriculumGroup secondCycle = studentCurricularPlan.getSecondCycle();
        return secondCycle != null && secondCycle.isExternal() && secondCycle.hasAnyCurriculumLines();
    }

    private boolean hasExternalSecondCycleAndNewRegistration(final StudentCurricularPlan studentCurricularPlan) {
        final CycleCurriculumGroup secondCycle = studentCurricularPlan.getSecondCycle();
        if (secondCycle != null && secondCycle.isExternal() && secondCycle.hasAnyCurriculumLines()) {
            final Student student = studentCurricularPlan.getRegistration().getStudent();
            return student.hasActiveRegistrationFor(secondCycle.getDegreeCurricularPlanOfDegreeModule());
        }
        return false;
    }

    @Atomic(mode = TxMode.WRITE)
    private void separateStudentProcedure(StudentCurricularPlan studentCurricularPlan) {
        try {
            new SeparationCyclesManagement().separateSecondCycle(studentCurricularPlan);
        } catch (final Exception e) {
            getLogger().error("Separating students with rules", e);
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private void separateByStudentNumberProcedure(Student student, DegreeCurricularPlan dcp) {
        final Registration registration = student.getMostRecentRegistration(dcp);
        try {
            new SeparateByStudentNumber().separateSecondCycle(registration.getLastStudentCurricularPlan());
        } catch (final Exception e) {
            getLogger().error("Repeating separate student", e);
        }
    }

    private class SeparateByStudentNumber extends SeparationCyclesManagement {
        @Override
        public Registration separateSecondCycle(StudentCurricularPlan studentCurricularPlan) {
            // do not check if can separate
            // the state of the registration can change during the execution of this long script and thus at least this validation has to be made
            // to prevent wrong creations of new registrations
            if (canRepeateSeparate(studentCurricularPlan)) {
                return createNewSecondCycle(studentCurricularPlan);
            }
            return null;
        }
    }

}
