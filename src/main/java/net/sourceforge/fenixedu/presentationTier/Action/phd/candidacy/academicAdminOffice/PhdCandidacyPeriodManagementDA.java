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
package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.candidacy.EPFLPhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.InstitutionPhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriodBean;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminPhdApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = AcademicAdminPhdApp.class, path = "candidacy-period-management",
        titleKey = "label.phd.candidacy.periods.management", accessGroup = "academic(MANAGE_PHD_PROCESSES)")
@Mapping(path = "/phdCandidacyPeriodManagement", module = "academicAdministration")
@Forwards({
        @Forward(name = "list", path = "/phd/candidacy/academicAdminOffice/periods/list.jsp"),
        @Forward(name = "createPhdCandidacyPeriod",
                path = "/phd/candidacy/academicAdminOffice/periods/createPhdCandidacyPeriod.jsp"),
        @Forward(name = "editPhdCandidacyPeriod", path = "/phd/candidacy/academicAdminOffice/periods/editPhdCandidacyPeriod.jsp"),
        @Forward(name = "view", path = "/phd/candidacy/academicAdminOffice/periods/view.jsp") })
public class PhdCandidacyPeriodManagementDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("phdCandidacyPeriods", PhdCandidacyPeriod.readPhdCandidacyPeriods());

        return mapping.findForward("list");
    }

    public ActionForward prepareCreatePhdCandidacyPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdCandidacyPeriodBean bean = new PhdCandidacyPeriodBean();
        request.setAttribute("phdCandidacyPeriodBean", bean);

        return mapping.findForward("createPhdCandidacyPeriod");
    }

    public ActionForward createPhdCandidacyPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdCandidacyPeriodBean bean = readPhdCandidacyPeriodBean();

        switch (bean.getType()) {
        case EPFL:
            try {
                EPFLPhdCandidacyPeriod.create(bean);
                break;
            } catch (final DomainException e) {
                addActionMessage("error", request, e.getKey(), e.getArgs());
                return createPhdCandidacyPeriodInvalid(mapping, form, request, response);
            }
        case INSTITUTION:
            try {
                InstitutionPhdCandidacyPeriod.create(bean);
                break;
            } catch (final DomainException e) {
                addActionMessage("error", request, e.getKey(), e.getArgs());
                return createPhdCandidacyPeriodInvalid(mapping, form, request, response);
            }
        default:
            throw new DomainException("error.PhdCandidacyPeriodBean.type.missing");
        }

        return list(mapping, form, request, response);
    }

    public ActionForward createPhdCandidacyPeriodInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("phdCandidacyPeriodBean", readPhdCandidacyPeriodBean());
        request.setAttribute("phdCandidacyPeriod", readPhdCandidacyPeriod(request));

        return mapping.findForward("createPhdCandidacyPeriod");
    }

    public ActionForward prepareEditPhdCandidacyPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdCandidacyPeriod phdCandidacyPeriod = readPhdCandidacyPeriod(request);
        request.setAttribute("phdCandidacyPeriodBean", new PhdCandidacyPeriodBean(phdCandidacyPeriod));
        request.setAttribute("phdCandidacyPeriod", readPhdCandidacyPeriod(request));

        return mapping.findForward("editPhdCandidacyPeriod");
    }

    public ActionForward editPhdCandidacyPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdCandidacyPeriod phdCandidacyPeriod = readPhdCandidacyPeriod(request);
        PhdCandidacyPeriodBean bean = readPhdCandidacyPeriodBean();

        try {
            phdCandidacyPeriod.edit(bean.getStart(), bean.getEnd());
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
            return editPhdCandidacyPeriodInvalid(mapping, form, request, response);
        }

        return list(mapping, form, request, response);
    }

    public ActionForward editPhdCandidacyPeriodInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("phdCandidacyPeriodBean", readPhdCandidacyPeriodBean());
        request.setAttribute("phdCandidacyPeriod", readPhdCandidacyPeriod(request));

        return mapping.findForward("editPhdCandidacyPeriod");
    }

    public ActionForward addPhdProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdCandidacyPeriodBean phdCandidacyPeriodBean = readPhdCandidacyPeriodBean();
        InstitutionPhdCandidacyPeriod phdCandidacyPeriod = (InstitutionPhdCandidacyPeriod) readPhdCandidacyPeriod(request);

        phdCandidacyPeriod.addPhdProgramListToPeriod(phdCandidacyPeriodBean.getPhdProgramList());

        return prepareEditPhdCandidacyPeriod(mapping, form, request, response);
    }

    public ActionForward removePhdProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        InstitutionPhdCandidacyPeriod phdCandidacyPeriod = (InstitutionPhdCandidacyPeriod) readPhdCandidacyPeriod(request);
        phdCandidacyPeriod.removePhdProgramInPeriod(readPhdProgram(request));

        return prepareEditPhdCandidacyPeriod(mapping, form, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PhdCandidacyPeriod period = readPhdCandidacyPeriod(request);

        request.setAttribute("phdCandidacyPeriod", period);

        return mapping.findForward("view");
    }

    private PhdProgram readPhdProgram(final HttpServletRequest request) {
        return getDomainObject(request, "phdProgramId");
    }

    private PhdCandidacyPeriodBean readPhdCandidacyPeriodBean() {
        return getRenderedObject("phdCandidacyPeriodBean");
    }

    private PhdCandidacyPeriod readPhdCandidacyPeriod(HttpServletRequest request) {
        return getDomainObject(request, "phdCandidacyPeriodId");
    }
}
