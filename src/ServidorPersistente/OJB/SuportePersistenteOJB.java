/*
 * SuportePersistenteOJB.java
 * 
 * Created on 19 de Agosto de 2002, 1:18
 */

package ServidorPersistente.OJB;

/**
 * @author ars
 */

import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.cache.CacheFilterRegistry;
import org.apache.ojb.odmg.HasBroker;
import org.apache.ojb.odmg.OJB;
import org.odmg.Database;
import org.odmg.Implementation;
import org.odmg.ODMGException;
import org.odmg.ODMGRuntimeException;
import org.odmg.Transaction;

import pt.utl.ist.berserk.storage.ITransactionBroker;
import pt.utl.ist.berserk.storage.exceptions.StorageException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;
import ServidorPersistente.IPersistentCurricularCourseGroup;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentAdvisory;
import ServidorPersistente.IPersistentAnnouncement;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentCandidateEnrolment;
import ServidorPersistente.IPersistentCandidateSituation;
import ServidorPersistente.IPersistentContributor;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.IPersistentCountry;
import ServidorPersistente.IPersistentCreditsTeacher;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurricularCourseEquivalence;
import ServidorPersistente.IPersistentCurricularCourseEquivalenceRestriction;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.IPersistentCurricularYear;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentDegreeCurricularPlanEnrolmentInfo;
import ServidorPersistente.IPersistentDegreeInfo;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEquivalence;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentEnrolmentPeriod;
import ServidorPersistente.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentEvaluationExecutionCourse;
import ServidorPersistente.IPersistentEvaluationMethod;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentExamExecutionCourse;
import ServidorPersistente.IPersistentExamStudentRoom;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentExternalPerson;
import ServidorPersistente.IPersistentGratuity;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentGuide;
import ServidorPersistente.IPersistentGuideEntry;
import ServidorPersistente.IPersistentGuideSituation;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentMasterDegreeCandidate;
import ServidorPersistente.IPersistentMasterDegreeProofVersion;
import ServidorPersistente.IPersistentMasterDegreeThesis;
import ServidorPersistente.IPersistentMasterDegreeThesisDataVersion;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.IPersistentPersonRole;
import ServidorPersistente.IPersistentPossibleCurricularCourseForOptionalCurricularCourse;
import ServidorPersistente.IPersistentPrecedence;
import ServidorPersistente.IPersistentPrice;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentQualification;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentRestriction;
import ServidorPersistente.IPersistentRole;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentShiftProfessorship;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.IPersistentStudentKind;
import ServidorPersistente.IPersistentStudentTestLog;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.IPersistentUniversity;
import ServidorPersistente.IPersistentWebSite;
import ServidorPersistente.IPersistentWebSiteItem;
import ServidorPersistente.IPersistentWebSiteSection;
import ServidorPersistente.IPersistentWorkLocation;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISalaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurmaTurnoPersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.Seminaries.CandidacyOJB;
import ServidorPersistente.OJB.Seminaries.CaseStudyChoiceOJB;
import ServidorPersistente.OJB.Seminaries.CaseStudyOJB;
import ServidorPersistente.OJB.Seminaries.EquivalencyOJB;
import ServidorPersistente.OJB.Seminaries.ModalityOJB;
import ServidorPersistente.OJB.Seminaries.ThemeOJB;
import ServidorPersistente.OJB.degree.finalProject.TeacherDegreeFinalProjectStudentOJB;
import ServidorPersistente.OJB.gaugingTests.physics.GaugingTestResultOJB;
import ServidorPersistente.OJB.gaugingTests.physics.IPersistentGaugingTestResult;
import ServidorPersistente.OJB.gesdis.CourseReportOJB;
import ServidorPersistente.OJB.grant.contract.GrantContractOJB;
import ServidorPersistente.OJB.grant.contract.GrantOrientationTeacherOJB;
import ServidorPersistente.OJB.grant.contract.GrantResponsibleTeacherOJB;
import ServidorPersistente.OJB.grant.contract.GrantTypeOJB;
import ServidorPersistente.OJB.grant.owner.GrantOwnerOJB;
import ServidorPersistente.OJB.guide.ReimbursementGuideOJB;
import ServidorPersistente.OJB.guide.ReimbursementGuideSituationOJB;
import ServidorPersistente.OJB.person.qualification.QualificationOJB;
import ServidorPersistente.OJB.places.campus.CampusOJB;
import ServidorPersistente.OJB.teacher.CareerOJB;
import ServidorPersistente.OJB.teacher.CategoryOJB;
import ServidorPersistente.OJB.teacher.ExternalActivityOJB;
import ServidorPersistente.OJB.teacher.OldPublicationOJB;
import ServidorPersistente.OJB.teacher.OrientationOJB;
import ServidorPersistente.OJB.teacher.PublicationsNumberOJB;
import ServidorPersistente.OJB.teacher.ServiceProviderRegimeOJB;
import ServidorPersistente.OJB.teacher.WeeklyOcupationOJB;
import ServidorPersistente.OJB.teacher.professorship.ShiftProfessorshipOJB;
import ServidorPersistente.OJB.teacher.professorship.SupportLessonOJB;
import ServidorPersistente.OJB.teacher.workingTime.TeacherInstitutionWorkingTimeOJB;
import ServidorPersistente.Seminaries.IPersistentSeminary;
import ServidorPersistente.Seminaries.IPersistentSeminaryCandidacy;
import ServidorPersistente.Seminaries.IPersistentSeminaryCaseStudy;
import ServidorPersistente.Seminaries.IPersistentSeminaryCaseStudyChoice;
import ServidorPersistente.Seminaries.IPersistentSeminaryCurricularCourseEquivalency;
import ServidorPersistente.Seminaries.IPersistentSeminaryModality;
import ServidorPersistente.Seminaries.IPersistentSeminaryTheme;
import ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;
import ServidorPersistente.gesdis.IPersistentCourseReport;
import ServidorPersistente.grant.IPersistentGrantContract;
import ServidorPersistente.grant.IPersistentGrantOrientationTeacher;
import ServidorPersistente.grant.IPersistentGrantOwner;
import ServidorPersistente.grant.IPersistentGrantResponsibleTeacher;
import ServidorPersistente.grant.IPersistentGrantType;
import ServidorPersistente.guide.IPersistentReimbursementGuide;
import ServidorPersistente.guide.IPersistentReimbursementGuideSituation;
import ServidorPersistente.places.campus.IPersistentCampus;
import ServidorPersistente.teacher.IPersistentCareer;
import ServidorPersistente.teacher.IPersistentCategory;
import ServidorPersistente.teacher.IPersistentExternalActivity;
import ServidorPersistente.teacher.IPersistentOldPublication;
import ServidorPersistente.teacher.IPersistentOrientation;
import ServidorPersistente.teacher.IPersistentPublicationsNumber;
import ServidorPersistente.teacher.IPersistentServiceProviderRegime;
import ServidorPersistente.teacher.IPersistentWeeklyOcupation;
import ServidorPersistente.teacher.professorship.IPersistentSupportLesson;
import ServidorPersistente.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;

