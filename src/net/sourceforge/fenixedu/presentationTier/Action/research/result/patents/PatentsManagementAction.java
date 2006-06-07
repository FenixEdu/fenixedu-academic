package net.sourceforge.fenixedu.presentationTier.Action.research.result.patents;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.result.Authorship;
import net.sourceforge.fenixedu.domain.research.result.Patent;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class PatentsManagementAction extends FenixDispatchAction {

    public ActionForward listPatents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        return mapping.findForward("listPatents");
    }

    public ActionForward prepareCreatePatent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Person> authorsList = new ArrayList<Person>();

        authorsList.add(getUserView(request).getPerson());
        request.setAttribute("authorsList", authorsList);
        
        return mapping.findForward("createPatent");
    }

    public ActionForward prepareEditPatent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        //If we get here from Result Authorships Management, id comes in an attribute 
        Integer patentId = (Integer) request.getAttribute("patentId");
        
        //If we get here from Patents Management, id comes in a paremeter
        if (patentId == null) {
            patentId = Integer.valueOf(request.getParameter("patentId"));
        }
        Patent patent = readPatentByOid(patentId);
        
        request.setAttribute("patent", patent);
        
        return mapping.findForward("editPatent");
    }
    
    public ActionForward prepareEditPatentData(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer patentId = Integer.valueOf(request.getParameter("patentId"));
        Patent patent = readPatentByOid(patentId);
        
        request.setAttribute("patent", patent);
        
        return mapping.findForward("editPatentData");
    }
    
    public ActionForward preparePatentDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer authorshipId = Integer.valueOf(request.getParameter("oid"));
        Patent patent = (Patent) readAuthorshipByOid(authorshipId).getResult();
        
        request.setAttribute("patent", patent);
       
        return mapping.findForward("patentDetails");
    }

    public ActionForward prepareDeletePatent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        Patent patent = readPatentByOid(resultId);
        
        request.setAttribute("patent", patent);

        return mapping.findForward("deletePatent");
    }

    public ActionForward deletePatent(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] args = { Integer.valueOf(request.getParameter("resultId")) };

        try {
            ServiceManagerServiceFactory.executeService(getUserView(request), "DeletePatent", args);
        } catch (FenixServiceException e) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("", new ActionMessage(e.getMessage()));
            saveMessages(request, actionMessages);
        }

        return mapping.findForward("listPatents");
    }

    private Authorship readAuthorshipByOid(Integer authorshipId) {
        return (Authorship) rootDomainObject.readAuthorshipByOID(authorshipId);
    }
    
    private Patent readPatentByOid(Integer patentId) throws FenixFilterException, FenixServiceException {
        return (Patent) rootDomainObject.readResultByOID(Integer.valueOf(patentId));
    }
}