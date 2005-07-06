/*
 * Created on 2:40:27 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.externalServices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.GetEnrolmentGrade;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalAdressInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalCitizenshipInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalCurricularCourseInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalDegreeBranchInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalDegreeCurricularPlanInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalEnrollmentInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalIdentificationInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalPersonInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoStudentExternalInformation;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz </a>
 * 
 * Created at 2:40:27 PM, Mar 11, 2005
 */
public class ReadStudentExternalInformation implements IService {

	public Collection run(String username) throws ExcepcaoPersistencia,
			FenixServiceException {
		Collection result = new ArrayList();
		IPessoaPersistente persistentPerson = PersistenceSupportFactory
				.getDefaultPersistenceSupport().getIPessoaPersistente();
		IPerson person = persistentPerson.lerPessoaPorUsername(username);
		Collection students = person.getStudents();
		for (Iterator iter = students.iterator(); iter.hasNext();) {
			InfoStudentExternalInformation info = new InfoStudentExternalInformation();
			IStudent student = (IStudent) iter.next();

			info.setPerson(this.buildExternalPersonInfo(person));
			info.setDegree(this.buildExternalDegreeCurricularPlanInfo(student));
			info.setCourses(this.buildExternalEnrollmentsInfo(student));
			info.setAvailableRemainingCourses(this
					.buildAvailableRemainingCourses(student));
			info.setNumber(student.getNumber().toString());

			result.add(info);
		}

		return result;
	}

	private Collection buildAvailableRemainingCourses(final IStudent student) {

		Collection allCourses = student.getActiveStudentCurricularPlan().getDegreeCurricularPlan().getCurricularCourses();
		Collection available = CollectionUtils.select(allCourses,
				new Predicate() {
					public boolean evaluate(Object arg0) {
						boolean availableToEnroll = true;
						boolean atLeastOnOpenScope = false;
						ICurricularCourse course = (ICurricularCourse) arg0;
						for (ICurricularCourseScope scope : (List<ICurricularCourseScope>) course
								.getScopes()) {
							atLeastOnOpenScope |= scope.isActive()
									.booleanValue();
						}

						availableToEnroll &= atLeastOnOpenScope;
						availableToEnroll &= !student
								.getActiveStudentCurricularPlan()
								.isCurricularCourseApproved(course);
						return availableToEnroll;
					};
				}

		);

		Collection availableInfos = CollectionUtils.collect(available,
				new Transformer() {
					public Object transform(Object arg0) {
						ICurricularCourse course = (ICurricularCourse) arg0;
						InfoExternalCurricularCourseInfo info = InfoExternalCurricularCourseInfo
								.newFromDomain(course);
						return info;

					};
				}

		);
		return availableInfos;
	}

	/**
	 * @param student
	 * @return
	 * @throws FenixServiceException
	 */
	private Collection buildExternalEnrollmentsInfo(IStudent student)
			throws FenixServiceException {
		Collection enrollments = new ArrayList();
		Collection curricularPlans = student.getStudentCurricularPlans();
		for (Iterator iter = curricularPlans.iterator(); iter.hasNext();) {
			IStudentCurricularPlan curricularPlan = (IStudentCurricularPlan) iter.next();
			for (Iterator iterEnrolments = curricularPlan.getEnrolments().iterator(); iterEnrolments.hasNext();) {
				IEnrolment enrollment = (IEnrolment) iterEnrolments.next();
				if (enrollment.getEnrollmentState().equals(
						EnrollmentState.APROVED)) {
					InfoExternalEnrollmentInfo info = InfoExternalEnrollmentInfo
							.newFromEnrollment(enrollment);

					GetEnrolmentGrade getEnrollmentGrade = new GetEnrolmentGrade();
					InfoEnrolmentEvaluation infoEnrollmentEvaluation = getEnrollmentGrade
							.run(enrollment);
					info.setFinalGrade(infoEnrollmentEvaluation.getGrade());
					enrollments.add(info);
				}
			}
		}

		return enrollments;
	}

