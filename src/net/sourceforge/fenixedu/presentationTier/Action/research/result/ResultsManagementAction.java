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
	final Result result = getResultFromRequest(request);
	if (result == null) {
	    return backToResultList(mapping, form, request, response);
	}
	
	request.setAttribute("resultId", result.getIdInternal());
	if (result instanceof ResultPatent) {
	    return mapping.findForward("editPatent");    
	} else if (result instanceof ResultPublication) {
	    return mapping.findForward("viewEditPublication");	   
	}
	return null;
    }

    public ActionForward backToResultList(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	final String resultType = (String) getFromRequest(request, "resultType");

	if (!(resultType == null || resultType.equals(""))) {
	    if (resultType.compareTo(ResultPatent.class.getSimpleName()) == 0) {
		return mapping.findForward("listPatents");
	    } 
	    return mapping.findForward("ListPublications");
	}
	return null;
    }

    private Result getResultByIdFromRequest(HttpServletRequest request) {
	final Integer resultId = Integer.valueOf(getFromRequest(request, "resultId").toString());

	if(resultId!=null) {
	    try {
		return Result.readByOid(resultId);
	    } catch (DomainException e) {
		addMessage(request, e.getKey(), e.getArgs());
	    }
	}
	return null;
    }
    
    public final Result getResultFromRequest(HttpServletRequest request) {
	Result result = null;
	try {
	    result = getResultByIdFromRequest(request);
	} catch (Exception e) {
	}
	
	if (result==null) {
	    try {
		result = (Result)getRenderedObject(null);
	    } catch (Exception e) {
	    }
	}

	if (result != null)
	    request.setAttribute("result", result);

	return result;
    }
    
    public Object getRenderedObject(String id) {
	if(id==null || id.equals("")){
	    if (RenderUtils.getViewState()!=null) {
		return RenderUtils.getViewState().getMetaObject().getObject();
	    }
	}
	else {
	    if(RenderUtils.getViewState(id)!=null) {
		return RenderUtils.getViewState(id).getMetaObject().getObject();
	    }
	}
	return null; 
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
