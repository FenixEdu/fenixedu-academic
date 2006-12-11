package pt.utl.ist.codeGenerator.database;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu._development.MetadataManager;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Identification;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.LoginAlias;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.MarkType;
import net.sourceforge.fenixedu.util.PeriodState;

import org.joda.time.YearMonthDay;

public class CreateTestData {

    public static void main(String[] args) {

	MetadataManager.init("build/WEB-INF/classes/domain_model.dml");
	SuportePersistenteOJB.fixDescriptors();
	RootDomainObject.init();

	ISuportePersistente persistentSupport = null;
	try {
	    persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
	    persistentSupport.iniciarTransaccao();
            clearData();
            createManagerUser();
            createTestData();
	    persistentSupport.confirmarTransaccao();
	} catch (Exception ex) {
	    ex.printStackTrace();

	    try {
		if (persistentSupport != null) {
		    persistentSupport.cancelarTransaccao();
		}
	    } catch (ExcepcaoPersistencia e) {
		throw new Error(e);
	    }
	}

	System.out.println("Creation of test data complete.");
	System.exit(0);
    }

    private static void createManagerUser() {
        final Person person = new Person();
        person.setName("Fenix System Administrator");
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.PERSON));
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.MANAGER));
        final User user = person.getUser();
        final Login login = user.readUserLoginIdentification();
        login.setActive(Boolean.TRUE);
        LoginAlias.createNewCustomLoginAlias(login, "admin");
        login.openLoginIfNecessary(RoleType.MANAGER);
    }

    private static void clearData() {
        final RootDomainObject rootDomainObject = RootDomainObject.getInstance();
        for (final Party party : rootDomainObject.getPartysSet()) {
            if (party.isPerson()) {
                final Person person = (Person) party;
                person.getPersonRolesSet().clear();
            }
        }

        for (final Set<Coordinator> coordinators = rootDomainObject.getCoordinatorsSet(); !coordinators.isEmpty(); coordinators.iterator().next().delete());
        for (final Set<Teacher> teachers = rootDomainObject.getTeachersSet(); !teachers.isEmpty(); teachers.iterator().next().delete());
        for (final Set<Employee> employees = rootDomainObject.getEmployeesSet(); !employees.isEmpty(); employees.iterator().next().delete());
        for (final Set<Identification> identifications = rootDomainObject.getIdentificationsSet(); !identifications.isEmpty(); identifications.iterator().next().delete());
        for (final Set<User> users = rootDomainObject.getUsersSet(); !users.isEmpty(); users.iterator().next().delete());
        for (final Set<Account> accounts = rootDomainObject.getAccountsSet(); !accounts.isEmpty(); accounts.iterator().next().delete());
        for (final Set<Party> parties = new HashSet<Party>(rootDomainObject.getPartysSet()); !parties.isEmpty(); ) {
            final Party party = parties.iterator().next();
            if (party.isPerson()) {
                final Person person = (Person) party;
                person.delete();
            }
            parties.remove(party);
        }
        for (final Set<ExecutionDegree> executionDegrees = rootDomainObject.getExecutionDegreesSet(); !executionDegrees.isEmpty(); executionDegrees.iterator().next().delete());
        for (final Set<DegreeCurricularPlan> degreeCurricularPlanss = rootDomainObject.getDegreeCurricularPlansSet(); !degreeCurricularPlanss.isEmpty(); degreeCurricularPlanss.iterator().next().delete());
        for (final Set<Degree> degrees = rootDomainObject.getDegreesSet(); !degrees.isEmpty(); degrees.iterator().next().delete());
        for (final Set<ExecutionYear> executionYears = rootDomainObject.getExecutionYearsSet(); !executionYears.isEmpty(); executionYears.iterator().next().delete());
        for (final Set<Campus> campi = rootDomainObject.getCampussSet(); !campi.isEmpty(); campi.iterator().next().delete());
    }

    private static void createTestData() {
        createExecutionYears();
        createCampus();
        createDegrees();
    }

    private static Teacher createTeachers(final int i) {
        final Person person = createPerson(i);
        final Teacher teacher = new Teacher(Integer.valueOf(i), person);
        final Login login = person.getUser().readUserLoginIdentification();
        login.openLoginIfNecessary(RoleType.TEACHER);
        return teacher;
    }

    private static Person createPerson(final int i) {
        final Person person = new Person();
        person.setName("Nome da Pessoa" + i);
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.PERSON));
        final User user = person.getUser();
        final Login login = user.readUserLoginIdentification();
        login.setActive(Boolean.TRUE);
        LoginAlias.createNewCustomLoginAlias(login, "person" + i);
        return person;
    }

    private static void createExecutionYears() {
        final ExecutionYear executionYear = new ExecutionYear();
        executionYear.setYear(constructExecutionYearString());
        executionYear.setState(PeriodState.CURRENT);
        final ExecutionPeriod executionPeriod1 = new ExecutionPeriod();
        executionPeriod1.setSemester(Integer.valueOf(1));
        executionPeriod1.setExecutionYear(executionYear);
        final ExecutionPeriod executionPeriod2 = new ExecutionPeriod();
        executionPeriod2.setSemester(Integer.valueOf(2));
        executionPeriod2.setExecutionYear(executionYear);
        executionPeriod1.setNextExecutionPeriod(executionPeriod2);

        final YearMonthDay yearMonthDay = new YearMonthDay();
        if (yearMonthDay.getMonthOfYear() < 8) {
            final YearMonthDay previousYear = yearMonthDay.minusYears(1);
            executionPeriod1.setBeginDateYearMonthDay(new YearMonthDay(previousYear.getYear(), 9, 1));
            executionPeriod1.setEndDateYearMonthDay(new YearMonthDay(yearMonthDay.getYear(), 1, 31));
            executionPeriod2.setBeginDateYearMonthDay(new YearMonthDay(yearMonthDay.getYear(), 2, 1));
            executionPeriod2.setEndDateYearMonthDay(new YearMonthDay(yearMonthDay.getYear(), 7, 31));
            if (yearMonthDay.getMonthOfYear() < 2) {
                executionPeriod1.setState(PeriodState.CURRENT);
                executionPeriod2.setState(PeriodState.OPEN);
            } else {
                executionPeriod1.setState(PeriodState.OPEN);
                executionPeriod2.setState(PeriodState.CURRENT);                
            }
        } else {
            executionPeriod1.setBeginDateYearMonthDay(new YearMonthDay(yearMonthDay.getYear(), 9, 1));
            executionPeriod1.setEndDateYearMonthDay(new YearMonthDay(yearMonthDay.getYear() + 1, 1, 31));
            executionPeriod2.setBeginDateYearMonthDay(new YearMonthDay(yearMonthDay.getYear() + 1, 2, 1));
            executionPeriod2.setEndDateYearMonthDay(new YearMonthDay(yearMonthDay.getYear() + 1, 7, 31));
            executionPeriod1.setState(PeriodState.CURRENT);
            executionPeriod2.setState(PeriodState.OPEN);
        }
    }

    private static void createCampus() {
        final Campus campus = new Campus();
        campus.setName("Nome do Campus");
    }

    private static void createDegrees() {
        final Person person = Person.readPersonByUsernameWithOpenedLogin("admin");
        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        final Campus campus = RootDomainObject.getInstance().getCampussSet().iterator().next();

        int i = 0;
        for (final DegreeType degreeType : DegreeType.values()) {
            ++i;
            final GradeScale gradeScale = degreeType.getGradeScale();
            final Degree degree;
            final DegreeCurricularPlan degreeCurricularPlan;
            if (degreeType.isBolonhaType()) {
                degree = new Degree("Nome do Curso", "Degree Name", "CODE" + i, degreeType, 0d, gradeScale, null);
                degreeCurricularPlan = degree.createBolonhaDegreeCurricularPlan("DegreeCurricularPlanName", gradeScale, person);
                degreeCurricularPlan.setCurricularStage(CurricularStage.PUBLISHED);
            } else {
                degree = new Degree("Nome do Curso", "Degree Name", "CODE" + i, degreeType, gradeScale, DegreeCurricularPlan.class.getName());
                degreeCurricularPlan = degree.createPreBolonhaDegreeCurricularPlan("DegreeCurricularPlanName", DegreeCurricularPlanState.ACTIVE,
                        new Date(), null, degreeType.getYears(), Integer.valueOf(1), Double.valueOf(degreeType.getDefaultEctsCredits()),
                        MarkType.TYPE20_OBJ, Integer.valueOf(100), null, gradeScale);
            }
            final ExecutionDegree executionDegree = degreeCurricularPlan.createExecutionDegree(executionYear, campus, Boolean.FALSE);
            final Teacher teacher = createTeachers(i);
            new Coordinator(executionDegree, teacher.getPerson(), Boolean.TRUE);
        }
    }

    private static String constructExecutionYearString() {
        final YearMonthDay yearMonthDay = new YearMonthDay();
        return yearMonthDay.getMonthOfYear() < 8 ?
                constructExecutionYearString(yearMonthDay.minusYears(1), yearMonthDay) :
                constructExecutionYearString(yearMonthDay, yearMonthDay.plusYears(1)) ;
    }

    private static String constructExecutionYearString(final YearMonthDay year1, final YearMonthDay year2) {
        return year1.toString("yyyy") + "/" + year2.toString("yyyy");
    }

}
