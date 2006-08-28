package net.sourceforge.fenixedu.presentationTier.Action.certificate;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoFinalResult;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FinalResulUnreachedActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.Data;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 *  
 */
public class ChooseFinalResultInfoAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        if (session != null) {

            session.removeAttribute(SessionConstants.SPECIALIZATIONS);

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

            // Get request Information
            Integer requesterNumber = new Integer((String) chooseDeclaration.get("requesterNumber"));
            String graduationType = (String) chooseDeclaration.get("graduationType");

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
            request.setAttribute("path", "FinalResult");

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
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            session.removeAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN);
            session.removeAttribute(SessionConstants.DATE);
            session.removeAttribute(SessionConstants.INFO_FINAL_RESULT);
            session.removeAttribute(SessionConstants.ENROLMENT_LIST);
            session.removeAttribute(SessionConstants.INFO_EXECUTION_YEAR);
            session.removeAttribute(SessionConstants.CONCLUSION_DATE);
            session.removeAttribute(SessionConstants.INFO_BRANCH);
            session.removeAttribute("total");
            session.removeAttribute("givenCredits");

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

            List enrolmentList = null;
            InfoFinalResult infoFinalResult = null;
            try {
                Object argsFinalResult[] = { infoStudentCurricularPlan };
                infoFinalResult = (InfoFinalResult) ServiceManagerServiceFactory.executeService(
                        userView, "FinalResult", argsFinalResult);
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

            //check the last exam date
            //                        InfoEnrolmentEvaluation infoEnrolmentEvaluation = new
            // InfoEnrolmentEvaluation();

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
            //String dataAux = null;
            Object result = null;
            //                        Iterator iterator = enrolmentList.iterator();
            //                        int i = 0;
            //                        while (iterator.hasNext())
            //                        {
            //                            result = iterator.next();
            //                            infoEnrolmentEvaluation = (InfoEnrolmentEvaluation)
            // (((InfoEnrolment) result)
            //                                            .getInfoEvaluations().get(i));
            //                            // dataAux =
            //                            // DateFormat.getDateInstance().format(
            //                            // infoEnrolmentEvaluation.getExamDate());
            //                            /*
            //                             * if (conclusionDate.compareTo(dataAux) == -1) {
            //                             * conclusionDate = dataAux;
            //                             */
            //                        }
            List newEnrolmentList = new ArrayList();
            //get the last enrolmentEvaluation
            Iterator iterator1 = enrolmentList.iterator();
            double sum = 0;
            while (iterator1.hasNext()) {
                result = iterator1.next();
                InfoEnrolment infoEnrolment2 = (InfoEnrolment) result;
                //                            InfoCurricularCourseScope infoCurricularCourseScope =
                // infoEnrolment2
                //                                            .getInfoCurricularCourseScope();
                //                            sum = sum
                //                                            + Double.parseDouble(String
                //                                                            .valueOf(infoCurricularCourseScope
                //                                                                            .getInfoCurricularCourse()
                //                                                                            .getCredits()));

                InfoCurricularCourse infoCurricularCourse = infoEnrolment2.getInfoCurricularCourse();
                sum = sum + Double.parseDouble(String.valueOf(infoCurricularCourse.getCredits()));

                List aux = infoEnrolment2.getInfoEvaluations();

                if (aux != null && aux.size() > 0) {
                    if (aux.size() > 1) {
                        BeanComparator dateComparator = new BeanComparator("when");
                        Collections.sort(aux, dateComparator);
                        Collections.reverse(aux);
                    }
                    InfoEnrolmentEvaluation latestEvaluation = (InfoEnrolmentEvaluation) aux.get(0);
                    infoEnrolment2.setInfoEnrolmentEvaluation(latestEvaluation);
                    newEnrolmentList.add(infoEnrolment2);
                }
            }
            if ((infoStudentCurricularPlan.getGivenCredits() != null)
                    && !infoStudentCurricularPlan.getGivenCredits().equals(new Double(0))) {
                sum = sum
                        + Double
                                .parseDouble(String.valueOf(infoStudentCurricularPlan.getGivenCredits()));
                session.setAttribute("givenCredits", "POR ATRIBUIÇÃO DE CRÉDITOS");
            }

            BigDecimal roundedSum = new BigDecimal(sum);
            session.setAttribute("total", roundedSum.setScale(1, BigDecimal.ROUND_HALF_UP));//.toBigInteger());

            session.setAttribute(SessionConstants.CONCLUSION_DATE, conclusionDate);
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

            session.setAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN,
                    infoStudentCurricularPlan);

            session.setAttribute(SessionConstants.DATE, formatedDate);
            if (infoStudentCurricularPlan.getInfoBranch() != null
                    && infoStudentCurricularPlan.getInfoBranch().getName().length() != 0)
                session.setAttribute(SessionConstants.INFO_BRANCH, infoStudentCurricularPlan
                        .getInfoBranch().getName());
            session.setAttribute(SessionConstants.INFO_EXECUTION_YEAR, anoLectivo);
            session.setAttribute(SessionConstants.ENROLMENT_LIST, newEnrolmentList);

            session.setAttribute(SessionConstants.INFO_FINAL_RESULT, infoFinalResult);

            return mapping.findForward("ChooseSuccess");

        }

        throw new Exception();
    }
}