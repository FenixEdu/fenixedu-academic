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
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurrentExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.certificate.GetEndOfScholarshipDate;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.enrolment.FinalResult;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.enrolment.GetEnrolmentList;
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
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.util.CertificateList;
import net.sourceforge.fenixedu.util.Data;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PrintCertificateDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	final HttpSession session = request.getSession(false);
	if (session != null) {
	    session.removeAttribute(PresentationConstants.MATRICULA);
	    session.removeAttribute(PresentationConstants.MATRICULA_ENROLMENT);
	    session.removeAttribute(PresentationConstants.DURATION_DEGREE);
	    session.removeAttribute(PresentationConstants.ENROLMENT);
	    session.removeAttribute(PresentationConstants.ENROLMENT_LIST);
	    session.removeAttribute(PresentationConstants.APROVMENT);
	    session.removeAttribute(PresentationConstants.EXTRA_CURRICULAR_APROVMENT);
	    session.removeAttribute(PresentationConstants.EXTRA_ENROLMENT_LIST);
	    session.removeAttribute(PresentationConstants.INFO_FINAL_RESULT);
	    session.removeAttribute(PresentationConstants.FINAL_RESULT);
	    session.removeAttribute(PresentationConstants.CONCLUSION_DATE);
	    session.removeAttribute(PresentationConstants.FINAL_RESULT_SIMPLE);
	    session.removeAttribute(PresentationConstants.FINAL_RESULT_DEGREE_SIMPLE);
	    session.removeAttribute(PresentationConstants.DISCRIMINATED_WITHOUT_AVERAGE);
	    session.removeAttribute(PresentationConstants.DISCRIMINATED_WITH_AVERAGE);
	    session.removeAttribute(PresentationConstants.FINAL_DEGREE_DISCRIMINATED_WITH_AVERAGE);
	    session.removeAttribute(PresentationConstants.DIPLOMA);
	    session.removeAttribute(PresentationConstants.DEGREE_LETTER);
	    session.removeAttribute(PresentationConstants.DATE);
	    session.removeAttribute("notString");
	    InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) request
		    .getAttribute(PresentationConstants.INFO_STUDENT_CURRICULAR_PLAN);
	    InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = (InfoMasterDegreeThesisDataVersion) request
		    .getAttribute(PresentationConstants.MASTER_DEGREE_THESIS_DATA_VERSION);
	    InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = (InfoMasterDegreeProofVersion) request
		    .getAttribute(PresentationConstants.MASTER_DEGREE_PROOF_HISTORY);
	    String certificate = (String) request.getAttribute(PresentationConstants.CERTIFICATE_TYPE);

	    String anoLectivo = new String();
	    InfoExecutionYear infoExecutionYear = null;
	    try {
		infoExecutionYear = ReadCurrentExecutionYear.run();
	    } catch (RuntimeException e) {

		throw new RuntimeException("Error", e);
	    }
	    if ((certificate.equals("Matrícula")) || (certificate.equals("Matrícula e Inscrição"))
		    || (certificate.equals(CertificateList.DURACAO_CURSO_STRING))) {

		List enrolmentList = GetEnrolmentList.run(infoStudentCurricularPlan.getIdInternal());
		if (enrolmentList.size() == 0)
		    anoLectivo = infoExecutionYear.getYear();
		else
		    anoLectivo = ((InfoEnrolment) enrolmentList.get(0)).getInfoExecutionPeriod().getInfoExecutionYear().getYear();
		if (certificate.equals("Matrícula"))
		    session.setAttribute(PresentationConstants.MATRICULA, certificate.toUpperCase());
		if (certificate.equals("Matrícula e Inscrição"))
		    session.setAttribute(PresentationConstants.MATRICULA_ENROLMENT, certificate.toUpperCase());
		if (certificate.equals(CertificateList.DURACAO_CURSO_STRING)) {

		    if (infoStudentCurricularPlan.getSpecialization()
			    .equals(Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE)) {
			certificate = "Matrícula";
			session.setAttribute(PresentationConstants.DURATION_DEGREE, certificate.toUpperCase());
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

		    enrolmentList = GetEnrolmentList.run(infoStudentCurricularPlan.getIdInternal());
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
			    anoLectivo = ((InfoEnrolment) extraEnrolment.get(0)).getInfoExecutionPeriod().getInfoExecutionYear()
				    .getYear();
			} else {
			    normalEnrolment.add(infoEnrolment2);
			    anoLectivo = ((InfoEnrolment) normalEnrolment.get(0)).getInfoExecutionPeriod().getInfoExecutionYear()
				    .getYear();
			}
		    }
		    if (normalEnrolment.size() != 0)
			session.setAttribute(PresentationConstants.ENROLMENT_LIST, normalEnrolment);
		    if (extraEnrolment.size() != 0)
			session.setAttribute(PresentationConstants.EXTRA_ENROLMENT_LIST, extraEnrolment);
		    session.setAttribute(PresentationConstants.ENROLMENT, certificate.toUpperCase());
		} else {
		    if ((certificate.equals("Aproveitamento"))
			    || (certificate.equals("Aproveitamento de Disciplinas Extra Curricular"))) {

			enrolmentList = GetEnrolmentList.run(infoStudentCurricularPlan.getIdInternal(), EnrollmentState.APROVED,
				Boolean.TRUE, Boolean.TRUE);
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

			    if (infoEnrolment2.getEnrolment().isExtraCurricular()) {
				extraEnrolment.add(infoEnrolment2);
				anoLectivo = ((InfoEnrolment) extraEnrolment.get(0)).getInfoExecutionPeriod()
					.getInfoExecutionYear().getYear();
			    } else {
				normalEnrolment.add(infoEnrolment2);
				anoLectivo = ((InfoEnrolment) normalEnrolment.get(0)).getInfoExecutionPeriod()
					.getInfoExecutionYear().getYear();
			    }
			}

			session.setAttribute(PresentationConstants.ENROLMENT_LIST, normalEnrolment);
			session.setAttribute(PresentationConstants.EXTRA_ENROLMENT_LIST, extraEnrolment);
			if (certificate.equals("Aproveitamento")) {
			    if (normalEnrolment.size() == 0) {
				ActionErrors errors = new ActionErrors();
				errors.add("AlunoNãoExiste", new ActionError("error.enrolment.notExist"));
				saveErrors(request, errors);
				return new ActionForward(mapping.getInput());
			    }
			    session.setAttribute(PresentationConstants.APROVMENT, certificate.toUpperCase());
			} else if (certificate.equals("Aproveitamento de Disciplinas Extra Curricular")) {
			    if (extraEnrolment.size() == 0) {
				ActionErrors errors = new ActionErrors();
				errors.add("AlunoNãoExiste", new ActionError("error.enrolment.notExist"));
				saveErrors(request, errors);
				return new ActionForward(mapping.getInput());
			    }
			    session.setAttribute(PresentationConstants.EXTRA_CURRICULAR_APROVMENT, certificate.toUpperCase());
			}
		    } else {
			if (infoStudentCurricularPlan.getSpecialization().equals(
				Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE)) {
			    if ((certificate.equals("Fim parte escolar simples"))
				    || (certificate.equals("Fim parte escolar discriminada sem média"))
				    || (certificate.equals("Fim parte escolar discriminada com média"))
				    || (certificate.equals("Diploma"))
				    || (certificate.equals("Fim de curso de Mestrado discriminada com média"))
				    || (certificate.equals("Carta de Curso"))) {
				InfoFinalResult infoFinalResult = null;
				try {

				    infoFinalResult = FinalResult.run(infoStudentCurricularPlan);
				} catch (FenixServiceException e) {
				    throw new FenixServiceException("");
				}
				if (infoFinalResult == null) {
				    ActionErrors actionErrors = new ActionErrors();
				    actionErrors.add("FinalResultError", new ActionError("error.exception.unreached"));
				    saveErrors(request, actionErrors);
				    return mapping.findForward("insucesso");
				    // throw new
				    //FinalResulUnreachedActionException("aqui")
				    // ;
				}

				enrolmentList = GetEnrolmentList.run(infoStudentCurricularPlan.getIdInternal(),
					EnrollmentState.APROVED);
				// if (enrolmentList.size() == 0) {
				// ActionErrors errors = new ActionErrors();
				// errors.add("AlunoNãoExiste", new ActionError(
				// "error.enrolment.notExist"));
				// saveErrors(request, errors);
				// return new ActionForward(mapping.getInput());
				// }

				String conclusionDate = null;
				Date endOfScholarshipDate = null;
				endOfScholarshipDate = GetEndOfScholarshipDate.run(infoStudentCurricularPlan.getIdInternal());
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
					anoLectivo = ((InfoEnrolment) extraEnrolment.get(0)).getInfoExecutionPeriod()
						.getInfoExecutionYear().getYear();
				    } else {
					normalEnrolment.add(infoEnrolment2);
					anoLectivo = ((InfoEnrolment) normalEnrolment.get(0)).getInfoExecutionPeriod()
						.getInfoExecutionYear().getYear();
				    }
				}

				Date date = GetEndOfScholarshipDate.run(infoStudentCurricularPlan.getIdInternal());
				conclusionDate = DateFormat.getDateInstance().format(date);
				session.setAttribute(PresentationConstants.ENROLMENT_LIST, normalEnrolment);
				session.setAttribute(PresentationConstants.EXTRA_ENROLMENT_LIST, extraEnrolment);
				session.setAttribute(PresentationConstants.CONCLUSION_DATE, conclusionDate);
				session.setAttribute(PresentationConstants.INFO_FINAL_RESULT, infoFinalResult);
				session.setAttribute(PresentationConstants.MASTER_DEGREE_THESIS_DATA_VERSION,
					infoMasterDegreeThesisDataVersion);
				//  							
				if (certificate.equals("Fim parte escolar simples"))
				    session.setAttribute(PresentationConstants.FINAL_RESULT_SIMPLE, certificate.toUpperCase());
				if (certificate.equals("Fim parte escolar discriminada sem média"))
				    session.setAttribute(PresentationConstants.DISCRIMINATED_WITHOUT_AVERAGE, certificate
					    .toUpperCase());
				if (certificate.equals("Fim parte escolar discriminada com média"))
				    session.setAttribute(PresentationConstants.DISCRIMINATED_WITH_AVERAGE, certificate.toUpperCase());
				if (certificate.equals("Diploma"))
				    session.setAttribute(PresentationConstants.DIPLOMA, certificate.toUpperCase());
				if (certificate.equals("Fim de curso de Mestrado discriminada com média")
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
					    String dataFinal = DateFormat.getDateInstance(DateFormat.LONG, locale).format(
						    infoMasterDegreeProofVersion.getProofDate());
					    session.setAttribute(PresentationConstants.CONCLUSION_DATE, dataFinal);
					    session.setAttribute(PresentationConstants.FINAL_RESULT, infoMasterDegreeProofVersion
						    .getFinalResult().name());
					} else {
					    ActionErrors errors = new ActionErrors();
					    errors.add("AlunoNãoExiste", new ActionError("error.invalidStudentType"));
					    saveErrors(request, errors);
					    return new ActionForward(mapping.getInput());
					}
				    }
				}
				if (certificate.equals("Fim de curso de Mestrado discriminada com média")) {
				    session.setAttribute(PresentationConstants.FINAL_DEGREE_DISCRIMINATED_WITH_AVERAGE, certificate
					    .toUpperCase());
				}
				if (certificate.equals("Carta de Curso")) {
				    session.setAttribute(PresentationConstants.DEGREE_LETTER, certificate.toUpperCase());
				    return mapping.findForward("printDegreeLetter");
				}

			    } else {
				if ((certificate.equals("Fim de curso de Mestrado simples"))) {
				    if (!infoMasterDegreeProofVersion.getFinalResult().equals(
					    MasterDegreeClassification.UNDEFINED)) {
					session.setAttribute(PresentationConstants.FINAL_RESULT_DEGREE_SIMPLE, certificate
						.toUpperCase());
					session.setAttribute(PresentationConstants.MASTER_DEGREE_THESIS_HISTORY,
						infoMasterDegreeProofVersion);
					String dataFinal = DateFormat.getDateInstance().format(
						infoMasterDegreeProofVersion.getProofDate());
					session.setAttribute(PresentationConstants.CONCLUSION_DATE, dataFinal);

					String notString = " ";
					if (infoMasterDegreeProofVersion.getFinalResult().equals(
						MasterDegreeClassification.NOT_APPROVED)) {
					    notString = " não ";
					}

					session.setAttribute("notString", notString);
					session.setAttribute(PresentationConstants.FINAL_RESULT, infoMasterDegreeProofVersion
						.getFinalResult().name());
					session.setAttribute(PresentationConstants.MASTER_DEGREE_THESIS_DATA_VERSION,
						infoMasterDegreeThesisDataVersion);
				    } else {
					ActionErrors errors = new ActionErrors();
					errors.add("AlunoNãoExiste", new ActionError("error.invalidStudentConclusion"));
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
	    session.setAttribute(PresentationConstants.INFO_STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);
	    Locale locale = new Locale("pt", "PT");
	    Date date = new Date();
	    String formatedDate = "Lisboa, " + DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);
	    request.setAttribute("anoLectivo", anoLectivo);
	    session.setAttribute(PresentationConstants.DATE, formatedDate);
	    session.setAttribute(PresentationConstants.CERTIFICATE_TYPE, certificate);
	    return mapping.findForward("PrintReady");
	}

	throw new Exception();
    }
}