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
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.util.EnrolmentAction;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author dcs-rjao
 * 
 *         24/Mar/2003
 * 
 * 
 * @deprecated use OptionalEnrolment
 * 
 */
@Deprecated
public class EnrolmentInOptionalCurricularCourse extends EnrolmentInOptionalCurricularCourse_Base {

    protected EnrolmentInOptionalCurricularCourse() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public EnrolmentInOptionalCurricularCourse(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
            ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition, String createdBy) {
        this();
        initializeAsNew(studentCurricularPlan, curricularCourse, executionSemester, enrolmentCondition, createdBy);
        createCurriculumLineLog(EnrolmentAction.ENROL);
    }

    @Override
    final public boolean isOptional() {
        return true;
    }

    // new student structure methods
    public EnrolmentInOptionalCurricularCourse(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
            CurricularCourse curricularCourse, ExecutionSemester executionSemester, EnrollmentCondition enrolmentCondition,
            String createdBy) {
        this();
        if (studentCurricularPlan == null || curriculumGroup == null || curricularCourse == null || executionSemester == null
                || enrolmentCondition == null || createdBy == null) {
            throw new DomainException("invalid arguments");
        }
        // TODO: check this
        // validateDegreeModuleLink(curriculumGroup, curricularCourse);
        initializeAsNew(studentCurricularPlan, curriculumGroup, curricularCourse, executionSemester, enrolmentCondition,
                createdBy);
    }
}