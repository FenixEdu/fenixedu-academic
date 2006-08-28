package net.sourceforge.fenixedu.presentationTier.Action.certificate;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ScholarshipNotFinishedServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoPrice;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.masterDegree.DocumentReason;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ScholarshipNotFinishedActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.CertificateList;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 */
public class ChooseCertificateInfoAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(SessionConstants.SPECIALIZATIONS);
            session.removeAttribute(SessionConstants.DOCUMENT_REASON);
            session.removeAttribute(SessionConstants.CERTIFICATE_LIST);

            IUserView userView = getUserView(request);

            List types = new ArrayList();
            types.add(DocumentType.CERTIFICATE);

            // inputs
            Object args[] = { GraduationType.MASTER_DEGREE, types };

            // output
            List getCertificateList = null;
            // certificateList = CertificateList.toArrayList();
            try {
                getCertificateList = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadCertificateList", args);

            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("A lista de certidões", e);
            }

            List result = new ArrayList();
            Iterator iterator = getCertificateList.iterator();
            while (iterator.hasNext()) {
                InfoPrice price = (InfoPrice) iterator.next();
                result.add(price.getDescription());
            }
            session.setAttribute(SessionConstants.DOCUMENT_REASON, DocumentReason.values());
            session.setAttribute(SessionConstants.CERTIFICATE_LIST, new CertificateList().toArrayList());

            return mapping.findForward("PrepareReady");
        }
        throw new Exception();

    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            DynaActionForm chooseDeclaration = (DynaActionForm) form;

            IUserView userView = getUserView(request);

            // remove sessions variables
            session.removeAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN);
            session.removeAttribute(SessionConstants.MASTER_DEGREE_THESIS_DATA_VERSION);
            session.removeAttribute(SessionConstants.MASTER_DEGREE_PROOF_HISTORY);
            session.removeAttribute(SessionConstants.DEGREE_TYPE);
            session.removeAttribute(SessionConstants.DATE);
            session.removeAttribute(SessionConstants.DOCUMENT_REASON_LIST);
            session.removeAttribute(SessionConstants.CERTIFICATE_TYPE);

            // Get the Information
            Integer requesterNumber = new Integer((String) chooseDeclaration.get("requesterNumber"));
            String graduationType = (String) chooseDeclaration.get("graduationType");
            String certificateString = (String) chooseDeclaration.get("certificateList");
            String[] destination = (String[]) chooseDeclaration.get("destination");

            if (destination.length != 0)
                session.setAttribute(SessionConstants.DOCUMENT_REASON_LIST, destination);
            session.setAttribute(SessionConstants.CERTIFICATE_TYPE, certificateString);

            final Registration registration = Registration.readStudentByNumberAndDegreeType(requesterNumber, DegreeType.MASTER_DEGREE);
            // inputs
            InfoStudent infoStudent = InfoStudent.newInfoFromDomain(registration);
            session.setAttribute(SessionConstants.DEGREE_TYPE, infoStudent.getDegreeType());

            // output
            List infoStudentCurricularPlanList = null;

            // get informations
            try {
                if (certificateString.equals("Matrícula")
                        || certificateString.equals("Matrícula e Inscrição")
                        || certificateString.equals("Duração do Degree")
                        || certificateString.equals("Inscrição")) {
                    ArrayList states = new ArrayList();
                    states.add(StudentCurricularPlanState.ACTIVE);
                    states.add(StudentCurricularPlanState.SCHOOLPARTCONCLUDED);
                    Object args[] = { infoStudent, Specialization.valueOf(graduationType), states };
                    infoStudentCurricularPlanList = (List) ServiceManagerServiceFactory.executeService(
                            userView, "CreateDeclaration", args);
                } else {
                    Object args[] = { infoStudent, Specialization.valueOf(graduationType) };
                    infoStudentCurricularPlanList = (List) ServiceManagerServiceFactory.executeService(
                            userView, "CreateDeclaration", args);
                }

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
            request.setAttribute("path", "Certificate");

            return mapping.findForward("ChooseStudentCurricularPlan");

        }
        throw new Exception();
    }

    public ActionForward chooseFinal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            // remove sessions variables
            session.removeAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN);
            session.removeAttribute(SessionConstants.MASTER_DEGREE_THESIS_DATA_VERSION);
            session.removeAttribute(SessionConstants.MASTER_DEGREE_PROOF_HISTORY);
            session.removeAttribute(SessionConstants.DEGREE_TYPE);
            session.removeAttribute(SessionConstants.DATE);

            IUserView userView = getUserView(request);

            String certificateString = (String) session.getAttribute(SessionConstants.CERTIFICATE_TYPE);

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

            int initialYear = infoStudentCurricularPlan.getInfoDegreeCurricularPlan().getInitialDate()
                    .getYear() + 1900;
            String initialExecutionYear = initialYear + "/" + ++initialYear;
            request.setAttribute("initialExecutionYear", initialExecutionYear);

            InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;
            InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = null;
            InfoExecutionYear infoExecutionYear = null;

            try {
                if (certificateString.equals("Fim de curso de Mestrado discriminada com média")
                        || certificateString.equals("Fim de curso de Mestrado simples")
                        || certificateString.equals("Carta de Curso")) {
                    Object argsMasterDegreeThesisDataVersion[] = { infoStudentCurricularPlan };
                    try {
                        infoMasterDegreeThesisDataVersion = (InfoMasterDegreeThesisDataVersion) ServiceUtils
                                .executeService(
                                        userView,
                                        "ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan",
                                        argsMasterDegreeThesisDataVersion);
                    } catch (NonExistingServiceException e) {
                        throw new NonExistingActionException("O registo da tese ", e);

                    }

                    /* * * get master degree proof * * */
                    Object argsMasterDegreeProofVersion[] = { studentCurricularPlanID };
                    try {
                        infoMasterDegreeProofVersion = (InfoMasterDegreeProofVersion) ServiceUtils
                                .executeService(userView,
                                        "ReadActiveMasterDegreeProofVersionByStudentCurricularPlan",
                                        argsMasterDegreeProofVersion);
                    } catch (NonExistingServiceException e) {
                        throw new NonExistingActionException("O registo da tese ", e);
                    } catch (ScholarshipNotFinishedServiceException e) {
                        throw new ScholarshipNotFinishedActionException("", e);
                    }

                }

                infoExecutionYear = (InfoExecutionYear) ServiceManagerServiceFactory.executeService(
                        userView, "ReadCurrentExecutionYear", null);

            } catch (RuntimeException e) {
                throw new RuntimeException("Error", e);
            }

            Locale locale = new Locale("pt", "PT");
            Date date = new Date();
            String formatedDate = "Lisboa, "
                    + DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);
            session.setAttribute(SessionConstants.DATE, formatedDate);

            session.setAttribute(SessionConstants.MASTER_DEGREE_THESIS_DATA_VERSION,
                    infoMasterDegreeThesisDataVersion);
            session.setAttribute(SessionConstants.MASTER_DEGREE_PROOF_HISTORY,
                    infoMasterDegreeProofVersion);
            session.setAttribute(SessionConstants.INFO_EXECUTION_YEAR, infoExecutionYear);
            session.setAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN,
                    infoStudentCurricularPlan);

            return mapping.findForward("ChooseSuccess");
        }

        throw new Exception();

    }

}