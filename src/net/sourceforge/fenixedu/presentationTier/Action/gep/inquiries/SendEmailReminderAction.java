/*
 * Created on 25/Mai/2005 - 11:00:37
 * 
 */

package net.sourceforge.fenixedu.presentationTier.Action.gep.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesEmailReminderReport;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriod;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.InquiriesUtil;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.utl.ist.fenix.tools.smtp.EmailSender;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class SendEmailReminderAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	InfoExecutionYear currentExecutionYear = (InfoExecutionYear) executeService("ReadCurrentExecutionYear");

	Object[] argsExecutionYearId = { currentExecutionYear.getIdInternal() };
	List<InfoDegreeCurricularPlan> degreeCurricularPlans = (List<InfoDegreeCurricularPlan>) ServiceUtils.executeService(
		"ReadActiveDegreeCurricularPlansByExecutionYear", argsExecutionYearId);

	final ComparatorChain comparatorChain = new ComparatorChain();
	comparatorChain.addComparator(new BeanComparator("infoDegree.tipoCurso"));
	comparatorChain.addComparator(new BeanComparator("infoDegree.nome"));
	comparatorChain.addComparator(new BeanComparator("name"));
	comparatorChain.addComparator(new BeanComparator("idInternal"));
	Collections.sort(degreeCurricularPlans, comparatorChain);

	request.setAttribute(InquiriesUtil.DEGREE_CURRICULAR_PLANS_LIST, degreeCurricularPlans);

	return mapping.findForward("chooseDegreeCurricularPlans");
    }

    public ActionForward sendEmails(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm form = (DynaActionForm) actionForm;

	Integer[] degreeCurricularPlanIds = (Integer[]) form.get("degreeCurricularPlanIds");
	final String fromName = form.getString("fromAddress");
	final String fromAddress = form.getString("fromAddress");

	InquiryResponsePeriod openPeriod = InquiryResponsePeriod.readOpenPeriod();
	if (openPeriod == null) {
	    return null;
	}
	ExecutionSemester executionSemester = openPeriod.getExecutionPeriod();

	List<InfoInquiriesEmailReminderReport> reportList = new ArrayList<InfoInquiriesEmailReminderReport>(
		degreeCurricularPlanIds.length);

	for (int i = 0; i < degreeCurricularPlanIds.length; i++) {

	    final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject
		    .readDegreeCurricularPlanByOID(degreeCurricularPlanIds[i]);

	    Set<Student> studentsList = (Set<Student>) executeService(
		    "student.ReadStudentsWithAttendsByDegreeCurricularPlanAndExecutionPeriod", degreeCurricularPlan,
		    executionSemester);

	    InfoInquiriesEmailReminderReport report = new InfoInquiriesEmailReminderReport();

	    final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionSemester
		    .getExecutionYear());
	    report.setExecutionDegree(InfoExecutionDegree.newInfoFromDomain(executionDegree));
	    report.setNumberDegreeStudents(studentsList.size());

	    for (Student student : studentsList) {
		sendEmailReminder(request, student, executionSemester, report, form, fromName, fromAddress);
	    }

	    reportList.add(report);

	}

	request.setAttribute(InquiriesUtil.EMAIL_REMINDER_REPORTS_LIST, reportList);
	return mapping.findForward("showReport");
    }

    private boolean sendEmailReminder(HttpServletRequest request, Student student, ExecutionSemester executionSemester,
	    InfoInquiriesEmailReminderReport report, DynaActionForm form, String fromName, String fromAddress)
	    throws FenixFilterException, FenixServiceException {

	if (student == null || student.getPerson() == null || student.getPerson().getDefaultEmailAddress() == null) {
	    return false;
	}
	String emailAddress = student.getPerson().getDefaultEmailAddress().getValue();
	if (!EmailSender.emailAddressFormatIsValid(emailAddress)) {
	    return false;
	}

	final Collection<String> inquiriesCoursesNamesToRespond = student.getInquiriesCoursesNamesToRespond(executionSemester);
	if (inquiriesCoursesNamesToRespond.isEmpty()) {
	    return false;
	}

	StringBuilder body = new StringBuilder();
	body.append(form.get("bodyTextIntro"));
	body.append("\n");

	for (String courseName : inquiriesCoursesNamesToRespond) {
	    body.append("\t*");
	    body.append(courseName);
	    body.append("\n");
	}

	body.append("\n");
	body.append(form.get("bodyTextEnd"));

	report.addNumberInquiries(inquiriesCoursesNamesToRespond.size());
	report.addUnansweredInquiries(inquiriesCoursesNamesToRespond.size());
	report.addStudentsWithEmail(1);

	String subject = (String) form.get("bodyTextSubject");

	final Collection<String> bccs = new ArrayList<String>(1);
	bccs.add(emailAddress);

	final Object[] args = { null, null, bccs, fromName, fromAddress, subject, body.toString() };
	executeService(request, "commons.SendMail", args);

	return false;
    }
}
