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
package net.sourceforge.fenixedu.applicationTier.Servico.student.curriculumLines;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.student.OptionalCurricularCoursesLocationBean;
import net.sourceforge.fenixedu.dataTransferObject.student.OptionalCurricularCoursesLocationBean.EnrolmentLocationBean;
import net.sourceforge.fenixedu.dataTransferObject.student.OptionalCurricularCoursesLocationBean.OptionalEnrolmentLocationBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.curriculumLine.MoveCurriculumLinesBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.Atomic;

public class MoveCurriculumLines {

    @Atomic
    public static void run(final MoveCurriculumLinesBean moveCurriculumLinesBean) {
        final StudentCurricularPlan studentCurricularPlan = moveCurriculumLinesBean.getStudentCurricularPlan();
        if (moveCurriculumLinesBean.isWithRules()) {
            studentCurricularPlan.moveCurriculumLines(moveCurriculumLinesBean);
        } else {
            studentCurricularPlan.moveCurriculumLinesWithoutRules(AccessControl.getPerson(), moveCurriculumLinesBean);
        }
    }

    @Atomic
    public static void run(final OptionalCurricularCoursesLocationBean bean) throws FenixServiceException {
        moveEnrolments(bean);
        moveOptionalEnrolments(bean);
    }

    private static void moveOptionalEnrolments(final OptionalCurricularCoursesLocationBean bean) {
        for (final OptionalEnrolmentLocationBean line : bean.getOptionalEnrolmentBeans()) {
            bean.getStudentCurricularPlan().convertOptionalEnrolmentToEnrolment(line.getEnrolment(), line.getCurriculumGroup());
        }
    }

    private static void moveEnrolments(final OptionalCurricularCoursesLocationBean bean) throws FenixServiceException {
        for (final EnrolmentLocationBean line : bean.getEnrolmentBeans()) {
            final CurriculumGroup curriculumGroup = line.getCurriculumGroup(bean.getStudentCurricularPlan());
            if (curriculumGroup == null) {
                throw new FenixServiceException("error.MoveCurriculumLines.invalid.curriculumGroup");
            }
            bean.getStudentCurricularPlan().convertEnrolmentToOptionalEnrolment(line.getEnrolment(), curriculumGroup,
                    line.getOptionalCurricularCourse());
        }
    }

}
