/*
 * Created on Nov 21, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.department;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ReadDetailedTeacherProfessorshipsByExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.DetailedProfessorship;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author jpvl
 */
@Mapping(module = "departmentAdmOffice", path = "/showTeacherProfessorshipsForSummariesManagement",
        input = "show-teacher-professorships-for-management", attribute = "teacherExecutionCourseResponsabilities",
        formBean = "teacherExecutionCourseResponsabilities", scope = "request")
@Forwards(value = { @Forward(name = "list-professorships",
        path = "/departmentAdmOffice/teacher/showTeacherProfessorshipsForManagementSummaries.jsp") })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException.class,
        key = "message.teacher-not-belong-to-department", handler = org.apache.struts.action.ExceptionHandler.class,
        path = "/teacherSearchForExecutionCourseAssociation.do?method=searchForm&page=0", scope = "request") })
public class ReadTeacherProfessorshipsByExecutionYearAction extends AbstractReadProfessorshipsAction {

    @Override
    List getDetailedProfessorships(IUserView userView, String teacherId, DynaActionForm actionForm, HttpServletRequest request)
            throws FenixServiceException {

        List detailedInfoProfessorshipList =
                ReadDetailedTeacherProfessorshipsByExecutionYear.runReadDetailedTeacherProfessorshipsByExecutionYear(teacherId,
                        (Integer) actionForm.get("executionYearId"));
        request.setAttribute("args", new TreeMap());
        return detailedInfoProfessorshipList;
    }

    @Override
    protected void extraPreparation(IUserView userView, InfoTeacher infoTeacher, HttpServletRequest request,
            DynaActionForm dynaForm) throws FenixServiceException {

        prepareConstants(userView, infoTeacher, request);
        prepareForm(dynaForm, request);
    }

    private void prepareForm(DynaActionForm dynaForm, HttpServletRequest request) {
        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) request.getAttribute("executionYear");
        InfoTeacher infoTeacher = (InfoTeacher) request.getAttribute("infoTeacher");
        dynaForm.set("externalId", infoTeacher.getExternalId());
        dynaForm.set("teacherId", infoTeacher.getExternalId().toString());
        if (dynaForm.get("executionYearId") == null) {
            dynaForm.set("executionYearId", infoExecutionYear.getExternalId());
        }

        List detailedProfessorshipList = (List) request.getAttribute("detailedProfessorshipList");

        List executionCourseIds = new ArrayList();
        Map hours = new HashMap();
        for (int i = 0; i < detailedProfessorshipList.size(); i++) {
            DetailedProfessorship dps = (DetailedProfessorship) detailedProfessorshipList.get(i);

            String executionCourseId = dps.getInfoProfessorship().getInfoExecutionCourse().getExternalId();
            if (dps.getResponsibleFor().booleanValue()) {
                executionCourseIds.add(executionCourseId);
            }
            if (dps.getMasterDegreeOnly().booleanValue()) {
                if (dps.getInfoProfessorship().getHours() != null) {
                    hours.put(executionCourseId.toString(), dps.getInfoProfessorship().getHours().toString());
                }
            }
        }

        dynaForm.set("executionCourseResponsability", executionCourseIds.toArray(new String[] {}));
        dynaForm.set("hours", hours);

    }

    private void prepareConstants(IUserView userView, InfoTeacher infoTeacher, HttpServletRequest request)
            throws FenixServiceException {

        List executionYears = ReadNotClosedExecutionYears.run();

        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) CollectionUtils.find(executionYears, new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                InfoExecutionYear infoExecutionYearElem = (InfoExecutionYear) arg0;
                if (infoExecutionYearElem.getState().equals(PeriodState.CURRENT)) {
                    return true;
                }
                return false;
            }
        });

        Department department = infoTeacher.getTeacher().getCurrentWorkingDepartment();
        InfoDepartment teacherDepartment = InfoDepartment.newInfoFromDomain(department);

        if (userView == null || !userView.hasRoleType(RoleType.CREDITS_MANAGER)) {

            final List<Department> departmentList = userView.getPerson().getManageableDepartmentCredits();
            request.setAttribute("isDepartmentManager", Boolean.valueOf(departmentList.contains(department)));

        } else {
            request.setAttribute("isDepartmentManager", Boolean.FALSE);
        }

        request.setAttribute("teacherDepartment", teacherDepartment);
        request.setAttribute("executionYear", infoExecutionYear);
        request.setAttribute("executionYears", executionYears);
    }
}