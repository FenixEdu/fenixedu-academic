/*
 * Created on 25/Mai/2005 - 11:00:37
 * 
 */

package net.sourceforge.fenixedu.presentationTier.Action.gep.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithAttendsAndInquiriesRegistries;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesEmailReminderReport;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRegistry;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
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

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	IUserView userView = SessionUtils.getUserView(request);

	InfoExecutionYear currentExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(
		userView, "ReadCurrentExecutionYear", null);

	Object[] argsExecutionYearId = { currentExecutionYear.getIdInternal() };
	List<InfoDegreeCurricularPlan> degreeCurricularPlans = (List<InfoDegreeCurricularPlan>) ServiceUtils
		.executeService(userView, "ReadActiveDegreeCurricularPlansByExecutionYear",
			argsExecutionYearId);

	final ComparatorChain comparatorChain = new ComparatorChain();
	comparatorChain.addComparator(new BeanComparator("infoDegree.tipoCurso"));
	comparatorChain.addComparator(new BeanComparator("infoDegree.nome"));
	comparatorChain.addComparator(new BeanComparator("name"));
	comparatorChain.addComparator(new BeanComparator("idInternal"));
	Collections.sort(degreeCurricularPlans, comparatorChain);

	request.setAttribute(InquiriesUtil.DEGREE_CURRICULAR_PLANS_LIST, degreeCurricularPlans);

	return mapping.findForward("chooseDegreeCurricularPlans");
    }

    public ActionForward sendEmails(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	IUserView userView = SessionUtils.getUserView(request);
	DynaActionForm form = (DynaActionForm) actionForm;

	Integer[] degreeCurricularPlanIds = (Integer[]) form.get("degreeCurricularPlanIds");
	final String fromName = form.getString("fromAddress");
	final String fromAddress = form.getString("fromAddress");

	// Obtaining the current execution period
	// FIXME: THIS SHOULD BE PARAMETRIZABLE
	InfoExecutionPeriod currentExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(
		userView, "ReadCurrentExecutionPeriod", null);
	List<InfoInquiriesEmailReminderReport> reportList = new ArrayList<InfoInquiriesEmailReminderReport>(
		degreeCurricularPlanIds.length);

	for (int i = 0; i < degreeCurricularPlanIds.length; i++) {

	    Object[] argsDegreeCPId = { degreeCurricularPlanIds[i] };
	    InfoExecutionDegree infoExecutionDegreeStudent = (InfoExecutionDegree) ServiceUtils
		    .executeService(userView, "ReadActiveExecutionDegreebyDegreeCurricularPlanID",
			    argsDegreeCPId);

	    Object[] argsDegreeCurriculapPlanIdAndExecutionPeriod = { degreeCurricularPlanIds[i],
		    currentExecutionPeriod.getIdInternal(), Boolean.TRUE };
	    List<InfoStudentWithAttendsAndInquiriesRegistries> studentsList = (List<InfoStudentWithAttendsAndInquiriesRegistries>) ServiceUtils
		    .executeService(userView,
			    "student.ReadStudentsWithAttendsByDegreeCurricularPlanAndExecutionPeriod",
			    argsDegreeCurriculapPlanIdAndExecutionPeriod);

	    InfoInquiriesEmailReminderReport report = new InfoInquiriesEmailReminderReport();
	    report.setExecutionDegree(infoExecutionDegreeStudent);
	    report.setNumberDegreeStudents(studentsList.size());

	    for (InfoStudentWithAttendsAndInquiriesRegistries student : studentsList) {
		sendEmailReminder(request, student, currentExecutionPeriod, report, form, fromName,
			fromAddress);
	    }

	    reportList.add(report);

	}

	request.setAttribute(InquiriesUtil.EMAIL_REMINDER_REPORTS_LIST, reportList);
	return mapping.findForward("showReport");
    }

    private boolean sendEmailReminder(HttpServletRequest request,
	    InfoStudentWithAttendsAndInquiriesRegistries student,
	    InfoExecutionPeriod currentExecutionPeriod, InfoInquiriesEmailReminderReport report,
	    DynaActionForm form, String fromName, String fromAddress) {

	String emailAddress = student.getInfoPerson().getEmail();

	int numberUnansweredInquiries = 0;

	String unevaluatedCoursesNames = "";
	for (InfoFrequenta studentAttends : student.getAttends()) {
	    InfoExecutionCourse attendingCourse = studentAttends.getDisciplinaExecucao();
	    boolean evaluated = false;
	    if (!attendingCourse.getAvailableForInquiries().booleanValue()) {
		evaluated = true;
	    } else {
		for (InfoInquiriesRegistry studentRegistries : student.getInquiriesRegistries()) {
		    if ((attendingCourse.equals(studentRegistries.getExecutionCourse()))
			    && (currentExecutionPeriod.equals(studentRegistries.getExecutionPeriod()))) {

			evaluated = true;
			break;
		    }
		}
	    }
	    if (!evaluated) {
		unevaluatedCoursesNames += "\t* " + attendingCourse.getNome() + "\n";
		numberUnansweredInquiries++;
	    }
	}

	report.addNumberInquiries(student.getAttends().size());
	report.addUnansweredInquiries(numberUnansweredInquiries);

	if (!EmailSender.emailAddressFormatIsValid(emailAddress)) {
	    return false;
	}
	report.addStudentsWithEmail(1);

	if (numberUnansweredInquiries == 0)
	    return false;

	String bodyIntro = (String) form.get("bodyTextIntro");
	String bodyEnd = (String) form.get("bodyTextEnd");
	String body = bodyIntro + "\n" + unevaluatedCoursesNames + "\n" + bodyEnd;

	String subject = (String) form.get("bodyTextSubject");

	final Collection<String> bccs = new ArrayList<String>(1);
	bccs.add(emailAddress);

	if (EmailSender.send(fromName, fromAddress, null, null, bccs, subject, body).isEmpty()) {
	    report.addSentEmails(1);
	    return true;
	}

	return false;
    }
}
