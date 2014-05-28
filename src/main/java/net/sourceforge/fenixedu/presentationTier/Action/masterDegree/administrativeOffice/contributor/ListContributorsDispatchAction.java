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
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.contributor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminContributorsApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */

@StrutsFunctionality(app = AcademicAdminContributorsApp.class, path = "consult",
        titleKey = "link.masterDegree.administrativeOffice.visualizeContributor", accessGroup = "academic(MANAGE_CONTRIBUTORS)")
@Mapping(path = "/visualizeContributors", module = "academicAdministration", formBean = "chooseContributorForm")
@Forwards({ @Forward(name = "PrepareReady", path = "/academicAdminOffice/contributor/chooseContributor.jsp"),
        @Forward(name = "ActionReady", path = "/academicAdminOffice/contributor/visualizeContributor.jsp"),
        @Forward(name = "ChooseContributor", path = "/academicAdminOffice/contributor/selectContributorFromList.jsp") })
public class ListContributorsDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm createContributorForm = (DynaActionForm) form;

        // Clean the form
        createContributorForm.set("contributorNumber", null);

        setActionToRequest(request);

        return mapping.findForward("PrepareReady");

    }

    protected void setActionToRequest(HttpServletRequest request) {
        request.setAttribute(PresentationConstants.CONTRIBUTOR_ACTION, "label.action.contributor.visualize");
    }

    public ActionForward getContributors(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {

        final Integer contributorNumber = getIntegerFromRequestOrForm(request, (DynaActionForm) form, "contributorNumber");
        if (contributorNumber == null) {
            return prepare(mapping, form, request, response);
        }
        final InfoContributor infoContributor =
                InfoContributor.newInfoFromDomain(Party.readByContributorNumber(contributorNumber.toString()));

        if (infoContributor == null) {
            addActionMessage(request, "error.contributor.not.found", contributorNumber.toString());
            return prepare(mapping, form, request, response);
        }

        request.setAttribute(PresentationConstants.CONTRIBUTOR, infoContributor);
        return mapping.findForward("ActionReady");

    }

    public ActionForward chooseContributor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        List contributorList = (List) request.getAttribute(PresentationConstants.CONTRIBUTOR_LIST);

        Integer choosenContributorPosition = Integer.valueOf(request.getParameter("contributorPosition"));

        InfoContributor infoContributor = (InfoContributor) contributorList.get(choosenContributorPosition.intValue());

        request.setAttribute(PresentationConstants.CONTRIBUTOR, infoContributor);
        return mapping.findForward("ActionReady");

    }
}