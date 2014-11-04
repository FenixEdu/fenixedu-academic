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
package org.fenixedu.academic.ui.struts.action.manager.organizationalStructure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.manager.organizationalStructureManagement.MergeExternalUnits;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UnitUtils;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerOrganizationalStructureApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ManagerOrganizationalStructureApp.class, path = "units-merge", titleKey = "title.units.merge")
@Mapping(module = "manager", path = "/unitsMerge", formBean = "unitsMergeForm")
@Forwards(value = {
        @Forward(name = "chooseUnit", path = "/manager/organizationalStructureManagament/mergeUnits/chooseUnitToStart.jsp"),
        @Forward(name = "seeChoosedUnit", path = "/manager/organizationalStructureManagament/mergeUnits/choosedUnit.jsp"),
        @Forward(name = "goToConfirmation", path = "/manager/organizationalStructureManagament/mergeUnits/confirmation.jsp") })
public class ExternalUnitsMergeDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward chooseUnitToStart(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Unit externalInstitutionUnit = UnitUtils.readExternalInstitutionUnit();
        request.setAttribute("externalInstitutionUnit", externalInstitutionUnit);
        return mapping.findForward("chooseUnit");
    }

    public ActionForward seeChoosedUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Unit externalUnit = getDestinationUnitFromParameter(request);
        if (externalUnit != null) {
            Unit earthUnit = UnitUtils.readEarthUnit();
            Unit externalInstitutionUnit = UnitUtils.readExternalInstitutionUnit();
            request.setAttribute("externalInstitutionUnit", externalInstitutionUnit);
            request.setAttribute("externalUnit", externalUnit);
            request.setAttribute("earthUnit", earthUnit);
        }

        return mapping.findForward("seeChoosedUnit");
    }

    public ActionForward mergeWithOfficial(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        readAndSetUnitsToMerge(request);
        request.setAttribute("official", true);
        return mapping.findForward("goToConfirmation");
    }

    public ActionForward mergeWithNoOfficialUnits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        readAndSetUnitsToMerge(request);
        request.setAttribute("official", false);
        return mapping.findForward("goToConfirmation");
    }

    public ActionForward mergeUnits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        Unit fromUnit = null, destinationUnit = null;
        IViewState viewState = RenderUtils.getViewState("noOfficialMerge");

        if (viewState != null) {
            fromUnit = getFromUnitFromParameter(request);
            destinationUnit = getDestinationUnitFromParameter(request);
        } else {
            DynaActionForm dynaActionForm = (DynaActionForm) form;
            fromUnit = getDomainObject(dynaActionForm, "fromUnitID");
            destinationUnit = getDomainObject(dynaActionForm, "destinationUnitID");
        }

        try {
            MergeExternalUnits.run(fromUnit, destinationUnit, Boolean.TRUE);
        } catch (DomainException e) {
            saveMessages(request, e);
            return returnToConfirmationPage(mapping, request, fromUnit, destinationUnit);
        }

        return chooseUnitToStart(mapping, form, request, response);
    }

    // Private methods

    private ActionForward returnToConfirmationPage(ActionMapping mapping, HttpServletRequest request, Unit fromUnit,
            Unit destinationUnit) {
        request.setAttribute("fromUnit", fromUnit);
        request.setAttribute("destinationUnit", destinationUnit);
        if (destinationUnit.isNoOfficialExternal()) {
            request.setAttribute("official", false);
        } else {
            request.setAttribute("official", false);
        }
        return mapping.findForward("goToConfirmation");
    }

    private void readAndSetUnitsToMerge(HttpServletRequest request) {
        Unit fromUnit = getFromUnitFromParameter(request);
        Unit destinationUnit = getDestinationUnitFromParameter(request);
        request.setAttribute("fromUnit", fromUnit);
        request.setAttribute("destinationUnit", destinationUnit);
    }

    private Unit getFromUnitFromParameter(final HttpServletRequest request) {
        final String unitIDString = request.getParameter("fromUnitID");
        return (Unit) FenixFramework.getDomainObject(unitIDString);
    }

    private Unit getDestinationUnitFromParameter(final HttpServletRequest request) {
        final String unitIDString = request.getParameter("unitID");
        return (Unit) FenixFramework.getDomainObject(unitIDString);
    }

    private void saveMessages(HttpServletRequest request, DomainException e) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("", new ActionMessage(e.getMessage(), e.getArgs()));
        saveMessages(request, actionMessages);
    }
}