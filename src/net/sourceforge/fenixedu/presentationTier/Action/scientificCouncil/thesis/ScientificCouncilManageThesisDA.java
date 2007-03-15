package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis;

import static net.sourceforge.fenixedu.domain.thesis.Thesis.getSubmittedThesis;
import static net.sourceforge.fenixedu.domain.thesis.Thesis.getApprovedThesis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisBean;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ScientificCouncilManageThesisDA extends FenixDispatchAction {

    private Thesis getThesis(HttpServletRequest request) {
	Integer id = getId(request.getParameter("thesisId"));
	if (id == null) {
	    return null;
	} else {
	    return RootDomainObject.getInstance().readThesisByOID(id);
	}
    }   
    
    private Integer getId(String id) {
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
    
    public ActionForward listSubmitted(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	request.setAttribute("theses", getSubmittedThesis());
	return mapping.findForward("list-submitted");
    }
    
    public ActionForward listApproved(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	request.setAttribute("theses", getApprovedThesis());
	return mapping.findForward("list-approved");
    }
    
    public ActionForward reviewSubmittedProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	Thesis thesis = getThesis(request);
	
	if (thesis == null) {
	    return listSubmitted(mapping, actionForm, request, response);
	}
	else {
	    request.setAttribute("thesis", thesis);
	    return mapping.findForward("review-submitted-proposal");
	}
    }
    
    public ActionForward reviewApprovedProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	Thesis thesis = getThesis(request);
	
	if (thesis == null) {
	    return listApproved(mapping, actionForm, request, response);
	}
	else {
	    request.setAttribute("thesis", thesis);
	    return mapping.findForward("review-approved-proposal");
	}
    }
    
    public ActionForward approveProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	Thesis thesis = getThesis(request);
	
	if (thesis != null) {
	    executeService("ApproveThesisProposal", thesis);
	}
	request.setAttribute("theses", getSubmittedThesis());
        return mapping.findForward("list-submitted");
    }
    
    public ActionForward justifySubmittedProposalRejection(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	Thesis thesis = getThesis(request);
	
	if (thesis == null) {
	    request.setAttribute("theses", getSubmittedThesis());
	    return mapping.findForward("list-submitted");
	}
	else {
	    request.setAttribute("thesis", thesis);
	    return mapping.findForward("justify-submitted-proposal-rejection");
	}
    }
    
    public ActionForward justifyApprovedProposalRejection(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	Thesis thesis = getThesis(request);
	
	if (thesis == null) {
	    request.setAttribute("theses", getSubmittedThesis());
	    return mapping.findForward("list-approved");
	}
	else {
	    TextBean bean = new TextBean();
	    request.setAttribute("bean", bean);
	    request.setAttribute("thesis", thesis);
	    return mapping.findForward("justify-approved-proposal-rejection");
	}
    }
    
    public ActionForward rejectSubmittedProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	Thesis thesis = getThesis(request);
	
	if (thesis != null) {
	    executeService("rejectThesisProposal", thesis);
	}
	request.setAttribute("theses", getSubmittedThesis());
        return mapping.findForward("list-submitted");
    }
    
    public ActionForward rejectApprovedProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	TextBean bean = (TextBean) getRenderedObject("bean");
        RenderUtils.invalidateViewState("bean");
        
        if (bean == null) {
            return justifyApprovedProposalRejection(mapping, actionForm, request, response);
        }
	
	Thesis thesis = getThesis(request);	
	if (thesis != null) {
	    executeService("rejectThesisProposal", thesis, bean.getText());
	}
	
	request.setAttribute("theses", getSubmittedThesis());
        return mapping.findForward("list-approved");
    }
    
    public ActionForward disapproveProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	Thesis thesis = getThesis(request);
	
	if (thesis != null) {
	    executeService("DisapproveThesisProposal", thesis);
	}
	request.setAttribute("theses", getApprovedThesis());
        return mapping.findForward("list-approved");
    }
    
    
}
