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
package org.fenixedu.academic.ui.struts.action.phd.program;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.academic.domain.phd.PhdProgramInformation;
import org.fenixedu.academic.domain.phd.PhdProgramInformationBean;
import org.fenixedu.academic.domain.phd.exceptions.PhdDomainOperationException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.phd.academicAdminOffice.PhdIndividualProgramProcessDA;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/phdProgramInformation", module = "academicAdministration", functionality = PhdIndividualProgramProcessDA.class)
@Forwards({
        @Forward(name = "listPhdPrograms", path = "/phd/academicAdminOffice/program/information/listPhdPrograms.jsp"),
        @Forward(name = "listPhdProgramInformations",
                path = "/phd/academicAdminOffice/program/information/listPhdProgramInformations.jsp"),
        @Forward(name = "createPhdInformation", path = "/phd/academicAdminOffice/program/information/createPhdInformation.jsp"),
        @Forward(name = "editPhdProgramInformation",
                path = "/phd/academicAdminOffice/program/information/editPhdProgramInformation.jsp") })
public class PhdProgramInformationDA extends FenixDispatchAction {

    public ActionForward listPhdPrograms(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute(
                "phdPrograms",
                AcademicAccessRule.getPhdProgramsAccessibleToFunction(AcademicOperationType.MANAGE_PHD_PROCESSES,
                        Authenticate.getUser()).collect(Collectors.toSet()));
        return mapping.findForward("listPhdPrograms");
    }

    public ActionForward listPhdProgramInformations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("phdProgram", readPhdProgram(request));
        return mapping.findForward("listPhdProgramInformations");
    }

    private PhdProgram readPhdProgram(final HttpServletRequest request) {
        return getDomainObject(request, "phdProgramId");
    }

    private PhdProgramInformationBean readPhdInformationBean() {
        return getRenderedObject("phdProgramInformationBean");
    }

    public ActionForward prepareCreatePhdInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgram phdProgram = readPhdProgram(request);
        request.setAttribute("phdProgram", phdProgram);
        request.setAttribute("phdProgramInformationBean", new PhdProgramInformationBean(phdProgram));

        return mapping.findForward("createPhdInformation");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        PhdProgramInformationBean readPhdInformationBean = readPhdInformationBean();
        try {
            PhdProgramInformation.createInformation(readPhdInformationBean);
        } catch (PhdDomainOperationException e) {
            request.setAttribute("phdProgramInformationBean", readPhdInformationBean);
            setError(request, mapping, null, null, e);
            return mapping.findForward("createPhdInformation");
        }

        return listPhdProgramInformations(mapping, form, request, response);
    }

    public ActionForward createInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgram phdProgram = readPhdProgram(request);
        request.setAttribute("phdProgram", phdProgram);
        request.setAttribute("phdProgramInformationBean", readPhdInformationBean());

        return mapping.findForward("createPhdInformation");
    }

    public ActionForward prepareEditPhdProgramInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgramInformation phdProgramInformation = readPhdProgramInformation(request);

        request.setAttribute("phdProgramInformation", phdProgramInformation);
        request.setAttribute("phdProgramInformationBean", new PhdProgramInformationBean(phdProgramInformation));

        return mapping.findForward("editPhdProgramInformation");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PhdProgramInformation phdProgramInformation = readPhdProgramInformation(request);
        PhdProgramInformationBean readPhdInformationBean = readPhdInformationBean();

        try {
            phdProgramInformation.edit(readPhdInformationBean);
        } catch (PhdDomainOperationException e) {
            request.setAttribute("phdProgramInformation", phdProgramInformation);
            request.setAttribute("phdProgramInformationBean", readPhdInformationBean);
            setError(request, mapping, null, null, e);
            return mapping.findForward("editPhdProgramInformation");
        }

        return listPhdProgramInformations(mapping, form, request, response);
    }

    public ActionForward editInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdProgramInformation phdProgramInformation = readPhdProgramInformation(request);

        request.setAttribute("phdProgramInformation", phdProgramInformation);
        request.setAttribute("phdProgramInformationBean", readPhdInformationBean());

        return mapping.findForward("editPhdProgramInformation");
    }

    private PhdProgramInformation readPhdProgramInformation(HttpServletRequest request) {
        return getDomainObject(request, "phdProgramInformationId");
    }
}
