package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.ThesisFileReadersGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisPresentationState;
import net.sourceforge.fenixedu.presentationTier.Action.student.thesis.ThesisFileBean;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.FileUtils;

public class ScientificCouncilManageThesisDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	final Thesis thesis = getThesis(request);
        request.setAttribute("thesis", thesis);

        if (thesis != null) {
            final ThesisPresentationState thesisPresentationState = ThesisPresentationState.getThesisPresentationState(thesis);
            request.setAttribute("thesisPresentationState", thesisPresentationState);
        }

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

    public Integer getIntegerParameter(final HttpServletRequest request, final String paramName) {
	final String string = request.getParameter(paramName);
	return string == null ? null : Integer.valueOf(string);
    }

    public ActionForward listScientificComission(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	final Integer degreeId = getIntegerParameter(request, "degreeId");
	final Degree degree = degreeId == null ? null : rootDomainObject.readDegreeByOID(degreeId);
	request.setAttribute("degree", degree);

	final Integer executionYearId = getIntegerParameter(request, "executionYearId");
	final ExecutionYear executionYear = (ExecutionYear) (executionYearId == null ? null : rootDomainObject.readAcademicPeriodByOID(executionYearId));
	request.setAttribute("executionYear", executionYear);

	if (degree != null || executionYear != null) {
	    final Set<ExecutionDegree> executionDegrees = new HashSet<ExecutionDegree>();
	    for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
		for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
		    if (executionDegree.getExecutionYear() == executionYear) {
			executionDegrees.add(executionDegree);
		    }
		}
	    }
	    request.setAttribute("executionDegrees", executionDegrees);
	}
        
        return mapping.findForward("list-scientific-comission");
    }

    private ThesisContextBean getContextBean(HttpServletRequest request) {
        ThesisContextBean bean = (ThesisContextBean) getRenderedObject("contextBean");
        RenderUtils.invalidateViewState("contextBean");

        if (bean != null) {
            return bean;
        } else {
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
            addActionMessage("mail", request, "thesis.approved.mail.sent");
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
            try {
                executeService("ApproveThesisDiscussion", thesis);
                addActionMessage("mail", request, "thesis.evaluated.mail.sent");
            } catch (DomainException e) {
                addActionMessage("error", request, e.getKey(), e.getArgs());
                return reviewThesis(mapping, actionForm, request, response);
            }
        }
        
        return listThesis(mapping, actionForm, request, response);
    }
    
    public ActionForward viewThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	setDocumentAvailability(request);
        return mapping.findForward("view-thesis");
    }

    private void setDocumentAvailability(final HttpServletRequest request) {
	final Thesis thesis = getThesis(request);
	final ThesisFile thesisFile = thesis.getDissertation();
	if (thesisFile != null) {
	    final Group group = thesisFile.getPermittedGroup();
	    if (containsThesisFileReadersGroup(group)) {
		request.setAttribute("containsThesisFileReadersGroup", Boolean.TRUE);
	    }
	}
    }

    private boolean containsThesisFileReadersGroup(final IGroup group) {
	if (group instanceof GroupUnion) {
	    final GroupUnion groupUnion = (GroupUnion) group;
	    for (IGroup child : groupUnion.getChildren()) {
		if (containsThesisFileReadersGroup(child)) {
		    return true;
		}
	    }
	} else if (group instanceof ThesisFileReadersGroup) {
	    return true;
	}
	return false;
    }

    public ActionForward showMakeDocumentUnavailablePage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("showMakeDocumentUnavailablePage", Boolean.TRUE);
	return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward showMakeDocumentsAvailablePage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("showMakeDocumentsAvailablePage", Boolean.TRUE);
	return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward showSubstituteDocumentsPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	final Thesis thesis = getThesis(request);
	final ThesisFile thesisFile = thesis.getDissertation();
	final ThesisFileBean thesisFileBean = new ThesisFileBean();
	thesisFileBean.setTitle(thesisFile.getTitle());
	thesisFileBean.setSubTitle(thesisFile.getSubTitle());
	thesisFileBean.setLanguage(thesisFile.getLanguage());
	request.setAttribute("fileBean", thesisFileBean);
	request.setAttribute("showSubstituteDocumentsPage", Boolean.TRUE);
	return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward showSubstituteExtendedAbstractPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	final ThesisFileBean thesisFileBean = new ThesisFileBean();
	request.setAttribute("fileBean", thesisFileBean);
	request.setAttribute("showSubstituteExtendedAbstractPage", Boolean.TRUE);
	return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward makeDocumentUnavailablePage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	final Thesis thesis = getThesis(request);
	executeService("MakeThesisDocumentsUnavailable", new Object[] { thesis });
	return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward makeDocumentAvailablePage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	final Thesis thesis = getThesis(request);
	executeService("MakeThesisDocumentsAvailable", new Object[] { thesis });
	return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward substituteDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	ThesisFileBean bean = (ThesisFileBean) getRenderedObject();
	RenderUtils.invalidateViewState();
    
	if (bean != null && bean.getFile() != null) {
	    File temporaryFile = null;
    	
	    try {
		temporaryFile = FileUtils.copyToTemporaryFile(bean.getFile());
		executeService("CreateThesisDissertationFile", getThesis(request), temporaryFile, bean.getSimpleFileName(), bean.getTitle(), bean.getSubTitle(), bean.getLanguage());
	    } finally {
		if (temporaryFile != null) {
		    temporaryFile.delete();
		}
	    }
	}

	return viewThesis(mapping, actionForm, request, response);
    }

    public ActionForward substituteExtendedAbstract(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
	ThesisFileBean bean = (ThesisFileBean) getRenderedObject();
	RenderUtils.invalidateViewState();
    
	if (bean != null && bean.getFile() != null) {
	    File temporaryFile = null;
    	
	    try {
		temporaryFile = FileUtils.copyToTemporaryFile(bean.getFile());
		executeService("CreateThesisAbstractFile", getThesis(request), temporaryFile, bean.getSimpleFileName(), bean.getTitle(), bean.getSubTitle(), bean.getLanguage());
	    } finally {
		if (temporaryFile != null) {
		    temporaryFile.delete();
		}
	    }
	}

	return viewThesis(mapping, actionForm, request, response);
    }

}
