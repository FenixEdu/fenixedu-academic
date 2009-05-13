package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.ContextSelectionBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ExecutionPeriodDA extends FenixContextDispatchAction {

    private static final Comparator<ExecutionDegree> executionDegreeComparator = new Comparator<ExecutionDegree>() {
	public int compare(ExecutionDegree executionDegree1, ExecutionDegree executionDegree2) {
	    final Degree degree1 = executionDegree1.getDegreeCurricularPlan().getDegree();
	    final Degree degree2 = executionDegree2.getDegreeCurricularPlan().getDegree();

	    int degreeTypeComparison = degree1.getDegreeType().compareTo(degree2.getDegreeType());
	    return (degreeTypeComparison != 0) ? degreeTypeComparison : degree1.getNome().compareTo(degree2.getNome());
	}
    };

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	ContextSelectionBean contextSelectionBean = (ContextSelectionBean) request
		.getAttribute(PresentationConstants.CONTEXT_SELECTION_BEAN);
	final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>(ExecutionDegree
		.filterByAcademicInterval(contextSelectionBean.getAcademicInterval()));
	Collections.sort(executionDegrees, executionDegreeComparator);
	request.setAttribute("executionDegrees", executionDegrees);

	return mapping.findForward("showForm");
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	ContextSelectionBean contextSelectionBean = (ContextSelectionBean) request
		.getAttribute(PresentationConstants.CONTEXT_SELECTION_BEAN);
	final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>(ExecutionDegree
		.filterByAcademicInterval(contextSelectionBean.getAcademicInterval()));
	Collections.sort(executionDegrees, executionDegreeComparator);
	request.setAttribute("executionDegrees", executionDegrees);

	return mapping.findForward("choose");
    }
}