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
package org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;

public class GradesToSubmitExecutionCourseSendMailBean implements Serializable {

    static private final long serialVersionUID = 1L;

    private DegreeCurricularPlan degreeCurricularPlan;
    private ExecutionCourse executionCourse;
    private boolean toSubmit;

    public GradesToSubmitExecutionCourseSendMailBean(final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionCourse executionCourse, final boolean toSubmit) {
        setDegreeCurricularPlan(degreeCurricularPlan);
        setExecutionCourse(executionCourse);
        setToSubmit(toSubmit);
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public ExecutionCourse getExecutionCourse() {
        return this.executionCourse;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    public boolean isToSubmit() {
        return toSubmit;
    }

    public void setToSubmit(boolean toSubmit) {
        this.toSubmit = toSubmit;
    }

    public ExecutionSemester getExecutionSemester() {
        return executionCourse.getExecutionPeriod();
    }

    public int getNumberOfEnroledStudents() {
        return executionCourse.getAttendsSet().size();
    }

    public int getNumberOfStudentsWithoutGrade() {
        int count = 0;

        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            if (degreeCurricularPlan == null || (degreeCurricularPlan != null
                    && degreeCurricularPlan.equals(curricularCourse.getDegreeCurricularPlan()))) {
                count += getNumberOfStudentsWithoutGrade(curricularCourse);
            }
        }
        return count;
    }

    private int getNumberOfStudentsWithoutGrade(CurricularCourse curricularCourse) {
        int total = 0;
        for (final CurriculumModule curriculumModule : curricularCourse.getCurriculumModulesSet()) {

            if (curriculumModule.isEnrolment()) {
                final Enrolment enrolment = (Enrolment) curriculumModule;

                if (enrolment.isValid(getExecutionSemester()) && enrolment.getEvaluationSeason().isNormal()) {

                    if (!enrolment.hasAssociatedMarkSheetOrFinalGrade(EvaluationSeason.readNormalSeason())) {
                        total++;
                    }

                } else if (enrolment.hasImprovement()
                        && !enrolment.hasAssociatedMarkSheet(EvaluationSeason.readImprovementSeason())
                        && enrolment.hasImprovementFor(getExecutionSemester())) {

                    total++;
                }
            }
        }
        return total;
    }

    public String getResponsibleTeacherNames() {
        final StringBuilder builder = new StringBuilder();
        for (final Professorship professorship : executionCourse.responsibleFors()) {

            final Person person = professorship.getPerson();
            builder.append(person.getName());

            final String email = person.getInstitutionalOrDefaultEmailAddressValue();
            if (!StringUtils.isEmpty(email)) {
                builder.append(" (").append(email).append(")");
            }

            builder.append(",");
        }

        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }

        return builder.toString();
    }
}
