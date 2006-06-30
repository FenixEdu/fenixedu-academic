package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultEventAssociationFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultEventAssociationSimpleCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreateBean;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultAssociationsManagementAction extends FenixDispatchAction {
    
    /* Actions for Result Unit Associations */
    public ActionForward prepareEditUnitAssociations(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        
        Result result = rootDomainObject.readResultByOID(resultId);
        request.setAttribute("result", result);
        
        ResultUnitAssociationCreateBean bean = new ResultUnitAssociationCreateBean();
        request.setAttribute("bean", bean);
        
        return mapping.findForward("editUnitAssociations");
    }
    
    public ActionForward createUnitAssociation(ActionMapping mapping,ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = getUserView(request);

        ResultUnitAssociationCreateBean bean = (ResultUnitAssociationCreateBean)RenderUtils.getViewState().getMetaObject().getObject();
        
        Integer oid = Integer.parseInt(request.getParameter("resultId"));
        ServiceUtils.executeService(userView, "CreateResultUnitAssociation", new Object[] { bean, oid });
        
        return prepareEditUnitAssociations(mapping, form, request, response);
    }

    public ActionForward removeUnitAssociation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = getUserView(request);
        Integer associationId = Integer.parseInt(request.getParameter("associationId"));

        ServiceUtils.executeService(userView, "DeleteResultUnitAssociation",new Object[] { associationId });

        return prepareEditUnitAssociations(mapping, form, request, response);
    }
    
    /* Actions for Result Event Associations */
    public ActionForward prepareEditEventAssociations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        
        Result result = rootDomainObject.readResultByOID(resultId);
        request.setAttribute("result", result);
        
        ResultEventAssociationSimpleCreationBean simpleBean = new ResultEventAssociationSimpleCreationBean();
        
        if (request.getAttribute("fullBean") == null) {
            request.setAttribute("simpleBean", simpleBean);
        }

        return mapping.findForward("editEventAssociations");
    }
    
    public ActionForward createEventAssociationSimple(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final IUserView userView = getUserView(request);
        
        if(RenderUtils.getViewState().getMetaObject().getObject() instanceof ResultEventAssociationSimpleCreationBean){
            ResultEventAssociationSimpleCreationBean simpleBean = (ResultEventAssociationSimpleCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
            if(simpleBean.getEvent() != null) {
                Integer oid = Integer.parseInt(request.getParameter("resultId"));
                ServiceUtils.executeService(userView, "CreateResultEventAssociation", new Object[] {simpleBean, oid });
                return prepareEditEventAssociations(mapping, form, request, response);
            }
            else {
                ResultEventAssociationFullCreationBean fullBean = new ResultEventAssociationFullCreationBean();
                fullBean.setEventName(new MultiLanguageString(simpleBean.getEventName()));
                fullBean.setRole(simpleBean.getRole());
                request.setAttribute("fullBean", fullBean);
                return prepareEditEventAssociations(mapping, form, request, response);
            }
        }
        else {
            request.setAttribute("fullBean", (ResultEventAssociationFullCreationBean) RenderUtils.getViewState().getMetaObject().getObject());
            return prepareEditEventAssociations(mapping, form, request, response);
        }        
    }
    
    public ActionForward createEventAssociationFull(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ResultEventAssociationFullCreationBean fullBean = (ResultEventAssociationFullCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        final IUserView userView = getUserView(request);
        Integer oid = Integer.parseInt(request.getParameter("resultId"));
        
        ServiceUtils.executeService(userView, "CreateResultEventAssociation", new Object[] { fullBean, oid });
        
        return prepareEditEventAssociations(mapping, form, request, response);
    }       
    
    public ActionForward removeEventAssociation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        final IUserView userView = getUserView(request);
        Integer associationId = Integer.parseInt(request.getParameter("associationId"));
        
        ServiceUtils.executeService(userView, "DeleteResultEventAssociation", new Object[] { associationId });
        
        return prepareEditEventAssociations(mapping, form, request, response);
    }
    
    
    /* Common Action -> Redirect to previous page */
    public ActionForward backToResult(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        final Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        Result result = rootDomainObject.readResultByOID(resultId);
        
        if (result instanceof ResultPatent){
            request.setAttribute("patentId", resultId);
            return mapping.findForward("editPatent");    
        }
        else {
            //request.setAttribute("publicationId", resultId);
            //return mapping.findForward("editPublication");
            return null;
        }
    }
}
