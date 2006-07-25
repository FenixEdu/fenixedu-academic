package net.sourceforge.fenixedu.presentationTier.Action.research.result.patents;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultPatentsManagementAction extends FenixDispatchAction {

    public ActionForward listPatents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final List<ResultPatent> resultPatents = new ArrayList<ResultPatent>();
        
        for(ResultParticipation participation : getUserView(request).getPerson().getPersonParticipationsWithPatents()) {
            resultPatents.add((ResultPatent)participation.getResult());
        }
        request.setAttribute("resultPatents", resultPatents);
        return mapping.findForward("listPatents");
    }

    public ActionForward prepareCreatePatent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final List<Person> participationsList = new ArrayList<Person>();

        participationsList.add(getUserView(request).getPerson());
        request.setAttribute("participationsList", participationsList);
        
        return mapping.findForward("createPatent");
    }

    public ActionForward prepareEditPatent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
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
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Object[] args = { Integer.valueOf(request.getParameter("resultId")) };
        
        try{
            ServiceManagerServiceFactory.executeService(getUserView(request), "DeleteResultPatent", args);
            addActionMessage(request,"researcher.result.deleted");
        }
        catch (FenixServiceException e) {
            addActionMessage(request,"researcher.result.error.resultNotFound");
        }

        return listPatents(mapping, actionForm, request, response);
    }
    
    private ResultPatent readPatentFromRequest(HttpServletRequest request) throws Exception {
        final String patentIdStr;
        if(request.getParameterMap().containsKey("oid")) {
            patentIdStr = request.getParameter("oid");
        }
        else {
            //If we get here from Result Participation Management, id comes in an attribute
            //If we get here from Patents Management, id comes in a paremeter
            patentIdStr = request.getParameterMap().containsKey("resultId") ?
                    request.getParameter("resultId") : (String) request.getAttribute("resultId");
        }
                
        final ResultPatent patent = (ResultPatent) rootDomainObject.readResultByOID(Integer.valueOf(patentIdStr));
        if (patent == null) {
            addActionMessage(request,"researcher.result.error.resultNotFound");
        }
        else {
            request.setAttribute("patent", patent);    
        }
        
        return patent;
    }
}