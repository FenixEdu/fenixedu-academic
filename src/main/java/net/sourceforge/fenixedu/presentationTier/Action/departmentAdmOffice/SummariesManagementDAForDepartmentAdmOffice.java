package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.TeacherSearchForSummariesManagement;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.SummariesManagementDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentAdmOffice", path = "/summariesManagement", formBean = "summariesManagementForm",
        functionality = TeacherSearchForSummariesManagement.class)
@Forwards({
        @Forward(name = "prepareInsertSummary", path = "/departmentAdmOffice/teacher/executionCourse/createSummary.jsp"),
        @Forward(name = "prepareShowSummaries", path = "/departmentAdmOffice/teacher/executionCourse/showSummaries.jsp"),
        @Forward(name = "showSummariesCalendar", path = "/departmentAdmOffice/teacher/executionCourse/showSummariesCalendar.jsp"),
        @Forward(name = "prepareInsertComplexSummary",
                path = "/departmentAdmOffice/teacher/executionCourse/createComplexSummary.jsp") })
public class SummariesManagementDAForDepartmentAdmOffice extends SummariesManagementDA {
}