	/**
	 * @param student
	 * @return
	 */
	private InfoExternalDegreeCurricularPlanInfo buildExternalDegreeCurricularPlanInfo(
			IStudent student) {
		InfoExternalDegreeCurricularPlanInfo info = new InfoExternalDegreeCurricularPlanInfo();
		IDegreeCurricularPlan degreeCurricularPlan = student
				.getActiveStudentCurricularPlan().getDegreeCurricularPlan();

		info.setName(degreeCurricularPlan.getName());
		info.setCode(degreeCurricularPlan.getDegree().getIdInternal()
				.toString());
		info.setBranch(this.buildExternalDegreeBranchInfo(student));

		Collection courses = student.getActiveStudentCurricularPlan()
				.getDegreeCurricularPlan().getCurricularCourses();
		for (Iterator iter = courses.iterator(); iter.hasNext();) {
			ICurricularCourse curricularCourse = (ICurricularCourse) iter
					.next();
			info.addCourse(InfoExternalCurricularCourseInfo
					.newFromDomain(curricularCourse));
		}

		return info;
	}

	/**
	 * @param student
	 * @return
	 */
	private InfoExternalDegreeBranchInfo buildExternalDegreeBranchInfo(
			IStudent student) {
		InfoExternalDegreeBranchInfo info = new InfoExternalDegreeBranchInfo();
		if (student.getActiveStudentCurricularPlan().getBranch() != null) {
			info.setName(student.getActiveStudentCurricularPlan().getBranch()
					.getName());
			info.setCode(student.getActiveStudentCurricularPlan().getBranch()
					.getCode());
		}

		return info;
	}

	/**
	 * @param infoPerson
	 * @return
	 */
	private InfoExternalPersonInfo buildExternalPersonInfo(IPerson person) {
		InfoExternalPersonInfo info = new InfoExternalPersonInfo();
		info.setAddress(this.buildInfoExternalAdressInfo(person));
		info.setBirthday(person.getNascimento().toString());
		info.setCelularPhone(person.getTelemovel());
		info.setCitizenship(this.builsExternalCitizenshipInfo(person));
		info.setEmail(person.getEmail());
		info.setFiscalNumber(person.getNumContribuinte());
		info.setIdentification(this.buildExternalIdentificationInfo(person));
		info.setName(person.getNome());
		info.setNationality(person.getNacionalidade());
		info.setPhone(person.getTelefone());
		info.setSex(person.getGender().toString());

		return info;
	}

	/**
	 * @param infoPerson
	 * @return
	 */
	private InfoExternalIdentificationInfo buildExternalIdentificationInfo(
			IPerson person) {
		InfoExternalIdentificationInfo info = new InfoExternalIdentificationInfo();
		info.setDocumentType(person.getIdDocumentType().toString());
		info.setNumber(person.getNumeroDocumentoIdentificacao());
		if (person.getDataEmissaoDocumentoIdentificacao() != null) {
			info.setEmitionDate(person.getDataEmissaoDocumentoIdentificacao()
					.toString());
		}
		if (person.getDataValidadeDocumentoIdentificacao() != null) {
			info.setExpiryDate(person.getDataValidadeDocumentoIdentificacao()
					.toString());
		}
		if (person.getLocalEmissaoDocumentoIdentificacao() != null) {
			info
					.setEmitionLocal(person
							.getLocalEmissaoDocumentoIdentificacao());
		}

		return info;
	}

	/**
	 * @param infoPerson
	 * @return
	 */
	private InfoExternalCitizenshipInfo builsExternalCitizenshipInfo(
			IPerson person) {
		InfoExternalCitizenshipInfo info = new InfoExternalCitizenshipInfo();
		info.setArea(person.getFreguesiaNaturalidade());
		info.setCounty(person.getConcelhoNaturalidade());

		return info;
	}

	/**
	 * @param infoPerson
	 * @return
	 */
	private InfoExternalAdressInfo buildInfoExternalAdressInfo(IPerson person) {
		InfoExternalAdressInfo info = new InfoExternalAdressInfo();
		info.setPostalCode(person.getCodigoPostal());
		info.setStreet(person.getMorada());
		info.setTown(person.getLocalidade());

		return info;
	}
}