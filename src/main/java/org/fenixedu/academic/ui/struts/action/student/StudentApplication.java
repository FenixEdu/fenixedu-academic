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
package org.fenixedu.academic.ui.struts.action.student;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsApplication;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

public class StudentApplication extends ForwardAction {

    static final String HINT = "Student";
    static final String ACCESS_GROUP = "role(STUDENT)";
    static final String BUNDLE = "StudentResources";

    @StrutsApplication(path = "consult", titleKey = "consult", bundle = BUNDLE, accessGroup = ACCESS_GROUP, hint = HINT)
    public static class StudentViewApp {
    }

    @StrutsApplication(descriptionKey = "participate", path = "participate", titleKey = "participate", bundle = BUNDLE,
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class StudentParticipateApp {
    }

    @StrutsApplication(descriptionKey = "submit", path = "submit", titleKey = "submit", bundle = BUNDLE,
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class StudentSubmitApp {
    }

    @StrutsApplication(descriptionKey = "enroll", path = "enroll", titleKey = "enroll", bundle = BUNDLE,
            accessGroup = ACCESS_GROUP, hint = HINT)
    public static class StudentEnrollApp {
    }

    @StrutsApplication(descriptionKey = "link.student.seniorTitle", path = "finalists", titleKey = "link.student.seniorTitle",
            bundle = BUNDLE, accessGroup = ACCESS_GROUP, hint = HINT)
    public static class StudentSeniorsApp {
    }

    @StrutsFunctionality(app = StudentEnrollApp.class, path = "exams", titleKey = "link.exams.enrolment")
    @Mapping(path = "/enrollment/evaluations/showStudentExams", module = "student",
            parameter = "/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=1")
    public static class ShowStudentExams extends ForwardAction {
    }

    @StrutsFunctionality(app = StudentEnrollApp.class, path = "written-tests", titleKey = "link.writtenTests.enrolment")
    @Mapping(path = "/enrollment/evaluations/showStudentWrittenTests", module = "student",
            parameter = "/student/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=2")
    public static class ShowStudentWrittenTests extends ForwardAction {
    }
}
