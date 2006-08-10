package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultsManagementAction extends FenixDispatchAction {
    public ActionForward backToResult(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final Result result = readResultFromRequest(request);
        if(result==null){
        	addActionMessage(request,"error.Result.not.found");
            return backToResultList(mapping, form, request, response);
        }
        
        String forwardTo = null;
        
        if (result instanceof ResultPatent){
            request.setAttribute("patentId", result.getIdInternal());
            forwardTo = new String("editPatent");    
        }
        else if (result instanceof ResultPublication){
            request.setAttribute("publicationId", result.getIdInternal());
            forwardTo = new String("viewEditPublication");
        }
        
        return mapping.findForward(forwardTo);
    }
    
    public ActionForward backToResultList(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final String resultType = request.getParameterMap().containsKey("resultType") ?
                request.getParameter("resultType") : (String) request.getAttribute("resultType");
        String forwardTo = null;
        
        if (!(resultType == null || resultType.equals(""))) {
            if(resultType.compareTo(ResultPatent.class.getSimpleName())==0){
                forwardTo = new String("listPatents");
            }
            else {
                forwardTo = new String("ListPublications");
            }
        }
        return mapping.findForward(forwardTo);
    }
    
    public Result readResultFromRequest(HttpServletRequest request) {
    	String resultIdStr = (String) request.getAttribute("resultId");
        
        if((resultIdStr==null || resultIdStr.equals("")) && request.getParameterMap().containsKey("resultId")) {
        	resultIdStr = request.getParameter("resultId");
        }
        
        Result result = null;
        if(resultIdStr!=null && !resultIdStr.equals("")){
        	result = rootDomainObject.readResultByOID(Integer.valueOf(resultIdStr));	
        }
        return result;
    }
}
