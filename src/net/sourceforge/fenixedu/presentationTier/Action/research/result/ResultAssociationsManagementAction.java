package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultEventAssociationCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreationBean;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultAssociationsManagementAction extends ResultsManagementAction {
    
    /**
     *  Actions for Result Unit Associations 
     */
    public ActionForward prepareEditUnitAssociations(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final Result result = readResultFromRequest(request);
        if(result==null) { return backToResultList(mapping, form, request, response); }
        
        final ResultUnitAssociationCreationBean bean = new ResultUnitAssociationCreationBean(result);
        request.setAttribute("bean", bean);
        request.setAttribute("result", result);
        
        return mapping.findForward("editUnitAssociations");
    }
    
    public ActionForward createUnitAssociation(ActionMapping mapping,ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final ResultUnitAssociationCreationBean bean = (ResultUnitAssociationCreationBean)RenderUtils.getViewState().getMetaObject().getObject();
        
        try {
			ResultUnitAssociation.create(bean);
		} catch (Exception e) {
			addActionMessage(request, e.getMessage());
		} 
        
        return prepareEditUnitAssociations(mapping, form, request, response);
    }

    public ActionForward removeUnitAssociation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final Integer associationId = Integer.parseInt(request.getParameter("associationId"));

        try {
			ResultUnitAssociation.remove(associationId);
		} catch (Exception e) {
			addActionMessage(request, e.getMessage());
		} 
        
        return prepareEditUnitAssociations(mapping, form, request, response);
    }
    
    /**
     *  Actions for Result Event Associations 
     */
    public ActionForward prepareEditEventAssociations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final Result result = readResultFromRequest(request);
        if(result==null) { return backToResultList(mapping, form, request, response); }

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
        request.setAttribute("result", result);
        
        return mapping.findForward("editEventAssociations");
    }
    
    public ActionForward createEventAssociation(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) {
        final Object renderedObject = RenderUtils.getViewState().getMetaObject().getObject();
        final ResultEventAssociationCreationBean bean = (ResultEventAssociationCreationBean) renderedObject;
        
        RenderUtils.invalidateViewState();
        
        if(!(bean.getEvent()==null && bean.getEventNameMLS()==null)) {
            try {
				ResultEventAssociation.create(bean);
    		} catch (Exception e) {
    			addActionMessage(request, e.getMessage());
    		} 
        }
        else {
            bean.setEventNameMLS(new MultiLanguageString(bean.getEventNameStr()));
            request.setAttribute("bean", bean);  
        }
        return prepareEditEventAssociations(mapping, form, request, response);
    }
    
    public ActionForward removeEventAssociation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final Integer associationId = Integer.parseInt(request.getParameter("associationId"));
        
        try {
			ResultEventAssociation.remove(associationId);
		} catch (Exception e) {
			addActionMessage(request, e.getMessage());
		} 
        
        return prepareEditEventAssociations(mapping, form, request, response);
    }   
}
