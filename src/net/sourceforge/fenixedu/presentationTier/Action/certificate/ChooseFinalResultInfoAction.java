package net.sourceforge.fenixedu.presentationTier.Action.certificate;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoFinalResult;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FinalResulUnreachedActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.Data;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ChooseFinalResultInfoAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return mapping.findForward("PrepareReady");
    }

    public ActionForward chooseStudent(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Integer number = new Integer((String) ((DynaActionForm) form).get("requesterNumber"));
	request.setAttribute("registrations", Registration.readByNumberAndDegreeType(number,
		DegreeType.MASTER_DEGREE));

	return mapping.findForward("ChooseStudentCurricularPlan");
    }

    public ActionForward chooseFinal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IUserView userView = getUserView(request);

	Integer studentCurricularPlanID = (Integer) request.getAttribute("studentCurricularPlanID");
	if (studentCurricularPlanID == null) {
	    studentCurricularPlanID = Integer.valueOf(request.getParameter("studentCurricularPlanID"));
	}

	InfoStudentCurricularPlan infoStudentCurricularPlan = null;

	Object args[] = { studentCurricularPlanID };
	try {
	    infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
		    .executeService(userView, "ReadStudentCurricularPlan", args);
	} catch (NonExistingServiceException e) {
	    throw new NonExistingActionException("O aluno");
	}

	List enrolmentList = null;
	InfoFinalResult infoFinalResult = null;
	try {
	    Object argsFinalResult[] = { infoStudentCurricularPlan };
	    infoFinalResult = (InfoFinalResult) ServiceManagerServiceFactory.executeService(userView,
		    "FinalResult", argsFinalResult);
	} catch (FenixServiceException e) {
	    throw new FenixServiceException("");
	}

	if (infoFinalResult == null) {
	    throw new FinalResulUnreachedActionException("");
	}

	try {
	    Object argsEnrolmentList[] = { infoStudentCurricularPlan.getIdInternal(),
		    EnrollmentState.APROVED };
	    enrolmentList = (List) ServiceManagerServiceFactory.executeService(userView,
		    "GetEnrolmentList", argsEnrolmentList);

	} catch (NonExistingServiceException e) {
	    throw new NonExistingActionException("Inscrição", e);
	}

	if (enrolmentList.size() == 0) {
	    throw new NonExistingActionException("Inscrição em Disciplinas");
	}

	String conclusionDate = null;
	Date endOfScholarshipDate = null;
	try {
	    Object argsTemp[] = { studentCurricularPlanID };
	    endOfScholarshipDate = (Date) ServiceManagerServiceFactory.executeService(userView,
		    "GetEndOfScholarshipDate", argsTemp);

	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	conclusionDate = Data.format2DayMonthYear(endOfScholarshipDate, "/");
	Date dateConclusion = Data.convertStringDate(conclusionDate, "/");
	conclusionDate = DateFormat.getDateInstance().format(dateConclusion);
	// String dataAux = null;
	Object result = null;

	List newEnrolmentList = new ArrayList();
	// get the last enrolmentEvaluation
	Iterator iterator1 = enrolmentList.iterator();
	double sum = 0;
	while (iterator1.hasNext()) {
	    result = iterator1.next();
	    InfoEnrolment infoEnrolment2 = (InfoEnrolment) result;

	    InfoCurricularCourse infoCurricularCourse = infoEnrolment2.getInfoCurricularCourse();
	    sum = sum + Double.parseDouble(String.valueOf(infoCurricularCourse.getCredits()));
	    newEnrolmentList.add(infoEnrolment2);
	}
	if ((infoStudentCurricularPlan.getGivenCredits() != null)
		&& !infoStudentCurricularPlan.getGivenCredits().equals(new Double(0))) {
	    sum = sum + Double.parseDouble(String.valueOf(infoStudentCurricularPlan.getGivenCredits()));
	    request.setAttribute("givenCredits", "POR ATRIBUIÇÃO DE CRÉDITOS");
	}

	BigDecimal roundedSum = new BigDecimal(sum);
	request.setAttribute("total", roundedSum.setScale(1, BigDecimal.ROUND_HALF_UP));// .toBigInteger());

	request.setAttribute(SessionConstants.CONCLUSION_DATE, conclusionDate);
	try {
	    ServiceManagerServiceFactory.executeService(userView, "ReadCurrentExecutionYear", null);

	} catch (RuntimeException e) {
	    throw new RuntimeException("Error", e);
	}
	Locale locale = new Locale("pt", "PT");
	Date date = new Date();
	String anoLectivo = "";
	if (newEnrolmentList != null && newEnrolmentList.size() > 0) {
	    anoLectivo = ((InfoEnrolment) newEnrolmentList.get(0)).getInfoExecutionPeriod()
		    .getInfoExecutionYear().getYear();
	}
	String formatedDate = "Lisboa, "
		+ DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);

	request.setAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);

	request.setAttribute(SessionConstants.DATE, formatedDate);
	if (infoStudentCurricularPlan.getInfoBranch() != null
		&& infoStudentCurricularPlan.getInfoBranch().getName().length() != 0)
	    request.setAttribute(SessionConstants.INFO_BRANCH, infoStudentCurricularPlan.getInfoBranch()
		    .getName());
	request.setAttribute(SessionConstants.INFO_EXECUTION_YEAR, anoLectivo);
	request.setAttribute(SessionConstants.ENROLMENT_LIST, newEnrolmentList);

	request.setAttribute(SessionConstants.INFO_FINAL_RESULT, infoFinalResult);

	return mapping.findForward("ChooseSuccess");

    }
}