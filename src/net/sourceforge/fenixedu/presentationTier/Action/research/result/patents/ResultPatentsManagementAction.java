package net.sourceforge.fenixedu.presentationTier.Action.research.result.patents;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent;
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
	
	request.setAttribute("resultPatents", getLoggedPerson(request).getResultPatents());
	
	return mapping.findForward("listPatents");
    }
    
    public ActionForward prepareDetails(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	final ResultPatent patent = (ResultPatent) (ResultPatent)getResultFromRequest(request);
	if (patent == null) {
	    return management(mapping, form, request, response);
	}
	
	return mapping.findForward("patentDetails");
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	
	final ResultPatent patent = (ResultPatent)getResultFromRequest(request);
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
	final ResultPatent patent = (ResultPatent)getResultFromRequest(request);
	if (patent == null) {
	    return management(mapping, form, request, response);
	}

	return mapping.findForward("editPatentData");
    }

    public ActionForward prepareDelete(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final ResultPatent patent = (ResultPatent)getResultFromRequest(request);
	if (patent == null) {
	    return management(mapping, form, request, response);
	}

	return mapping.findForward("deletePatent");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	final Integer resultId = getRequestParameterAsInteger(request, "resultId");

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
    public ResultPatent getRenderedObject(String id) {
	return (ResultPatent) super.getRenderedObject(id);
    }
}