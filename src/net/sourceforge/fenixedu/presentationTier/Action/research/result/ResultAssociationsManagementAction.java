package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultEventAssociationCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultAssociationsManagementAction extends ResultsManagementAction {
    
    /**
     *  Actions for Result Unit Associations 
     */
    public ActionForward prepareEditUnitAssociations(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Result result = readResultFromRequest(request);
        if(result==null){
            return backToResultList(mapping, form, request, response);
        }
        
        final ResultUnitAssociationCreationBean bean = new ResultUnitAssociationCreationBean(result);
        
        request.setAttribute("bean", bean);
        return mapping.findForward("editUnitAssociations");
    }
    
    public ActionForward createUnitAssociation(ActionMapping mapping,ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ResultUnitAssociationCreationBean bean = (ResultUnitAssociationCreationBean)RenderUtils.getViewState().getMetaObject().getObject();
        final Object[] args = { bean };
        
        try {
            ServiceUtils.executeService(getUserView(request), "CreateResultUnitAssociation", args);
        }
        catch (InvalidArgumentsServiceException e) {
            addActionMessage(request, "error.Result.not.found");
        }
        catch (FenixServiceException e) {
            addActionMessage(request, e.getMessage());
        }
        catch (DomainException e) {
            addActionMessage(request, e.getKey());
        }
        
        return prepareEditUnitAssociations(mapping, form, request, response);
    }

    public ActionForward removeUnitAssociation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Integer associationId = Integer.parseInt(request.getParameter("associationId"));
        final Object[] args = { associationId };

        try {
            ServiceUtils.executeService(getUserView(request), "DeleteResultUnitAssociation", args);    
        }
        catch (InvalidArgumentsServiceException e) {
            addActionMessage(request, "error.ResultAssociation.not.found");
        }

        return prepareEditUnitAssociations(mapping, form, request, response);
    }
    
    /**
     *  Actions for Result Event Associations 
     */
    public ActionForward prepareEditEventAssociations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Result result = readResultFromRequest(request);
        if(result==null){
            return backToResultList(mapping, form, request, response);
        }

        String creationSchema = null;
        ResultEventAssociationCreationBean bean = (ResultEventAssociationCreationBean)request.getAttribute("bean");
        if (bean == null) {
            bean = new ResultEventAssociationCreationBean(result);
            creationSchema = "resultEventAssociation.simpleCreation";
            request.setAttribute("bean", bean);
        }
        else {
            creationSchema = "resultEventAssociation.fullCreation";
        }
        request.setAttribute("creationSchema",creationSchema);
        
        return mapping.findForward("editEventAssociations");
    }
    
    public ActionForward createEventAssociation(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Object renderedObject = RenderUtils.getViewState().getMetaObject().getObject();
        final IUserView userView = getUserView(request);
        final ResultEventAssociationCreationBean bean = (ResultEventAssociationCreationBean) renderedObject;
        
        RenderUtils.invalidateViewState();
        
        if(!(bean.getEvent()==null && bean.getEventNameMLS()==null)) {
            final Object[] args = { bean };
            
            try {
                ServiceUtils.executeService(userView, "CreateResultEventAssociation", args);    
            }
            catch (InvalidArgumentsServiceException e) {
                addActionMessage(request, "error.Result.not.found");
            }
            catch (FenixServiceException e) {
                addActionMessage(request, e.getMessage());
            }
            catch (DomainException e) {
                addActionMessage(request, e.getKey(),e.getArgs());
            }
        }
        else {
            bean.setEventNameMLS(new MultiLanguageString(bean.getEventNameStr()));
            request.setAttribute("bean", bean);  
        }
        return prepareEditEventAssociations(mapping, form, request, response);
    }
    
    public ActionForward removeEventAssociation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Integer associationId = Integer.parseInt(request.getParameter("associationId"));
        final IUserView userView = getUserView(request);
        final Object[] args = { associationId };
        
        try {
            ServiceUtils.executeService(userView, "DeleteResultEventAssociation", args);
        }
        catch (InvalidArgumentsServiceException e) {
            addActionMessage(request, "error.ResultAssociation.not.found");
        }
        
        return prepareEditEventAssociations(mapping, form, request, response);
    }   
}
