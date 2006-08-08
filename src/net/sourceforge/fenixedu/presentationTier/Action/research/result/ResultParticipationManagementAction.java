package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.research.result.ChangeResultParticipationsOrder;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.ChangeResultParticipationsOrder.OrderChange;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultParticipationManagementAction extends ResultsManagementAction {

    public ActionForward prepareEditParticipation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Result result = readResultFromRequest(request);
        if(result==null){ return backToResultList(mapping, form, request, response); }
        
        //Action Warning Messages
        final Person person = getUserView(request).getPerson();
        if(!result.hasPersonParticipation(person)) {
            addActionMessage(request, "researcher.result.editResult.participation.warning");
        }
        if(result.getResultParticipationsCount()==1){
            addActionMessage(request, "researcher.result.lastConnection.warning");
        }
        
        ResultParticipationCreationBean bean = (ResultParticipationCreationBean)request.getAttribute("bean");
        if (bean == null) {
            bean = new ResultParticipationCreationBean(result);
            request.setAttribute("bean", bean);
        }
        
        //Defining schemas to use
        String resultParticipationsSchema = "result.participations";
        String createResultParticipationSchema = "resultParticipation.creation";
        
        if(bean.isBeanExternal()) {
            createResultParticipationSchema = "resultParticipation.fullCreation";
        }
        
        if(result instanceof ResultPublication)
        {
            if(((ResultPublication)result).haveResultPublicationRole()) {
                resultParticipationsSchema = "result.participationsWithRole";
                if (!bean.isBeanExternal()) {
                    createResultParticipationSchema = "resultParticipation.creationWithRole";
                }
                else {
                    createResultParticipationSchema = "resultParticipation.fullCreationWithRole";
                }
            }
        }
        request.setAttribute("resultParticipationsSchema", resultParticipationsSchema);
        request.setAttribute("createResultParticipationSchema", createResultParticipationSchema);
        
        return mapping.findForward("editParticipation");
    }
    
    public ActionForward createParticipation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Result result = readResultFromRequest(request);
        if(result==null){ return backToResultList(mapping, form, request, response); }
        
        final ResultParticipationCreationBean bean = (ResultParticipationCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        
        RenderUtils.invalidateViewState();
        
        if(checkBean(bean)) {
            final Object[] args = { bean };
            
            try {
                ServiceUtils.executeService(getUserView(request), "CreateResultParticipation", args);
            }
            catch (Exception e) {
                addActionMessage(request, e.getMessage());
            }
        }
        else {
            bean.setBeanExternal(true);
            request.setAttribute("bean", bean);
        }
        return prepareEditParticipation(mapping,form,request,response);
    }
    
    public ActionForward removeParticipation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ResultParticipation participation = readResultParticipationFromRequest(request);
        if(participation==null){ return backToResultList(mapping, form, request, response); }
       
        ResultParticipation.deleteResultParticipation(participation);
        
        return prepareEditParticipation(mapping,form,request,response);
    }
    
    public ActionForward moveUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return move(ChangeResultParticipationsOrder.OrderChange.MoveUp, mapping, form, request, response);
    }
    
    public ActionForward saveOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String treeStructure = request.getParameter("tree");
        final Result result = readResultFromRequest(request);
        
        final List<ResultParticipation> participations = result.getOrderedParticipations();
        List<ResultParticipation> newParticipationsOrder = new ArrayList<ResultParticipation>();
        
        if (treeStructure == null || treeStructure.length() == 0) {
            return prepareEditParticipation(mapping,form,request,response);
        }
        
        final String[] nodes = treeStructure.split(",");
        for(int i = 0; i < nodes.length; i++) {
            String[] parts = nodes[i].split("-");
            
            Integer index = getId(parts[0])-1;
            ResultParticipation participation = participations.get(index);
            newParticipationsOrder.add(participation);
        }
        
        ResultParticipation.saveResultParticipationsOrder(result, newParticipationsOrder);
        
        return prepareEditParticipation(mapping,form,request,response);
    }
    
    public ActionForward moveDown(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return move(ChangeResultParticipationsOrder.OrderChange.MoveDown, mapping, form, request, response);
    }

    public ActionForward moveTop(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return move(ChangeResultParticipationsOrder.OrderChange.MoveTop, mapping, form, request, response);
    }

    public ActionForward moveBottom(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return move(ChangeResultParticipationsOrder.OrderChange.MoveBottom, mapping, actionForm, request, response);
    }
    
    private ActionForward move(OrderChange orderChange, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ResultParticipation participation = readResultParticipationFromRequest(request);
        if(participation==null){ return backToResultList(mapping, form, request, response); }

        ResultParticipation.changeOrder(participation,orderChange);

        return prepareEditParticipation(mapping,form,request,response);
    }
    
    private boolean checkBean(ResultParticipationCreationBean bean) {
        //Person already exists in system. 
        if (bean.getPerson()!=null) {
            return true;
        }
        //External person creation
        if (bean.getPersonName()!=null && !bean.getPersonName().equals("")) {
            if(bean.getOrganization()!=null || (bean.getOrganizationName()!=null && !bean.getOrganizationName().equals(""))) {
                return true;
            }
        }
        return false;
    }
    
    private ResultParticipation readResultParticipationFromRequest(HttpServletRequest request) throws Exception {
        final Integer participationId = Integer.valueOf(request.getParameter("participationId"));
        final ResultParticipation participation = rootDomainObject.readResultParticipationByOID(participationId);
        
        if (participation == null) {
            addActionMessage(request, "error.ResultParticipation.not.found");
        }
        
        return participation;
    }
    
    protected Integer getId(String id) {
        if (id == null) {
            return null;
        }
        
        try {
            return new Integer(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}