package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluationWithResponsibleForGrade;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPerson;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Angela 04/07/2003
 * 
 */
public class ReadStudentEnrolmentEvaluation extends Service {

	public InfoSiteEnrolmentEvaluation run(Integer studentEvaluationCode) throws FenixServiceException, ExcepcaoPersistencia {

		EnrolmentEvaluation enrolmentEvaluation = null;
		InfoEnrolmentEvaluation infoEnrolmentEvaluation = new InfoEnrolmentEvaluation();
		InfoEnrolment infoEnrolment = new InfoEnrolment();
		InfoTeacher infoTeacher = new InfoTeacher();
		List infoEnrolmentEvaluations = new ArrayList();

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp
				.getIPersistentEnrolmentEvaluation();
		IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
		enrolmentEvaluation = (EnrolmentEvaluation) persistentEnrolmentEvaluation.readByOID(
				EnrolmentEvaluation.class, studentEvaluationCode);

		infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
				.newInfoFromDomain(enrolmentEvaluation.getEnrolment());

		Person person = enrolmentEvaluation.getPersonResponsibleForGrade();
		Teacher teacher = persistentTeacher.readTeacherByUsername(person.getUsername());
		infoTeacher = InfoTeacherWithPerson.newInfoFromDomain(teacher);

		infoEnrolmentEvaluation = InfoEnrolmentEvaluationWithResponsibleForGrade
				.newInfoFromDomain(enrolmentEvaluation);
		infoEnrolmentEvaluation.setInfoPersonResponsibleForGrade(infoTeacher.getInfoPerson());
		if (enrolmentEvaluation.getEmployee() != null)
			infoEnrolmentEvaluation.setInfoEmployee(InfoPerson.newInfoFromDomain(enrolmentEvaluation
					.getEmployee().getPerson()));
		infoEnrolmentEvaluation.setInfoEnrolment(infoEnrolment);
		infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);

		// enrolmenEvaluation.setEnrolment

		InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
		infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
		infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);

		return infoSiteEnrolmentEvaluation;

	}

}