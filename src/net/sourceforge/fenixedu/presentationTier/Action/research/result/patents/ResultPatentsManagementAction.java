package net.sourceforge.fenixedu.presentationTier.Action.research.result.patents;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import net.sourceforge.fenixedu.presentationTier.Action.research.result.ResultsManagementAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultPatentsManagementAction extends ResultsManagementAction {

    public ActionForward management(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	
	/*for(Message message : RenderUtils.getViewState().getMessages()) {
	   addActionMessage(request, message.getMessage());
	}*/
	
	request.setAttribute("resultPatents", getLoggedPerson(request).getResearchResultPatents());
	
	return mapping.findForward("listPatents");
    }
    
    public ActionForward prepareDetails(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	final ResearchResultPatent patent = (ResearchResultPatent) (ResearchResultPatent)getResultFromRequest(request);
	if (patent == null) {
	    return management(mapping, form, request, response);
	}
	
	return mapping.findForward("patentDetails");
    }

    public ActionForward createPatent(ActionMapping mapping, ActionForm form,
    	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
    	    FenixServiceException {
    	
    	final ResearchResultPatent patent = (ResearchResultPatent)getResultFromRequest(request);
    	Object[] args = { patent };
    	executeService(request, "AddDefaultDocumentToResearchResult", args);
    	
    	return showPatent(mapping, form, request, response);
    }
    public ActionForward showPatent(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	
	final ResearchResultPatent patent = (ResearchResultPatent)getResultFromRequest(request);
	if (patent == null) {
	    return management(mapping, form, request, response);
	}
	
	if (!patent.hasPersonParticipation(getLoggedPerson(request))) {
	    addActionMessage(request, "researcher.ResultParticipation.last.participation.warning");
	}

	return mapping.findForward("editPatent");
    }
    
    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("createPatent");
    }

    public ActionForward prepareEditData(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final ResearchResultPatent patent = (ResearchResultPatent)getResultFromRequest(request);
	if (patent == null) {
	    return management(mapping, form, request, response);
	}

	return mapping.findForward("editPatentData");
    }
    
    public ActionForward updateMetaInformation(ActionMapping mapping, ActionForm form,
    	    HttpServletRequest request, HttpServletResponse response) throws Exception {
    	final ResearchResultPatent patent = (ResearchResultPatent)getResultFromRequest(request);
    	if (patent == null) {
    	    return management(mapping, form, request, response);
    	}

    	executeService("UpdateMetaInformation", patent);
    	return showPatent(mapping, form, request, response);
      }

    public ActionForward prepareDelete(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final ResearchResultPatent patent = (ResearchResultPatent)getResultFromRequest(request);
	if (patent == null) {
	    return management(mapping, form, request, response);
	}
	request.setAttribute("confirm", "yes");
	return mapping.findForward("editPatent");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	final Integer resultId = getRequestParameterAsInteger(request, "resultId");
	
	
	if(getFromRequest(request, "cancel") != null) {
		ResearchResultPatent patent = (ResearchResultPatent) getResultByIdFromRequest(request);
		request.setAttribute("result",patent);	
		request.setAttribute("resultId",resultId);
		return mapping.findForward("editPatent");
	}
	
	if(getFromRequest(request, "confirm")!=null) {
            try {
                final Object[] args = { resultId };
                executeService(request, "DeleteResultPatent", args);
            } catch (Exception e) {
                final ActionForward defaultForward = management(mapping, form, request, response);
                return processException(request, mapping, defaultForward, e);
            }
	}

	return management(mapping, form, request, response);
    }

    @Override
    public ResearchResultPatent getRenderedObject(String id) {
	return (ResearchResultPatent) super.getRenderedObject(id);
    }
}