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
/*
 * Created on Nov 21, 2003 by jpvl
 * 
 */
package org.fenixedu.academic.ui.struts.action.department;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.commons.ReadNotClosedExecutionYears;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.teacher.ReadTeacherByOID;
import org.fenixedu.academic.service.services.teacher.professorship.ReadDetailedTeacherProfessorshipsByExecutionYear;
import org.fenixedu.academic.dto.InfoDepartment;
import org.fenixedu.academic.dto.InfoExecutionYear;
import org.fenixedu.academic.dto.InfoTeacher;
import org.fenixedu.academic.dto.teacher.professorship.DetailedProfessorship;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.ui.struts.action.departmentAdmOffice.TeacherSearchForSummariesManagement;
import org.fenixedu.academic.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.ExceptionHandler;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

/**
 * @author jpvl
 */
@Mapping(module = "departmentAdmOffice", path = "/showTeacherProfessorshipsForSummariesManagement",
        formBean = "teacherExecutionCourseResponsabilities", functionality = TeacherSearchForSummariesManagement.class)
@Forwards({ @Forward(name = "list-professorships",
        path = "/departmentAdmOffice/teacher/showTeacherProfessorshipsForManagementSummaries.jsp") })
@Exceptions({ @ExceptionHandling(type = NotAuthorizedException.class, key = "message.teacher-not-belong-to-department",
        handler = ExceptionHandler.class, path = "/teacherSearchForExecutionCourseAssociation.do", scope = "request") })
public class ReadTeacherProfessorshipsByExecutionYearAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        InfoTeacher infoTeacher = getInfoTeacher(request, dynaForm);

        List detailedInfoProfessorshipList = getDetailedProfessorships(userView, infoTeacher.getExternalId(), dynaForm, request);

        ComparatorChain chain = new ComparatorChain();

        Comparator executionPeriodComparator =
                new BeanComparator("infoProfessorship.infoExecutionCourse.infoExecutionPeriod.semester");
        Comparator nameComparator = new BeanComparator("infoProfessorship.infoExecutionCourse.nome");

        chain.addComparator(executionPeriodComparator);
        chain.addComparator(nameComparator);
        Collections.sort(detailedInfoProfessorshipList, chain);

        request.setAttribute("detailedProfessorshipList", detailedInfoProfessorshipList);

        prepareConstants(userView, infoTeacher, request);
        prepareForm(dynaForm, request);
        return mapping.findForward("list-professorships");
    }

    protected InfoTeacher getInfoTeacher(HttpServletRequest request, DynaActionForm dynaForm) throws Exception {
        InfoTeacher infoTeacher = (InfoTeacher) request.getAttribute("infoTeacher");
        if (infoTeacher == null) {
            final User userView = Authenticate.getUser();
            infoTeacher = (InfoTeacher) ReadTeacherByOID.runReadTeacherByOID(dynaForm.get("externalId").toString());
            request.setAttribute("infoTeacher", infoTeacher);

        }
        return infoTeacher;
    }

    List getDetailedProfessorships(User userView, String teacherId, DynaActionForm actionForm, HttpServletRequest request)
            throws FenixServiceException {
        List detailedInfoProfessorshipList =
                ReadDetailedTeacherProfessorshipsByExecutionYear.runReadDetailedTeacherProfessorshipsByExecutionYear(teacherId,
                        (String) actionForm.get("executionYearId"));
        request.setAttribute("args", new TreeMap());
        return detailedInfoProfessorshipList;
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

    private void prepareConstants(User userView, InfoTeacher infoTeacher, HttpServletRequest request)
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

        Department department = infoTeacher.getTeacher().getDepartment();
        InfoDepartment teacherDepartment = InfoDepartment.newInfoFromDomain(department);

        final Collection<Department> departmentList = userView.getPerson().getManageableDepartmentCreditsSet();
        request.setAttribute("isDepartmentManager", Boolean.valueOf(departmentList.contains(department)));

        request.setAttribute("teacherDepartment", teacherDepartment);
        request.setAttribute("executionYear", infoExecutionYear);
        request.setAttribute("executionYears", executionYears);
    }
}