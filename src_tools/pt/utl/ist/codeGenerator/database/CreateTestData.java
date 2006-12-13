package pt.utl.ist.codeGenerator.database;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu._development.MetadataManager;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Identification;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.LoginAlias;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.OldBuilding;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;
import net.sourceforge.fenixedu.util.MarkType;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.TipoSala;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class CreateTestData {

    private static final RoomManager roomManager = new RoomManager();

    public static void main(String[] args) {
        try {
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
        } finally {
            System.err.flush();
            System.out.flush();
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

        System.out.println("Deleting lessons.");
        for (final Set<Lesson> lessons = rootDomainObject.getLessonsSet(); !lessons.isEmpty(); lessons.iterator().next().delete());
        System.out.println("Deleting shifts.");
        for (final Set<Shift> shifts = rootDomainObject.getShiftsSet(); !shifts.isEmpty(); shifts.iterator().next().delete());
        System.out.println("Deleting evaluation methods.");
        for (final Set<EvaluationMethod> evaluationMethods = rootDomainObject.getEvaluationMethodsSet(); !evaluationMethods.isEmpty(); evaluationMethods.iterator().next().delete());
        System.out.println("Deleting bibliographic references.");
        for (final Set<BibliographicReference> bibliographicReferences = rootDomainObject.getBibliographicReferencesSet(); !bibliographicReferences.isEmpty(); bibliographicReferences.iterator().next().delete());

        System.out.println("Deleting execution course sites.");
        for (final Set<ExecutionCourseSite> sites = rootDomainObject.getExecutionCourseSitesSet(); !sites.isEmpty(); sites.iterator().next().delete());
        System.out.println("Deleting forums.");
        for (final Set<Forum> forums = rootDomainObject.getForunsSet(); !forums.isEmpty(); forums.iterator().next().delete());
        System.out.println("Deleting announcements.");
        for (final Set<Announcement> announcements = rootDomainObject.getAnnouncementsSet(); !announcements.isEmpty(); announcements.iterator().next().delete());
        System.out.println("Deleting announcement boards.");
        for (final Set<AnnouncementBoard> announcementBoards = rootDomainObject.getAnnouncementBoardsSet(); !announcementBoards.isEmpty(); announcementBoards.iterator().next().delete());
        System.out.println("Deleting lesson plannings.");
        for (final Set<LessonPlanning> lessonPlannings = rootDomainObject.getLessonPlanningsSet(); !lessonPlannings.isEmpty(); lessonPlannings.iterator().next().deleteWithoutReOrder());
        System.out.println("Deleting evaluations.");
        for (final Set<Evaluation> evaluations = rootDomainObject.getEvaluationsSet(); !evaluations.isEmpty(); evaluations.iterator().next().delete());
        System.out.println("Deleting professorships.");
        for (final Set<Professorship> professorships = rootDomainObject.getProfessorshipsSet(); !professorships.isEmpty(); professorships.iterator().next().delete());
        System.out.println("Deleting execution courses.");
        for (final Set<ExecutionCourse> executionCourses = rootDomainObject.getExecutionCoursesSet(); !executionCourses.isEmpty(); executionCourses.iterator().next().delete());

        System.out.println("Deleting person roles.");
        for (final Party party : rootDomainObject.getPartysSet()) {
            if (party.isPerson()) {
                final Person person = (Person) party;
                person.getPersonRolesSet().clear();
            }
        }

        System.out.println("Deleting coordinators.");
        for (final Set<Coordinator> coordinators = rootDomainObject.getCoordinatorsSet(); !coordinators.isEmpty(); coordinators.iterator().next().delete());
        System.out.println("Deleting teachers.");
        for (final Set<Teacher> teachers = rootDomainObject.getTeachersSet(); !teachers.isEmpty(); teachers.iterator().next().delete());
        System.out.println("Deleting employees.");
        for (final Set<Employee> employees = rootDomainObject.getEmployeesSet(); !employees.isEmpty(); employees.iterator().next().delete());
        System.out.println("Deleting identifications.");
        for (final Set<Identification> identifications = rootDomainObject.getIdentificationsSet(); !identifications.isEmpty(); identifications.iterator().next().delete());
        System.out.println("Deleting users.");
        for (final Set<User> users = rootDomainObject.getUsersSet(); !users.isEmpty(); users.iterator().next().delete());
        System.out.println("Deleting accounts.");
        for (final Set<Account> accounts = rootDomainObject.getAccountsSet(); !accounts.isEmpty(); accounts.iterator().next().delete());
        System.out.println("Deleting people.");
        for (final Set<Party> parties = new HashSet<Party>(rootDomainObject.getPartysSet()); !parties.isEmpty(); ) {
            final Party party = parties.iterator().next();
            if (party.isPerson()) {
                final Person person = (Person) party;
                person.delete();
            }
            parties.remove(party);
        }

        System.out.println("Deleting execution degrees.");
        for (final Set<ExecutionDegree> executionDegrees = rootDomainObject.getExecutionDegreesSet(); !executionDegrees.isEmpty(); executionDegrees.iterator().next().delete());
        System.out.println("Deleting curricular course scopes.");
        for (final Set<CurricularCourseScope> curricularCourseScopes = rootDomainObject.getCurricularCourseScopesSet(); !curricularCourseScopes.isEmpty(); curricularCourseScopes.iterator().next().delete());
        System.out.println("Deleting degree modules.");
        for (final Set<DegreeModule> degreeModules = rootDomainObject.getDegreeModulesSet(); !degreeModules.isEmpty(); degreeModules.iterator().next().delete());
        System.out.println("Deleting branches.");
        for (final Set<Branch> branches = rootDomainObject.getBranchsSet(); !branches.isEmpty(); branches.iterator().next().delete());
        System.out.println("Deleting degree curricular plans.");
        for (final Set<DegreeCurricularPlan> degreeCurricularPlanss = rootDomainObject.getDegreeCurricularPlansSet(); !degreeCurricularPlanss.isEmpty(); degreeCurricularPlanss.iterator().next().delete());
        System.out.println("Deleting degree infos.");
        for (final Set<DegreeInfo> degreeInfos = rootDomainObject.getDegreeInfosSet(); !degreeInfos.isEmpty(); degreeInfos.iterator().next().delete());
        System.out.println("Deleting degrees.");
        for (final Set<Degree> degrees = rootDomainObject.getDegreesSet(); !degrees.isEmpty(); degrees.iterator().next().delete());
        System.out.println("Deleting execution years.");
        for (final Set<ExecutionYear> executionYears = rootDomainObject.getExecutionYearsSet(); !executionYears.isEmpty(); executionYears.iterator().next().delete());
        System.out.println("Deleting occupation periods.");
        for (final Set<OccupationPeriod> occupationPeriods = rootDomainObject.getOccupationPeriodsSet(); !occupationPeriods.isEmpty(); occupationPeriods.iterator().next().deleteIfEmpty());

        System.out.println("Deleting spaces.");
        for (final Set<Space> spaces = rootDomainObject.getSpacesSet(); !spaces.isEmpty(); spaces.iterator().next().delete());
        System.out.println("Deleting campi.");
        for (final Set<Campus> campi = rootDomainObject.getCampussSet(); !campi.isEmpty(); campi.iterator().next().delete());

        System.out.println("Completed clearing any existing data.");
        System.out.println("Loading the test data...");
    }

    private static void createTestData() {
        createExecutionYears();
        createCampus();
        createRooms();
        createDegrees();
        createExecutionCourses();
    }

    private static void createRooms() {
	final Campus campus = RootDomainObject.getInstance().getCampussSet().iterator().next();
	for (int b = 1; b <= 10; b++) {
	    final OldBuilding oldBuilding = new OldBuilding();
	    oldBuilding.setCampus(campus);
	    oldBuilding.setName("Edifício" + b);
	    for (int r = 1; r <= 100; r++) {
		final OldRoom oldRoom = new OldRoom();
		oldRoom.setBuilding(oldBuilding);
		oldRoom.setName("Sala" + r);
		oldRoom.setCapacidadeNormal(Integer.valueOf(50));
		oldRoom.setCapacidadeExame(Integer.valueOf(25));
		oldRoom.setPiso(Integer.valueOf(0));
		oldRoom.setTipo(new TipoSala(TipoSala.PLANA));
		roomManager.push(oldRoom);
	    }
	}
    }

    private static Teacher createTeachers(final int i) {
        final Person person = createPerson("Guru Diplomado", i);
        final Teacher teacher = new Teacher(Integer.valueOf(i), person);
        final Login login = person.getUser().readUserLoginIdentification();
        login.openLoginIfNecessary(RoleType.TEACHER);
        return teacher;
    }

    private static Person createPerson(final String namePrefix, final int i) {
        final Person person = new Person();
        person.setName(namePrefix + i);
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

        new OccupationPeriod(executionPeriod1.getBeginDateYearMonthDay(), executionPeriod1.getEndDateYearMonthDay().minusDays(32));
        new OccupationPeriod(executionPeriod1.getEndDateYearMonthDay().minusDays(31), executionPeriod1.getEndDateYearMonthDay());
        new OccupationPeriod(executionPeriod2.getBeginDateYearMonthDay(), executionPeriod2.getEndDateYearMonthDay().minusDays(32));
        new OccupationPeriod(executionPeriod2.getEndDateYearMonthDay().minusDays(31), executionPeriod2.getEndDateYearMonthDay());
        new OccupationPeriod(executionPeriod2.getEndDateYearMonthDay().plusDays(31), executionPeriod2.getEndDateYearMonthDay().plusDays(46));
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
            createPeriodsForExecutionDegree(executionDegree);
        }
    }

    private static void createPeriodsForExecutionDegree(ExecutionDegree executionDegree) {
        final ExecutionYear executionYear = executionDegree.getExecutionYear();
        final ExecutionPeriod executionPeriod1 = executionYear.getFirstExecutionPeriod();
        final ExecutionPeriod executionPeriod2 = executionYear.getLastExecutionPeriod();

        final OccupationPeriod occupationPeriod1 = OccupationPeriod.readFor(executionPeriod1.getBeginDateYearMonthDay(), executionPeriod1.getEndDateYearMonthDay().minusDays(32));
        final OccupationPeriod occupationPeriod2 = OccupationPeriod.readFor(executionPeriod1.getEndDateYearMonthDay().minusDays(31), executionPeriod1.getEndDateYearMonthDay());
        final OccupationPeriod occupationPeriod3 = OccupationPeriod.readFor(executionPeriod2.getBeginDateYearMonthDay(), executionPeriod2.getEndDateYearMonthDay().minusDays(32));
        final OccupationPeriod occupationPeriod4 = OccupationPeriod.readFor(executionPeriod2.getEndDateYearMonthDay().minusDays(31), executionPeriod2.getEndDateYearMonthDay());
        final OccupationPeriod occupationPeriod5 = OccupationPeriod.readFor(executionPeriod2.getEndDateYearMonthDay().plusDays(31), executionPeriod2.getEndDateYearMonthDay().plusDays(46));

        executionDegree.setPeriodLessonsFirstSemester(occupationPeriod1);
        executionDegree.setPeriodExamsFirstSemester(occupationPeriod2);
        executionDegree.setPeriodLessonsSecondSemester(occupationPeriod3);
        executionDegree.setPeriodExamsSecondSemester(occupationPeriod4);
        executionDegree.setPeriodExamsSpecialSeason(occupationPeriod5);
    }

    private static void createExecutionCourses() {
        for (final DegreeModule degreeModule : RootDomainObject.getInstance().getDegreeModulesSet()) {
            if (degreeModule instanceof CurricularCourse) {
                final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
                for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance().getExecutionPeriodsSet()) {
                    if (!curricularCourse.getActiveDegreeModuleScopesInExecutionPeriod(executionPeriod).isEmpty()) {
                        createExecutionCourse(executionPeriod, curricularCourse);
                    }
                }
            }
        }
    }

    private static void createExecutionCourse(final ExecutionPeriod executionPeriod, final CurricularCourse curricularCourse) {
        final ExecutionCourse executionCourse = new ExecutionCourse(curricularCourse.getName(), curricularCourse.getAcronym(), executionPeriod);
        executionCourse.addAssociatedCurricularCourses(curricularCourse);
        executionCourse.setTheoreticalHours(Double.valueOf(3d));
        executionCourse.setPraticalHours(Double.valueOf(2d));
        final ExecutionCourseSite executionCourseSite = executionCourse.createSite();
        executionCourseSite.setInitialStatement("Bla bla bla bla bla bla bla.");
        executionCourseSite.setAlternativeSite("http://www.google.com/");
        executionCourseSite.setIntroduction("Blur blur bla blur ble blur bla.");
        executionCourseSite.setLessonPlanningAvailable(Boolean.TRUE);
        createProfessorship(executionCourse, Boolean.TRUE);
        createProfessorship(executionCourse, Boolean.FALSE);
        createAnnouncementsAndPlanning(executionCourse);
        createShifts(executionCourse);
        createEvaluationMethod(executionCourse);
        createBibliographicReferences(executionCourse);
    }

    private static void createEvaluationMethod(final ExecutionCourse executionCourse) {
	final EvaluationMethod evaluationMethod = new EvaluationMethod();
	evaluationMethod.setExecutionCourse(executionCourse);
	evaluationMethod.setEvaluationElements(new MultiLanguageString("Método de avaliação. Bla bla bla bla bla."));
	evaluationMethod.getEvaluationElements().setContent(Language.en, "Evaluation method. Blur blur ble blur bla.");
    }

    private static void createBibliographicReferences(final ExecutionCourse executionCourse) {
	createBibliographicReference(executionCourse, Boolean.FALSE);
	createBibliographicReference(executionCourse, Boolean.TRUE);
    }

    private static void createBibliographicReference(final ExecutionCourse executionCourse, final Boolean optional) {
	final BibliographicReference bibliographicReference = new BibliographicReference();
	bibliographicReference.setAuthors("Nome do Autor");
	bibliographicReference.setExecutionCourse(executionCourse);
	bibliographicReference.setOptional(optional);
	bibliographicReference.setReference("Referência");
	bibliographicReference.setTitle("Título");
	bibliographicReference.setYear("Ano");
    }

    private static void createShifts(final ExecutionCourse executionCourse) {
        final Shift shift1 = new Shift(executionCourse, ShiftType.TEORICA, Integer.valueOf(50), Integer.valueOf(50));
        createLesson(shift1, 90);
        createLesson(shift1, 90);
        final Shift shift2 = new Shift(executionCourse, ShiftType.PRATICA, Integer.valueOf(50), Integer.valueOf(50));
        createLesson(shift2, 120);
    }

    private static void createLesson(final Shift shift, int durationInMinutes) {
	final HourMinuteSecond start = roomManager.getNextHourMinuteSecond(durationInMinutes);
	final HourMinuteSecond end = start.plusMinutes(durationInMinutes);
	final Calendar cStart = toCalendar(start);
	final Calendar cEnd = toCalendar(end);
	final DiaSemana diaSemana = new DiaSemana(roomManager.getNextWeekDay());
	final OldRoom oldRoom = roomManager.getNextOldRoom();
	final RoomOccupation roomOccupation = new RoomOccupation(oldRoom, cStart, cEnd, diaSemana, 1);
        final ExecutionPeriod executionPeriod = shift.getDisciplinaExecucao().getExecutionPeriod();
        final OccupationPeriod occupationPeriod = OccupationPeriod.readFor(executionPeriod.getBeginDateYearMonthDay(), executionPeriod.getEndDateYearMonthDay().minusDays(32));
        roomOccupation.setPeriod(occupationPeriod);
	final Lesson lesson = new Lesson(diaSemana, cStart, cEnd, shift.getTipo(), oldRoom, roomOccupation, shift, Integer.valueOf(0), Integer.valueOf(1));
        lesson.setExecutionPeriod(executionPeriod);
    }

    private static Calendar toCalendar(final HourMinuteSecond hourMinuteSecond) {
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.HOUR_OF_DAY, hourMinuteSecond.getHour());
	calendar.set(Calendar.MINUTE, hourMinuteSecond.getMinuteOfHour());
	calendar.set(Calendar.SECOND, hourMinuteSecond.getSecondOfMinute());
	return calendar;
    }

    private static void createAnnouncementsAndPlanning(final ExecutionCourse executionCourse) {
        final AnnouncementBoard announcementBoard = executionCourse.getBoard();
        final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
        final YearMonthDay start = executionPeriod.getBeginDateYearMonthDay();
        final YearMonthDay end = executionPeriod.getEndDateYearMonthDay();
        for (YearMonthDay day = start; day.compareTo(end) < 0; day = day.plusDays(7)) {
            createAnnouncements(announcementBoard, day);
            createPlanning(executionCourse, ShiftType.TEORICA);
            createPlanning(executionCourse, ShiftType.TEORICA);
            createPlanning(executionCourse, ShiftType.PRATICA);
        }
    }

    private static void createPlanning(ExecutionCourse executionCourse, ShiftType shiftType) {
        final LessonPlanning lessonPlanning = new LessonPlanning(new MultiLanguageString("Titulo do Planeamento"), new MultiLanguageString("Corpo do Planeamento"), shiftType, executionCourse);
        lessonPlanning.getTitle().setContent(Language.en, "Title of the planning.");
        lessonPlanning.getPlanning().setContent(Language.en, "Planning contents.");
    }

    private static void createAnnouncements(final AnnouncementBoard announcementBoard, final YearMonthDay day) {
        final Announcement announcement = new Announcement();
        announcement.setAnnouncementBoard(announcementBoard);
        announcement.setAuthor("Autor do anúncio");
        announcement.setAuthorEmail("http://www.google.com/");
        announcement.setBody(new MultiLanguageString("Corpo do anúncio. Bla bla bla bla."));
        announcement.getBody().setContent(Language.en, "Content of the announcement. Blur blur blur blur.");
        announcement.setCreationDate(day.toDateTimeAtMidnight());
        announcement.setCreator(null);
        announcement.setExcerpt(new MultiLanguageString("Bla ..."));
        announcement.getExcerpt().setContent(Language.en, "Blur ...");
        announcement.setKeywords(new MultiLanguageString("Bla"));
        announcement.getKeywords().setContent(Language.en, "Blur");
        announcement.setLastModification(day.toDateTimeAtCurrentTime());
        announcement.setPlace("Here.");
//        announcement.setPublicationBegin();
//        announcement.setPublicationEnd();
//        announcement.setReferedSubjectBegin();
//        announcement.setReferedSubjectEnd();
        announcement.setSubject(new MultiLanguageString("Assunto Bla."));
        announcement.getSubject().setContent(Language.en, "Subject blur.");
        announcement.setVisible(Boolean.TRUE);
    }

    private static void createPreBolonhaCurricularCourses(final DegreeCurricularPlan degreeCurricularPlan, int dcpCounter, final ExecutionYear executionYear, final Branch branch) {
	for (final CurricularSemester curricularSemester : RootDomainObject.getInstance().getCurricularSemestersSet()) {
	    final CurricularYear curricularYear = curricularSemester.getCurricularYear();
	    for (int i = 1; i < 6; i++) {
		final String x = "" + dcpCounter + i + curricularYear.getYear() + curricularSemester.getSemester();
		final CurricularCourse curricularCourse = degreeCurricularPlan.createCurricularCourse("Disciplina" + x, "C" + x, "D" + x, Boolean.TRUE, CurricularStage.OLD);
                curricularCourse.setNameEn("Course" + x);
		curricularCourse.setType(CurricularCourseType.NORMAL_COURSE);
                curricularCourse.setTheoreticalHours(Double.valueOf(3d));
                curricularCourse.setPraticalHours(Double.valueOf(2d));
		new CurricularCourseScope(branch, curricularCourse, curricularSemester, executionYear.getBeginDateYearMonthDay().toDateMidnight().toCalendar(null), null, null);
                final Curriculum curriculum = new Curriculum();
                curriculum.setCurricularCourse(curricularCourse);
                curriculum.setGeneralObjectives("Objectivos gerais bla bla bla bla bla.");
                curriculum.setGeneralObjectivesEn("General objectives blur ble ble blur.");
                curriculum.setOperacionalObjectives("Objectivos Operacionais bla bla bla bla bla.");
                curriculum.setOperacionalObjectivesEn("Operational objectives blur ble ble blur.");
                curriculum.setProgram("Programa bla bla bla bla bla.");
                curriculum.setProgramEn("Program blur ble ble blur.");
                curriculum.setLastModificationDateDateTime(new DateTime());
                final CompetenceCourse competenceCourse = new CompetenceCourse(curricularCourse.getCode(), curricularCourse.getName(), null);
                curricularCourse.setCompetenceCourse(competenceCourse);
	    }
	}
    }

    private static void createProfessorship(final ExecutionCourse executionCourse, final Boolean isResponsibleFor) {
        final int n = RootDomainObject.getInstance().getTeachersSet().size();
        final Teacher teacher = createTeachers(n + 1);
        final Professorship professorship = new Professorship();
        professorship.setTeacher(teacher);
        professorship.setExecutionCourse(executionCourse);
        professorship.setResponsibleFor(isResponsibleFor);
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
