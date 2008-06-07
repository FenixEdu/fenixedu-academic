package net.sourceforge.fenixedu.presentationTier.Action.certificate;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.masterDegree.DocumentReason;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 */
public class ChooseDeclarationInfoAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	return mapping.findForward("PrepareReady");
    }

    public ActionForward chooseStudent(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Integer number = new Integer((String) ((DynaActionForm) form).get("requesterNumber"));
	request.setAttribute("registrations", Registration.readByNumberAndDegreeType(number,
		DegreeType.MASTER_DEGREE));

	request.setAttribute(SessionConstants.DOCUMENT_REASON, DocumentReason.values());

	return mapping.findForward("ChooseStudentCurricularPlan");
    }

    /**
         * @param mapping
         * @param form
         * @param request
         * @param response
         * @return
         */
    public ActionForward chooseFinal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IUserView userView = getUserView(request);

	DynaActionForm chooseDeclaration = (DynaActionForm) form;

	// Get the Information
	String[] destination = (String[]) chooseDeclaration.get("destination");
	Integer studentCurricularPlanID = (Integer) chooseDeclaration.get("studentCurricularPlanID");

	if (destination.length != 0) {
	    request.setAttribute(SessionConstants.DOCUMENT_REASON_LIST, destination);
	}

	InfoStudentCurricularPlan infoStudentCurricularPlan = InfoStudentCurricularPlan
		.newInfoFromDomain(rootDomainObject
			.readStudentCurricularPlanByOID(studentCurricularPlanID));

	InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(ExecutionYear
		.readCurrentExecutionYear());

	List enrolmentList = null;
	Object argsEnrolment[] = { infoStudentCurricularPlan.getIdInternal() };
	try {
	    enrolmentList = (List) ServiceManagerServiceFactory.executeService(
		    "GetEnrolmentList", argsEnrolment);

	} catch (NonExistingServiceException e) {
	    throw new NonExistingActionException("Inscrição", e);
	}

	String anoLectivo;
	if (enrolmentList.size() == 0) {
	    anoLectivo = infoExecutionYear.getYear();
	} else {
	    anoLectivo = ((InfoEnrolment) enrolmentList.get(0)).getInfoExecutionPeriod()
		    .getInfoExecutionYear().getYear();
	}

	Locale locale = new Locale("pt", "PT");
	Date date = new Date();
	String formatedDate = "Lisboa, "
		+ DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);
	request.setAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);
	request.setAttribute(SessionConstants.DATE, formatedDate);
	request.setAttribute(SessionConstants.INFO_EXECUTION_YEAR, infoExecutionYear);
	request.setAttribute("anoLectivo", anoLectivo);
	return mapping.findForward("ChooseSuccess");

    }

}