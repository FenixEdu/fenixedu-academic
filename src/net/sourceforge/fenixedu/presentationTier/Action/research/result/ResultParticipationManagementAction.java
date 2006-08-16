package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.ChangeResultParticipationsOrder;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.ChangeResultParticipationsOrder.OrderChange;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultParticipationManagementAction extends ResultsManagementAction {

    public ActionForward prepareEditParticipation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final Result result = readResultFromRequest(request);
        if(result==null){ return backToResultList(mapping, form, request, response); }
                
        ResultParticipationCreationBean bean = (ResultParticipationCreationBean)request.getAttribute("bean");
        if (bean == null) {
            bean = new ResultParticipationCreationBean(result);
            request.setAttribute("bean", bean);
        }
        
        checkNeededSchemas(request, result, bean);	//Define schemas to use
        checkNeededWarnings(request, result);		//Action Warning Messages
        request.setAttribute("result", result);

        return mapping.findForward("editParticipation");
    }
    
	public ActionForward createParticipation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final ResultParticipationCreationBean bean = (ResultParticipationCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        
        RenderUtils.invalidateViewState();
        
        if(checkBean(bean)) {
			try {
				ResultParticipation.createResultParticipation(bean);
			} catch (Exception e) {
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
            HttpServletRequest request, HttpServletResponse response) {
        final ResultParticipation participation = readResultParticipationFromRequest(request);

        if(participation==null){ return backToResultList(mapping, form, request, response); }

		try {
			ResultParticipation.deleteResultParticipation(participation);
		} catch (Exception  e) {
			addActionMessage(request, e.getMessage());
		}
			
        return prepareEditParticipation(mapping,form,request,response);
    }
    
    public ActionForward saveOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final String treeStructure = request.getParameter("tree");
        final Result result = readResultFromRequest(request);
        
        if (treeStructure == null || treeStructure.length() == 0) {
            return prepareEditParticipation(mapping,form,request,response);
        }
        
        final List<ResultParticipation> newParticipationsOrder = reOrderParticipations(treeStructure, result);
                
        try {
			ResultParticipation.saveResultParticipationsOrder(result, newParticipationsOrder);
		} catch (Exception e) {
			addActionMessage(request, e.getMessage());
		}
        
        return prepareEditParticipation(mapping,form,request,response);
    }
    
	public ActionForward moveUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return move(ChangeResultParticipationsOrder.OrderChange.MoveUp, mapping, form, request, response);
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
    
    private void checkNeededSchemas(HttpServletRequest request, Result result, ResultParticipationCreationBean bean) {
    	String resultParticipationsSchema = "result.participations";
        String createResultParticipationSchema = "resultParticipation.creation";
        
        if(bean.isBeanExternal()) {
            createResultParticipationSchema = "resultParticipation.fullCreation";
        }
        
        //Defining schemas with roles
        if(result instanceof ResultPublication)
        {
            if(((ResultPublication)result).hasResultPublicationRole()) {
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
	}

	private void checkNeededWarnings(HttpServletRequest request, Result result) {
    	final Person person = getUserView(request).getPerson();
        if(!result.hasPersonParticipation(person)) {
            addActionMessage(request, "researcher.result.editResult.participation.warning");
        }
        if(result.getResultParticipationsCount()==1){
            addActionMessage(request, "researcher.result.lastConnection.warning");
        }
	}

    private List<ResultParticipation> reOrderParticipations(String treeStructure, Result result) {
    	final List<ResultParticipation> newParticipationsOrder = new ArrayList<ResultParticipation>();
    	final List<ResultParticipation> oldParticipationsOrder = result.getOrderedParticipations();
        final String[] nodes = treeStructure.split(",");
        
        for(int i = 0; i < nodes.length; i++) {
            String[] parts = nodes[i].split("-");
            
            Integer index = getId(parts[0])-1;
            ResultParticipation participation = oldParticipationsOrder.get(index);
            newParticipationsOrder.add(participation);
        }

		return newParticipationsOrder;
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
    
    private ResultParticipation readResultParticipationFromRequest(HttpServletRequest request) {
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