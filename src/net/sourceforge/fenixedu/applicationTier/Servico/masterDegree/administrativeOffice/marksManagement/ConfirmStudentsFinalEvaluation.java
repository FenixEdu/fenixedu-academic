package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

/**
 * @author Fernanda Quitério 10/07/2003
 * 
 */
public class ConfirmStudentsFinalEvaluation extends Service {

	public Boolean run(Integer curricularCourseCode, String yearString, IUserView userView)
			throws FenixServiceException, ExcepcaoPersistencia {

		Employee employee = userView.getPerson().getEmployee();

		CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseCode);

		List enrolments = null;
		if (yearString != null) {
			enrolments = curricularCourse.getEnrolmentsByYear(yearString);
		} else {
			enrolments = curricularCourse.getCurriculumModules();
		}

		List<EnrolmentEvaluation> enrolmentEvaluations = new ArrayList<EnrolmentEvaluation>();
		Iterator iterEnrolment = enrolments.listIterator();
		while (iterEnrolment.hasNext()) {
			Enrolment enrolment = (Enrolment) iterEnrolment.next();

			List allEnrolmentEvaluations = enrolment.getEvaluations();
			EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) allEnrolmentEvaluations
					.get(allEnrolmentEvaluations.size() - 1);
			enrolmentEvaluations.add(enrolmentEvaluation);
		}

		if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {

			ListIterator iterEnrolmentEvaluations = enrolmentEvaluations.listIterator();
			while (iterEnrolmentEvaluations.hasNext()) {

				EnrolmentEvaluation enrolmentEvaluationElem = (EnrolmentEvaluation) iterEnrolmentEvaluations
						.next();

				if (enrolmentEvaluationElem.getGrade() != null
						&& enrolmentEvaluationElem.getGrade().length() > 0
						&& enrolmentEvaluationElem.getEnrolmentEvaluationState().equals(
								EnrolmentEvaluationState.TEMPORARY_OBJ)) {

					enrolmentEvaluationElem.confirmSubmission(employee,
							"Lançamento de Notas na Secretaria");
				}
			}
		}

		return Boolean.TRUE;
	}

}