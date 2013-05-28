package net.sourceforge.fenixedu.presentationTier.Action.manager.organizationalStructure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement.MergeExternalUnits;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "manager", path = "/unitsMerge", attribute = "unitsMergeForm", formBean = "unitsMergeForm", scope = "request",
        parameter = "method")
@Forwards(value = {
        @Forward(name = "chooseUnit", path = "/manager/organizationalStructureManagament/mergeUnits/chooseUnitToStart.jsp"),
        @Forward(name = "seeChoosedUnit", path = "/manager/organizationalStructureManagament/mergeUnits/choosedUnit.jsp"),
        @Forward(name = "goToConfirmation", path = "/manager/organizationalStructureManagament/mergeUnits/confirmation.jsp") })
public class ExternalUnitsMergeDA extends FenixDispatchAction {

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
            HttpServletResponse response) throws  FenixServiceException {

        Unit fromUnit = null, destinationUnit = null;
        IViewState viewState = RenderUtils.getViewState("noOfficialMerge");

        if (viewState != null) {
            fromUnit = getFromUnitFromParameter(request);
            destinationUnit = getDestinationUnitFromParameter(request);
        } else {
            DynaActionForm dynaActionForm = (DynaActionForm) form;
            Integer fromUnitID = (Integer) dynaActionForm.get("fromUnitID");
            Integer destinationUnitID = (Integer) dynaActionForm.get("destinationUnitID");
            fromUnit = (Unit) AbstractDomainObject.fromExternalId(fromUnitID);
            destinationUnit = (Unit) AbstractDomainObject.fromExternalId(destinationUnitID);
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
        final Integer uniID = Integer.valueOf(unitIDString);
        return (Unit) AbstractDomainObject.fromExternalId(uniID);
    }

    private Unit getDestinationUnitFromParameter(final HttpServletRequest request) {
        final String unitIDString = request.getParameter("unitID");
        final Integer uniID = Integer.valueOf(unitIDString);
        return (Unit) AbstractDomainObject.fromExternalId(uniID);
    }

    private void saveMessages(HttpServletRequest request, DomainException e) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("", new ActionMessage(e.getMessage(), e.getArgs()));
        saveMessages(request, actionMessages);
    }
}