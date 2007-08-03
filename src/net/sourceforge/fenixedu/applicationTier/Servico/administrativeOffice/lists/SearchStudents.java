/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.LoginAlias;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

/**
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class SearchStudents extends Service {

    public static class SearchParameters {

	private List<RegistrationAgreement> registrationAgreement;

	private List<RegistrationStateType> registrationStateType;

	private Boolean equivalence;

	private DegreeCurricularPlan degreeCurricularPlan;

	private ExecutionYear executionYear;

	private Degree degree;

	public SearchParameters(
		List<RegistrationAgreement> registrationAgreement,
		List<RegistrationStateType> registrationStateType,
		Boolean equivalence, DegreeCurricularPlan degreeCurricularPlan,
		ExecutionYear executionYear) {

	    this.registrationAgreement = registrationAgreement;
	    this.registrationStateType = registrationStateType;
	    this.equivalence = equivalence;
	 //   this.degree = degree;
	    if (executionYear != null) {
		this.executionYear = executionYear;
	    }
	    if (degreeCurricularPlan != null) {
		this.degreeCurricularPlan = degreeCurricularPlan;
	    }
	}

	private boolean emptyParameters() {
	    return this.registrationAgreement.isEmpty()
		    && this.registrationStateType.isEmpty()
		    && !this.equivalence && this.executionYear == null;
	}

	public List<RegistrationAgreement> getRegistrationAgreement() {
	    return registrationAgreement;
	}

	public List<RegistrationStateType> getRegistrationStateType() {
	    return registrationStateType;
	}

	public Boolean getEquivalence() {
	    return equivalence;
	}

	public DegreeCurricularPlan getDegreeCurricularPlan() {
	    return degreeCurricularPlan;
	}

	public ExecutionYear getExecutionYear() {
	    return executionYear;
	}

	public Degree getDegree() {
	    return degree;
	}

    }

    public List<StudentCurricularPlan> run(SearchParameters searchParameters,
	    Predicate predicate) throws FenixServiceException {

	final List<StudentCurricularPlan> registrations = new ArrayList<StudentCurricularPlan>();
	
	if (searchParameters.getDegreeCurricularPlan() == null
		&& searchParameters.getExecutionYear() == null) {
	    registrations.addAll(searchParameters.getDegree()
		    .getLastStudentCurricularPlans());
	    if (searchParameters.emptyParameters())
		return sortList(registrations,predicate);

	}
	if (searchParameters.getDegreeCurricularPlan() == null
		&& searchParameters.getExecutionYear() != null) {
	    for (DegreeCurricularPlan degreeCurricularPlan : searchParameters
		    .getDegree().getDegreeCurricularPlansForYear(
			    searchParameters.getExecutionYear())) {
		registrations
			.addAll(degreeCurricularPlan
				.getStudentsCurricularPlanGivenEntryYear(searchParameters
					.getExecutionYear()));
	    }
	    if (searchParameters.emptyParameters())
		return sortList(registrations,predicate);

	}
	if (searchParameters.getDegreeCurricularPlan() != null
		&& searchParameters.getExecutionYear() == null) {
	    registrations.addAll(searchParameters.getDegreeCurricularPlan()
		    .getStudentCurricularPlans());
	    if (searchParameters.emptyParameters())
		return sortList(registrations,predicate);
	}
	if (searchParameters.getDegreeCurricularPlan() != null
		&& searchParameters.getExecutionYear() != null) {
	    registrations.addAll(searchParameters.getDegreeCurricularPlan().getStudentsCurricularPlans(searchParameters.getExecutionYear(), registrations));
	    if (searchParameters.emptyParameters())
		return sortList(registrations,predicate);
	}
	
	return getStudentByDegree(searchParameters, sortList(registrations,predicate));
    }

    private List<StudentCurricularPlan> getStudentByDegree(
	    SearchParameters searchParameters,
	    List<StudentCurricularPlan> result) {

	final List<StudentCurricularPlan> scps = new ArrayList<StudentCurricularPlan>();
	for (final StudentCurricularPlan studentCurricularPlan : result) {
	    final Registration registration = studentCurricularPlan
		    .getRegistration();

	    if (registration != null) {

		if (!registration.hasAnyEnrolmentsIn(searchParameters
			.getExecutionYear())) {
		    continue;
		}

		if (!searchParameters.getRegistrationAgreement().isEmpty()
			&& !searchParameters
				.getRegistrationAgreement()
				.contains(
					registration.getRegistrationAgreement())) {
		    continue;
		}

		if (!searchParameters.getRegistrationStateType().isEmpty()
			&& !searchParameters
				.getRegistrationStateType()
				.contains(
					registration
						.getStateInDate(
							registration
								.getStartDate()
								.toDateTimeAtCurrentTime())
						.getStateType())) {
		    continue;
		}

		if (!searchParameters.equivalence
			&& studentCurricularPlan.getHasAnyEquivalences()) {
		    continue;
		}
	    }

	    scps.add(studentCurricularPlan);
	    Collections.sort(scps, new BeanComparator("registration.number"));
	    Collections.reverse(scps);
	}
	return scps;
    }

 
    private List<StudentCurricularPlan> sortList(
	    List<StudentCurricularPlan> list, Predicate predicate) {

	list = (List<StudentCurricularPlan>) CollectionUtils.select(list,
		predicate);
	Collections.sort(list, new BeanComparator("registration.number"));
	Collections.reverse(list);
	return list;
    }

   

    public static class SearchStudentPredicate implements Predicate {

	private SearchParameters searchParameters;

	public SearchStudentPredicate(SearchParameters searchParameters) {
	    this.searchParameters = searchParameters;
	}

	public boolean evaluate(Object arg0) {
	    final StudentCurricularPlan scp = (StudentCurricularPlan) arg0;
	    return true;
	    // return verifyActiveState(searchParameters.getActivePersons(),
	    // person)
	    // && verifySimpleParameter(person.getDocumentIdNumber(),
	    // searchParameters.getDocumentIdNumber())
	    // && verifyIdDocumentType(searchParameters.getIdDocumentType(),
	    // person)
	    // && verifyUsernameEquality(searchParameters.getUsername(),
	    // person)
	    // && verifyNameEquality(searchParameters.getNameWords(),
	    // person)
	    // && verifyParameter(person.getEmail(),
	    // searchParameters.getEmail())
	    // && verifyDegreeType(searchParameters.getDegree(),
	    // searchParameters.getDegreeType(), person)
	    // && verifyStudentNumber(searchParameters.getStudentNumber(),
	    // person);
	}

	private boolean verifyIdDocumentType(IDDocumentType idDocumentType,
		Person person) {
	    return (idDocumentType == null || person.getIdDocumentType() == idDocumentType);
	}

	private boolean verifyStudentNumber(Integer studentNumber, Person person) {
	    return (studentNumber == null || (person.hasStudent() && person
		    .getStudent().getNumber().equals(studentNumber)));
	}

	private boolean verifyActiveState(Boolean activePersons, Person person) {
	    return (activePersons == null || person.hasRole(RoleType.PERSON)
		    .equals(activePersons));
	}

	private boolean verifyUsernameEquality(String usernameToSearch,
		Person person) {
	    if (usernameToSearch == null) {
		return true;
	    }
	    String normalizedUsername = StringNormalizer
		    .normalize(usernameToSearch.trim());
	    for (LoginAlias alias : person.getLoginAlias()) {
		String normalizedAlias = StringNormalizer.normalize(alias
			.getAlias());
		if (normalizedAlias.indexOf(normalizedUsername) != -1) {
		    return true;
		}
	    }
	    return false;
	}

	private boolean verifyDegreeType(final Degree degree,
		final DegreeType degreeType, final Person person) {
	    return degreeType == null
		    || verifyDegreeType(degree, person
			    .getStudentByType(degreeType));
	}

	private boolean verifyDegreeType(final Degree degree,
		final Registration registrationByType) {
	    return registrationByType != null
		    && (degree == null || verifyDegree(degree,
			    registrationByType));
	}

	private boolean verifyDegree(final Degree degree,
		final Registration registrationByType) {
	    final StudentCurricularPlan studentCurricularPlan = registrationByType
		    .getActiveStudentCurricularPlan();
	    return (studentCurricularPlan != null && degree == studentCurricularPlan
		    .getDegreeCurricularPlan().getDegree());
	}

	private boolean verifySimpleParameter(String parameter,
		String searchParameter) {
	    return (searchParameter == null)
		    || (parameter != null && simpleNnormalizeAndCompare(
			    parameter, searchParameter));
	}

	private boolean verifyParameter(String parameter, String searchParameter) {
	    return (searchParameter == null)
		    || (parameter != null && normalizeAndCompare(parameter,
			    searchParameter));
	}

	private boolean simpleNnormalizeAndCompare(String parameter,
		String searchParameter) {
	    String personParameter = parameter;
	    return (personParameter.indexOf(searchParameter) == -1) ? false
		    : true;
	}

	private boolean normalizeAndCompare(String parameter,
		String searchParameter) {
	    String personParameter = StringNormalizer.normalize(parameter
		    .trim());
	    return (personParameter.indexOf(searchParameter) == -1) ? false
		    : true;
	}

	private static boolean verifyNameEquality(String[] nameWords,
		Person person) {
	    return person.verifyNameEquality(nameWords);
	}

	public SearchParameters getSearchParameters() {
	    return searchParameters;
	}
    }

}
