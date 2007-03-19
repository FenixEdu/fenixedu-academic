package pt.utl.ist.codeGenerator.database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu._development.MetadataManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.SupportLessonDTO;
import net.sourceforge.fenixedu.domain.Attends;
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
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCoursesSpecialSeason;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.Exam;
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
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreement;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.installments.InstallmentWithMonthlyPenalty;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.FullGratuityPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.GratuityPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester;
import net.sourceforge.fenixedu.domain.accounting.postingRules.AdministrativeOfficeFeeAndInsurancePR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.FixedAmountPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.FixedAmountWithPenaltyFromDatePR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.GratuityWithPaymentPlanPR;
import net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests.CertificateRequestPR;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.UnitServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreements.DegreeCurricularPlanServiceAgreement;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.OldBuilding;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentDataByExecutionYear;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceItem;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.ContractType;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;
import net.sourceforge.fenixedu.util.MarkType;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.Season;
import net.sourceforge.fenixedu.util.TipoSala;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.YearMonthDay;

public class CreateTestData {

    private static final LessonRoomManager lessonRoomManager = new LessonRoomManager();
    private static final ExamRoomManager examRoomManager = new ExamRoomManager();
    private static final WrittenTestsRoomManager writtenTestsRoomManager = new WrittenTestsRoomManager();

