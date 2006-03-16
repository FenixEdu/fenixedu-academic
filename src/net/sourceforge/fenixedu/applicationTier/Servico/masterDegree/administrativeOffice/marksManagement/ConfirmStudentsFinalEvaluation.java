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
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

/**
 * @author Fernanda Quitério 10/07/2003
 * 
 */
public class ConfirmStudentsFinalEvaluation extends Service {

	public Boolean run(Integer curricularCourseCode, String yearString, IUserView userView)
			throws FenixServiceException, ExcepcaoPersistencia {
		IPersistentEnrollment persistentEnrolment = persistentSupport.getIPersistentEnrolment();
		IPessoaPersistente persistentPerson = persistentSupport.getIPessoaPersistente();
		IPersistentEmployee persistentEmployee = persistentSupport.getIPersistentEmployee();

		Person person = Person.readPersonByUsername(userView.getUtilizador());
		Employee employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());

		CurricularCourse curricularCourse = (CurricularCourse) persistentObject.readByOID(
				CurricularCourse.class, curricularCourseCode);

		List enrolments = null;
		if (yearString != null) {
			enrolments = persistentEnrolment.readByCurricularCourseAndYear(curricularCourseCode,
					yearString);
		} else {
			enrolments = curricularCourse.getCurriculumModules();
		}

		List enrolmentEvaluations = new ArrayList();
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