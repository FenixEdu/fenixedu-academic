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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/editContributor", module = "academicAdministration", formBean = "createContributorForm",
        functionality = EditContributorsDispatchAction.class)
@Forwards({ @Forward(name = "EditReady", path = "/academicAdminOffice/contributor/editContributor.jsp"),
        @Forward(name = "EditSuccess", path = "/academicAdminOffice/contributor/editContributorSuccess.jsp") })
@Exceptions(value = { @ExceptionHandling(type = ExistingActionException.class,
        key = "resources.Action.exceptions.ExistingActionException", handler = FenixErrorExceptionHandler.class,
        scope = "request") })
public class EditContributorDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm editContributorForm = (DynaActionForm) form;

        InfoContributor infoContributor = getInfoContributor(request, editContributorForm);

        editContributorForm.set("contributorNumber", String.valueOf(infoContributor.getContributorNumber()));
        editContributorForm.set("contributorName", infoContributor.getContributorName());
        editContributorForm.set("contributorAddress", infoContributor.getContributorAddress());
        editContributorForm.set("areaCode", infoContributor.getAreaCode());
        editContributorForm.set("areaOfAreaCode", infoContributor.getAreaOfAreaCode());
        editContributorForm.set("area", infoContributor.getArea());
        editContributorForm.set("parishOfResidence", infoContributor.getParishOfResidence());
        editContributorForm.set("districtSubdivisionOfResidence", infoContributor.getDistrictSubdivisionOfResidence());
        editContributorForm.set("districtOfResidence", infoContributor.getDistrictOfResidence());
        editContributorForm.set("contributorId", infoContributor.getExternalId());

        return mapping.findForward("EditReady");

    }

    private InfoContributor getInfoContributor(HttpServletRequest request, DynaActionForm form) {
        final InfoContributor infoContributor = (InfoContributor) request.getAttribute(PresentationConstants.CONTRIBUTOR);

        String id = getStringFromRequest(request, "contributorId");
        if (id == null) {
            id = (String) form.get("contributorId");
        }

        return infoContributor != null ? infoContributor : InfoContributor.newInfoFromDomain((Party) FenixFramework
                .getDomainObject(id));
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm editContributorForm = (DynaActionForm) form;

        InfoContributor infoContributor = getInfoContributor(request, editContributorForm);

        // Get the Information
        String contributorNumberString = (String) editContributorForm.get("contributorNumber");
        Integer contributorNumber = Integer.valueOf(contributorNumberString);
        String contributorName = (String) editContributorForm.get("contributorName");
        String contributorAddress = (String) editContributorForm.get("contributorAddress");
        String areaCode = (String) editContributorForm.get("areaCode");
        String areaOfAreaCode = (String) editContributorForm.get("areaOfAreaCode");
        String area = (String) editContributorForm.get("area");
        String parishOfResidence = (String) editContributorForm.get("parishOfResidence");
        String districtSubdivisionOfResidence = (String) editContributorForm.get("districtSubdivisionOfResidence");
        String districtOfResidence = (String) editContributorForm.get("districtOfResidence");

        InfoContributor newInfoContributor = null;
        try {
            infoContributor.editContributor(contributorNumber, contributorName, contributorAddress, areaCode, areaOfAreaCode,
                    area, parishOfResidence, districtSubdivisionOfResidence, districtOfResidence);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("O Contribuinte", e);
        }

        request.setAttribute(PresentationConstants.CONTRIBUTOR, newInfoContributor);
        return mapping.findForward("EditSuccess");

    }

}