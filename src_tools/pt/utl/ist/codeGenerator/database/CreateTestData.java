package pt.utl.ist.codeGenerator.database;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu._development.MetadataManager;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Identification;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.LoginAlias;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.MarkType;
import net.sourceforge.fenixedu.util.MultiLanguageString;
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
        for (final Set<CurricularCourseScope> curricularCourseScopes = rootDomainObject.getCurricularCourseScopesSet(); !curricularCourseScopes.isEmpty(); curricularCourseScopes.iterator().next().delete());
        for (final Set<DegreeModule> degreeModules = rootDomainObject.getDegreeModulesSet(); !degreeModules.isEmpty(); degreeModules.iterator().next().delete());
        for (final Set<Branch> branches = rootDomainObject.getBranchsSet(); !branches.isEmpty(); branches.iterator().next().delete());
        for (final Set<DegreeCurricularPlan> degreeCurricularPlanss = rootDomainObject.getDegreeCurricularPlansSet(); !degreeCurricularPlanss.isEmpty(); degreeCurricularPlanss.iterator().next().delete());
        for (final Set<DegreeInfo> degreeInfos = rootDomainObject.getDegreeInfosSet(); !degreeInfos.isEmpty(); degreeInfos.iterator().next().delete());
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
	final YearMonthDay yearMonthDay = new YearMonthDay();
        final ExecutionYear executionYear = new ExecutionYear();
        executionYear.setYear(constructExecutionYearString());
        executionYear.setState(PeriodState.CURRENT);
        final ExecutionPeriod executionPeriod1 = new ExecutionPeriod();
        executionPeriod1.setSemester(Integer.valueOf(1));
        executionPeriod1.setExecutionYear(executionYear);
        executionPeriod1.setName("Semester 1");
        final ExecutionPeriod executionPeriod2 = new ExecutionPeriod();
        executionPeriod2.setSemester(Integer.valueOf(2));
        executionPeriod2.setExecutionYear(executionYear);
        executionPeriod2.setName("Semester 2");
        executionPeriod1.setNextExecutionPeriod(executionPeriod2);

        if (yearMonthDay.getMonthOfYear() < 8) {
            final YearMonthDay previousYear = yearMonthDay.minusYears(1);
            executionYear.setBeginDateYearMonthDay(new YearMonthDay(previousYear.getYear(), 9, 1));
            executionYear.setEndDateYearMonthDay(new YearMonthDay(yearMonthDay.getYear(), 7, 31));
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
            executionYear.setBeginDateYearMonthDay(new YearMonthDay(yearMonthDay.getYear(), 9, 1));
            executionYear.setEndDateYearMonthDay(new YearMonthDay(yearMonthDay.getYear() + 1, 7, 31));
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
                final Branch branch = new Branch("", "", "", degreeCurricularPlan);
                branch.setBranchType(BranchType.COMNBR);
                createPreBolonhaCurricularCourses(degreeCurricularPlan, i, executionYear, branch);
            }

            createDegreeInfo(degree);
            degreeCurricularPlan.setDescription("Bla bla bla. Descrição do plano curricular do curso. Bla bla bla");
            degreeCurricularPlan.setDescriptionEn("Blur ble bla. Description of the degrees curricular plan. Goo goo foo foo.");

            final ExecutionDegree executionDegree = degreeCurricularPlan.createExecutionDegree(executionYear, campus, Boolean.FALSE);
            final Teacher teacher = createTeachers(i);
            new Coordinator(executionDegree, teacher.getPerson(), Boolean.TRUE);
        }
    }

    private static void createPreBolonhaCurricularCourses(final DegreeCurricularPlan degreeCurricularPlan, int dcpCounter, final ExecutionYear executionYear, final Branch branch) {
	final Degree degree = degreeCurricularPlan.getDegree();
	final DegreeType degreeType = degree.getDegreeType();
	for (final CurricularSemester curricularSemester : RootDomainObject.getInstance().getCurricularSemestersSet()) {
	    final CurricularYear curricularYear = curricularSemester.getCurricularYear();
	    for (int i = 1; i < 6; i++) {
		final String x = "" + dcpCounter + i + curricularYear.getYear() + curricularSemester.getSemester();
		final CurricularCourse curricularCourse = degreeCurricularPlan.createCurricularCourse("Disciplina" + x, "C" + x, "D" + x, Boolean.TRUE, CurricularStage.OLD);
		curricularCourse.setType(CurricularCourseType.NORMAL_COURSE);
		new CurricularCourseScope(branch, curricularCourse, curricularSemester, executionYear.getBeginDateYearMonthDay().toDateMidnight().toCalendar(null), null, null);
	    }
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

    private static void createDegreeInfo(final Degree degree) {
        final DegreeInfo degreeInfo = degree.createCurrentDegreeInfo();
        degreeInfo.setDescription(new MultiLanguageString("Descrição do curso. Bla bla bla bla bla."));
        degreeInfo.getDescription().setContent(Language.en, "Description of the degree. Blur blur blur and more blur.");
        degreeInfo.setHistory(new MultiLanguageString("Historial do curso. Bla bla bla bla bla."));
        degreeInfo.getHistory().setContent(Language.en, "History of the degree. Blur blur blur and more blur.");
        degreeInfo.setObjectives(new MultiLanguageString("Objectivos do curso. Bla bla bla bla bla."));
        degreeInfo.getObjectives().setContent(Language.en, "Objectives of the degree. Blur blur blur and more blur.");
        degreeInfo.setDesignedFor(new MultiLanguageString("Propósito do curso. Bla bla bla bla bla."));
        degreeInfo.getDesignedFor().setContent(Language.en, "Purpose of the degree. Blur blur blur and more blur.");
        degreeInfo.setProfessionalExits(new MultiLanguageString("Saídas profissionais. Bla bla bla bla bla."));
        degreeInfo.getProfessionalExits().setContent(Language.en, "Professional exists of the degree. Blur blur blur and more blur.");
        degreeInfo.setOperationalRegime(new MultiLanguageString("Regime operacional. Bla bla bla bla bla."));
        degreeInfo.getOperationalRegime().setContent(Language.en, "Operational regime of the degree. Blur blur blur and more blur.");
        degreeInfo.setGratuity(new MultiLanguageString("Propinas. Bla bla bla bla bla."));
        degreeInfo.getGratuity().setContent(Language.en, "Gratuity of the degree. Blur blur blur and more blur.");
        degreeInfo.setSchoolCalendar(new MultiLanguageString("Calendário escolar. Bla bla bla bla bla."));
        degreeInfo.getSchoolCalendar().setContent(Language.en, "School calendar of the degree. Blur blur blur and more blur.");
        degreeInfo.setCandidacyPeriod(new MultiLanguageString("Periodo de candidaturas. Bla bla bla bla bla."));
        degreeInfo.getCandidacyPeriod().setContent(Language.en, "Candidacy period of the degree. Blur blur blur and more blur.");
        degreeInfo.setSelectionResultDeadline(new MultiLanguageString("Prazo de publicação de resultados de candidaturas. Bla bla bla bla bla."));
        degreeInfo.getSelectionResultDeadline().setContent(Language.en, "Seletion result deadline of the degree. Blur blur blur and more blur.");
        degreeInfo.setEnrolmentPeriod(new MultiLanguageString("Periodo de inscrições. Bla bla bla bla bla."));
        degreeInfo.getEnrolmentPeriod().setContent(Language.en, "Enrolment period of the degree. Blur blur blur and more blur.");
        degreeInfo.setAdditionalInfo(new MultiLanguageString("Informação adicional. Bla bla bla bla bla."));
        degreeInfo.getAdditionalInfo().setContent(Language.en, "Additional information of the degree. Blur blur blur and more blur.");
        degreeInfo.setLinks(new MultiLanguageString("Links. Bla bla bla bla bla."));
        degreeInfo.getLinks().setContent(Language.en, "Links of the degree. Blur blur blur and more blur.");
        degreeInfo.setTestIngression(new MultiLanguageString("Testes de ingressão. Bla bla bla bla bla."));
        degreeInfo.getTestIngression().setContent(Language.en, "Ingression tests of the degree. Blur blur blur and more blur.");
        degreeInfo.setClassifications(new MultiLanguageString("Classificações. Bla bla bla bla bla."));
        degreeInfo.getClassifications().setContent(Language.en, "Classifications of the degree. Blur blur blur and more blur.");
        degreeInfo.setAccessRequisites(new MultiLanguageString("Requisitos de acesso. Bla bla bla bla bla."));
        degreeInfo.getAccessRequisites().setContent(Language.en, "Access requisites of the degree. Blur blur blur and more blur.");
        degreeInfo.setCandidacyDocuments(new MultiLanguageString("Documentos de candidatura. Bla bla bla bla bla."));
        degreeInfo.getCandidacyDocuments().setContent(Language.en, "Candidacy documents of the degree. Blur blur blur and more blur.");
        degreeInfo.setDriftsInitial(Integer.valueOf(1));
        degreeInfo.setDriftsFirst(Integer.valueOf(1));
        degreeInfo.setDriftsSecond(Integer.valueOf(1));
        degreeInfo.setMarkMin(Double.valueOf(12));
        degreeInfo.setMarkMax(Double.valueOf(20));
        degreeInfo.setMarkAverage(Double.valueOf(15));
        degreeInfo.setQualificationLevel(new MultiLanguageString("Nível de qualificação. Bla bla bla bla bla."));
        degreeInfo.getQualificationLevel().setContent(Language.en, "Qualification level of the degree. Blur blur blur and more blur.");
        degreeInfo.setRecognitions(new MultiLanguageString("Reconhecimentos. Bla bla bla bla bla."));
        degreeInfo.getRecognitions().setContent(Language.en, "Recognitions of the degree. Blur blur blur and more blur.");
    }

}
