package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.LoginAlias;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.StringNormalizer;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.CollectionPager;

public class SearchPerson extends Service {

    public static class SearchParameters {

	private String email, username, documentIdNumber;

	private String[] nameWords;

	private Role role;

	private Degree degree;

	private Department department;

	private DegreeType degreeType;

	public SearchParameters(String name, String email, String username, String documentIdNumber,
		String roleType, String degreeTypeString, Integer degreeId, Integer departmentId) {

	    this.nameWords = (name != null && !name.equals("")) ? getNameWords(name) : null;
	    this.email = (email != null && !email.equals("")) ? StringNormalizer.normalize(email.trim())
		    : null;
	    this.username = (username != null && !username.equals("")) ? StringNormalizer
		    .normalize(username.trim()) : null;
	    this.documentIdNumber = (documentIdNumber != null && !documentIdNumber.equals("")) ? StringNormalizer
		    .normalize(documentIdNumber.trim())
		    : null;

	    if (roleType != null && roleType.length() > 0) {
		role = (Role) Role.getRoleByRoleType(RoleType.valueOf(roleType));
	    }

	    if (degreeId != null) {
		degree = rootDomainObject.readDegreeByOID(degreeId);
	    }

	    if (degreeTypeString != null && degreeTypeString.length() > 0) {
		degreeType = DegreeType.valueOf(degreeTypeString);
	    }

	    if (departmentId != null) {
		department = rootDomainObject.readDepartmentByOID(departmentId);
	    }
	}

	private boolean emptyParameters() {
	    return StringUtils.isEmpty(this.email) && StringUtils.isEmpty(this.username)
		    && StringUtils.isEmpty(this.documentIdNumber) && this.role == null
		    && this.degree == null && this.department == null && this.degreeType == null
		    && this.nameWords == null;
	}

	private static String[] getNameWords(String name) {
	    String[] nameWords = null;
	    if (name != null && !StringUtils.isEmpty(name.trim())) {
		nameWords = name.trim().split(" ");
		StringNormalizer.normalize(nameWords);
	    }
	    return nameWords;
	}

	public Degree getDegree() {
	    return degree;
	}

	public DegreeType getDegreeType() {
	    return degreeType;
	}

	public Department getDepartment() {
	    return department;
	}

	public String getDocumentIdNumber() {
	    return documentIdNumber;
	}

	public String getEmail() {
	    return email;
	}

	public String[] getNameWords() {
	    return nameWords;
	}

	public Role getRole() {
	    return role;
	}

	public String getUsername() {
	    return username;
	}
    }

    public CollectionPager run(SearchParameters searchParameters, Predicate predicate)
	    throws FenixServiceException {

	if (searchParameters.emptyParameters()) {
	    return new CollectionPager<Person>(new ArrayList<Person>(), 25);
	}

	final List<Person> persons;
	List<Person> allValidPersons = new ArrayList<Person>();
	List<Teacher> teachers = new ArrayList<Teacher>();

	Role roleBd = searchParameters.getRole();
	if (roleBd == null) {
	    roleBd = Role.getRoleByRoleType(RoleType.PERSON);
	}

	if ((roleBd.getRoleType() == RoleType.TEACHER) && (searchParameters.getDepartment() != null)) {
	    persons = new ArrayList<Person>();
	    teachers = searchParameters.getDepartment().getAllCurrentTeachers();
	    for (Teacher teacher : teachers) {
		persons.add(teacher.getPerson());
	    }
	} else if (roleBd.getRoleType() == RoleType.EMPLOYEE) {
	    persons = new ArrayList<Person>();
	    for (Person person : roleBd.getAssociatedPersons()) {
		if (person.getTeacher() == null) {
		    persons.add(person);
		}
	    }
	} else {
	    persons = roleBd.getAssociatedPersons();
	}

	allValidPersons = (List<Person>) CollectionUtils.select(persons, predicate);
	Collections.sort(allValidPersons, Person.COMPARATOR_BY_NAME);
	List<InfoPerson> infoPersons = new ArrayList<InfoPerson>();

	for (Person person : (List<Person>) allValidPersons) {
	    infoPersons.add(InfoPerson.newInfoFromDomain(person));
	}

	return new CollectionPager<InfoPerson>(infoPersons, 25);
    }

    // --------------- Search Person Predicate
    // -------------------------------------------

    public static class SearchPersonPredicate implements Predicate {

	private SearchParameters searchParameters;

	public SearchPersonPredicate(SearchParameters searchParameters) {
	    this.searchParameters = searchParameters;
	}

	public boolean evaluate(Object arg0) {
	    Person person = (Person) arg0;

	    return verifyParameter(person.getEmail(), searchParameters.getEmail())
		    && verifyUsernameEquality(searchParameters.getUsername(), person)
		    && verifyParameter(person.getDocumentIdNumber(), searchParameters
			    .getDocumentIdNumber())
		    && verifyNameEquality(searchParameters.getNameWords(), person)
		    && verifyDegreeType(searchParameters.getDegree(), searchParameters.getDegreeType(),
			    person);
	}

	private boolean verifyUsernameEquality(String usernameToSearch, Person person) {
	    if (usernameToSearch == null) {
		return true;
	    }
	    String normalizedUsername = StringNormalizer.normalize(usernameToSearch.trim());
	    for (LoginAlias alias : person.getLoginAlias()) {
		String normalizedAlias = StringNormalizer.normalize(alias.getAlias());
		if (normalizedAlias.indexOf(normalizedUsername) != -1) {
		    return true;
		}
	    }
	    return false;
	}

	private boolean verifyDegreeType(final Degree degree, final DegreeType degreeType,
		final Person person) {
	    return degreeType == null || verifyDegreeType(degree, person.getStudentByType(degreeType));
	}

	private boolean verifyDegreeType(final Degree degree, final Registration registrationByType) {
	    return registrationByType != null
		    && (degree == null || verifyDegree(degree, registrationByType));
	}

	private boolean verifyDegree(final Degree degree, final Registration registrationByType) {
	    final StudentCurricularPlan studentCurricularPlan = registrationByType
		    .getActiveStudentCurricularPlan();
	    return (studentCurricularPlan != null && degree == studentCurricularPlan
		    .getDegreeCurricularPlan().getDegree());
	}

	private boolean verifyParameter(String parameter, String searchParameter) {
	    return (searchParameter == null)
		    || (parameter != null && normalizeAndCompare(parameter, searchParameter));
	}

	private boolean normalizeAndCompare(String parameter, String searchParameter) {
	    String personParameter = StringNormalizer.normalize(parameter.trim());
	    return (personParameter.indexOf(searchParameter) == -1) ? false : true;
	}

	private static boolean verifyNameEquality(String[] nameWords, Person person) {
	    return person.verifyNameEquality(nameWords);
	}

	public SearchParameters getSearchParameters() {
	    return searchParameters;
	}
    }

    public static List<Person> searchPersonInAllPersons(SearchPersonPredicate predicate) {
	if (predicate.getSearchParameters() != null && predicate.getSearchParameters().emptyParameters()) {
	    return new ArrayList<Person>();
	}
	List<Person> persons = (List<Person>) CollectionUtils.select(Person.readAllPersons(), predicate);
	Collections.sort(persons, Person.COMPARATOR_BY_NAME);
	return persons;
    }
}
