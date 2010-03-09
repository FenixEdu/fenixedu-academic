package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.LoginAlias;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.UsernameUtils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

public class ErasmusIndividualCandidacy extends ErasmusIndividualCandidacy_Base {

    public ErasmusIndividualCandidacy() {
	super();
    }

    ErasmusIndividualCandidacy(final ErasmusIndividualCandidacyProcess process, final ErasmusIndividualCandidacyProcessBean bean) {
	this();

	if ("Raul Gonzalez".equals(bean.getPersonBean().getName())) {
	    bean.getPersonBean().setPerson(Person.readPersonByUsername("ist90427"));
	} else if ("Javier Garcia".equals(bean.getPersonBean().getName())) {
	    bean.getPersonBean().setPerson(Person.readPersonByUsername("ist90428"));
	}

	bean.getPersonBean().setCreateLoginIdentificationAndUserIfNecessary(false);

	Person person = init(bean, process);

	setSelectedDegree(bean.getSelectedDegree());

	createEramusStudentData(bean);

	associateCurricularCourses(bean.getSelectedCurricularCourses());

	if (bean.isToAccessFenix()) {
	    if ("Raul Gonzalez".equals(person.getName())) {
		Login login = person.getLoginIdentification();
		if (login == null) {
		    User user = person.getUser();
		    if (user == null) {
			user = new User(person);
		    }
		    login = new Login(person.getUser());
		    final LoginAlias loginAlias = login.getInstitutionalLoginAlias();
		    if (loginAlias == null) {
			String userUId = "ist90427";
			if (Person.readPersonByUsername("ist90427") != null) {
			    userUId = UsernameUtils.updateIstUsername(person.getUser().getPerson());
			}

			if (!StringUtils.isEmpty(userUId)) {
			    LoginAlias.createNewInstitutionalLoginAlias(login, userUId);
			    person.getUser().setUserUId(userUId);
			}
		    }
		}
	    } else if ("Javier Garcia".equals(person.getName())) {
		Login login = person.getLoginIdentification();
		if (login == null) {
		    User user = person.getUser();
		    if (user == null) {
			user = new User(person);
		    }
		    login = new Login(person.getUser());
		    final LoginAlias loginAlias = login.getInstitutionalLoginAlias();
		    if (loginAlias == null) {
			String userUId = "ist90428";
			if (Person.readPersonByUsername("ist90428") != null) {
			    userUId = UsernameUtils.updateIstUsername(person.getUser().getPerson());
			}

			if (!StringUtils.isEmpty(userUId)) {
			    LoginAlias.createNewInstitutionalLoginAlias(login, userUId);
			    person.getUser().setUserUId(userUId);
			}
		    }
		}
	    }

	    person.addPersonRoleByRoleType(RoleType.PERSON);
	    person.addPersonRoleByRoleType(RoleType.CANDIDATE);
	    person.getLoginIdentification().setPassword(PasswordEncryptor.encryptPassword("pass"));
	}
    }

    private void associateCurricularCourses(Set<CurricularCourse> selectedCurricularCourses) {
	for (CurricularCourse curricularCourse : selectedCurricularCourses) {
	    addCurricularCourses(curricularCourse);
	}
    }

    private void createEramusStudentData(ErasmusIndividualCandidacyProcessBean bean) {
	setErasmusStudentData(new ErasmusStudentData(this, bean.getErasmusStudentDataBean(), bean.calculateErasmusVacancy()));
    }

    @Override
    protected void createDebt(final Person person) {

    }

    @Override
    protected void checkParameters(final Person person, final IndividualCandidacyProcess process,
	    final IndividualCandidacyProcessBean bean) {
	ErasmusIndividualCandidacyProcess secondCycleIndividualCandidacyProcess = (ErasmusIndividualCandidacyProcess) process;
	ErasmusIndividualCandidacyProcessBean secondCandidacyProcessBean = (ErasmusIndividualCandidacyProcessBean) bean;
	LocalDate candidacyDate = bean.getCandidacyDate();
	Degree selectedDegree = secondCandidacyProcessBean.getSelectedDegree();

	checkParameters(person, secondCycleIndividualCandidacyProcess, candidacyDate, selectedDegree);
    }

    private void checkParameters(final Person person, final ErasmusIndividualCandidacyProcess process,
	    final LocalDate candidacyDate, final Degree degree) {

	checkParameters(person, process, candidacyDate);

	/*
	 * 31/03/2009 - The candidacy may be submited externally hence may not
	 * be associated to a person
	 * 
	 * 
	 * if(person.hasValidSecondCycleIndividualCandidacy(process.
	 * getCandidacyExecutionInterval())) { throw newDomainException(
	 * "error.SecondCycleIndividualCandidacy.person.already.has.candidacy",
	 * process .getCandidacyExecutionInterval().getName()); }
	 */

	if (degree == null) {
	    throw new DomainException("error.SecondCycleIndividualCandidacy.invalid.degree");
	}
    }

    void editDegreeAndCoursesInformation(ErasmusIndividualCandidacyProcessBean bean) {
	this.setSelectedDegree(bean.getSelectedDegree());

	Set<CurricularCourse> setOne = new HashSet<CurricularCourse>(this.getCurricularCourses());
	setOne.addAll(bean.getSelectedCurricularCourses());

	for (CurricularCourse curricularCourse : this.getCurricularCourses()) {
	    if (hasCurricularCourses(curricularCourse) && !bean.getSelectedCurricularCourses().contains(curricularCourse)) {
		removeCurricularCourses(curricularCourse);
	    } else if (!hasCurricularCourses(curricularCourse) && bean.getSelectedCurricularCourses().contains(curricularCourse)) {
		addCurricularCourses(curricularCourse);
	    }
	}
    }

}
