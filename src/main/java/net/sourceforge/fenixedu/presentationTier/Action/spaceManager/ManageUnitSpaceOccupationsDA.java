package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.space.DeleteUnitSpaceOccupation;
import net.sourceforge.fenixedu.dataTransferObject.parking.SearchPartyBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.domain.space.UnitSpaceOccupation;
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

@Mapping(module = "SpaceManager", path = "/manageUnitSpaceOccupations", scope = "session", parameter = "method")
@Forwards(value = {
        @Forward(name = "prepareManageUnitSpaceOccupationInterval", path = "/spaceManager/manageUnitSpaceOccupationInterval.jsp",
                tileProperties = @Tile(title = "private.spacemanagement.searchspaces")),
        @Forward(name = "prepareManageUnitSpaceOccupations", path = "/spaceManager/manageUnitSpaceOccupations.jsp",
                tileProperties = @Tile(title = "private.spacemanagement.searchspaces")) })
public class ManageUnitSpaceOccupationsDA extends FenixDispatchAction {

    public ActionForward prepareManageUnitSpaceOccupations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        readAndSetAllAttributes(request);
        return mapping.findForward("prepareManageUnitSpaceOccupations");
    }

    public ActionForward prepareManageUnitOccupationInterval(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceInformation(request, spaceInformation);
        Unit responsibleUnit = getUnit(request);
        request.setAttribute("unit", responsibleUnit);
        return mapping.findForward("prepareManageUnitSpaceOccupationInterval");
    }

    public ActionForward prepareAddExternalUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceInformation(request, spaceInformation);
        Unit responsibleUnit = getExternalResponsibleUnit(request);
        if (responsibleUnit == null) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage("error.unit.space.occupation.empty.external.unit"));
            saveMessages(request, actionMessages);
            return prepareManageUnitSpaceOccupations(mapping, form, request, response);
        }
        request.setAttribute("unit", responsibleUnit);
        return mapping.findForward("prepareManageUnitSpaceOccupationInterval");
    }

    public ActionForward deleteUnitSpaceOccupation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        UnitSpaceOccupation unitSpaceOccupation = getUnitSpaceOccupation(request);

        try {
            DeleteUnitSpaceOccupation.run(unitSpaceOccupation);
        } catch (DomainException domainException) {
            saveMessages(request, domainException);
        }

        return prepareManageUnitSpaceOccupations(mapping, form, request, response);
    }

    public ActionForward prepareEditUnitSpaceOccupation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceInformation(request, spaceInformation);
        UnitSpaceOccupation unitSpaceOccupation = getUnitSpaceOccupation(request);
        request.setAttribute("unitSpaceOccupation", unitSpaceOccupation);
        request.setAttribute("unit", unitSpaceOccupation.getUnit());
        return mapping.findForward("prepareManageUnitSpaceOccupationInterval");
    }

    private SpaceInformation getSpaceInformationFromParameter(final HttpServletRequest request) {
        final String spaceInformationIDString =
                request.getParameterMap().containsKey("spaceInformationID") ? request.getParameter("spaceInformationID") : (String) request
                        .getAttribute("spaceInformationID");
        return AbstractDomainObject.fromExternalId(spaceInformationIDString);
    }

    private void readAndSetAllAttributes(HttpServletRequest request) throws FenixServiceException {

        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        request.setAttribute("selectedSpaceInformation", spaceInformation);
        request.setAttribute("selectedSpace", spaceInformation.getSpace());
        request.setAttribute("initialUnit", UnitUtils.readInstitutionUnit());
        request.setAttribute("searchExternalPartyBean", new SearchPartyBean());
    }

    private Unit getUnit(final HttpServletRequest request) {
        final String unitIDString =
                request.getParameterMap().containsKey("unitID") ? request.getParameter("unitID") : (String) request
                        .getAttribute("unitID");
        return (Unit) AbstractDomainObject.fromExternalId(unitIDString);
    }

    private UnitSpaceOccupation getUnitSpaceOccupation(final HttpServletRequest request) {
        final String unitSpaceOccupationIDString =
                request.getParameterMap().containsKey("unitSpaceOccupationID") ? request.getParameter("unitSpaceOccupationID") : (String) request
                        .getAttribute("unitSpaceOccupationID");
        return (UnitSpaceOccupation) AbstractDomainObject.fromExternalId(unitSpaceOccupationIDString);
    }

    private void setSpaceInformation(HttpServletRequest request, final SpaceInformation spaceInformation) {
        request.setAttribute("selectedSpaceInformation", spaceInformation);
        request.setAttribute("selectedSpace", spaceInformation.getSpace());
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

}