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
import net.sourceforge.fenixedu.presentationTier.Action.research.result.ResultsManagementAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class PatentsManagementAction extends FenixDispatchAction {

    final ResultsManagementAction authorsManagement = new ResultsManagementAction();

    public ActionForward listPatents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("patentsList", readPersonPatents(getUserView(request).getPerson()));

        return mapping.findForward("listPatents");
    }

    public ActionForward prepareCreateEditPatent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer patentId = Integer.valueOf(request.getParameter("resultId"));
        String action = request.getParameter("action");
        List<Person> authorsList = authorsManagement.getFormAuthorsList(form);
        
        if (patentId != -1 && action.equals("editPatent")) {
            Patent patent = readPatentByOid(patentId);
            request.setAttribute("patent", patent);
        }
        request.setAttribute("action", action);
        request.setAttribute("authorsList", authorsList);

        return mapping.findForward("createEditPatent");
    }

    public ActionForward preparePatentDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer resultId = Integer.valueOf(request.getParameter("oid"));
        Patent patent = readPatentByOid(resultId);
        List<Person> authorsList = getPatentAuthorsList(patent);

        request.setAttribute("patent", patent);
        request.setAttribute("authorsList", authorsList);

        return mapping.findForward("patentDetails");
    }

    public ActionForward prepareDeletePatent(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        Patent patent = readPatentByOid(resultId);
        List<Person> authorsList = getPatentAuthorsList(patent);

        request.setAttribute("patent", patent);
        request.setAttribute("authorsList", authorsList);

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

        request.setAttribute("patentsList", readPersonPatents(getUserView(request).getPerson()));

        return mapping.findForward("listPatents");
    }

    private List<Patent> readPersonPatents(Person person) throws FenixServiceException,
            FenixFilterException {

        List<Patent> patentsList = new ArrayList<Patent>();

        for (Authorship authorship : person.getPersonAuthorshipsWithPatents()) {
            patentsList.add((Patent) authorship.getResult());
        }

        return patentsList;
    }

    private Patent readPatentByOid(Integer resultId) throws FenixFilterException, FenixServiceException {

        Patent patent = (Patent) rootDomainObject.readResultByOID(Integer.valueOf(resultId));

        return patent;
    }

    private List<Person> getPatentAuthorsList(Patent patent) {
        List<Person> authorsList = new ArrayList<Person>();

        for (Authorship authorShip : patent.getResultAuthorships()) {
            authorsList.add(authorShip.getAuthor());
        }

        return authorsList;
    }
}