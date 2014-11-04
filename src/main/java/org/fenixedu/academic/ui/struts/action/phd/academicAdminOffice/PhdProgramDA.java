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
package org.fenixedu.academic.ui.struts.action.phd.academicAdminOffice;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.academic.domain.phd.PhdProgramContextPeriod;
import org.fenixedu.academic.domain.phd.PhdProgramContextPeriodBean;
import org.fenixedu.academic.domain.phd.exceptions.PhdDomainOperationException;
import org.fenixedu.academic.ui.struts.action.phd.PhdDA;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/phdProgram", module = "academicAdministration", functionality = PhdIndividualProgramProcessDA.class)
@Forwards({
        @Forward(name = "listPhdProgramForPeriods",
                path = "/phd/academicAdminOffice/periods/phdProgram/listPhdProgramForPeriods.jsp"),
        @Forward(name = "viewPhdProgramPeriods", path = "/phd/academicAdminOffice/periods/phdProgram/viewPhdProgramPeriods.jsp"),
        @Forward(name = "addPhdProgramPeriod", path = "/phd/academicAdminOffice/periods/phdProgram/addPhdProgramPeriod.jsp") })
public class PhdProgramDA extends PhdDA {

    // Phd Program Management Periods

    public ActionForward listPhdProgramForPeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute(
                "phdPrograms",
                AcademicAccessRule.getPhdProgramsAccessibleToFunction(AcademicOperationType.MANAGE_PHD_PROCESSES,
                        Authenticate.getUser()).collect(Collectors.toSet()));
        return mapping.findForward("listPhdProgramForPeriods");
    }

    public ActionForward viewPhdProgramPeriods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgram phdProgram = (PhdProgram) getDomainObject(request, "phdProgramId");

        request.setAttribute("phdProgram", phdProgram);
        return mapping.findForward("viewPhdProgramPeriods");
    }

    public ActionForward removePhdProgramPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgramContextPeriod period = getDomainObject(request, "phdProgramContextPeriodId");
        period.deletePeriod();

        return viewPhdProgramPeriods(mapping, form, request, response);
    }

    public ActionForward prepareAddPhdProgramPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgram phdProgram = (PhdProgram) getDomainObject(request, "phdProgramId");
        PhdProgramContextPeriodBean bean = new PhdProgramContextPeriodBean(phdProgram);

        request.setAttribute("phdProgram", phdProgram);
        request.setAttribute("phdProgramContextPeriodBean", bean);

        return mapping.findForward("addPhdProgramPeriod");
    }

    public ActionForward addPhdProgramPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            PhdProgram phdProgram = getDomainObject(request, "phdProgramId");
            PhdProgramContextPeriodBean bean = getRenderedObject("phdProgramContextPeriodBean");
            phdProgram.create(bean);

        } catch (PhdDomainOperationException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());
            return addPhdProgramPeriodInvalid(mapping, form, request, response);
        }

        return viewPhdProgramPeriods(mapping, form, request, response);
    }

    public ActionForward addPhdProgramPeriodInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgram phdProgram = getDomainObject(request, "phdProgramId");
        PhdProgramContextPeriodBean bean = getRenderedObject("phdProgramContextPeriodBean");

        request.setAttribute("phdProgram", phdProgram);
        request.setAttribute("phdProgramContextPeriodBean", bean);

        return mapping.findForward("addPhdProgramPeriod");
    }

    // End of Phd Program Management Periods

}
