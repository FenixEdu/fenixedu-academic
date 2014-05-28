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
package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

import org.apache.commons.lang.StringUtils;

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
        return executionCourse.getAttends().size();
    }

    public int getNumberOfStudentsWithoutGrade() {
        int count = 0;

        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            if (degreeCurricularPlan != null && degreeCurricularPlan.equals(curricularCourse.getDegreeCurricularPlan())) {
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

                if (enrolment.isValid(getExecutionSemester())
                        && enrolment.getEnrolmentEvaluationType() == EnrolmentEvaluationType.NORMAL) {

                    if (!enrolment.hasAssociatedMarkSheetOrFinalGrade(MarkSheetType.NORMAL)) {
                        total++;
                    }

                } else if (enrolment.hasImprovement() && !enrolment.hasAssociatedMarkSheet(MarkSheetType.IMPROVEMENT)
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