    public static void main(String[] args) {
        try {
            MetadataManager.init("build/WEB-INF/classes/domain_model.dml");
            SuportePersistenteOJB.fixDescriptors();
            RootDomainObject.init();

            ISuportePersistente persistentSupport = null;
            try {
                persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
                persistentSupport.iniciarTransaccao();
        	setPrivledges();
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

    private static void setPrivledges() {
	AccessControl.setUserView(new MockUserView());
    }

    private static void createManagerUser() {
        final Person person = new Person();
        person.setName("Fenix System Administrator");
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.PERSON));
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.MANAGER));
        person.setIdDocumentType(IDDocumentType.IDENTITY_CARD);
        person.setDocumentIdNumber(person.getIdInternal().toString());
        final User user = person.getUser();
        final Login login = user.readUserLoginIdentification();
        login.setPassword(PasswordEncryptor.encryptPassword("pass"));
        login.setActive(Boolean.TRUE);
        LoginAlias.createNewCustomLoginAlias(login, "admin");
        login.openLoginIfNecessary(RoleType.MANAGER);
        person.setIsPassInKerberos(Boolean.TRUE);
    }

    private static void clearData() {
        final RootDomainObject rootDomainObject = RootDomainObject.getInstance();

//        if (hasAnyAssociatedSummaries() || hasAnyAssociatedShiftProfessorship()
//                || hasAnySupportLessons() || hasAnyDegreeTeachingServices()
//                || hasAnyTeacherMasterDegreeServices()) {

        System.out.println("Deleting shift professorships.");
        for (final Set<ShiftProfessorship> shiftProfessorships = rootDomainObject.getShiftProfessorshipsSet(); !shiftProfessorships.isEmpty(); shiftProfessorships.iterator().next().delete());

        System.out.println("Deleting support lessons");
        for (final Set<SupportLesson> supportLessons = rootDomainObject.getSupportLessonsSet(); !supportLessons.isEmpty(); supportLessons.iterator().next().delete());

        System.out.println("Deleting teacher service items.");
        for (final Set<TeacherServiceItem> teacherServiceItems = rootDomainObject.getTeacherServiceItemsSet(); !teacherServiceItems.isEmpty(); teacherServiceItems.iterator().next().delete());

        System.out.println("Deleting teacher services.");
        for (final Set<TeacherService> teacherServices = rootDomainObject.getTeacherServicesSet(); !teacherServices.isEmpty(); teacherServices.iterator().next().delete());

        System.out.println("Deleting events.");
        for (final Set<Event> events = rootDomainObject.getAccountingEventsSet(); !events.isEmpty(); events.iterator().next().delete());

        System.out.println("Deleting service agreements.");
        for (final Set<ServiceAgreement> serviceAgreements = rootDomainObject.getServiceAgreementsSet(); !serviceAgreements.isEmpty(); serviceAgreements.iterator().next().delete());

        System.out.println("Deleting posting rules.");
        for (final Set<PostingRule> postingRules = rootDomainObject.getPostingRulesSet(); !postingRules.isEmpty(); postingRules.iterator().next().delete());

        System.out.println("Deleting service agreement templates.");
        for (final Set<ServiceAgreementTemplate> serviceAgreementTemplateSet = rootDomainObject.getServiceAgreementTemplatesSet(); !serviceAgreementTemplateSet.isEmpty(); serviceAgreementTemplateSet.iterator().next().delete());

        System.out.println("Deleting writtenEvaluation enrolments.");
        for (final Set<WrittenEvaluationEnrolment> writtenEvaluationEnrolment = rootDomainObject.getWrittenEvaluationEnrolmentsSet(); !writtenEvaluationEnrolment.isEmpty(); writtenEvaluationEnrolment.iterator().next().delete());

        System.out.println("Deleting students from shifts.");
        if (!rootDomainObject.getShiftsSet().isEmpty()) {
            for (final Iterator<Shift> shiftIterator = rootDomainObject.getShiftsSet().iterator(); !shiftIterator.hasNext(); ) {
                final Shift shift = shiftIterator.next();
                for (final Set<Registration> registrations = shift.getStudentsSet(); !registrations.isEmpty(); registrations.remove(registrations.iterator().next()));
            }
            for (final Shift shift : rootDomainObject.getShiftsSet()) {
                if (!shift.getStudentsSet().isEmpty()) {
                    shift.getStudentsSet().clear();
                }
            }
        }
        for (final Set<StudentGroup> studentGroups = rootDomainObject.getStudentGroupsSet(); !studentGroups.isEmpty(); studentGroups.iterator().next().delete());
        System.out.println("Deleting student data by execution year.");
        for (final Set<StudentDataByExecutionYear> data = rootDomainObject.getStudentDataByExecutionYearSet(); !data.isEmpty(); data.iterator().next().delete());
        System.out.println("Deleting attends.");
        for (final Set<Attends> attends = rootDomainObject.getAttendssSet(); !attends.isEmpty(); attends.iterator().next().delete());

        System.out.println("Deleting enrolment periods.");
        for (final Set<EnrolmentPeriod> enrolmentPeriods = rootDomainObject.getEnrolmentPeriodsSet(); !enrolmentPeriods.isEmpty(); enrolmentPeriods.iterator().next().delete());

        System.out.println("Deleting curriculum modules.");
        for (final Set<CurriculumModule> curriculumModules = rootDomainObject.getCurriculumModulesSet(); !curriculumModules.isEmpty(); curriculumModules.iterator().next().delete());
        System.out.println("Deleting student curricular plans.");
        for (final Set<StudentCurricularPlan> studentCurricularPlans = rootDomainObject.getStudentCurricularPlansSet(); !studentCurricularPlans.isEmpty(); studentCurricularPlans.iterator().next().delete());
        System.out.println("Deleting registrations.");
        for (final Set<Registration> registrations = rootDomainObject.getRegistrationsSet(); !registrations.isEmpty(); registrations.iterator().next().delete());
        System.out.println("Deleting students.");
        for (final Set<Student> students = rootDomainObject.getStudentsSet(); !students.isEmpty(); students.iterator().next().delete());

        System.out.println("Deleting school classes.");
        for (final Set<SchoolClass> schoolClasses = rootDomainObject.getSchoolClasssSet(); !schoolClasses.isEmpty(); schoolClasses.iterator().next().delete());
        System.out.println("Deleting lessons.");
        for (final Set<Lesson> lessons = rootDomainObject.getLessonsSet(); !lessons.isEmpty(); lessons.iterator().next().delete());
        System.out.println("Deleting shifts.");
        for (final Set<Shift> shifts = rootDomainObject.getShiftsSet(); !shifts.isEmpty(); shifts.iterator().next().delete());
        System.out.println("Deleting evaluation methods.");
        for (final Set<EvaluationMethod> evaluationMethods = rootDomainObject.getEvaluationMethodsSet(); !evaluationMethods.isEmpty(); evaluationMethods.iterator().next().delete());
        System.out.println("Deleting bibliographic references.");
        for (final Set<BibliographicReference> bibliographicReferences = rootDomainObject.getBibliographicReferencesSet(); !bibliographicReferences.isEmpty(); bibliographicReferences.iterator().next().delete());

        System.out.println("Deleting sites.");
        for (final Set<Site> sites = rootDomainObject.getSitesSet(); !sites.isEmpty(); sites.iterator().next().delete());
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

        System.out.println("Deleting coordinators.");
        for (final Set<Coordinator> coordinators = rootDomainObject.getCoordinatorsSet(); !coordinators.isEmpty(); coordinators.iterator().next().delete());
        System.out.println("Deleting teachers.");
        for (final Set<Teacher> teachers = rootDomainObject.getTeachersSet(); !teachers.isEmpty(); teachers.iterator().next().delete());

        for (final Iterator<Accountability> acountabilityIterator = RootDomainObject.getInstance().getAccountabilitysIterator(); acountabilityIterator.hasNext(); ) {
            final Accountability accountability = acountabilityIterator.next();
            if (accountability instanceof Contract) {
                acountabilityIterator.remove();
                accountability.delete();
            }
        }

        System.out.println("Deleting employees.");
        for (final Set<Employee> employees = rootDomainObject.getEmployeesSet(); !employees.isEmpty(); employees.iterator().next().delete());

        System.out.println("Deleting person roles.");
        for (final Party party : rootDomainObject.getPartysSet()) {
            if (party.isPerson()) {
                final Person person = (Person) party;
                person.getPersonRolesSet().clear();
            }
        }

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
        for (final Set<OccupationPeriod> occupationPeriods = rootDomainObject.getOccupationPeriodsSet(); !occupationPeriods.isEmpty(); occupationPeriods.iterator().next().delete());

        System.out.println("Deleting spaces.");
        for (final Set<Space> spaces = rootDomainObject.getSpacesSet(); !spaces.isEmpty(); spaces.iterator().next().delete());
        System.out.println("Deleting campi.");
        for (final Set<Campus> campi = rootDomainObject.getCampussSet(); !campi.isEmpty(); campi.iterator().next().delete());

        System.out.println("Deleting administrative offices.");
        for (final Set<AdministrativeOffice> administrativeOffices = rootDomainObject.getAdministrativeOfficesSet(); !administrativeOffices.isEmpty(); administrativeOffices.iterator().next().delete());

        System.out.println("Deleting administrative offices.");
        for (final Set<Department> departments = rootDomainObject.getDepartmentsSet(); !departments.isEmpty(); departments.iterator().next().delete());

        System.out.println("Completed clearing any existing data.");
        System.out.println("Loading the test data...");
    }

    private static void createTestData() {
        createUnits();
        createExecutionYears();
        createCampus();
        createRooms();
        createDegrees();
        createExecutionCourses();
        connectShiftsToSchoolClasses();
        createWrittenEvaluations();
        createStudents();
    }

    private static void createUnits() {
        final RootDomainObject rootDomainObject = RootDomainObject.getInstance();
        final Unit institutionUnit = rootDomainObject.getInstitutionUnit();
        institutionUnit.setName("Escola do Galo");
        institutionUnit.setAcronym("Fenix");
        institutionUnit.setType(PartyTypeEnum.DEPARTMENT);
        final UnitServiceAgreementTemplate unitServiceAgreementTemplate = new UnitServiceAgreementTemplate(institutionUnit);
        new FixedAmountPR(EntryType.INSURANCE_FEE, EventType.INSURANCE, new DateTime().minusYears(1), null, unitServiceAgreementTemplate, Money.valueOf(2));
        final AdministrativeOffice administrativeOfficeDegree = new AdministrativeOffice(AdministrativeOfficeType.DEGREE, institutionUnit);
        new AdministrativeOfficeServiceAgreementTemplate(administrativeOfficeDegree);
        final AdministrativeOffice administrativeOfficeMasterDegree = new AdministrativeOffice(AdministrativeOfficeType.MASTER_DEGREE, rootDomainObject.getExternalInstitutionUnit());
        new AdministrativeOfficeServiceAgreementTemplate(administrativeOfficeMasterDegree);
        new FixedAmountWithPenaltyFromDatePR(EntryType.ADMINISTRATIVE_OFFICE_FEE,
                    EventType.ADMINISTRATIVE_OFFICE_FEE, new DateTime(), null, administrativeOfficeDegree
                            .getServiceAgreementTemplate(), new Money("21"), new Money("10.50"),
                    new YearMonthDay(2006, 12, 16));
        new FixedAmountWithPenaltyFromDatePR(EntryType.ADMINISTRATIVE_OFFICE_FEE,
                EventType.ADMINISTRATIVE_OFFICE_FEE, new DateTime(), null, administrativeOfficeMasterDegree
                        .getServiceAgreementTemplate(), new Money("21"), new Money("10.50"),
                new YearMonthDay(2006, 12, 16));
        new AdministrativeOfficeFeeAndInsurancePR(new DateTime(), null, administrativeOfficeDegree
                .getServiceAgreementTemplate());
        new AdministrativeOfficeFeeAndInsurancePR(new DateTime(), null, administrativeOfficeMasterDegree
                .getServiceAgreementTemplate());
        final Department department = new Department();
        department.setDepartmentUnit(institutionUnit);
        department.setName("Department Name");
        department.setRealName("Xpto");
        department.setCode("Xpto");

        createAdminPostingRules(administrativeOfficeDegree.getServiceAgreementTemplate());
        createAdminPostingRules(administrativeOfficeMasterDegree.getServiceAgreementTemplate());
    }

    private static void createAdminPostingRules(AdministrativeOfficeServiceAgreementTemplate agreementTemplate) {
            new CertificateRequestPR(EntryType.SCHOOL_REGISTRATION_CERTIFICATE_REQUEST_FEE,
                    EventType.SCHOOL_REGISTRATION_CERTIFICATE_REQUEST, new DateTime(), null,
                    agreementTemplate, new Money("7"), new Money("0"), new Money("0.12"));

            new CertificateRequestPR(EntryType.ENROLMENT_CERTIFICATE_REQUEST_FEE,
                    EventType.ENROLMENT_CERTIFICATE_REQUEST, new DateTime(), null, agreementTemplate,
                    new Money("7"), new Money("1"), new Money("0.12"));

            new CertificateRequestPR(EntryType.APPROVEMENT_CERTIFICATE_REQUEST_FEE,
                    EventType.APPROVEMENT_CERTIFICATE_REQUEST, new DateTime(), null, agreementTemplate,
                    new Money("7"), new Money("1"), new Money("0.12"));

            new CertificateRequestPR(EntryType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST_FEE,
                    EventType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST, new DateTime(), null,
                    agreementTemplate, new Money("15"), new Money("1"), new Money("0.12"));
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
		oldRoom.setName("Sala" + b + r);
		oldRoom.setCapacidadeNormal(Integer.valueOf(50));
		oldRoom.setCapacidadeExame(Integer.valueOf(25));
		oldRoom.setPiso(Integer.valueOf(0));
		oldRoom.setTipo(new TipoSala(TipoSala.PLANA));
		lessonRoomManager.push(oldRoom);
		examRoomManager.add(oldRoom);
		writtenTestsRoomManager.add(oldRoom);
	    }
	}
    }

    private static Teacher createTeachers(final int i) {
        final Person person = createPerson("Guru Diplomado", "teacher", i);
        final Employee employee = new Employee(person, Integer.valueOf(i), Boolean.TRUE);
        final Teacher teacher = new Teacher(Integer.valueOf(i), person);
        person.addPersonRoleByRoleType(RoleType.EMPLOYEE);
        person.addPersonRoleByRoleType(RoleType.TEACHER);
        final Login login = person.getUser().readUserLoginIdentification();
        login.openLoginIfNecessary(RoleType.TEACHER);
        final Contract contractWorking = new Contract(person, new YearMonthDay().minusYears(2), new YearMonthDay().plusYears(2), RootDomainObject.getInstance().getInstitutionUnit(), ContractType.WORKING);
        final Contract contractSalary = new Contract(person, new YearMonthDay().minusYears(2), new YearMonthDay().plusYears(2), RootDomainObject.getInstance().getInstitutionUnit(), ContractType.SALARY);
        final Contract contractMailing = new Contract(person, new YearMonthDay().minusYears(2), new YearMonthDay().plusYears(2), RootDomainObject.getInstance().getInstitutionUnit(), ContractType.MAILING);
        person.addPersonRoleByRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE);
        person.addPersonRoleByRoleType(RoleType.TIME_TABLE_MANAGER);
        person.addPersonRoleByRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
        person.addPersonRoleByRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
        final Vigilant vigilant = new Vigilant(person, ExecutionYear.readCurrentExecutionYear());
        final VigilantGroup vigilantGroup;
        if (RootDomainObject.getInstance().getVigilantGroupsSet().isEmpty()) {
            vigilantGroup = new VigilantGroup();
            vigilantGroup.setName("Officers of the Law");
            vigilantGroup.setUnit(RootDomainObject.getInstance().getInstitutionUnit());
            vigilantGroup.setContactEmail("nowhere@nowhere.com");
            vigilantGroup.setRulesLink("http://www.google.com/");
            ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
            vigilantGroup.setExecutionYear(currentYear);
        } else {
            vigilantGroup = RootDomainObject.getInstance().getVigilantGroupsSet().iterator().next();
        }
        vigilantGroup.addVigilants(vigilant);
        return teacher;
    }

    private static Person createPerson(final String namePrefix, final String usernamePrefix, final int i) {
        final Person person = new Person();
        person.setName(namePrefix + i);
        person.addPersonRoles(Role.getRoleByRoleType(RoleType.PERSON));
        person.setDateOfBirthYearMonthDay(new YearMonthDay().minusYears(23));
        person.setIdDocumentType(IDDocumentType.IDENTITY_CARD);
        person.setDocumentIdNumber(person.getIdInternal().toString());
        person.setEmail("abc" + person.getIdInternal() + "@gmail.com");
        final User user = person.getUser();
        final Login login = user.readUserLoginIdentification();
        login.setPassword(PasswordEncryptor.encryptPassword("pass"));
        login.setActive(Boolean.TRUE);
        LoginAlias.createNewCustomLoginAlias(login, usernamePrefix + i);
        person.setIsPassInKerberos(Boolean.TRUE);
        return person;
    }

    private static void createStudents() {
	final RootDomainObject rootDomainObject = RootDomainObject.getInstance();
	int i = 1;
	for (final DegreeCurricularPlan degreeCurricularPlan : rootDomainObject.getDegreeCurricularPlansSet()) {
	    for (int k = 1; k <= 20; k++) {
		createStudent(degreeCurricularPlan, i++);
	    }
	}
    }

    private static void createStudent(final DegreeCurricularPlan degreeCurricularPlan, final int i) {
	final Person person = createPerson("Esponja de Informação", "student", i);
	final Student student = new Student(person, Integer.valueOf(i));
	//final Registration registration = new Registration(person, degreeCurricularPlan);
	final Registration registration = new Registration(person, Integer.valueOf(i));
	registration.setDegree(degreeCurricularPlan.getDegree());
	registration.setStudent(student);
	final StudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan(registration, degreeCurricularPlan, StudentCurricularPlanState.ACTIVE, new YearMonthDay().minusMonths(6));
	person.addPersonRoleByRoleType(RoleType.STUDENT);
        final Login login = person.getUser().readUserLoginIdentification();
        login.openLoginIfNecessary(RoleType.STUDENT);
        createStudentEnrolments(studentCurricularPlan);
        new DegreeCurricularPlanServiceAgreement(student.getPerson(), degreeCurricularPlan.getServiceAgreementTemplate());
        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        final AdministrativeOffice administrativeOffice = AdministrativeOffice.readByAdministrativeOfficeType(studentCurricularPlan.getDegreeType().getAdministrativeOfficeType());
        new GratuityEventWithPaymentPlan(administrativeOffice, student.getPerson(), studentCurricularPlan, executionYear);
        new AdministrativeOfficeFeeAndInsuranceEvent(administrativeOffice, student.getPerson(), executionYear);
        //new InsuranceEvent(student.getPerson(), executionYear);
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

        executionPeriod1.setInquiryResponseBeginDateTime(executionPeriod1.getBeginDateYearMonthDay().toDateTimeAtMidnight());
        executionPeriod1.setInquiryResponseEndDateTime(executionPeriod1.getEndDateYearMonthDay().toDateTimeAtMidnight());
        executionPeriod2.setInquiryResponseBeginDateTime(executionPeriod2.getBeginDateYearMonthDay().toDateTimeAtMidnight());
        executionPeriod2.setInquiryResponseEndDateTime(executionPeriod2.getEndDateYearMonthDay().toDateTimeAtMidnight());

        createOtherExecutionYears(executionYear, executionPeriod1, executionPeriod2, 1, "2005/2006");
        createOtherExecutionYears(executionYear, executionPeriod1, executionPeriod2, 2, "2004/2005");
        createOtherExecutionYears(executionYear, executionPeriod1, executionPeriod2, 3, "2003/2004");
        createOtherExecutionYears(executionYear, executionPeriod1, executionPeriod2, 4, "2002/2003");
        createOtherExecutionYears(executionYear, executionPeriod1, executionPeriod2, 5, "2001/2002");
        createOtherExecutionYears(executionYear, executionPeriod1, executionPeriod2, 6, "2000/2001");
    }

    private static void createOtherExecutionYears(final ExecutionYear executionYear, final ExecutionPeriod executionPeriod1, final ExecutionPeriod executionPeriod2, int i, final String yearString) {
        final ExecutionPeriod firstExecutionPeriod = findFirstExecutionPeriod();
        final ExecutionYear otherExecutionYear = new ExecutionYear();
        otherExecutionYear.setYear(yearString);
        otherExecutionYear.setState(PeriodState.NOT_OPEN);
        final ExecutionPeriod otherExecutionPeriod1 = new ExecutionPeriod();
        otherExecutionPeriod1.setSemester(Integer.valueOf(1));
        otherExecutionPeriod1.setExecutionYear(otherExecutionYear);
        otherExecutionPeriod1.setName("Semester 1");
        final ExecutionPeriod otherExecutionPeriod2 = new ExecutionPeriod();
        otherExecutionPeriod2.setSemester(Integer.valueOf(2));
        otherExecutionPeriod2.setExecutionYear(otherExecutionYear);
        otherExecutionPeriod2.setName("Semester 2");
        otherExecutionPeriod1.setNextExecutionPeriod(otherExecutionPeriod2);
        otherExecutionPeriod2.setNextExecutionPeriod(firstExecutionPeriod);

        otherExecutionYear.setBeginDateYearMonthDay(executionYear.getBeginDateYearMonthDay().minusYears(i));
        otherExecutionYear.setEndDateYearMonthDay(executionYear.getEndDateYearMonthDay().minusYears(i));
        otherExecutionPeriod1.setBeginDateYearMonthDay(executionPeriod1.getBeginDateYearMonthDay().minusYears(i));
        otherExecutionPeriod1.setEndDateYearMonthDay(executionPeriod1.getEndDateYearMonthDay().minusYears(i));
        otherExecutionPeriod2.setBeginDateYearMonthDay(executionPeriod2.getBeginDateYearMonthDay().minusYears(i));
        otherExecutionPeriod2.setEndDateYearMonthDay(executionPeriod2.getEndDateYearMonthDay().minusYears(i));
        otherExecutionPeriod1.setState(PeriodState.NOT_OPEN);
        otherExecutionPeriod2.setState(PeriodState.NOT_OPEN);

//        new OccupationPeriod(otherExecutionPeriod1.getBeginDateYearMonthDay(), executionPeriod1.getEndDateYearMonthDay().minusDays(32));
//        new OccupationPeriod(execuotherotherExecutionPeriodEndDateYearMonthDay().minusDays(31), otherExecutionPeriod1.getEndDateYearMonthDay());
//        new OccupationPeriod(otherExecutionPeriod2.getBeginDateYearMonthDay(), otherExecutionPeriod2.getEndDateYearMonthDay().minusDays(32));
//        new OccupationPeriod(otherExecutionPeriod2.getEndDateYearMonthDay().minusDays(31), otherExecutionPeriod2.getEndDateYearMonthDay());
//        new OccupationPeriod(otherExecutionPeriod2.getEndDateYearMonthDay().plusDays(31), otherExecutionPeriod2.getEndDateYearMonthDay().plusDays(46));
//
//        otherExecutionPeriod1.setInquiryResponseBeginDateTime(otherExecutionPeriod1.getBeginDateYearMonthDay().toDateTimeAtMidnight());
//        otherExecutionPeriod1.setInquiryResponseEndDateTime(otherExecutionPeriod1.getEndDateYearMonthDay().toDateTimeAtMidnight());
//        otherExecutionPeriod2.setInquiryResponseBeginDateTime(otherExecutionPeriod2.getBeginDateYearMonthDay().toDateTimeAtMidnight());
//        otherExecutionPeriod2.setInquiryResponseEndDateTime(otherExecutionPeriod2.getEndDateYearMonthDay().toDateTimeAtMidnight());
    }

    private static ExecutionPeriod findFirstExecutionPeriod() {
        for (final ExecutionPeriod executionPeriod : RootDomainObject.getInstance().getExecutionPeriodsSet()) {
            if (executionPeriod.getPreviousExecutionPeriod() == null) {
                return executionPeriod;
            }
        }
        return null;
    }

    private static void createCampus() {
        final Campus campus = new Campus();
        campus.setName("Herdade do Conhecimento");
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
                degree = new Degree("Agricultura do Conhecimento", "Knowledge Agriculture", "CODE" + i, degreeType, 0d, gradeScale, null);
                degreeCurricularPlan = degree.createBolonhaDegreeCurricularPlan("DegreeCurricularPlanName", gradeScale, person);
                degreeCurricularPlan.setCurricularStage(CurricularStage.PUBLISHED);
            } else {
                degree = new Degree("Agricultura do Conhecimento", "Knowledge Agriculture", "CODE" + i, degreeType, gradeScale, DegreeCurricularPlan.class.getName());
                degreeCurricularPlan = degree.createPreBolonhaDegreeCurricularPlan("DegreeCurricularPlanName", DegreeCurricularPlanState.ACTIVE,
                        new Date(), null, degreeType.getYears(), Integer.valueOf(1), Double.valueOf(degreeType.getDefaultEctsCredits()),
                        MarkType.TYPE20_OBJ, Integer.valueOf(100), null, gradeScale);
                final Branch branch = new Branch("", "", "", degreeCurricularPlan);
                branch.setBranchType(BranchType.COMNBR);
                createPreBolonhaCurricularCourses(degreeCurricularPlan, i, executionYear, branch);
            }


            final Unit unit = RootDomainObject.getInstance().getInstitutionUnit();
            final Department department = unit.getDepartment();
            department.addDegrees(degree);
            degree.setUnit(unit);

            createDegreeInfo(degree);
            degreeCurricularPlan.setDescription("Bla bla bla. Descrição do plano curricular do curso. Bla bla bla");
            degreeCurricularPlan.setDescriptionEn("Blur ble bla. Description of the degrees curricular plan. Goo goo foo foo.");


            final ExecutionDegree executionDegree = degreeCurricularPlan.createExecutionDegree(executionYear, campus, Boolean.FALSE);
            final Teacher teacher = createTeachers(i);
            new Coordinator(executionDegree, teacher.getPerson(), Boolean.TRUE);
            createPeriodsForExecutionDegree(executionDegree);
            createSchoolClasses(executionDegree);
            createEnrolmentPeriods(degreeCurricularPlan, executionYear);
            executionDegree.setCampus(RootDomainObject.getInstance().getCampussSet().iterator().next());

            createAgreementsAndPostingRules(executionYear, degreeCurricularPlan);
        }
    }

    private static void createAgreementsAndPostingRules(final ExecutionYear executionYear, final DegreeCurricularPlan degreeCurricularPlan) {
        new DegreeCurricularPlanServiceAgreementTemplate(degreeCurricularPlan);
        

        final GratuityPaymentPlan gratuityPaymentPlan = new FullGratuityPaymentPlan(executionYear, degreeCurricularPlan.getServiceAgreementTemplate(), true);

        new InstallmentWithMonthlyPenalty(gratuityPaymentPlan, new Money("350"),
                    executionYear.getBeginDateYearMonthDay(),
                    new YearMonthDay(2006, 12, 15), new BigDecimal("0.01"),
                    new YearMonthDay(2007, 1, 1), 9);

        new InstallmentWithMonthlyPenalty(gratuityPaymentPlan, new Money("570.17"),
                    new YearMonthDay(2006, 12, 16), new YearMonthDay(2007, 5, 31),
                    new BigDecimal("0.01"), new YearMonthDay(2007, 6, 1), 4);

        final GratuityPaymentPlan gratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester = new GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester(
                    executionYear, degreeCurricularPlan.getServiceAgreementTemplate());

        new InstallmentWithMonthlyPenalty(
                    gratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester, new Money("920.17"), executionYear.getBeginDateYearMonthDay(),
        new YearMonthDay(2007, 5, 31), new BigDecimal("0.01"), new YearMonthDay(2007, 06, 1), 4);

        new GratuityWithPaymentPlanPR(EntryType.GRATUITY_FEE, EventType.GRATUITY, new DateTime(), null, degreeCurricularPlan.getServiceAgreementTemplate());
    }

    private static void createEnrolmentPeriods(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionYear executionYear) {
	for (final ExecutionPeriod executionPeriod : executionYear.getExecutionPeriodsSet()) {
	    final Date start = executionPeriod.getBeginDateYearMonthDay().toDateMidnight().toDate();
	    final Date end = executionPeriod.getEndDateYearMonthDay().toDateMidnight().toDate();

	    new EnrolmentPeriodInClasses(degreeCurricularPlan, executionPeriod, start, end);
	    new EnrolmentPeriodInCurricularCourses(degreeCurricularPlan, executionPeriod, start, end);
	    new EnrolmentPeriodInCurricularCoursesSpecialSeason(degreeCurricularPlan, executionPeriod, start, end);
	}
    }

    private static void createSchoolClasses(final ExecutionDegree executionDegree) {
	final ExecutionYear executionYear = executionDegree.getExecutionYear();
	final Degree degree = executionDegree.getDegree();
	final DegreeType degreeType = degree.getTipoCurso();
	for (final ExecutionPeriod executionPeriod : executionYear.getExecutionPeriodsSet()) {
	    for (int y = 1; y <= degreeType.getYears(); y++) {
		for (int i = 1; i <= 3 ; i++) {
		    final String name = degreeType.isBolonhaType() ? Integer.toString(i) : degree.getSigla() + y + i;
		    new SchoolClass(executionDegree, executionPeriod, name, Integer.valueOf(y));
		}
	    }
	}
    }

    private static void createPeriodsForExecutionDegree(ExecutionDegree executionDegree) {
        final ExecutionYear executionYear = executionDegree.getExecutionYear();
        final ExecutionPeriod executionPeriod1 = executionYear.getFirstExecutionPeriod();
        final ExecutionPeriod executionPeriod2 = executionYear.getLastExecutionPeriod();

        final OccupationPeriod occupationPeriod1 = OccupationPeriod.readOccupationPeriod(executionPeriod1.getBeginDateYearMonthDay(), executionPeriod1.getEndDateYearMonthDay().minusDays(32));
        final OccupationPeriod occupationPeriod2 = OccupationPeriod.readOccupationPeriod(executionPeriod1.getEndDateYearMonthDay().minusDays(31), executionPeriod1.getEndDateYearMonthDay());
        final OccupationPeriod occupationPeriod3 = OccupationPeriod.readOccupationPeriod(executionPeriod2.getBeginDateYearMonthDay(), executionPeriod2.getEndDateYearMonthDay().minusDays(32));
        final OccupationPeriod occupationPeriod4 = OccupationPeriod.readOccupationPeriod(executionPeriod2.getEndDateYearMonthDay().minusDays(31), executionPeriod2.getEndDateYearMonthDay());
        final OccupationPeriod occupationPeriod5 = OccupationPeriod.readOccupationPeriod(executionPeriod2.getEndDateYearMonthDay().plusDays(31), executionPeriod2.getEndDateYearMonthDay().plusDays(46));

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
        executionCourse.setAvailableForInquiries(Boolean.TRUE);
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
        createShiftProfessorhips(executionCourse);
    }

    private static void createShiftProfessorhips(final ExecutionCourse executionCourse) {
        final ExecutionPeriod executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
        for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
            final Teacher teacher = professorship.getTeacher();
            for (final Shift shift : executionCourse.getAssociatedShiftsSet()) {
                if ((professorship.isResponsibleFor() && shift.getTipo() == ShiftType.TEORICA)
                        || (!professorship.isResponsibleFor() && shift.getTipo() != ShiftType.TEORICA)) {
                    final ShiftProfessorship shiftProfessorship = new ShiftProfessorship();
                    shiftProfessorship.setShift(shift);
                    shiftProfessorship.setProfessorship(professorship);
                    shiftProfessorship.setPercentage(Double.valueOf(100));
                    TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
                    if (teacherService == null) {
                        teacherService = new TeacherService(teacher, executionPeriod);
                    }
                    final DegreeTeachingService degreeTeachingService = new DegreeTeachingService(teacherService, professorship, shift, Double.valueOf(100), RoleType.SCIENTIFIC_COUNCIL);

                    final SupportLessonDTO supportLessonDTO = new SupportLessonDTO();
                    supportLessonDTO.setProfessorshipID(professorship.getIdInternal());
                    supportLessonDTO.setPlace("Room23");
                    supportLessonDTO.setStartTime(new DateTime().withField(DateTimeFieldType.hourOfDay(), 20).toDate());
                    supportLessonDTO.setEndTime(new DateTime().withField(DateTimeFieldType.hourOfDay(), 21).toDate());
                    supportLessonDTO.setWeekDay(new DiaSemana(DiaSemana.SABADO));
                    final SupportLesson supportLesson = SupportLesson.create(supportLessonDTO, professorship, RoleType.SCIENTIFIC_COUNCIL);
                }
            }
        }
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
	final HourMinuteSecond start = lessonRoomManager.getNextHourMinuteSecond(durationInMinutes);
	final HourMinuteSecond end = start.plusMinutes(durationInMinutes);
	final Calendar cStart = toCalendar(start);
	final Calendar cEnd = toCalendar(end);
	final DiaSemana diaSemana = new DiaSemana(lessonRoomManager.getNextWeekDay());
	final OldRoom oldRoom = lessonRoomManager.getNextOldRoom();
	final ExecutionPeriod executionPeriod = shift.getDisciplinaExecucao().getExecutionPeriod();
	final OccupationPeriod occupationPeriod = OccupationPeriod.readOccupationPeriod(executionPeriod.getBeginDateYearMonthDay(), executionPeriod.getEndDateYearMonthDay().minusDays(32));
	final RoomOccupation roomOccupation = new RoomOccupation(oldRoom, cStart, cEnd, diaSemana, 1, occupationPeriod);              
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
		final CurricularCourse curricularCourse = degreeCurricularPlan.createCurricularCourse("Germinação do Conhecimento" + x, "C" + x, "D" + x, Boolean.TRUE, CurricularStage.OLD);
                curricularCourse.setNameEn("Knowledge Germination" + x);
		curricularCourse.setType(CurricularCourseType.NORMAL_COURSE);
                curricularCourse.setTheoreticalHours(Double.valueOf(3d));
                curricularCourse.setPraticalHours(Double.valueOf(2d));
                curricularCourse.setMinimumValueForAcumulatedEnrollments(Integer.valueOf(1));
                curricularCourse.setMaximumValueForAcumulatedEnrollments(Integer.valueOf(2));
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

    private static void connectShiftsToSchoolClasses() {
	final RootDomainObject rootDomainObject = RootDomainObject.getInstance();
	for (final CurricularCourseScope curricularCourseScope : rootDomainObject.getCurricularCourseScopesSet()) {
	    final CurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
	    final CurricularYear curricularYear = curricularSemester.getCurricularYear();
	    final CurricularCourse curricularCourse = curricularCourseScope.getCurricularCourse();
	    final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
	    final ExecutionCourse executionCourse = curricularCourse.getAssociatedExecutionCoursesSet().iterator().next();
	    final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreesSet().iterator().next();
	    for (final Shift shift : executionCourse.getAssociatedShiftsSet()) {
		for (final SchoolClass schoolClass : executionDegree.getSchoolClassesSet()) {
		    if (schoolClass.getExecutionPeriod() == executionCourse.getExecutionPeriod()
			    && schoolClass.getAnoCurricular().intValue() == curricularYear.getYear().intValue()) {
			schoolClass.addAssociatedShifts(shift);
		    }
		}
	    }
	}
	// TODO : do the same for bolonha structure.
    }

    private static void createWrittenEvaluations() {
	final RootDomainObject rootDomainObject = RootDomainObject.getInstance();
	for (final ExecutionPeriod executionPeriod : rootDomainObject.getExecutionPeriodsSet()) {
	    createWrittenEvaluations(executionPeriod, new Season(Season.SEASON1), "Teste1");
            for (int i = 0; i++ < 500 ; writtenTestsRoomManager.getNextDateTime(executionPeriod));
	    createWrittenEvaluations(executionPeriod, new Season(Season.SEASON2), "Teste2");
	}
    }

    private static void createWrittenEvaluations(final ExecutionPeriod executionPeriod, final Season season, final String writtenTestName) {
	for (final ExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCoursesSet()) {
	    createWrittenEvaluation(executionPeriod, executionCourse, writtenTestName);
	    createExam(executionPeriod, executionCourse, season);
	}
    }

    private static void createWrittenEvaluation(final ExecutionPeriod executionPeriod, final ExecutionCourse executionCourse, final String name) {
	final DateTime startDateTime = writtenTestsRoomManager.getNextDateTime(executionPeriod);
	final DateTime endDateTime = startDateTime.plusMinutes(120);
	final OldRoom oldRoom = writtenTestsRoomManager.getNextOldRoom(executionPeriod);
	final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
	executionCourses.add(executionCourse);
	final List<DegreeModuleScope> degreeModuleScopes = new ArrayList<DegreeModuleScope>();
	for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
	    degreeModuleScopes.addAll(curricularCourse.getDegreeModuleScopes());
	}
	final List<OldRoom> oldRooms = new ArrayList<OldRoom>();
	oldRooms.add(oldRoom);
	final OccupationPeriod occupationPeriod = new OccupationPeriod(startDateTime.toYearMonthDay(), endDateTime.toYearMonthDay());
	final WrittenTest writtenTest = new WrittenTest(startDateTime.toDate(), startDateTime.toDate(), endDateTime.toDate(), executionCourses, degreeModuleScopes, oldRooms, occupationPeriod, name);
        createWrittenEvaluationEnrolmentPeriodAndVigilancies(executionPeriod, writtenTest, executionCourse);
    }

    private static void createExam(final ExecutionPeriod executionPeriod, final ExecutionCourse executionCourse, final Season season) {
	final DateTime startDateTime = examRoomManager.getNextDateTime(executionPeriod);
	final DateTime endDateTime = startDateTime.plusMinutes(180);
	final OldRoom oldRoom = examRoomManager.getNextOldRoom(executionPeriod);
	final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
	executionCourses.add(executionCourse);
	final List<DegreeModuleScope> degreeModuleScopes = new ArrayList<DegreeModuleScope>();
	for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
	    degreeModuleScopes.addAll(curricularCourse.getDegreeModuleScopes());
	}
	final List<OldRoom> oldRooms = new ArrayList<OldRoom>();
	oldRooms.add(oldRoom);
	final OccupationPeriod occupationPeriod = new OccupationPeriod(startDateTime.toYearMonthDay(), endDateTime.toYearMonthDay());
	final Exam exam = new Exam(startDateTime.toDate(), startDateTime.toDate(), endDateTime.toDate(), executionCourses, degreeModuleScopes, oldRooms, occupationPeriod, season);
        createWrittenEvaluationEnrolmentPeriodAndVigilancies(executionPeriod, exam, executionCourse);
    }

    private static void createWrittenEvaluationEnrolmentPeriodAndVigilancies(final ExecutionPeriod executionPeriod, final WrittenEvaluation writtenEvaluation, final ExecutionCourse executionCourse) {
        writtenEvaluation.setEnrollmentBeginDayDateYearMonthDay(executionPeriod.getBeginDateYearMonthDay());
        writtenEvaluation.setEnrollmentBeginTimeDateHourMinuteSecond(new HourMinuteSecond(0, 0, 0));
        writtenEvaluation.setEnrollmentEndDayDateYearMonthDay(writtenEvaluation.getDayDateYearMonthDay().minusDays(1));
        writtenEvaluation.setEnrollmentEndTimeDateHourMinuteSecond(new HourMinuteSecond(0, 0, 0));

        final YearMonthDay yearMonthDay = writtenEvaluation.getDayDateYearMonthDay();
        writtenEvaluation.setDayDateYearMonthDay(new YearMonthDay().plusDays(1));
        final Vigilancy vigilancy = new VigilancyWithCredits(writtenEvaluation);
        writtenEvaluation.setDayDateYearMonthDay(yearMonthDay);
        for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
            final Vigilant vigilant = professorship.getTeacher().getPerson().getVigilantForGivenExecutionYear(executionPeriod.getExecutionYear());
            vigilant.addVigilancys(vigilancy);
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

    private static void createStudentEnrolments(final StudentCurricularPlan studentCurricularPlan) {
	final ExecutionPeriod executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
	if (studentCurricularPlan.isBolonha()) {
	    
	} else {
	    final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
	    for (final DegreeModuleScope degreeModuleScope : degreeCurricularPlan.getDegreeModuleScopes()) {
		if (degreeModuleScope.getCurricularYear().intValue() == 1 && degreeModuleScope.getCurricularSemester() == executionPeriod.getSemester().intValue()) {
		    if (degreeModuleScope.isActiveForExecutionPeriod(executionPeriod)) {
			final Enrolment enrolment = new Enrolment(studentCurricularPlan, degreeModuleScope.getCurricularCourse(), executionPeriod, EnrollmentCondition.FINAL, null);
			final Attends attends = getAttendsFor(enrolment, executionPeriod);
			createStudentShifts(attends);
		    }
		}
	    }
	}
    }

    private static Attends getAttendsFor(final Enrolment enrolment, final ExecutionPeriod executionPeriod) {
	for (final Attends attends : enrolment.getAttendsSet()) {
	    if (attends.getExecutionCourse().getExecutionPeriod() == executionPeriod) {
		return attends;
	    }
	}
	return null;
    }

    private static void createStudentShifts(final Attends attends) {
	final ExecutionCourse executionCourse = attends.getExecutionCourse();
	for (final Shift shift : executionCourse.getAssociatedShiftsSet()) {
	    if (!isEnroledInShift(attends, shift.getTipo())) {
		shift.reserveForStudent(attends.getRegistration());
	    }
	}
    }

    private static boolean isEnroledInShift(final Attends attends, final ShiftType shiftType) {
	final ExecutionCourse executionCourse = attends.getExecutionCourse();
	for (final Shift shift : attends.getRegistration().getShiftsSet()) {
	    if (shift.getTipo() == shiftType && shift.getDisciplinaExecucao() == executionCourse) {
		return true;
	    }
	}
	return false;
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

    private static class MockUserView implements IUserView {

	public DateTime getExpirationDate() {
	    return null;
}
	public String getFullName() {
	    return null;
	}

	public Person getPerson() {
	    return null;
	}

	public Collection<RoleType> getRoleTypes() {
	    return null;
	}

	public String getUtilizador() {
	    return null;
	}

	public boolean hasRoleType(RoleType roleType) {
	    return true;
	}

	public boolean isPublicRequester() {
	    return false;
	}	
    }

}