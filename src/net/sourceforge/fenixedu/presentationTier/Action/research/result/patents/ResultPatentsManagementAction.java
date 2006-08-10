package net.sourceforge.fenixedu.presentationTier.Action.research.result.patents;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent;
import net.sourceforge.fenixedu.presentationTier.Action.research.result.ResultsManagementAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultPatentsManagementAction extends ResultsManagementAction {

    public ActionForward listPatents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final List<ResultPatent> resultPatents = new ArrayList<ResultPatent>();
        
        for(ResultParticipation participation : getUserView(request).getPerson().getPersonParticipationsWithPatents()) {
            resultPatents.add((ResultPatent)participation.getResult());
        }
        request.setAttribute("resultPatents", resultPatents);
        return mapping.findForward("listPatents");
    }

    public ActionForward prepareCreatePatent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("createPatent");
    }

    public ActionForward prepareEditPatent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final ResultPatent patent = readPatentFromRequest(request);

        if(patent == null) {
            return listPatents(mapping, form, request, response);
        }
        
        final Person person = getUserView(request).getPerson();
        if(!patent.hasPersonParticipation(person)){
            addActionMessage(request,"researcher.result.editResult.participation.warning");
        }

        return mapping.findForward("editPatent");
    }
    
    public ActionForward prepareEditPatentData(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final ResultPatent patent = readPatentFromRequest(request);
        if (patent == null) {
            return listPatents(mapping, form, request, response);
        }
        
        return mapping.findForward("editPatentData");
    }
    
    public ActionForward preparePatentDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final ResultPatent patent = (ResultPatent) readPatentFromRequest(request);
        if (patent == null) {
            return listPatents(mapping, form, request, response);
        }
        
        return mapping.findForward("patentDetails");
    }

    public ActionForward prepareDeletePatent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String from = request.getParameter("from");
        final ResultPatent patent = readPatentFromRequest(request);
        if (patent == null) {
            return listPatents(mapping, form, request, response);
        }
        
        if(from != null && !from.equals("")){
            request.setAttribute("from", from);
        }       
        
        return mapping.findForward("deletePatent");
    }

    public ActionForward deletePatent(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        
        try {
			ResultPatent.remove(resultId);
			addActionMessage(request,"researcher.result.deleted");
		} catch (Exception e) {
			addActionMessage(request, e.getMessage());
		} 
        
        return listPatents(mapping, actionForm, request, response);
    }
    
    private ResultPatent readPatentFromRequest(HttpServletRequest request) {
    	ResultPatent patent = (ResultPatent) readResultFromRequest(request);
    	
    	if (patent==null) {
    		try {
    			patent = (ResultPatent) RenderUtils.getViewState().getMetaObject().getObject();	
    		} catch (Exception e) {}
    	}
        
        if (patent == null) {
        	addActionMessage(request,"error.Result.not.found");
        }
        else {
            request.setAttribute("patent", patent);    
        }
        
        return patent;
    }
}