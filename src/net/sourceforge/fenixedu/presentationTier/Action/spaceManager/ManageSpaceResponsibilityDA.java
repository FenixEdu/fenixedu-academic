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

public class ManageSpaceResponsibilityDA extends FenixDispatchAction {

    public ActionForward showSpaceResponsibility(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {

        readAndSetAllAttributes(request);
        return mapping.findForward("showSpaceResponsibility");
    }

    public ActionForward deleteSpaceResponsibility(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {

        SpaceResponsibility spaceResponsibility = getSpaceResponsibility(request);
        Object[] args = { spaceResponsibility };
        try {
            ServiceUtils.executeService(getUserView(request), "DeleteSpaceResponsibility", args);
        } catch (DomainException domainException) {
            saveActionMessageOnRequest(request, domainException.getKey(), domainException.getArgs());
        }

        readAndSetAllAttributes(request);
        return mapping.findForward("showSpaceResponsibility");
    }

    private void readAndSetAllAttributes(HttpServletRequest request) throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {
        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceInformation(request, spaceInformation);
        readAndSetInitialUnit(request);
        request.setAttribute("searchExternalPartyBean", new SearchPartyBean());
    }

    public ActionForward prepareEditSpaceResponsibility(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {

        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceInformation(request, spaceInformation);
        SpaceResponsibility spaceResponsibility = getSpaceResponsibility(request);
        request.setAttribute("spaceResponsibility", spaceResponsibility);
        return mapping.findForward("manageResponsabilityInterval");
    }

    public ActionForward manageResponsabilityInterval(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {

        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceInformation(request, spaceInformation);
        Unit responsibleUnit = getResponsibleUnit(request);
        request.setAttribute("unit", responsibleUnit);
        return mapping.findForward("manageResponsabilityInterval");
    }
    
    public ActionForward prepareAddExternalUnit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, ExcepcaoPersistencia {

        SpaceInformation spaceInformation = getSpaceInformationFromParameter(request);
        setSpaceInformation(request, spaceInformation);
        Unit responsibleUnit = getExternalResponsibleUnit(request);
        request.setAttribute("unit", responsibleUnit);
        return mapping.findForward("manageResponsabilityInterval");
    }

    private Unit getExternalResponsibleUnit(HttpServletRequest request) {
	IViewState summaryViewState = RenderUtils.getViewState();
	SearchPartyBean bean = (SearchPartyBean) summaryViewState.getMetaObject().getObject();	
	return (Unit) bean.getParty();
    }

    private void saveActionMessageOnRequest(HttpServletRequest request, String errorKey, String[] args) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add(errorKey, new ActionMessage(errorKey, args));
        saveMessages(request, actionMessages);
    }

    private void readAndSetInitialUnit(HttpServletRequest request)
            throws FenixFilterException, FenixServiceException, ExcepcaoPersistencia {       
        request.setAttribute("initialUnit", UnitUtils.readInstitutionUnit());
    }

    private void setSpaceInformation(HttpServletRequest request, final SpaceInformation spaceInformation) {
        request.setAttribute("selectedSpaceInformation", spaceInformation);
        request.setAttribute("selectedSpace", spaceInformation.getSpace());
    }

    private SpaceInformation getSpaceInformationFromParameter(final HttpServletRequest request) {
        final String spaceInformationIDString = request.getParameterMap().containsKey(
                "spaceInformationID") ? request.getParameter("spaceInformationID") : (String) request
                .getAttribute("spaceInformationID");
        final Integer spaceInformationID = spaceInformationIDString != null ? Integer
                .valueOf(spaceInformationIDString) : null;
        return rootDomainObject.readSpaceInformationByOID(spaceInformationID);
    }

    private SpaceResponsibility getSpaceResponsibility(final HttpServletRequest request) {
        final String spaceResponsibilityIDString = request.getParameterMap().containsKey(
                "spaceResponsibilityID") ? request.getParameter("spaceResponsibilityID")
                : (String) request.getAttribute("spaceResponsibilityID");
        final Integer spaceResponsibilityID = spaceResponsibilityIDString != null ? Integer
                .valueOf(spaceResponsibilityIDString) : null;
        return rootDomainObject.readSpaceResponsibilityByOID(spaceResponsibilityID);
    }

    private Unit getResponsibleUnit(final HttpServletRequest request) {
        final String unitIDString = request.getParameterMap().containsKey("unitID") ? request
                .getParameter("unitID") : (String) request.getAttribute("unitID");
        final Integer unitID = unitIDString != null ? Integer.valueOf(unitIDString) : null;
        return (Unit) rootDomainObject.readPartyByOID(unitID);
    }
}
