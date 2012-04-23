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
import net.sourceforge.fenixedu.applicationTier.Servico.student.ConfirmAttributionOfFinalDegreeWork;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadFinalDegreeWorkStudentGroupByUsername;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroup;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupProposal;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupStudent;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Luis Cruz
 */
@Mapping(module = "student", path = "/finalDegreeWorkAttribution", input = "/finalDegreeWorkAttribution.do?method=prepare&page=0", attribute = "finalDegreeWorkAttributionForm", formBean = "finalDegreeWorkAttributionForm", scope = "request", parameter = "method")
@Forwards(value = {
	@Forward(name = "NoConfirmationInProcessException", path = "/student/finalDegreeWork/noConfirmationInProcess.jsp"),
		@Forward(name = "prepareShowFinalDegreeWorkList", path = "/finalDegreeWorkAttribution.do?method=prepare&page=0"),
	@Forward(name = "showFinalDegreeWorkList", path = "/student/finalDegreeWork/attribution.jsp") })
@Exceptions(value = {
	@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.ConfirmAttributionOfFinalDegreeWork.NoAttributionToConfirmException.class, key = "error.message.NoAttributionToConfirmException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
	@ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.student.CheckCandidacyConditionsForFinalDegreeWork.NoDegreeStudentCurricularPlanFoundException.class, key = "error.message.NoDegreeStudentCurricularPlanFoundException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class FinalDegreeWorkAttributionDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	IUserView userView = UserView.getUser();

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

	ExecutionDegree selectedExecutionDegree = null;
	for (final Registration registration : userView.getPerson().getStudent().getRegistrationsSet()) {
	    for (final GroupStudent groupStudent : registration.getAssociatedGroupStudentsSet()) {
		final FinalDegreeWorkGroup group = groupStudent.getFinalDegreeDegreeWorkGroup();
		if (group != null) {
		    final ExecutionDegree executionDegree = group.getExecutionDegree();
		    if (executionDegree != null) {
			selectedExecutionDegree = executionDegree;
		    }
		}
	    }
	}
	InfoGroup infoGroup = (InfoGroup) ReadFinalDegreeWorkStudentGroupByUsername.run(userView.getPerson(), selectedExecutionDegree);
	if (infoGroup != null && infoGroup.getGroupProposals() != null) {
	    final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(infoGroup.getExecutionDegree()
		    .getIdInternal());
	    if (!executionDegree.hasScheduling() || executionDegree.getScheduling().getAttributionByTeachers() != Boolean.TRUE) {
		return mapping.findForward("NoConfirmationInProcessException");
	    }

	    Collections.sort(infoGroup.getGroupProposals(), new BeanComparator("orderOfPreference"));

	    request.setAttribute("infoGroup", infoGroup);

	    InfoGroupProposal infoGroupProposal = (InfoGroupProposal) CollectionUtils.find(infoGroup.getGroupProposals(),
		    new PREDICATE_FIND_PROPOSAL_ATTRIBUTED_TO_GROUP_BY_TEACHER(infoGroup.getIdInternal()));
	    if (infoGroupProposal != null) {
		finalDegreeWorkAttributionForm.set("attributedByTeacher", infoGroupProposal.getFinalDegreeWorkProposal()
			.getIdInternal().toString());
	    }

	    String confirmAttributions[] = new String[infoGroup.getGroupStudents().size()];
	    for (int i = 0; i < infoGroup.getGroupStudents().size(); i++) {
		InfoGroupStudent infoGroupStudent = (InfoGroupStudent) infoGroup.getGroupStudents().get(i);
		if (infoGroupStudent != null && infoGroupStudent.getFinalDegreeWorkProposalConfirmation() != null) {
		    confirmAttributions[i] = infoGroupStudent.getFinalDegreeWorkProposalConfirmation().getIdInternal().toString();
		    confirmAttributions[i] += infoGroupStudent.getStudent().getIdInternal();
		}
	    }
	    finalDegreeWorkAttributionForm.set("confirmAttributions", confirmAttributions);

	    request.setAttribute("finalDegreeWorkAttributionForm", finalDegreeWorkAttributionForm);
	}

	return mapping.findForward("showFinalDegreeWorkList");
    }

    public ActionForward confirmAttribution(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	DynaActionForm finalDegreeWorkAttributionForm = (DynaActionForm) form;

	String selectedGroupProposalOID = (String) finalDegreeWorkAttributionForm.get("selectedGroupProposal");

	IUserView userView = UserView.getUser();

	if (selectedGroupProposalOID != null && !selectedGroupProposalOID.equals("")) {

	    ConfirmAttributionOfFinalDegreeWork.run(userView.getUtilizador(), new Integer(selectedGroupProposalOID));
	}

	return mapping.findForward("prepareShowFinalDegreeWorkList");
    }

    private class PREDICATE_FIND_PROPOSAL_ATTRIBUTED_TO_GROUP_BY_TEACHER implements Predicate {

	Integer groupID = null;

	public boolean evaluate(Object arg0) {
	    InfoGroupProposal infoGroupProposal = (InfoGroupProposal) arg0;
	    return (infoGroupProposal.getFinalDegreeWorkProposal().getGroupAttributedByTeacher() != null)
		    && (groupID.equals(infoGroupProposal.getFinalDegreeWorkProposal().getGroupAttributedByTeacher()
			    .getIdInternal()));
	}

	public PREDICATE_FIND_PROPOSAL_ATTRIBUTED_TO_GROUP_BY_TEACHER(Integer groupID) {
	    super();
	    this.groupID = groupID;
	}
    }

}