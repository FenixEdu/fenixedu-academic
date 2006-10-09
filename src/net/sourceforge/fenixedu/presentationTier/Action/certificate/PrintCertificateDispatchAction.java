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
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentInExtraCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoFinalResult;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeClassification;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.Data;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PrintCertificateDispatchAction extends FenixDispatchAction {
    
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
        final HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = getUserView(request);
            session.removeAttribute(SessionConstants.MATRICULA);
            session.removeAttribute(SessionConstants.MATRICULA_ENROLMENT);
            session.removeAttribute(SessionConstants.DURATION_DEGREE);
            session.removeAttribute(SessionConstants.ENROLMENT);
            session.removeAttribute(SessionConstants.ENROLMENT_LIST);
            session.removeAttribute(SessionConstants.APROVMENT);
            session.removeAttribute(SessionConstants.EXTRA_CURRICULAR_APROVMENT);
            session.removeAttribute(SessionConstants.EXTRA_ENROLMENT_LIST);
            session.removeAttribute(SessionConstants.INFO_FINAL_RESULT);
            session.removeAttribute(SessionConstants.FINAL_RESULT);
            session.removeAttribute(SessionConstants.CONCLUSION_DATE);
            session.removeAttribute(SessionConstants.FINAL_RESULT_SIMPLE);
            session.removeAttribute(SessionConstants.FINAL_RESULT_DEGREE_SIMPLE);
            session.removeAttribute(SessionConstants.DISCRIMINATED_WITHOUT_AVERAGE);
            session.removeAttribute(SessionConstants.DISCRIMINATED_WITH_AVERAGE);
            session.removeAttribute(SessionConstants.FINAL_DEGREE_DISCRIMINATED_WITH_AVERAGE);
            session.removeAttribute(SessionConstants.DIPLOMA);
            session.removeAttribute(SessionConstants.DEGREE_LETTER);
            session.removeAttribute(SessionConstants.DATE);
            session.removeAttribute("notString");
            InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) session
                    .getAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN);
            InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = (InfoMasterDegreeThesisDataVersion) session
                    .getAttribute(SessionConstants.MASTER_DEGREE_THESIS_DATA_VERSION);
            InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = (InfoMasterDegreeProofVersion) session
                    .getAttribute(SessionConstants.MASTER_DEGREE_PROOF_HISTORY);
            String certificate = (String) session.getAttribute(SessionConstants.CERTIFICATE_TYPE);
            String anoLectivo = new String();
            InfoExecutionYear infoExecutionYear = null;
            try {
                infoExecutionYear = (InfoExecutionYear) ServiceManagerServiceFactory.executeService(
                        userView, "ReadCurrentExecutionYear", null);
            } catch (RuntimeException e) {
                throw new RuntimeException("Error", e);
            }
            if ((certificate.equals("Matrícula")) || (certificate.equals("Matrícula e Inscrição"))
                    || (certificate.equals("Duração do Degree"))) {
                List enrolmentList = null;
                Object args[] = { infoStudentCurricularPlan.getIdInternal() };
                try {
                    enrolmentList = (List) ServiceManagerServiceFactory.executeService(userView,
                            "GetEnrolmentList", args);
                } catch (NonExistingServiceException e) {
                    throw new NonExistingActionException("Inscrição", e);
                }
                if (enrolmentList.size() == 0)
                    anoLectivo = infoExecutionYear.getYear();
                else
                    anoLectivo = ((InfoEnrolment) enrolmentList.get(0)).getInfoExecutionPeriod()
                            .getInfoExecutionYear().getYear();
                if (certificate.equals("Matrícula"))
                    session.setAttribute(SessionConstants.MATRICULA, certificate.toUpperCase());
                if (certificate.equals("Matrícula e Inscrição"))
                    session
                            .setAttribute(SessionConstants.MATRICULA_ENROLMENT, certificate
                                    .toUpperCase());
                if (certificate.equals("Duração do Degree")) {
                    if (infoStudentCurricularPlan.getSpecialization().equals(
                            Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE)) {
                        certificate = new String("Matrícula");
                        session
                                .setAttribute(SessionConstants.DURATION_DEGREE, certificate
                                        .toUpperCase());
                    } else {
                        ActionErrors errors = new ActionErrors();
                        errors.add("AlunoNãoExiste", new ActionError("error.invalidStudentType"));
                        saveErrors(request, errors);
                        return new ActionForward(mapping.getInput());
                    }
                }
            } else {
                // get informations
                List enrolmentList = null;
                if (certificate.equals("Inscrição")) {
                    // , EnrolmentState.ENROLED
                    Object args[] = { infoStudentCurricularPlan.getIdInternal() };
                    try {
                        enrolmentList = (List) ServiceManagerServiceFactory.executeService(userView,
                                "GetEnrolmentList", args);
                    } catch (NonExistingServiceException e) {
                        throw new NonExistingActionException("Inscrição", e);
                    }
                    if (enrolmentList.size() == 0) {
                        ActionErrors errors = new ActionErrors();
                        errors.add("AlunoNãoExiste", new ActionError("error.enrolment.notExist"));
                        saveErrors(request, errors);
                        return new ActionForward(mapping.getInput());
                    }
                    List normalEnrolment = new ArrayList();
                    List extraEnrolment = new ArrayList();
                    // InfoEnrolment infoEnrolment = new InfoEnrolment();
                    Object result = null;
                    Iterator iterator = enrolmentList.iterator();
                    while (iterator.hasNext()) {
                        result = iterator.next();
                        InfoEnrolment infoEnrolment2 = (InfoEnrolment) result;
                        
                        if (result instanceof InfoEnrolmentInExtraCurricularCourse) {
                            extraEnrolment.add(infoEnrolment2);
                            anoLectivo = ((InfoEnrolment) extraEnrolment.get(0))
                                    .getInfoExecutionPeriod().getInfoExecutionYear().getYear();
                        } else {
                            normalEnrolment.add(infoEnrolment2);
                            anoLectivo = ((InfoEnrolment) normalEnrolment.get(0))
                                    .getInfoExecutionPeriod().getInfoExecutionYear().getYear();
                        }
                    }
                    if (normalEnrolment.size() != 0)
                        session.setAttribute(SessionConstants.ENROLMENT_LIST, normalEnrolment);
                    if (extraEnrolment.size() != 0)
                        session.setAttribute(SessionConstants.EXTRA_ENROLMENT_LIST, extraEnrolment);
                    session.setAttribute(SessionConstants.ENROLMENT, certificate.toUpperCase());
                } else {
                    if ((certificate.equals("Aproveitamento"))
                            || (certificate.equals("Aproveitamento de Disciplinas Extra Curricular"))) {
                        Object args[] = { infoStudentCurricularPlan.getIdInternal(),
                                EnrollmentState.APROVED, new Boolean(true) };
                        try {
                            enrolmentList = (List) ServiceManagerServiceFactory.executeService(userView,
                                    "GetEnrolmentList", args);
                        } catch (NonExistingServiceException e) {
                            throw new NonExistingActionException("Inscrição", e);
                        }
                        if (enrolmentList.size() == 0) {
                            ActionErrors errors = new ActionErrors();
                            errors.add("AlunoNãoExiste", new ActionError("error.enrolment.notExist"));
                            saveErrors(request, errors);
                            return new ActionForward(mapping.getInput());
                        }
                        List normalEnrolment = new ArrayList();
                        List extraEnrolment = new ArrayList();
                        // InfoEnrolment infoEnrolment = new InfoEnrolment();
                        Object result = null;
                        Iterator iterator = enrolmentList.iterator();
                        while (iterator.hasNext()) {
                            result = iterator.next();
                            InfoEnrolment infoEnrolment2 = (InfoEnrolment) result;
                           
                            if (result instanceof InfoEnrolmentInExtraCurricularCourse) {
                                extraEnrolment.add(infoEnrolment2);
                                anoLectivo = ((InfoEnrolment) extraEnrolment.get(0))
                                        .getInfoExecutionPeriod().getInfoExecutionYear().getYear();
                            } else {
                                normalEnrolment.add(infoEnrolment2);
                                anoLectivo = ((InfoEnrolment) normalEnrolment.get(0))
                                        .getInfoExecutionPeriod().getInfoExecutionYear().getYear();
                            }
                        }

                        session.setAttribute(SessionConstants.ENROLMENT_LIST, normalEnrolment);
                        session.setAttribute(SessionConstants.EXTRA_ENROLMENT_LIST, extraEnrolment);
                        if (certificate.equals("Aproveitamento")) {
                            if (normalEnrolment.size() == 0) {
                                ActionErrors errors = new ActionErrors();
                                errors
                                        .add("AlunoNãoExiste", new ActionError(
                                                "error.enrolment.notExist"));
                                saveErrors(request, errors);
                                return new ActionForward(mapping.getInput());
                            }
                            session.setAttribute(SessionConstants.APROVMENT, certificate.toUpperCase());
                        } else if (certificate.equals("Aproveitamento de Disciplinas Extra Curricular")) {
                            if (extraEnrolment.size() == 0) {
                                ActionErrors errors = new ActionErrors();
                                errors
                                        .add("AlunoNãoExiste", new ActionError(
                                                "error.enrolment.notExist"));
                                saveErrors(request, errors);
                                return new ActionForward(mapping.getInput());
                            }
                            session.setAttribute(SessionConstants.EXTRA_CURRICULAR_APROVMENT,
                                    certificate.toUpperCase());
                        }
                    } else {
                        if (infoStudentCurricularPlan.getSpecialization().equals(
                                Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE)) {
                            if ((certificate.equals("Fim parte escolar simples"))
                                    || (certificate.equals("Fim parte escolar discriminada sem média"))
                                    || (certificate.equals("Fim parte escolar discriminada com média"))
                                    || (certificate.equals("Diploma"))
                                    || (certificate
                                            .equals("Fim de curso de Mestrado discriminada com média"))
                                    || (certificate.equals("Carta de Curso"))) {
                                InfoFinalResult infoFinalResult = null;
                                try {
                                    Object args[] = { infoStudentCurricularPlan };
                                    infoFinalResult = (InfoFinalResult) ServiceManagerServiceFactory
                                            .executeService(userView, "FinalResult", args);
                                } catch (FenixServiceException e) {
                                    throw new FenixServiceException("");
                                }
                                if (infoFinalResult == null) {
                                    ActionErrors actionErrors = new ActionErrors();
                                    actionErrors.add("FinalResultError", new ActionError(
                                            "error.exception.unreached"));
                                    saveErrors(request, actionErrors);
                                    return mapping.findForward("insucesso");
                                    // throw new
                                    // FinalResulUnreachedActionException("aqui");
                                }
                                Object args[] = { infoStudentCurricularPlan.getIdInternal(),
                                        EnrollmentState.APROVED };
                                try {
                                    enrolmentList = (List) ServiceManagerServiceFactory.executeService(
                                            userView, "GetEnrolmentList", args);
                                } catch (NonExistingServiceException e) {
                                    throw new NonExistingActionException("Inscrição", e);
                                }
                                if (enrolmentList.size() == 0) {
                                    ActionErrors errors = new ActionErrors();
                                    errors.add("AlunoNãoExiste", new ActionError(
                                            "error.enrolment.notExist"));
                                    saveErrors(request, errors);
                                    return new ActionForward(mapping.getInput());
                                }

                                String conclusionDate = null;
                                Date endOfScholarshipDate = null;
                                try {
                                    Object argsTemp[] = { infoStudentCurricularPlan.getIdInternal() };
                                    endOfScholarshipDate = (Date) ServiceManagerServiceFactory
                                            .executeService(userView, "GetEndOfScholarshipDate",
                                                    argsTemp);
                                } catch (FenixServiceException e) {
                                    throw new FenixActionException(e);
                                }
                                conclusionDate = Data.format2DayMonthYear(endOfScholarshipDate, "-");

                                List normalEnrolment = new ArrayList();
                                List extraEnrolment = new ArrayList();
                                Object result = null;
                                Iterator iterator = enrolmentList.iterator();
                                while (iterator.hasNext()) {
                                    result = iterator.next();
                                    InfoEnrolment infoEnrolment2 = (InfoEnrolment) result;
                                    if (result instanceof InfoEnrolmentInExtraCurricularCourse) {
                                        extraEnrolment.add(infoEnrolment2);
                                        anoLectivo = ((InfoEnrolment) extraEnrolment.get(0))
                                                .getInfoExecutionPeriod().getInfoExecutionYear()
                                                .getYear();
                                    } else {
                                        normalEnrolment.add(infoEnrolment2);
                                        anoLectivo = ((InfoEnrolment) normalEnrolment.get(0))
                                                .getInfoExecutionPeriod().getInfoExecutionYear()
                                                .getYear();
                                    }
                                }

                                Object argsAux[] = { infoStudentCurricularPlan.getIdInternal() };
                                Date date = null;
                                try {
                                    date = (Date) ServiceManagerServiceFactory.executeService(userView,
                                            "GetEndOfScholarshipDate", argsAux);
                                } catch (NonExistingServiceException e) {
                                    throw new NonExistingActionException("Inscrição", e);
                                }
                                conclusionDate = DateFormat.getDateInstance().format(date);
                                session.setAttribute(SessionConstants.ENROLMENT_LIST, normalEnrolment);
                                session.setAttribute(SessionConstants.EXTRA_ENROLMENT_LIST,
                                        extraEnrolment);
                                session.setAttribute(SessionConstants.CONCLUSION_DATE, conclusionDate);
                                session
                                        .setAttribute(SessionConstants.INFO_FINAL_RESULT,
                                                infoFinalResult);
                                session.setAttribute(SessionConstants.MASTER_DEGREE_THESIS_DATA_VERSION,
                                        infoMasterDegreeThesisDataVersion);
                                //  							
                                if (certificate.equals("Fim parte escolar simples"))
                                    session.setAttribute(SessionConstants.FINAL_RESULT_SIMPLE,
                                            certificate.toUpperCase());
                                if (certificate.equals("Fim parte escolar discriminada sem média"))
                                    session.setAttribute(SessionConstants.DISCRIMINATED_WITHOUT_AVERAGE,
                                            certificate.toUpperCase());
                                if (certificate.equals("Fim parte escolar discriminada com média"))
                                    session.setAttribute(SessionConstants.DISCRIMINATED_WITH_AVERAGE,
                                            certificate.toUpperCase());
                                if (certificate.equals("Diploma"))
                                    session.setAttribute(SessionConstants.DIPLOMA, certificate
                                            .toUpperCase());
                                if (certificate
                                        .equals("Fim de curso de Mestrado discriminada com média")
                                        || certificate.equals("Carta de Curso"))

                                {
                                    if (infoMasterDegreeProofVersion != null) {
                                        if (!infoMasterDegreeProofVersion.getFinalResult().equals(
                                                MasterDegreeClassification.UNDEFINED)) {
                                            String notString = " ";
                                            if (infoMasterDegreeProofVersion.getFinalResult().equals(
                                                    MasterDegreeClassification.NOT_APPROVED)) {
                                                notString = " não ";
                                            }
                                            session.setAttribute("notString", notString);

                                            Locale locale = new Locale("pt", "PT");
                                            String dataFinal = DateFormat.getDateInstance(
                                                    DateFormat.LONG, locale).format(
                                                    infoMasterDegreeProofVersion.getProofDate());
                                            session.setAttribute(SessionConstants.CONCLUSION_DATE,
                                                    dataFinal);
                                            session
                                                    .setAttribute(SessionConstants.FINAL_RESULT,
                                                            infoMasterDegreeProofVersion
                                                                    .getFinalResult().name());
                                        } else {
                                            ActionErrors errors = new ActionErrors();
                                            errors.add("AlunoNãoExiste", new ActionError(
                                                    "error.invalidStudentType"));
                                            saveErrors(request, errors);
                                            return new ActionForward(mapping.getInput());
                                        }
                                    }
                                }
                                if (certificate
                                        .equals("Fim de curso de Mestrado discriminada com média")) {
                                    session.setAttribute(
                                            SessionConstants.FINAL_DEGREE_DISCRIMINATED_WITH_AVERAGE,
                                            certificate.toUpperCase());
                                }
                                if (certificate.equals("Carta de Curso")) {
                                    session.setAttribute(SessionConstants.DEGREE_LETTER, certificate
                                            .toUpperCase());
                                    return mapping.findForward("printDegreeLetter");
                                }

                            } else {
                                if ((certificate.equals("Fim de curso de Mestrado simples"))) {
                                    if (!infoMasterDegreeProofVersion.getFinalResult().equals(
                                            MasterDegreeClassification.UNDEFINED)) {
                                        session.setAttribute(
                                                SessionConstants.FINAL_RESULT_DEGREE_SIMPLE, certificate
                                                        .toUpperCase());
                                        session.setAttribute(
                                                SessionConstants.MASTER_DEGREE_THESIS_HISTORY,
                                                infoMasterDegreeProofVersion);
                                        String dataFinal = DateFormat.getDateInstance().format(
                                                infoMasterDegreeProofVersion.getProofDate());
                                        session
                                                .setAttribute(SessionConstants.CONCLUSION_DATE,
                                                        dataFinal);

                                        String notString = " ";
                                        if (infoMasterDegreeProofVersion.getFinalResult().equals(
                                                MasterDegreeClassification.NOT_APPROVED)) {
                                            notString = " não ";
                                        }

                                        session.setAttribute("notString", notString);
                                        session.setAttribute(SessionConstants.FINAL_RESULT,
                                                infoMasterDegreeProofVersion.getFinalResult().name());
                                        session.setAttribute(
                                                SessionConstants.MASTER_DEGREE_THESIS_DATA_VERSION,
                                                infoMasterDegreeThesisDataVersion);
                                    } else {
                                        ActionErrors errors = new ActionErrors();
                                        errors.add("AlunoNãoExiste", new ActionError(
                                                "error.invalidStudentConclusion"));
                                        saveErrors(request, errors);
                                        return new ActionForward(mapping.getInput());
                                    }
                                }
                            }
                        } else {
                            ActionErrors errors = new ActionErrors();
                            errors.add("AlunoNãoExiste", new ActionError("error.invalidStudentType"));
                            saveErrors(request, errors);
                            return new ActionForward(mapping.getInput());
                        }
                    }
                }
            }
            session.setAttribute(SessionConstants.INFO_STUDENT_CURRICULAR_PLAN,
                    infoStudentCurricularPlan);
            Locale locale = new Locale("pt", "PT");
            Date date = new Date();
            String formatedDate = "Lisboa, "
                    + DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);
            request.setAttribute("anoLectivo", anoLectivo);
            session.setAttribute(SessionConstants.DATE, formatedDate);
            session.setAttribute(SessionConstants.CERTIFICATE_TYPE, certificate);
            return mapping.findForward("PrintReady");
        }

        throw new Exception();
    }
}