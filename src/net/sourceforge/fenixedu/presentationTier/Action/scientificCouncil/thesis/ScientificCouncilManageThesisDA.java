package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis;

import static net.sourceforge.fenixedu.domain.thesis.Thesis.getApprovedThesis;
import static net.sourceforge.fenixedu.domain.thesis.Thesis.getConfirmedThesis;
import static net.sourceforge.fenixedu.domain.thesis.Thesis.getEvaluatedThesis;
import static net.sourceforge.fenixedu.domain.thesis.Thesis.getSubmittedThesis;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ScientificCouncilManageThesisDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("thesis", getThesis(request));
        
        return super.execute(mapping, actionForm, request, response);
    }

    private Thesis getThesis(HttpServletRequest request) {
        Integer id = getId(request.getParameter("thesisID"));
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
    
    public ActionForward listThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Thesis> theses = new ArrayList<Thesis>();

        theses.addAll(getSubmittedThesis());
        theses.addAll(getApprovedThesis());
        theses.addAll(getConfirmedThesis());
        theses.addAll(getEvaluatedThesis());

        request.setAttribute("theses", theses);
        return mapping.findForward("list-thesis");
    }

    public ActionForward reviewProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    return mapping.findForward("review-proposal");
    }
    
    public ActionForward approveProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        	Thesis thesis = getThesis(request);

        if (thesis != null) {
            executeService("ApproveThesisProposal", thesis);
        }
        
        return listThesis(mapping, actionForm, request, response);
    }
    
    public ActionForward confirmRejectProposal(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("confirmReject", true);
        return reviewProposal(mapping, actionForm, request, response);
    }
    
    public ActionForward reviewThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("review-thesis");
    }
    
    public ActionForward confirmApprove(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("confirmApprove", true);
        return reviewThesis(mapping, actionForm, request, response);
    }
    
    public ActionForward confirmDisapprove(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("confirmDisapprove", true);
        return reviewThesis(mapping, actionForm, request, response);
    }
    
    public ActionForward approveThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        if (thesis != null) {
            executeService("ApproveThesisDiscussion", thesis);
        }
        
        return listThesis(mapping, actionForm, request, response);
    }
    
    public ActionForward disapproveThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thesis thesis = getThesis(request);

        if (thesis != null) {
            executeService("DisapproveThesisDiscussion", thesis);
        }
        
        return listThesis(mapping, actionForm, request, response);
    }
    
}
