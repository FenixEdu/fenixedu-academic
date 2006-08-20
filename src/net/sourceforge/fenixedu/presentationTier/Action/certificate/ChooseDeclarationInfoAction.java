package net.sourceforge.fenixedu.presentationTier.Action.certificate;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.masterDegree.DocumentReason;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 *  
 */
public class ChooseDeclarationInfoAction extends DispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        if (session != null) {

            session.removeAttribute(SessionConstants.SPECIALIZATIONS);
            session.removeAttribute(SessionConstants.DOCUMENT_REASON);
            session.setAttribute(SessionConstants.DOCUMENT_REASON, DocumentReason.values());

            return mapping.findForward("PrepareReady");
        }
        throw new Exception();

    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            DynaActionForm chooseDeclaration = (DynaActionForm) form;

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            //remove sessions variables
            session.removeAttribute(SessionConstants.DEGREE_TYPE);
            session.removeAttribute(SessionConstants.DOCUMENT_REASON_LIST);

            // Get request Information
            Integer requesterNumber = new Integer((String) chooseDeclaration.get("requesterNumber"));
            String graduationType = (String) chooseDeclaration.get("graduationType");
            String[] destination = (String[]) chooseDeclaration.get("destination");

            if (destination.length != 0)
                session.setAttribute(SessionConstants.DOCUMENT_REASON_LIST, destination);

            final Registration registration = Registration.readStudentByNumberAndDegreeType(requesterNumber, DegreeType.MASTER_DEGREE);

            // inputs
            InfoStudent infoStudent = new InfoStudent(registration);
            session.setAttribute(SessionConstants.DEGREE_TYPE, infoStudent.getDegreeType());

            // output
            List infoStudentCurricularPlanList = null;

            //get informations
            try {
                ArrayList states = new ArrayList();
                states.add(StudentCurricularPlanState.ACTIVE);
                states.add(StudentCurricularPlanState.SCHOOLPARTCONCLUDED);
                Object args[] = { infoStudent, Specialization.valueOf(graduationType), states };
                infoStudentCurricularPlanList = (List) ServiceManagerServiceFactory.executeService(
                        userView, "CreateDeclaration", args);

            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("A Declaração", e);
            }

            if (infoStudentCurricularPlanList == null) {
                throw new NonExistingActionException("O aluno");
            }

            if (infoStudentCurricularPlanList.size() == 1) {
                InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) infoStudentCurricularPlanList
                        .get(0);
                request.setAttribute("studentCurricularPlanID", infoStudentCurricularPlan
                        .getIdInternal());

                return chooseFinal(mapping, form, request, response);

            }
            request.setAttribute("studentCurricularPlans", infoStudentCurricularPlanList);
            request.setAttribute("path", "Declaration");

            return mapping.findForward("ChooseStudentCurricularPlan");

        }
        throw new Exception();
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

        HttpSession session = request.getSession(false);

        if (session != null) {
            String anoLectivo;
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            session.removeAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN);
            session.removeAttribute(SessionConstants.DATE);

            Integer studentCurricularPlanID = (Integer) request.getAttribute("studentCurricularPlanID");
            if (studentCurricularPlanID == null) {
                studentCurricularPlanID = Integer.valueOf(request
                        .getParameter("studentCurricularPlanID"));
            }

            InfoStudentCurricularPlan infoStudentCurricularPlan = null;

            Object args[] = { studentCurricularPlanID };
            try {
                infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                        .executeService(userView, "ReadStudentCurricularPlan", args);
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("O aluno");
            }

            InfoExecutionYear infoExecutionYear = null;
            try {
                infoExecutionYear = (InfoExecutionYear) ServiceManagerServiceFactory.executeService(
                        userView, "ReadCurrentExecutionYear", null);

            } catch (RuntimeException e) {
                throw new RuntimeException("Error", e);
            }
            List enrolmentList = null;
            Object argsEnrolment[] = { infoStudentCurricularPlan.getIdInternal() };
            try {
                enrolmentList = (List) ServiceManagerServiceFactory.executeService(userView,
                        "GetEnrolmentList", argsEnrolment);

            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("Inscrição", e);
            }

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
            session.setAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN,
                    infoStudentCurricularPlan);
            session.setAttribute(SessionConstants.DATE, formatedDate);
            session.setAttribute(SessionConstants.INFO_EXECUTION_YEAR, infoExecutionYear);
            request.setAttribute("anoLectivo", anoLectivo);
            return mapping.findForward("ChooseSuccess");

        }

        throw new Exception();
    }

}