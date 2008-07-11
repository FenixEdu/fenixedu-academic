package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentInExtraCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Angela Created on 8/Out/2003
 */
public class EditStudentCurricularCoursePlan extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	DynaActionForm editStudentCurricularPlanForm = (DynaActionForm) form;
	Integer studentCurricularPlanId = new Integer(getFromRequest("studentCurricularPlanId", request));
	IUserView userView = getUserView(request);

	Object args[] = { studentCurricularPlanId };

	InfoStudentCurricularPlan infoStudentCurricularPlan = null;
	try {
	    infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory.executeService(
		    "ReadPosGradStudentCurricularPlanById", args);
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	Object argsBranches[] = { infoStudentCurricularPlan.getInfoDegreeCurricularPlan().getIdInternal() };
	List branchList = null;

	try {
	    branchList = (List) ServiceManagerServiceFactory.executeService("ReadBranchesByDegreeCurricularPlanId", argsBranches);
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	// put request
	request.setAttribute(SessionConstants.BRANCH, (branchList == null) ? new ArrayList() : branchList);
	request.setAttribute("currentState", infoStudentCurricularPlan.getCurrentState());

	request.setAttribute("student", infoStudentCurricularPlan.getInfoStudent());
	request.setAttribute("studentCurricularPlan", infoStudentCurricularPlan);

	editStudentCurricularPlanForm.set("specialization", infoStudentCurricularPlan.getSpecialization().toString());

	if (infoStudentCurricularPlan.getInfoBranch() != null) {
	    editStudentCurricularPlanForm.set("branch", infoStudentCurricularPlan.getInfoBranch().getIdInternal());
	}

	editStudentCurricularPlanForm.set("currentState", infoStudentCurricularPlan.getCurrentState().toString());
	editStudentCurricularPlanForm.set("credits", (infoStudentCurricularPlan.getGivenCredits() == null) ? "0.0" : String
		.valueOf(infoStudentCurricularPlan.getGivenCredits()));
	editStudentCurricularPlanForm.set("startDate", infoStudentCurricularPlan.getStartDateFormatted());
	String[] formValues = new String[infoStudentCurricularPlan.getInfoEnrolments().size()];
	int i = 0;
	for (Iterator iter = infoStudentCurricularPlan.getInfoEnrolments().iterator(); iter.hasNext();) {
	    Object enrollment = iter.next();
	    if (enrollment instanceof InfoEnrolmentInExtraCurricularCourse) {
		Integer enrollmentId = ((InfoEnrolmentInExtraCurricularCourse) enrollment).getIdInternal();
		formValues[i] = enrollmentId.toString();
	    }
	    i++;
	}
	DynaActionForm coursesForm = (DynaActionForm) form;
	coursesForm.set("extraCurricularCourses", formValues);
	return mapping.findForward("editStudentCurricularCoursePlan");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	final IUserView userView = getUserView(request);

	final Integer scpOID = getIntegerFromRequest(request, "studentCurricularPlanId");
	request.setAttribute("studentCurricularPlanId", scpOID);

	final DynaActionForm editForm = (DynaActionForm) form;
	final String currentState = (String) editForm.get("currentState");
	final Integer branchOID = (Integer) editForm.get("branch");
	final String specialization = (String) editForm.get("specialization");
	final String startDate = (String) editForm.get("startDate");
	final String observations = (String) editForm.get("observations");

	final Object creditsObj = editForm.get("credits");
	final Double credits = (creditsObj != null && ((String) creditsObj).length() > 0) ? (Double.valueOf((String) creditsObj))
		: null;

	final List<Integer> extraCurricularOIDs = new ArrayList<Integer>();
	for (final String extraCurricular : Arrays.asList((String[]) editForm.get("extraCurricularCourses"))) {
	    extraCurricularOIDs.add(Integer.valueOf(extraCurricular));
	}

	final Object args[] = { userView, scpOID, currentState, credits, startDate, extraCurricularOIDs, observations, branchOID,
		specialization };
	try {
	    ServiceManagerServiceFactory.executeService("EditPosGradStudentCurricularPlanStateAndCredits", args);
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	return mapping.findForward("ShowStudentCurricularCoursePlan");
    }

    public ActionForward enrol(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	Enrolment enrolment = (Enrolment) rootDomainObject
		.readCurriculumModuleByOID(getIntegerFromRequest(request, "enrolmentID"));
	executeService("SetEnrolmentState", enrolment, EnrollmentState.ENROLLED);

	request.setAttribute("studentCurricularPlanId", enrolment.getStudentCurricularPlan().getIdInternal());

	return prepare(mapping, form, request, response);
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
	String parameterString = request.getParameter(parameter);
	if (parameterString == null) {
	    parameterString = request.getAttribute(parameter).toString();
	}
	return parameterString;
    }
}