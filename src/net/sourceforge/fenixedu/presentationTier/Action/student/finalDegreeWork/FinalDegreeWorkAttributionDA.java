/*
 * Created 2004/04/25
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.finalDegreeWork;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroup;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupProposal;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupStudent;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Luis Cruz
 */
public class FinalDegreeWorkAttributionDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	IUserView userView = SessionUtils.getUserView(request);

	final DynaActionForm finalDegreeWorkAttributionForm = (DynaActionForm) form;

    final ExecutionYear executionYear;
    final String executionYearOID = (String) finalDegreeWorkAttributionForm.get("executionYearOID");
    if (executionYearOID == null || executionYearOID.equals("")) {
    	executionYear = ExecutionYear.readCurrentExecutionYear();
    	finalDegreeWorkAttributionForm.set("executionYearOID", executionYear.getIdInternal().toString());
    } else {
    	executionYear = rootDomainObject.readExecutionYearByOID(Integer.valueOf(executionYearOID));
    }

    final Set<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR);
    executionYears.addAll(rootDomainObject.getExecutionYearsSet());
	request.setAttribute("executionYears", executionYears);

	Object[] args = { userView.getPerson(), executionYear };
	InfoGroup infoGroup = (InfoGroup) ServiceUtils.executeService(userView,
		"ReadFinalDegreeWorkStudentGroupByUsername", args);
	if (infoGroup != null && infoGroup.getGroupProposals() != null) {
	    final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(infoGroup
		    .getExecutionDegree().getIdInternal());
	    if (!executionDegree.hasScheduling()
		    || executionDegree.getScheduling().getAttributionByTeachers() != Boolean.TRUE) {
		return mapping.findForward("NoConfirmationInProcessException");
	    }

	    Collections.sort(infoGroup.getGroupProposals(), new BeanComparator("orderOfPreference"));

	    request.setAttribute("infoGroup", infoGroup);

	    InfoGroupProposal infoGroupProposal = (InfoGroupProposal) CollectionUtils.find(infoGroup
		    .getGroupProposals(), new PREDICATE_FIND_PROPOSAL_ATTRIBUTED_TO_GROUP_BY_TEACHER(
		    infoGroup.getIdInternal()));
	    if (infoGroupProposal != null) {
		finalDegreeWorkAttributionForm.set("attributedByTeacher", infoGroupProposal
			.getFinalDegreeWorkProposal().getIdInternal().toString());
	    }

	    String confirmAttributions[] = new String[infoGroup.getGroupStudents().size()];
	    for (int i = 0; i < infoGroup.getGroupStudents().size(); i++) {
		InfoGroupStudent infoGroupStudent = (InfoGroupStudent) infoGroup.getGroupStudents().get(
			i);
		if (infoGroupStudent != null
			&& infoGroupStudent.getFinalDegreeWorkProposalConfirmation() != null) {
		    confirmAttributions[i] = infoGroupStudent.getFinalDegreeWorkProposalConfirmation()
			    .getIdInternal().toString();
		    confirmAttributions[i] += infoGroupStudent.getStudent().getIdInternal();
		}
	    }
	    finalDegreeWorkAttributionForm.set("confirmAttributions", confirmAttributions);

	    request.setAttribute("finalDegreeWorkAttributionForm", finalDegreeWorkAttributionForm);
	}

	return mapping.findForward("showFinalDegreeWorkList");
    }

    
    public ActionForward confirmAttribution(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	DynaActionForm finalDegreeWorkAttributionForm = (DynaActionForm) form;

	String selectedGroupProposalOID = (String) finalDegreeWorkAttributionForm
		.get("selectedGroupProposal");

	IUserView userView = SessionUtils.getUserView(request);

	if (selectedGroupProposalOID != null && !selectedGroupProposalOID.equals("")) {
	    Object args[] = { userView.getUtilizador(), new Integer(selectedGroupProposalOID) };
	    ServiceUtils.executeService(userView, "ConfirmAttributionOfFinalDegreeWork", args);
	}

	return mapping.findForward("prepareShowFinalDegreeWorkList");
    }

    private class PREDICATE_FIND_PROPOSAL_ATTRIBUTED_TO_GROUP_BY_TEACHER implements Predicate {

	Integer groupID = null;

	public boolean evaluate(Object arg0) {
	    InfoGroupProposal infoGroupProposal = (InfoGroupProposal) arg0;
	    return (infoGroupProposal.getFinalDegreeWorkProposal().getGroupAttributedByTeacher() != null)
		    && (groupID.equals(infoGroupProposal.getFinalDegreeWorkProposal()
			    .getGroupAttributedByTeacher().getIdInternal()));
	}

	public PREDICATE_FIND_PROPOSAL_ATTRIBUTED_TO_GROUP_BY_TEACHER(Integer groupID) {
	    super();
	    this.groupID = groupID;
	}
    }

}