package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.candidacy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.CreateDFACandidacyBean;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DFACandidacyDispatchAction extends FenixDispatchAction{
	
    public ActionForward prepareCreateCandidacy(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
    	CreateDFACandidacyBean createDFACandidacyBean = new CreateDFACandidacyBean();
    	request.setAttribute("createCandidacyBean", createDFACandidacyBean);
    	return mapping.findForward("chooseExecutionDegree");
    }

    public ActionForward chooseExecutionDegreePostBack(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        Object object = RenderUtils.getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("createCandidacyBean", object);
        
        return mapping.getInputForward();
    }
    
    public ActionForward fillCandidateData(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        Object object = RenderUtils.getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("createCandidacyBean", object);
        
        return mapping.findForward("fillCandidateData");
    	
    }    


    public ActionForward createCandidacy(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixFilterException{
    	CreateDFACandidacyBean createDFACandidacyBean = (CreateDFACandidacyBean) RenderUtils.getViewState().getMetaObject().getObject();
        try {
        	DFACandidacy candidacy = (DFACandidacy) ServiceUtils.executeService(getUserView(request), "CreateDFACandidacy", new Object[] {createDFACandidacyBean.getExecutionDegree(), createDFACandidacyBean.getName(),
        			createDFACandidacyBean.getIdentificationNumber(), createDFACandidacyBean.getIdDocumentType()});
        }catch (DomainException e) {
			//TODO
		} catch (NotAuthorizedFilterException e) {
			//TODO
		} 
        
        
        return mapping.getInputForward();
    	
    }    

}
