package net.sourceforge.fenixedu.presentationTier.Action.research.result.patents;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent;
import net.sourceforge.fenixedu.presentationTier.Action.research.result.ResultsManagementAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultPatentsManagementAction extends ResultsManagementAction {

    public ActionForward management(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	final List<ResultPatent> resultPatents = new ArrayList<ResultPatent>();

	for (ResultParticipation participation : getLoggedPerson(request)
		.getPersonParticipationsWithPatents()) {
	    resultPatents.add((ResultPatent) participation.getResult());
	}
	request.setAttribute("resultPatents", resultPatents);
	return mapping.findForward("listPatents");
    }

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("createPatent");
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	final ResultPatent patent = readPatentFromRequest(request);

	if (patent == null) {
	    return management(mapping, form, request, response);
	}

	if (!patent.hasPersonParticipation(getLoggedPerson(request))) {
	    addActionMessage(request, "researcher.ResultParticipation.last.participation.warning");
	}

	return mapping.findForward("editPatent");
    }

    public ActionForward prepareEditData(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final ResultPatent patent = readPatentFromRequest(request);
	if (patent == null) {
	    return management(mapping, form, request, response);
	}

	return mapping.findForward("editPatentData");
    }

    public ActionForward prepareDetails(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final ResultPatent patent = (ResultPatent) readPatentFromRequest(request);
	if (patent == null) {
	    return management(mapping, form, request, response);
	}

	return mapping.findForward("patentDetails");
    }

    public ActionForward prepareDelete(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final String from = (String) getFromRequest(request, "from");
	final ResultPatent patent = readPatentFromRequest(request);
	if (patent == null) {
	    return management(mapping, form, request, response);
	}

	if (from != null && !from.equals("")) {
	    request.setAttribute("from", from);
	}

	return mapping.findForward("deletePatent");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	final Integer resultId = getRequestParameterAsInteger(request, "resultId");

	try {
	    final Object[] args = { resultId };
	    executeService(request, "DeleteResultPatent", args);
	} catch (Exception e) {
	    final ActionForward defaultForward = management(mapping, actionForm, request, response);
	    return processException(request, mapping, defaultForward, e);
	}

	return management(mapping, actionForm, request, response);
    }

    private ResultPatent readPatentFromRequest(HttpServletRequest request) {
	ResultPatent patent = (ResultPatent) readResultFromRequest(request);

	if (patent == null) {
	    try {
		patent = getRenderedObject();
	    } catch (Exception e) {
	    }
	}

	if (patent == null) {
	    addActionMessage(request, "error.researcher.Result.null");
	} else {
	    request.setAttribute("patent", patent);
	}
	return patent;
    }

    @Override
    public ResultPatent getRenderedObject() {
	return (ResultPatent) super.getRenderedObject();
    }
}