public class SuportePersistenteOJB implements ISuportePersistente, ITransactionBroker {
    Implementation _odmg = null;
    private static SuportePersistenteOJB _instance = null;

	protected Implementation getImplementation() {
        return _odmg;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#clearCache()
	 */
    public void clearCache()
    {
        if (_odmg != null)
        {
            HasBroker hasBroker = ((HasBroker) _odmg.currentTransaction());
            if (hasBroker != null)
            {
                hasBroker.getBroker().clearCache();
            }
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#clearCache()
	 */
    public Integer getNumberCachedItems()
    {
        Integer numberCachedObjects = null;

        if (_odmg != null)
        {
            HasBroker hasBroker = ((HasBroker) _odmg.currentTransaction());
            if (hasBroker != null)
            {
                PersistenceBroker broker = hasBroker.getBroker();

                System.out.println("###########################################33");
                System.out.println(
                    "broker.serviceObjectCache().class= " + broker.serviceObjectCache().getClass());

                CacheFilterRegistry cacheFilter = (CacheFilterRegistry) broker.serviceObjectCache();

                System.out.println("###########################################33");
                System.out.println(
                    "broker.serviceObjectCache().class= "
                        + cacheFilter.getCache(null, null, CacheFilterRegistry.METHOD_LOOKUP));

                FenixObjectCacheDefaultImpl cache =
                    (FenixObjectCacheDefaultImpl) cacheFilter.getCache(
                        null,
                        null,
                        CacheFilterRegistry.METHOD_LOOKUP);

                numberCachedObjects = cache.getNumberOfCachedObjects();
            }
        }

        return numberCachedObjects;
    }

    public static synchronized SuportePersistenteOJB getInstance() throws ExcepcaoPersistencia
    {
        if (_instance == null)
        {
            _instance = new SuportePersistenteOJB();
        }
        return _instance;
    }

    public static synchronized void resetInstance()
    {
        if (_instance != null)
        {
            PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.clearCache();
            _instance = null;
        }
    }

    /** Creates a new instance of SuportePersistenteOJB */
    private SuportePersistenteOJB() throws ExcepcaoPersistencia
    {
        init();
    }

    private void init() throws ExcepcaoPersistencia
    {

        _odmg = OJB.getInstance();
        try
        {
            openDatabase();
        }
        catch (ODMGException e)
        {
            throw new ExcepcaoPersistencia();
        }
    }

    protected void finalize() throws Throwable
    {
    }

    public void iniciarTransaccao() throws ExcepcaoPersistencia
    {
        try
        {
            openDatabase();
            Transaction tx = _odmg.newTransaction();
            tx.begin();
        }
        catch (ODMGException ex1)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.OPEN_DATABASE, ex1);
        }
        catch (ODMGRuntimeException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.BEGIN_TRANSACTION, ex);
        }
    }

