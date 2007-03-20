package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ScientificCouncilManageThesisDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("thesis", getThesis(request));
        
        Degree degree = getDegree(request);
        ExecutionYear executionYear = getExecutionYear(request);

        setFilterContext(request, degree, executionYear);
        
        return super.execute(mapping, actionForm, request, response);
    }

    private void setFilterContext(HttpServletRequest request, Degree degree, ExecutionYear executionYear) {
	request.setAttribute("degree", degree);
        request.setAttribute("degreeId", degree == null ? "" : degree.getIdInternal());
	request.setAttribute("executionYear", executionYear);
	request.setAttribute("executionYearId", executionYear == null ? "" : executionYear.getIdInternal());
    }

    private Thesis getThesis(HttpServletRequest request) {
        Integer id = getId(request.getParameter("thesisID"));
        if (id == null) {
            return null;
        } else {
            return RootDomainObject.getInstance().readThesisByOID(id);
        }
    }   

    private Degree getDegree(HttpServletRequest request) {
	Integer id = getId(request.getParameter("degreeID"));
	if (id == null) {
	    return null;
	} else {
	    return RootDomainObject.getInstance().readDegreeByOID(id);
	}
    }
    
    private ExecutionYear getExecutionYear(HttpServletRequest request) {
	Integer id = getId(request.getParameter("executionYearID"));
	if (id == null) {
	    return null;
	} else {
	    return RootDomainObject.getInstance().readExecutionYearByOID(id);
	}
    }
    
    private Integer getId(String id) {
        if (id == null || id.equals("")) {
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
	ThesisContextBean bean = getContextBean(request);
	
	Degree degree = bean.getDegree();
	ExecutionYear executionYear = bean.getExecutionYear();

	setFilterContext(request, degree, executionYear);
	
	List<Thesis> theses = new ArrayList<Thesis>();

        theses.addAll(Thesis.getSubmittedThesis(degree, executionYear));
        theses.addAll(Thesis.getApprovedThesis(degree, executionYear));
        theses.addAll(Thesis.getConfirmedThesis(degree, executionYear));
        theses.addAll(Thesis.getEvaluatedThesis(degree, executionYear));
        
        request.setAttribute("contextBean", bean);
        request.setAttribute("theses", theses);
        
        return mapping.findForward("list-thesis");
    }

    private ThesisContextBean getContextBean(HttpServletRequest request) {
	ThesisContextBean bean = (ThesisContextBean) getRenderedObject("contextBean");
	RenderUtils.invalidateViewState("contextBean");
	
	if (bean != null) {
	    return bean;
	}
	else {
            Degree degree = getDegree(request);
            ExecutionYear executionYear = getExecutionYear(request);

            if (executionYear == null) {
        	executionYear = ExecutionYear.readCurrentExecutionYear();
            }
            
            return new ThesisContextBean(degree, executionYear);
	}
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
