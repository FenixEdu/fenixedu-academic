package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ResultsManagementAction extends FenixDispatchAction {
    public ActionForward backToResult(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	final Result result = readResultFromRequest(request);
	if (result == null) {
	    return backToResultList(mapping, form, request, response);
	}
	
	String forwardTo = null;

	if (result instanceof ResultPatent) {
	    request.setAttribute("patentId", result.getIdInternal());
	    forwardTo = new String("editPatent");
	} else if (result instanceof ResultPublication) {
	    request.setAttribute("publicationId", result.getIdInternal());
	    forwardTo = new String("viewEditPublication");
	}

	return mapping.findForward(forwardTo);
    }

    public ActionForward backToResultList(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	final String resultType = (String) getFromRequest(request, "resultType");
	String forwardTo = null;

	if (!(resultType == null || resultType.equals(""))) {
	    if (resultType.compareTo(ResultPatent.class.getSimpleName()) == 0) {
		forwardTo = new String("listPatents");
	    } else {
		forwardTo = new String("ListPublications");
	    }
	}
	return mapping.findForward(forwardTo);
    }

    public Result readResultFromRequest(HttpServletRequest request) {
	String resultIdStr = (String) getFromRequest(request, "resultId");

	if ((resultIdStr == null || resultIdStr.equals(""))
		&& request.getParameterMap().containsKey("resultId")) {
	    resultIdStr = request.getParameter("resultId");
	}

	Result result = null;
	if (resultIdStr != null && !resultIdStr.equals("")) {
	    try {
		result = Result.readByOid(Integer.valueOf(resultIdStr));
	    } catch (DomainException e) {
		addMessage(request, e.getKey(), e.getArgs());
	    }
	}
	return result;
    }
    
    public Object getRenderedObject() {
	if(RenderUtils.getViewState()==null) {
	    return null;
	}
	
	return RenderUtils.getViewState().getMetaObject().getObject(); 
    }
    
    public ActionForward processException(HttpServletRequest request, ActionMapping mapping,
            ActionForward input, Exception e){
	if (!(e instanceof DomainException)) {
            addMessage(request, e.getMessage());
            e.printStackTrace();
        }
	else {
	    final DomainException ex = (DomainException) e;
	    
	    addMessage(request, ex.getKey(), ex.getArgs());
	    
	    if (RenderUtils.getViewState()!=null){
		ViewDestination destination = RenderUtils.getViewState().getDestination("exception");
		RenderUtils.invalidateViewState();
		if(destination!=null) {
		    return destination.getActionForward();
		}
	    }
	}
	
	return input;
    }
    
    private void addMessage(HttpServletRequest request, String key, String...args){
	ActionMessages messages = getMessages(request);

        if (messages == null) {
            messages = new ActionMessages();
        }

        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key, args));
        saveMessages(request, messages);
    }
}
