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
package org.fenixedu.academic.ui.struts.action.academicAdministration.executionCourseManagement;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.InfoDegreeCurricularPlan;
import org.fenixedu.academic.dto.InfoExecutionPeriod;
import org.fenixedu.academic.service.services.commons.ReadActiveDegreeCurricularPlansByDegreeType;
import org.fenixedu.academic.service.services.commons.ReadNotClosedExecutionPeriods;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.manager.CreateExecutionCoursesForDegreeCurricularPlansAndExecutionPeriod;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminExecutionsApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.Pair;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = AcademicAdminExecutionsApp.class, path = "create-execution-courses",
        titleKey = "link.manager.createExecutionCourses", accessGroup = "academic(MANAGE_EXECUTION_COURSES)")
@Mapping(module = "academicAdministration", path = "/createExecutionCourses",
        input = "/createExecutionCourses.do?method=chooseDegreeType", formBean = "createExecutionCoursesForm")
@Forwards(value = {
        @Forward(name = "chooseDegreeCurricularPlans",
                path = "/academicAdministration/executionCourseManagement/chooseDegreeCurricularPlans.jsp"),
        @Forward(name = "chooseDegreeType", path = "/academicAdministration/executionCourseManagement/chooseDegreeType.jsp"),
        @Forward(name = "createExecutionCoursesSuccess",
                path = "/academicAdministration/executionCourseManagement/createExecutionCoursesSuccess.jsp") })
public class CreateExecutionCoursesDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward chooseDegreeType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("degreeTypes", Bennu.getInstance().getDegreeTypeSet());
        return mapping.findForward("chooseDegreeType");

    }

    public ActionForward chooseDegreeCurricularPlans(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        try {

            DynaActionForm actionForm = (DynaActionForm) form;
            String degreeType = (String) actionForm.get("degreeType");

            if (StringUtils.isEmpty(degreeType)) {
                degreeType = request.getParameter("degreeType");
            }

            if (StringUtils.isEmpty(degreeType)) {
                throw new DomainException("error.selection.noDegreeType");
            }

            Collection<InfoDegreeCurricularPlan> degreeCurricularPlans =
                    ReadActiveDegreeCurricularPlansByDegreeType.runForAcademicAdmin(Predicate.isEqual(FenixFramework
                            .getDomainObject(degreeType)));
            List<InfoExecutionPeriod> executionPeriods = ReadNotClosedExecutionPeriods.run();

            request.setAttribute("degreeCurricularPlans", degreeCurricularPlans);
            request.setAttribute("executionPeriods", executionPeriods);
            request.setAttribute("degreeType", degreeType);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage());
            return chooseDegreeType(mapping, form, request, response);
        }

        return mapping.findForward("chooseDegreeCurricularPlans");

    }

    public ActionForward createExecutionCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        DynaActionForm actionForm = (DynaActionForm) form;
        String[] degreeCurricularPlansIDs = (String[]) actionForm.get("degreeCurricularPlansIDs");
        String executionPeriodID = (String) actionForm.get("executionPeriodID");
        try {
            HashMap<String, Pair<Integer, String>> result =
                    CreateExecutionCoursesForDegreeCurricularPlansAndExecutionPeriod.run(degreeCurricularPlansIDs,
                            executionPeriodID);

            // avmc (ist150958): success messages: 1 line for each DCP
            final ExecutionInterval executionPeriod = FenixFramework.getDomainObject(executionPeriodID);
            addActionMessage("successHead", request,
                    "message.executionCourseManagement.createExecutionCoursesForDegreeCurricularPlan.successHead",
                    executionPeriod.getName() + " " + executionPeriod.getExecutionYear().getYear());

            for (final String degreeCurricularPlanID : degreeCurricularPlansIDs) {
                final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);
                addActionMessage("successDCP", request,
                        "message.executionCourseManagement.createExecutionCoursesForDegreeCurricularPlan.successDCP",
                        degreeCurricularPlan.getPresentationName(), result.get(degreeCurricularPlanID).getKey().toString(),
                        result.get(degreeCurricularPlanID).getValue());
            }

        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage());
            return chooseDegreeCurricularPlans(mapping, actionForm, request, response);
        }
        return mapping.findForward("createExecutionCoursesSuccess");

    }
}
