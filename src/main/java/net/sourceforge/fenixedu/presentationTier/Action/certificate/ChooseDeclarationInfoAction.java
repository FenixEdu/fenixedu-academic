package net.sourceforge.fenixedu.presentationTier.Action.certificate;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.enrolment.GetEnrolmentList;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.masterDegree.DocumentReason;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 */
@Mapping(module = "masterDegreeAdministrativeOffice", path = "/chooseDeclarationInfoAction",
        input = "df.page.chooseStudentForDeclaration", attribute = "chooseCertificateInfoForm",
        formBean = "chooseCertificateInfoForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "ChooseSuccess", path = "/printDeclaration.do?method=prepare"),
        @Forward(name = "PrepareReady", path = "df.page.chooseStudentForDeclaration"),
        @Forward(name = "ChooseStudentCurricularPlan", path = "df.page.chooseStudentCurricularPlanForDeclaration") })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
        key = "resources.Action.exceptions.NonExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class ChooseDeclarationInfoAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        return mapping.findForward("PrepareReady");
    }

    public ActionForward chooseStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Integer number = new Integer((String) ((DynaActionForm) form).get("requesterNumber"));
        request.setAttribute("registrations", Registration.readByNumberAndDegreeType(number, DegreeType.MASTER_DEGREE));

        request.setAttribute(PresentationConstants.DOCUMENT_REASON, DocumentReason.values());

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

        User userView = getUserView(request);

        DynaActionForm chooseDeclaration = (DynaActionForm) form;

        // Get the Information
        String[] destination = (String[]) chooseDeclaration.get("destination");

        if (destination.length != 0) {
            request.setAttribute(PresentationConstants.DOCUMENT_REASON_LIST, destination);
        }

        InfoStudentCurricularPlan infoStudentCurricularPlan =
                InfoStudentCurricularPlan.newInfoFromDomain(this.<StudentCurricularPlan> getDomainObject(chooseDeclaration,
                        "studentCurricularPlanID"));

        InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(ExecutionYear.readCurrentExecutionYear());

        List enrolmentList = null;

        // try {
        enrolmentList = GetEnrolmentList.run(infoStudentCurricularPlan.getExternalId());

        // } catch (NonExistingServiceException e) {
        // throw new NonExistingActionException("Inscrição", e);
        // }

        String anoLectivo;
        if (enrolmentList.size() == 0) {
            anoLectivo = infoExecutionYear.getYear();
        } else {
            anoLectivo =
                    ((InfoEnrolment) enrolmentList.iterator().next()).getInfoExecutionPeriod().getInfoExecutionYear().getYear();
        }

        Locale locale = new Locale("pt", "PT");
        Date date = new Date();
        String formatedDate = "Lisboa, " + DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);
        request.setAttribute(PresentationConstants.INFO_STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);
        request.setAttribute(PresentationConstants.DATE, formatedDate);
        request.setAttribute(PresentationConstants.INFO_EXECUTION_YEAR, infoExecutionYear);
        request.setAttribute("anoLectivo", anoLectivo);
        return mapping.findForward("ChooseSuccess");

    }

}