package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.space.DeleteSpaceResponsibility;
import net.sourceforge.fenixedu.dataTransferObject.parking.SearchPartyBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.domain.space.SpaceResponsibility;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "SpaceManager", path = "/manageSpaceResponsibility", scope = "session", parameter = "method")
@Forwards(value = {
        @Forward(name = "manageResponsabilityInterval", path = "/spaceManager/manageResponsabilityInterval.jsp",
                tileProperties = @Tile(title = "private.spacemanagement.searchspaces")),
        @Forward(name = "showSpaceResponsibility", path = "/spaceManager/spaceResponsibilityManagement.jsp",
                tileProperties = @Tile(title = "private.spacemanagement.searchspaces")) })
public class ManageSpaceResponsibilityDA extends FenixDispatchAction {

    public ActionForward showSpaceResponsibility(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        readAndSetAllAttributes(request);
        return mapping.findForward("showSpaceResponsibility");
    }

    public ActionForward deleteSpaceResponsibility(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        SpaceResponsibility spaceResponsibility = getSpaceResponsibility(request);

        try {
            DeleteSpaceResponsibility.run(spaceResponsibility);
        } catch (DomainException domainException) {
            saveMessages(request, domainException);
        }

        return showSpaceResponsibility(mapping, form, request, response);
    }

    private void readAndSetAllAttributes(HttpServletRequest request) throws FenixServiceException {

        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceInformation(request, spaceInformation);
        request.setAttribute("possibleInternalUnits", UnitUtils.readAllInternalActiveUnitsThatCanBeResponsibleOfSpaces());
        request.setAttribute("searchExternalPartyBean", new SearchPartyBean());
    }

    public ActionForward prepareEditSpaceResponsibility(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceInformation(request, spaceInformation);
        SpaceResponsibility spaceResponsibility = getSpaceResponsibility(request);
        request.setAttribute("spaceResponsibility", spaceResponsibility);
        request.setAttribute("unit", spaceResponsibility.getUnit());
        return mapping.findForward("manageResponsabilityInterval");
    }

    public ActionForward manageResponsabilityInterval(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceInformation(request, spaceInformation);
        Unit responsibleUnit = getResponsibleUnit(request);
        request.setAttribute("unit", responsibleUnit);
        return mapping.findForward("manageResponsabilityInterval");
    }

    public ActionForward prepareAddExternalUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceInformation(request, spaceInformation);
        Unit responsibleUnit = getExternalResponsibleUnit(request);
        if (responsibleUnit == null) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage("error.space.responsability.empty.external.unit"));
            saveMessages(request, actionMessages);
            return showSpaceResponsibility(mapping, form, request, response);
        }
        request.setAttribute("unit", responsibleUnit);
        return mapping.findForward("manageResponsabilityInterval");
    }

    private Unit getExternalResponsibleUnit(HttpServletRequest request) {
        IViewState viewState = RenderUtils.getViewState();
        SearchPartyBean bean = (SearchPartyBean) viewState.getMetaObject().getObject();
        return (Unit) bean.getParty();
    }

    private void saveMessages(HttpServletRequest request, DomainException e) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("", new ActionMessage(e.getMessage(), e.getArgs()));
        saveMessages(request, actionMessages);
    }

    private void setSpaceInformation(HttpServletRequest request, final SpaceInformation spaceInformation) {
        request.setAttribute("selectedSpaceInformation", spaceInformation);
        request.setAttribute("selectedSpace", spaceInformation.getSpace());
    }

    private SpaceInformation getSpaceInformationFromParameter(final HttpServletRequest request) {
        final String spaceInformationIDString =
                request.getParameterMap().containsKey("spaceInformationID") ? request.getParameter("spaceInformationID") : (String) request
                        .getAttribute("spaceInformationID");
        return AbstractDomainObject.fromExternalId(spaceInformationIDString);
    }

    private SpaceResponsibility getSpaceResponsibility(final HttpServletRequest request) {
        final String spaceResponsibilityIDString =
                request.getParameterMap().containsKey("spaceResponsibilityID") ? request.getParameter("spaceResponsibilityID") : (String) request
                        .getAttribute("spaceResponsibilityID");
        return (SpaceResponsibility) AbstractDomainObject.fromExternalId(spaceResponsibilityIDString);
    }

    private Unit getResponsibleUnit(final HttpServletRequest request) {
        final String unitIDString =
                request.getParameterMap().containsKey("unitID") ? request.getParameter("unitID") : (String) request
                        .getAttribute("unitID");
        return (Unit) AbstractDomainObject.fromExternalId(unitIDString);
    }
}