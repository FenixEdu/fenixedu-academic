package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.parking.SearchPartyBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.domain.space.SpaceResponsibility;
import net.sourceforge.fenixedu.domain.space.UnitSpaceOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ManageUnitSpaceOccupationsDA extends FenixDispatchAction {

    public ActionForward prepareManageUnitSpaceOccupations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException, ExcepcaoPersistencia {

	readAndSetAllAttributes(request);
	return mapping.findForward("prepareManageUnitSpaceOccupations");
    }

    public ActionForward prepareManageUnitOccupationInterval(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException, ExcepcaoPersistencia {

	SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
	setSpaceInformation(request, spaceInformation);
	Unit responsibleUnit = getUnit(request);
	request.setAttribute("unit", responsibleUnit);
	return mapping.findForward("prepareManageUnitSpaceOccupationInterval");
    }

    public ActionForward prepareAddExternalUnit(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException, ExcepcaoPersistencia {

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

    public ActionForward deleteUnitSpaceOccupation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException, ExcepcaoPersistencia {

	UnitSpaceOccupation unitSpaceOccupation = getUnitSpaceOccupation(request);
	Object[] args = { unitSpaceOccupation };
	try {
	    ServiceUtils.executeService(getUserView(request), "DeleteUnitSpaceOccupation", args);
	} catch (DomainException domainException) {
	    saveMessages(request, domainException);
	}

	return prepareManageUnitSpaceOccupations(mapping, form, request, response);
    }

    public ActionForward prepareEditUnitSpaceOccupation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException, ExcepcaoPersistencia {

	SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
	setSpaceInformation(request, spaceInformation);
	UnitSpaceOccupation unitSpaceOccupation = getUnitSpaceOccupation(request);
	request.setAttribute("unitSpaceOccupation", unitSpaceOccupation);
	request.setAttribute("unit", unitSpaceOccupation.getUnit());
	return mapping.findForward("prepareManageUnitSpaceOccupationInterval");
    }
     
    private SpaceInformation getSpaceInformationFromParameter(final HttpServletRequest request) {
	final String spaceInformationIDString = request.getParameterMap().containsKey(
		"spaceInformationID") ? request.getParameter("spaceInformationID") : (String) request
		.getAttribute("spaceInformationID");
	final Integer spaceInformationID = spaceInformationIDString != null ? Integer
		.valueOf(spaceInformationIDString) : null;
	return rootDomainObject.readSpaceInformationByOID(spaceInformationID);
    }

    private void readAndSetAllAttributes(HttpServletRequest request) throws FenixFilterException,
	    FenixServiceException, ExcepcaoPersistencia {

	SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
	request.setAttribute("selectedSpaceInformation", spaceInformation);
	request.setAttribute("selectedSpace", spaceInformation.getSpace());
	request.setAttribute("initialUnit", UnitUtils.readInstitutionUnit());
	request.setAttribute("searchExternalPartyBean", new SearchPartyBean());
    }

    private Unit getUnit(final HttpServletRequest request) {
	final String unitIDString = request.getParameterMap().containsKey("unitID") ? request
		.getParameter("unitID") : (String) request.getAttribute("unitID");
	final Integer unitID = unitIDString != null ? Integer.valueOf(unitIDString) : null;
	return (Unit) rootDomainObject.readPartyByOID(unitID);
    }

    private UnitSpaceOccupation getUnitSpaceOccupation(final HttpServletRequest request) {
	final String unitSpaceOccupationIDString = request.getParameterMap().containsKey(
		"unitSpaceOccupationID") ? request.getParameter("unitSpaceOccupationID")
		: (String) request.getAttribute("unitSpaceOccupationID");
	final Integer unitSpaceOccupationID = unitSpaceOccupationIDString != null ? Integer
		.valueOf(unitSpaceOccupationIDString) : null;
	return (UnitSpaceOccupation) rootDomainObject.readSpaceOccupationByOID(unitSpaceOccupationID);
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