    private void openDatabase() throws ODMGException
    {
        Database db = _odmg.getDatabase(null);
        if (db == null)
        {
            db = _odmg.newDatabase();
            db.open("OJB/repository.xml", Database.OPEN_READ_WRITE);
        }
    }

    public void confirmarTransaccao() throws ExcepcaoPersistencia
    {
        try
        {
            openDatabase();
            Transaction tx = _odmg.currentTransaction();

            if (tx == null)
                System.out.println("SuportePersistente.OJB - Nao ha transaccao activa");
            else
            {
                tx.commit();
                //				Database db =_odmg.getDatabase(null);
                //				if (db!= null) db.close();
            }
        }
        catch (ODMGException ex1)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.CLOSE_DATABASE, ex1);
        }
        catch (ODMGRuntimeException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.COMMIT_TRANSACTION, ex);
        }
    }

    public void cancelarTransaccao() throws ExcepcaoPersistencia
    {
        try
        {
            openDatabase();
            Transaction tx = _odmg.currentTransaction();

            if (tx != null)
            {
                tx.abort();
                //					_odmg.getDatabase(null).close();
            }

        }
        catch (ODMGException ex1)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.CLOSE_DATABASE, ex1);
        }
        catch (ODMGRuntimeException ex)
        {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.ABORT_TRANSACTION, ex);
        }
    }

    public IAulaPersistente getIAulaPersistente()
    {
        return new AulaOJB();
    }
    public ISalaPersistente getISalaPersistente()
    {
        return new SalaOJB();
    }
    public ITurmaPersistente getITurmaPersistente()
    {
        return new TurmaOJB();
    }
    public ITurnoPersistente getITurnoPersistente()
    {
        return new TurnoOJB();
    }

    public IFrequentaPersistente getIFrequentaPersistente()
    {
        return new FrequentaOJB();
    }
    public IPersistentEnrolment getIPersistentEnrolment()
    {
        return new EnrolmentOJB();
    }
    public IPersistentEnrolmentEvaluation getIPersistentEnrolmentEvaluation()
    {
        return new EnrolmentEvaluationOJB();
    }
    public ITurmaTurnoPersistente getITurmaTurnoPersistente()
    {
        return new TurmaTurnoOJB();
    }
    public ITurnoAlunoPersistente getITurnoAlunoPersistente()
    {
        return new TurnoAlunoOJB();
    }
    public ITurnoAulaPersistente getITurnoAulaPersistente()
    {
        return new TurnoAulaOJB();
    }

    public IPersistentCurricularCourse getIPersistentCurricularCourse()
    {
        return new CurricularCourseOJB();
    }
    public IPersistentExecutionCourse getIPersistentExecutionCourse()
    {
        return new ExecutionCourseOJB();
    }
    public IPersistentCountry getIPersistentCountry()
    {
        return new CountryOJB();
    }
    public IPessoaPersistente getIPessoaPersistente()
    {
        return new PessoaOJB();
    }

    public ICursoPersistente getICursoPersistente()
    {
        return new CursoOJB();
    }
    public ICursoExecucaoPersistente getICursoExecucaoPersistente()
    {
        return new CursoExecucaoOJB();
    }

    public IPersistentStudent getIPersistentStudent()
    {
        return new StudentOJB();
    }

    public IPersistentDepartment getIDepartamentoPersistente()
    {
        return new DepartmentOJB();
    }
    public IDisciplinaDepartamentoPersistente getIDisciplinaDepartamentoPersistente()
    {
        return new DisciplinaDepartamentoOJB();
    }
    public IPersistentDegreeCurricularPlan getIPersistentDegreeCurricularPlan()
    {
        return new DegreeCurricularPlanOJB();
    }
    public IStudentCurricularPlanPersistente getIStudentCurricularPlanPersistente()
    {
        return new StudentCurricularPlanOJB();
    }

    public IPersistentMasterDegreeCandidate getIPersistentMasterDegreeCandidate()
    {
        return new MasterDegreeCandidateOJB();
    }
    public IPersistentCandidateSituation getIPersistentCandidateSituation()
    {
        return new CandidateSituationOJB();
    }

    public IPersistentBibliographicReference getIPersistentBibliographicReference()
    {
        return new BibliographicReferenceOJB();
    }

    /**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentExecutionPeriod()
	 */
    public IPersistentExecutionPeriod getIPersistentExecutionPeriod()
    {
        return new ExecutionPeriodOJB();
    }
    /**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentExecutionYear()
	 */

    public IPersistentExecutionYear getIPersistentExecutionYear()
    {
        return new ExecutionYearOJB();
    }

    /**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentSite()
	 */
    public IPersistentSite getIPersistentSite()
    {
        return new SiteOJB();
    }

    /**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentSection()
	 */
    public IPersistentSection getIPersistentSection()
    {
        return new SectionOJB();
    }

    /**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentItem()
	 */
    public IPersistentItem getIPersistentItem()
    {
        return new ItemOJB();
    }

    /**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentAnnouncement()
	 */
    public IPersistentAnnouncement getIPersistentAnnouncement()
    {
        return new AnnouncementOJB();
    }

    /**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentCurriculum()
	 */
    public IPersistentCurriculum getIPersistentCurriculum()
    {
        return new CurriculumOJB();
    }

    /**
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentTeacher()
	 */
    public IPersistentTeacher getIPersistentTeacher()
    {
        return new TeacherOJB();
    }

    public IPersistentExam getIPersistentExam()
    {
        return new ExamOJB();
    }

    public IPersistentExamExecutionCourse getIPersistentExamExecutionCourse()
    {
        return new ExamExecutionCourseOJB();
    }

    public IPersistentBranch getIPersistentBranch()
    {
        return new BranchOJB();
    }

    public IPersistentCurricularYear getIPersistentCurricularYear()
    {
        return new CurricularYearOJB();
    }

    public IPersistentContributor getIPersistentContributor()
    {
        return new ContributorOJB();
    }

    public IPersistentCurricularSemester getIPersistentCurricularSemester()
    {
        return new CurricularSemesterOJB();
    }

    public IPersistentEnrolmentEquivalence getIPersistentEnrolmentEquivalence()
    {
        return new EnrolmentEquivalenceOJB();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentProfessorship()
	 */
    public IPersistentProfessorship getIPersistentProfessorship()
    {
        return new ProfessorshipOJB();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentResponsibleFor()
	 */
    public IPersistentResponsibleFor getIPersistentResponsibleFor()
    {
        return new ResponsibleForOJB();
    }

    public IPersistentPrice getIPersistentPrice()
    {
        return new PriceOJB();
    }

    public IPersistentGuideEntry getIPersistentGuideEntry()
    {
        return new GuideEntryOJB();
    }

    public IPersistentGuide getIPersistentGuide()
    {
        return new GuideOJB();
    }

    public IPersistentGuideSituation getIPersistentGuideSituation()
    {
        return new GuideSituationOJB();
    }

    public IPersistentCurricularCourseScope getIPersistentCurricularCourseScope()
    {
        return new CurricularCourseScopeOJB();
    }

    public IPersistentRole getIPersistentRole()
    {
        return new RoleOJB();
    }

    public IPersistentPersonRole getIPersistentPersonRole()
    {
        return new PersonRoleOJB();
    }

    public IPersistentPrecedence getIPersistentPrecedence()
    {
        return new PrecedenceOJB();
    }

    public IPersistentRestriction getIPersistentRestriction()
    {
        return new PersistentRestriction();
    }

    public IPersistentEnrolmentPeriod getIPersistentEnrolmentPeriod()
    {
        return new PersistentEnrolmentPeriod();
    }

    public IPersistentDegreeCurricularPlanEnrolmentInfo getIPersistentDegreeEnrolmentInfo()
    {
        return new DegreeCurricularPlanEnrolmentInfoOJB();
    }

    public IPersistentPossibleCurricularCourseForOptionalCurricularCourse getIPersistentChosenCurricularCourseForOptionalCurricularCourse()
    {
        return new PossibleCurricularCourseForOptionalCurricularCourseOJB();
    }

    public IPersistentStudentKind getIPersistentStudentKind()
    {
        return new StudentKindOJB();
    }

    public IPersistentShiftProfessorship getIPersistentTeacherShiftPercentage()
    {
        return new ShiftProfessorshipOJB();
    }

    public IPersistentCreditsTeacher getIPersistentCreditsTeacher()
    {
        return new CreditsTeacherOJB();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentExamStudentRoom()
	 */
    public IPersistentExamStudentRoom getIPersistentExamStudentRoom()
    {
        return new ExamStudentRoomOJB();
    }

    public IPersistentMark getIPersistentMark()
    {
        return new MarkOJB();
    }
    public IPersistentEvaluation getIPersistentEvaluation()
    {
        return new EvaluationOJB();
    }

    public IPersistentEvaluationExecutionCourse getIPersistentEvaluationExecutionCourse()
    {
        return new EvaluationExecutionCourseOJB();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentEmployee()
	 */
    public IPersistentEmployee getIPersistentEmployee()
    {
        return new EmployeeOJB();
    }

    public IPersistentEquivalentEnrolmentForEnrolmentEquivalence getIPersistentEquivalentEnrolmentForEnrolmentEquivalence()
    {
        return new EquivalentEnrolmentForEnrolmentEquivalenceOJB();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentSummary()
	 */
    public IPersistentSummary getIPersistentSummary()
    {
        return new SummaryOJB();
    }

    public IPersistentQualification getIPersistentQualification()
    {
        return new QualificationOJB();
    }

    public IPersistentCandidateEnrolment getIPersistentCandidateEnrolment()
    {
        return new CandidateEnrolmentOJB();
    }

    public IPersistentStudentGroup getIPersistentStudentGroup()
    {
        return new StudentGroupOJB();
    }

    public IPersistentGratuity getIPersistentGratuity()
    {
        return new GratuityOJB();
    }

    public IPersistentStudentGroupAttend getIPersistentStudentGroupAttend()
    {
        return new StudentGroupAttendOJB();
    }

    public IPersistentGroupProperties getIPersistentGroupProperties()
    {
        return new GroupPropertiesOJB();
    }

    public IPersistentCurricularCourseEquivalence getIPersistentCurricularCourseEquivalence()
    {
        return new CurricularCourseEquivalenceOJB();
    }

    public IPersistentCurricularCourseEquivalenceRestriction getIPersistentCurricularCourseEquivalenceRestriction()
    {
        return new CurricularCourseEquivalenceRestrictionOJB();
    }

    //by gedl AT rnl DOT ist DOT utl DOT pt (July the 25th, 2003)
    public IPersistentSeminaryModality getIPersistentSeminaryModality()
    {
        return new ModalityOJB();
    }

    // by Barbosa (October 28th, 2003)
    public IPersistentGrantOwner getIPersistentGrantOwner()
    {
        return new GrantOwnerOJB();
    }
    // by Barbosa (November 18th, 2003)
    public IPersistentGrantContract getIPersistentGrantContract()
    {
        return new GrantContractOJB();
    }
    //By Barbosa (November 19th, 2003)
    public IPersistentGrantType getIPersistentGrantType()
    {
        return new GrantTypeOJB();
    }
    //By Barbosa (November 20th, 2003)
    public IPersistentGrantResponsibleTeacher getIPersistentGrantResponsibleTeacher()
    {
        return new GrantResponsibleTeacherOJB();
    }
    //By Barbosa (November 20th, 2003)
    public IPersistentGrantOrientationTeacher getIPersistentGrantOrientationTeacher()
    {
        return new GrantOrientationTeacherOJB();
    }

    public IPersistentMasterDegreeThesis getIPersistentMasterDegreeThesis()
    {
        return new MasterDegreeThesisOJB();
    }
    public IPersistentMasterDegreeThesisDataVersion getIPersistentMasterDegreeThesisDataVersion()
    {
        return new MasterDegreeThesisDataVersionOJB();
    }
    public IPersistentMasterDegreeProofVersion getIPersistentMasterDegreeProofVersion()
    {
        return new MasterDegreeProofVersionOJB();
    }

    public IPersistentExternalPerson getIPersistentExternalPerson()
    {
        return new ExternalPersonOJB();
    }

    public IPersistentUniversity getIPersistentUniversity()
    {
        return new UniversityOJB();
    }

    public IPersistentEvaluationMethod getIPersistentEvaluationMethod()
    {
        return new EvaluationMethodOJB();
    }

    public IPersistentCoordinator getIPersistentCoordinator()
    {
        return new CoordinatorOJB();
    }

    public IPersistentDegreeInfo getIPersistentDegreeInfo()
    {
        return new DegreeInfoOJB();
    }

    public IPersistentCourseReport getIPersistentCourseReport()
    {
        return new CourseReportOJB();
    }

    public IPersistentCategory getIPersistentCategory()
    {
        return new CategoryOJB();
    }

    public IPersistentCareer getIPersistentCareer()
    {
        return new CareerOJB();
    }

    public IPersistentWeeklyOcupation getIPersistentWeeklyOcupation()
    {
        return new WeeklyOcupationOJB();
    }

    public IPersistentExternalActivity getIPersistentExternalActivity()
    {
        return new ExternalActivityOJB();
    }

    public IPersistentServiceProviderRegime getIPersistentServiceProviderRegime()
    {
        return new ServiceProviderRegimeOJB();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentShiftProfessorship()
	 */
    public IPersistentShiftProfessorship getIPersistentShiftProfessorship()
    {
        return new ShiftProfessorshipOJB();
    }

    public IPersistentReimbursementGuide getIPersistentReimbursementGuide()
    {

        return new ReimbursementGuideOJB();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentReimbursementGuideSituation()
	 */
    public IPersistentReimbursementGuideSituation getIPersistentReimbursementGuideSituation()
    {
        return new ReimbursementGuideSituationOJB();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentOrientation()
	 */
    public IPersistentOrientation getIPersistentOrientation()
    {
        return new OrientationOJB();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentPublicationsNumber()
	 */
    public IPersistentPublicationsNumber getIPersistentPublicationsNumber()
    {
        return new PublicationsNumberOJB();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentOldPublication()
	 */
    public IPersistentOldPublication getIPersistentOldPublication()
    {
        return new OldPublicationOJB();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentGaugingTestResult()
	 */
    public IPersistentGaugingTestResult getIPersistentGaugingTestResult()
    {

        return new GaugingTestResultOJB();
    }


    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentSupportLesson()
	 */
    public IPersistentSupportLesson getIPersistentSupportLesson()
    {
        return new SupportLessonOJB();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentTeacherDegreeFinalProjectStudent()
	 */
    public IPersistentTeacherDegreeFinalProjectStudent getIPersistentTeacherDegreeFinalProjectStudent()
    {
        return new TeacherDegreeFinalProjectStudentOJB();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentTeacherInstitutionWorkingTime()
	 */
    public IPersistentTeacherInstitutionWorkingTime getIPersistentTeacherInstitutionWorkingTime()
    {
        return new TeacherInstitutionWorkingTimeOJB();
    }
    
    public IPersistentCurricularCourseGroup getIPersistentCurricularCourseGroup()
    {
    	return new CurricularCourseGroupOJB();
    }
	/* (non-Javadoc)
		* @see ServidorPersistente.ISuportePersistente#getIPersistentCampus()
		*/
	   public IPersistentCampus getIPersistentCampus()
	   {
		   return new CampusOJB();
	   }
	   
	//by gedl AT rnl DOT ist DOT utl DOT pt (July the 28th, 2003) 
		public IPersistentSeminaryTheme getIPersistentSeminaryTheme()
		{
			return new ThemeOJB();
		}
     
//	   by gedl AT rnl DOT ist DOT utl DOT pt (July the 28th, 2003) 
		public IPersistentSeminary getIPersistentSeminary()
		{
			return new ServidorPersistente.OJB.Seminaries.SeminaryOJB();
		}
     
//	   by gedl AT rnl DOT ist DOT utl DOT pt (July the 28th, 2003) 
		public IPersistentSeminaryCaseStudy getIPersistentSeminaryCaseStudy()
		{
			return new CaseStudyOJB();
		}
     
//	   by gedl AT rnl DOT ist DOT utl DOT pt (July the 29th, 2003) 
		public IPersistentSeminaryCandidacy getIPersistentSeminaryCandidacy()
		{
			return new CandidacyOJB();
		}
     
//	   by gedl AT rnl DOT ist DOT utl DOT pt (July the 29th, 2003) 
		public IPersistentSeminaryCaseStudyChoice getIPersistentSeminaryCaseStudyChoice()
		{
			return new CaseStudyChoiceOJB();
		}
     
//	   by gedl AT rnl DOT ist DOT utl DOT pt (August the 4th, 2003) 
		public IPersistentSeminaryCurricularCourseEquivalency getIPersistentSeminaryCurricularCourseEquivalency()
		{
			return new EquivalencyOJB();
		}


	   public IPersistentMetadata getIPersistentMetadata() {
		   return new MetadataOJB();
	   }

	   public IPersistentQuestion getIPersistentQuestion() {
		   return new QuestionOJB();
	   }

	   public IPersistentTest getIPersistentTest() {
		   return new TestOJB();
	   }

	   public IPersistentTestQuestion getIPersistentTestQuestion() {
		   return new TestQuestionOJB();
	   }
	
	   public IPersistentDistributedTest getIPersistentDistributedTest(){
		   return new DistributedTestOJB();
	   }
	
	   public IPersistentStudentTestQuestion getIPersistentStudentTestQuestion(){
		   return new StudentTestQuestionOJB();
	   }
	
	   public IPersistentStudentTestLog getIPersistentStudentTestLog(){
		   return new StudentTestLogOJB();
	   }
	
	   public IPersistentAdvisory getIPersistentAdvisory() {
		   return new AdvisoryOJB();
	   }

	   public IPersistentWebSite getIPersistentWebSite() {
		   return new WebSiteOJB();
	   }
	   public IPersistentWebSiteSection getIPersistentWebSiteSection() {
		   return new WebSiteSectionOJB();
	   }
	   public IPersistentWebSiteItem getIPersistentWebSiteItem() {
		   return new WebSiteItemOJB();
	   }
	   public IPersistentWorkLocation getIPersistentWorkLocation()
	   {
		   return new WorkLocationOJB();
	   }

	   public void beginTransaction() throws StorageException
	   {
		   try
		   {
			   this.iniciarTransaccao();
		   }
		   catch (ExcepcaoPersistencia e)
		   {
			   throw new StorageException("error in wrapping method",e);
		   }
		
	   }

//	   by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	   public void commitTransaction() throws StorageException
	   {
		   try
		   {
			   this.confirmarTransaccao();
		   }
		   catch (ExcepcaoPersistencia e)
		   {
			   throw new StorageException("error in wrapping method",e);
		   }
		
	   }

//	   by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	   public void abortTransaction() throws StorageException
	   {
		   try
		   {
			   this.cancelarTransaccao();
		   }
		   catch (ExcepcaoPersistencia e)
		   {
			   throw new StorageException("error in wrapping method",e);
		   }
		
	   }

//	   by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	   public void lockRead(List list) throws StorageException
	   {
		   try {

			   Transaction tx = _odmg.currentTransaction();

			   if (tx == null)
				   throw new StorageException("No current transaction!");
			   if (list != null) {
				   for (int i = 0; i < list.size(); i++) {
					   Object obj = list.get(i);
					   tx.lock(obj, Transaction.READ);
				   }
			   }
		   } catch (ODMGRuntimeException ex) {
			   throw new StorageException(ExcepcaoPersistencia.READ_LOCK, ex);
		   }
	   }

//	   by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	   public void lockRead(Object obj) throws StorageException
	   {
		   try {
			   Transaction tx = _odmg.currentTransaction();
			   tx.lock(obj, Transaction.WRITE);

		   } catch (ODMGRuntimeException ex) {
			   throw new StorageException(
				   ExcepcaoPersistencia.UPGRADE_LOCK,
				   ex);
		   }		
	   }

//	   by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	   public void lockWrite(Object obj) throws StorageException
	   {
		   try {
			   Transaction tx = _odmg.currentTransaction();
			   tx.lock(obj, Transaction.WRITE);

		   } catch (ODMGRuntimeException ex) {
			   throw new StorageException(
				   ExcepcaoPersistencia.UPGRADE_LOCK,
				   ex);
		   }	
	   }
//	   by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	   public PersistenceBroker currentBroker() throws StorageException
	   {
		   Transaction tx = this._odmg.currentTransaction();
		   if (tx == null)
			   throw new StorageException(StorageException.NO_TRANSACTION_IN_COURSE);
		   return ((HasBroker) tx).getBroker();
	   }
}
