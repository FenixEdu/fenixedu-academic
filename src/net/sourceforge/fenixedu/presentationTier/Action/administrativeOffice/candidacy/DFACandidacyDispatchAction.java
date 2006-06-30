package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.candidacy;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.CreateDFACandidacyBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.DFACandidacyBean;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class DFACandidacyDispatchAction extends FenixDispatchAction{
	
    public ActionForward prepareCreateCandidacy(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
    	CreateDFACandidacyBean createDFACandidacyBean = new CreateDFACandidacyBean();
    	request.setAttribute("candidacyBean", createDFACandidacyBean);
    	return mapping.findForward("chooseExecutionDegree");
    }

    public ActionForward chooseExecutionDegreePostBack(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        Object object = RenderUtils.getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("candidacyBean", object);
        
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
        DFACandidacy candidacy = null;
        try {
        	candidacy = (DFACandidacy) ServiceUtils.executeService(getUserView(request), "CreateDFACandidacy", new Object[] {createDFACandidacyBean.getExecutionDegree(), createDFACandidacyBean.getName(),
        			createDFACandidacyBean.getIdentificationNumber(), createDFACandidacyBean.getIdDocumentType()});
        } catch (DomainException e) {
			//TODO
		} catch (NotAuthorizedFilterException e) {
			//TODO
		} 
        request.setAttribute("candidacy", candidacy);
        
        return mapping.findForward("showCreatedCandidacy");
    	
    }
    
    public ActionForward prepareGenPass(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixFilterException{
        return mapping.findForward("prepareGenPass");
        
    }
    
    public ActionForward generatePass(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixFilterException{
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer candidacyNumber = (Integer) form.get("candidacyNumber");
        Candidacy candidacy = Candidacy.readByCandidacyNumber(candidacyNumber);
        if(candidacy == null){
            addActionMessage(request, "error.no.candidacy", null);
            return prepareGenPass(mapping, actionForm, request, response);
        }
        if(!candidacy.getActiveCandidacySituation().canGeneratePass()){
            addActionMessage(request, "error.enrolmentFee.to.pay", null);
            return prepareGenPass(mapping, actionForm, request, response);            
        }
        try {
            String pass = (String) ServiceUtils.executeService(getUserView(request), "GenerateNewPassword", new Object[] {candidacy.getPerson()});
            request.setAttribute("password", pass);
        } catch (NotAuthorizedFilterException e) {
            //TODO
        } 
        
        request.setAttribute("candidacy", candidacy);
        return mapping.findForward("generatePassword");
    }
    
    public ActionForward prepareValidateCandidacyData(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixFilterException{
        return mapping.findForward("prepareValidateCandidacyData");
        
    } 

    public ActionForward prepareListCandidacies(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixFilterException{
        DFACandidacyBean candidacyBean = new DFACandidacyBean();
        request.setAttribute("candidacyBean", candidacyBean);
        return mapping.findForward("listCandidacies");        
    } 

    public ActionForward listCandidacies(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixFilterException{
        DFACandidacyBean dfaCandidacyBean = (DFACandidacyBean) RenderUtils.getViewState().getMetaObject().getObject();
        Set<DFACandidacy> candidacies = dfaCandidacyBean.getExecutionDegree().getDfaCandidaciesSet();
        request.setAttribute("candidacies", candidacies);
        request.setAttribute("search", dfaCandidacyBean);
        return mapping.findForward("listCandidacies");        
    } 
}
