/*
 * SuportePersistenteOJB.java
 * 
 * Created on 19 de Agosto de 2002, 1:18
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author ars
 */

import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.persistenceTier.*;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.CandidacyOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.CaseStudyChoiceOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.CaseStudyOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.EquivalencyOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.ModalityOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.SeminaryOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.ThemeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.credits.ManagementPositionCreditLineOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.credits.OtherTypeCreditLineOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.credits.ServiceExemptionCreditLineOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.degree.finalProject.TeacherDegreeFinalProjectStudentOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.gaugingTests.physics.GaugingTestResultOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.gaugingTests.physics.IPersistentGaugingTestResult;
import net.sourceforge.fenixedu.persistenceTier.OJB.gesdis.CourseHistoricOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.gesdis.CourseReportOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.gesdis.StudentCourseReportOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantContractMovementOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantContractOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantContractRegimeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantCostCenterOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantInsuranceOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantOrientationTeacherOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantPartOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantPaymentEntityOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantSubsidyOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.contract.GrantTypeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.grant.owner.GrantOwnerOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.gratuity.masterDegree.SibsPaymentFileEntryOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.gratuity.masterDegree.SibsPaymentFileOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.guide.ReimbursementGuideEntryOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.guide.ReimbursementGuideOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.inquiries.InquiriesCourseOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.inquiries.InquiriesRegistryOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.inquiries.InquiriesRoomOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.inquiries.InquiriesTeacherOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.inquiries.OldInquiriesCoursesResOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.inquiries.OldInquiriesSummaryOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.inquiries.OldInquiriesTeachersResOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.log.EnrolmentLogOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness.CostCenterOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness.ExtraWorkCompensationOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness.ExtraWorkHistoricOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness.ExtraWorkOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness.ExtraWorkResquestsOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness.MoneyCostCenterOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.person.qualification.QualificationOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.places.campus.CampusOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.projectsManagement.ProjectAccessOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.publication.AuthorshipOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.publication.PublicationAttributeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.publication.PublicationFormatOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.publication.PublicationOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.publication.PublicationTeacherOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.publication.PublicationTypeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.sms.SentSmsOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.student.DelegateOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.student.SeniorOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.CareerOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.CategoryOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.ExternalActivityOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.OldPublicationOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.OrientationOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.PublicationsNumberOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.ServiceProviderRegimeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.WeeklyOcupationOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.professorship.NonAffiliatedTeacherOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.professorship.ShiftProfessorshipOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.professorship.SupportLessonOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.workingTime.TeacherInstitutionWorkingTimeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.transactions.GratuityTransactionOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.transactions.InsuranceTransactionOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.transactions.PaymentTransactionOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.transactions.ReimbursementTransactionOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.transactions.SmsTransactionOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.transactions.TransactionOJB;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminary;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCandidacy;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCaseStudy;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCaseStudyChoice;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCurricularCourseEquivalency;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryModality;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryTheme;
import net.sourceforge.fenixedu.persistenceTier.cache.ObjectCacheOSCacheImpl;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentOtherTypeCreditLine;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentServiceExemptionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentCourseHistoric;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentCourseReport;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentStudentCourseReport;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractMovement;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPart;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantType;
import net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree.IPersistentSibsPaymentFile;
import net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree.IPersistentSibsPaymentFileEntry;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuide;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuideEntry;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesCourse;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesRegistry;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesRoom;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesTeacher;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesCoursesRes;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesSummary;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentCostCenter;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWork;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkCompensation;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkHistoric;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkRequests;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentMoneyCostCenter;
import net.sourceforge.fenixedu.persistenceTier.places.campus.IPersistentCampus;
import net.sourceforge.fenixedu.persistenceTier.projectsManagement.IPersistentProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthorship;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationAttribute;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationFormat;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import net.sourceforge.fenixedu.persistenceTier.sms.IPersistentSentSms;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentDelegate;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentSenior;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentCareer;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentCategory;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOldPublication;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOrientation;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentPublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentServiceProviderRegime;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentWeeklyOcupation;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentNonAffiliatedTeacher;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;
import net.sourceforge.fenixedu.persistenceTier.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentGratuityTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentPaymentTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentReimbursementTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentSmsTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentTransaction;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.odmg.HasBroker;
import org.apache.ojb.odmg.OJB;
import org.odmg.Database;
import org.odmg.Implementation;
import org.odmg.ODMGException;
import org.odmg.ODMGRuntimeException;
import org.odmg.Transaction;

import pt.utl.ist.berserk.storage.ITransactionBroker;
import pt.utl.ist.berserk.storage.exceptions.StorageException;

public class SuportePersistenteOJB implements ISuportePersistente, ITransactionBroker {
    Implementation _odmg = null;

    private static SuportePersistenteOJB _instance = null;

    private static HashMap descriptorMap = null;

    public void setDescriptor(DescriptorRepository descriptorRepository, String hashName) {
        descriptorMap.put(hashName, descriptorRepository);
    }

    public DescriptorRepository getDescriptor(String hashName) {

        return (DescriptorRepository) descriptorMap.get(hashName);
    }

    protected Implementation getImplementation() {
        return _odmg;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#clearCache()
     */
    public void clearCache() {
        if (_odmg != null) {
            HasBroker hasBroker = ((HasBroker) _odmg.currentTransaction());
            if (hasBroker != null) {
                hasBroker.getBroker().clearCache();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#clearCache()
     */
    public Integer getNumberCachedItems() {
        return new Integer(ObjectCacheOSCacheImpl.getNumberOfCachedItems());
    }

    public static synchronized SuportePersistenteOJB getInstance() throws ExcepcaoPersistencia {
        if (_instance == null) {
            _instance = new SuportePersistenteOJB();
        }
        if (descriptorMap == null) {
            descriptorMap = new HashMap();

        }
        return _instance;
    }

    public static synchronized void resetInstance() {
        if (_instance != null) {
            PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.clearCache();
            _instance = null;
        }
    }

    /** Creates a new instance of SuportePersistenteOJB */
    private SuportePersistenteOJB() throws ExcepcaoPersistencia {
        init();
    }

    private void init() throws ExcepcaoPersistencia {

        _odmg = OJB.getInstance();
        try {
            openDatabase();
        } catch (ODMGException e) {
            throw new ExcepcaoPersistencia();
        }
    }

    protected void finalize() throws Throwable {
    }

    public void iniciarTransaccao() throws ExcepcaoPersistencia {
        try {
            openDatabase();
            Transaction tx = _odmg.newTransaction();
            tx.begin();
        } catch (ODMGException ex1) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.OPEN_DATABASE, ex1);
        } catch (ODMGRuntimeException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.BEGIN_TRANSACTION, ex);
        }
    }

    private void openDatabase() throws ODMGException {
        Database db = _odmg.getDatabase(null);
        if (db == null) {
            db = _odmg.newDatabase();
            db.open("OJB/repository.xml", Database.OPEN_READ_WRITE);
        }
    }

    public void confirmarTransaccao() throws ExcepcaoPersistencia {
        try {
            openDatabase();
            Transaction tx = _odmg.currentTransaction();

            if (tx != null)
            {
                tx.commit();
                //				Database db =_odmg.getDatabase(null);
                //				if (db!= null) db.close();
            }
        } catch (ODMGException ex1) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.CLOSE_DATABASE, ex1);
        } catch (ODMGRuntimeException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.COMMIT_TRANSACTION, ex);
        }
    }

    public void cancelarTransaccao() throws ExcepcaoPersistencia {
        try {
            openDatabase();
            Transaction tx = _odmg.currentTransaction();

            if (tx != null) {
                tx.abort();
                //					_odmg.getDatabase(null).close();
            }

        } catch (ODMGException ex1) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.CLOSE_DATABASE, ex1);
        } catch (ODMGRuntimeException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.ABORT_TRANSACTION, ex);
        }
    }

    public IAulaPersistente getIAulaPersistente() {
        return new AulaOJB();
    }

    public ISalaPersistente getISalaPersistente() {
        return new SalaOJB();
    }

    public ITurmaPersistente getITurmaPersistente() {
        return new TurmaOJB();
    }

    public ITurnoPersistente getITurnoPersistente() {
        return new TurnoOJB();
    }

    public IFrequentaPersistente getIFrequentaPersistente() {
        return new FrequentaOJB();
    }

    public IPersistentEnrollment getIPersistentEnrolment() {
        return new EnrollmentOJB();
    }

    public IPersistentEnrolmentEvaluation getIPersistentEnrolmentEvaluation() {
        return new EnrolmentEvaluationOJB();
    }

    public ITurmaTurnoPersistente getITurmaTurnoPersistente() {
        return new TurmaTurnoOJB();
    }

    public ITurnoAlunoPersistente getITurnoAlunoPersistente() {
        return new TurnoAlunoOJB();
    }

    /*
     * public ITurnoAulaPersistente getITurnoAulaPersistente() { return new
     * TurnoAulaOJB(); }
     */
    public IPersistentCurricularCourse getIPersistentCurricularCourse() {
        return new CurricularCourseOJB();
    }

    public IPersistentExecutionCourse getIPersistentExecutionCourse() {
        return new ExecutionCourseOJB();
    }

    public IPersistentCountry getIPersistentCountry() {
        return new CountryOJB();
    }

    public IPessoaPersistente getIPessoaPersistente() {
        return new PessoaOJB();
    }

    public ICursoPersistente getICursoPersistente() {
        return new CursoOJB();
    }

    public IPersistentExecutionDegree getIPersistentExecutionDegree() {
        return new CursoExecucaoOJB();
    }

    public IPersistentStudent getIPersistentStudent() {
        return new StudentOJB();
    }
    
    public IPersistentSenior getIPersistentSenior() {
        return new SeniorOJB();
    }

    public IPersistentDepartment getIDepartamentoPersistente() {
        return new DepartmentOJB();
    }

    public IDisciplinaDepartamentoPersistente getIDisciplinaDepartamentoPersistente() {
        return new DisciplinaDepartamentoOJB();
    }

    public IPersistentDegreeCurricularPlan getIPersistentDegreeCurricularPlan() {
        return new DegreeCurricularPlanOJB();
    }

    public IPersistentStudentCurricularPlan getIStudentCurricularPlanPersistente() {
        return new StudentCurricularPlanOJB();
    }

    public IPersistentMasterDegreeCandidate getIPersistentMasterDegreeCandidate() {
        return new MasterDegreeCandidateOJB();
    }

    public IPersistentCandidateSituation getIPersistentCandidateSituation() {
        return new CandidateSituationOJB();
    }

    public IPersistentBibliographicReference getIPersistentBibliographicReference() {
        return new BibliographicReferenceOJB();
    }

    /**
     * @see ServidorPersistente.ISuportePersistente#getIPersistentExecutionPeriod()
     */
    public IPersistentExecutionPeriod getIPersistentExecutionPeriod() {
        return new ExecutionPeriodOJB();
    }

    /**
     * @see ServidorPersistente.ISuportePersistente#getIPersistentExecutionYear()
     */

    public IPersistentExecutionYear getIPersistentExecutionYear() {
        return new ExecutionYearOJB();
    }

    /**
     * @see ServidorPersistente.ISuportePersistente#getIPersistentSite()
     */
    public IPersistentSite getIPersistentSite() {
        return new SiteOJB();
    }

    /**
     * @see ServidorPersistente.ISuportePersistente#getIPersistentSection()
     */
    public IPersistentSection getIPersistentSection() {
        return new SectionOJB();
    }

    /**
     * @see ServidorPersistente.ISuportePersistente#getIPersistentItem()
     */
    public IPersistentItem getIPersistentItem() {
        return new ItemOJB();
    }

    /**
     * @see ServidorPersistente.ISuportePersistente#getIPersistentAnnouncement()
     */
    public IPersistentAnnouncement getIPersistentAnnouncement() {
        return new AnnouncementOJB();
    }

    /**
     * @see ServidorPersistente.ISuportePersistente#getIPersistentCurriculum()
     */
    public IPersistentCurriculum getIPersistentCurriculum() {
        return new CurriculumOJB();
    }

    /**
     * @see ServidorPersistente.ISuportePersistente#getIPersistentTeacher()
     */
    public IPersistentTeacher getIPersistentTeacher() {
        return new TeacherOJB();
    }

    public IPersistentExam getIPersistentExam() {
        return new ExamOJB();
    }

    public IPersistentExamExecutionCourse getIPersistentExamExecutionCourse() {
        return new ExamExecutionCourseOJB();
    }

    public IPersistentBranch getIPersistentBranch() {
        return new BranchOJB();
    }

    public IPersistentCurricularYear getIPersistentCurricularYear() {
        return new CurricularYearOJB();
    }

    public IPersistentContributor getIPersistentContributor() {
        return new ContributorOJB();
    }

    public IPersistentCurricularSemester getIPersistentCurricularSemester() {
        return new CurricularSemesterOJB();
    }

    public IPersistentEnrolmentEquivalence getIPersistentEnrolmentEquivalence() {
        return new EnrolmentEquivalenceOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentProfessorship()
     */
    public IPersistentProfessorship getIPersistentProfessorship() {
        return new ProfessorshipOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentResponsibleFor()
     */
    public IPersistentResponsibleFor getIPersistentResponsibleFor() {
        return new ResponsibleForOJB();
    }

    public IPersistentPrice getIPersistentPrice() {
        return new PriceOJB();
    }

    public IPersistentGuideEntry getIPersistentGuideEntry() {
        return new GuideEntryOJB();
    }

    public IPersistentGuide getIPersistentGuide() {
        return new GuideOJB();
    }

    public IPersistentGuideSituation getIPersistentGuideSituation() {
        return new GuideSituationOJB();
    }

    public IPersistentCurricularCourseScope getIPersistentCurricularCourseScope() {
        return new CurricularCourseScopeOJB();
    }

    public IPersistentRole getIPersistentRole() {
        return new RoleOJB();
    }

    public IPersistentPersonRole getIPersistentPersonRole() {
        return new PersonRoleOJB();
    }

    public IPersistentPrecedence getIPersistentPrecedence() {
        return new PrecedenceOJB();
    }

    public IPersistentRestriction getIPersistentRestriction() {
        return new RestrictionOJB();
    }

    public IPersistentEnrolmentPeriod getIPersistentEnrolmentPeriod() {
        return new PersistentEnrolmentPeriod();
    }

    public IPersistentDegreeCurricularPlanEnrolmentInfo getIPersistentDegreeEnrolmentInfo() {
        return new DegreeCurricularPlanEnrolmentInfoOJB();
    }

    public IPersistentStudentKind getIPersistentStudentKind() {
        return new StudentKindOJB();
    }

    public IPersistentShiftProfessorship getIPersistentTeacherShiftPercentage() {
        return new ShiftProfessorshipOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentExamStudentRoom()
     */
    public IPersistentExamStudentRoom getIPersistentExamStudentRoom() {
        return new ExamStudentRoomOJB();
    }

    public IPersistentMark getIPersistentMark() {
        return new MarkOJB();
    }

    public IPersistentEvaluation getIPersistentEvaluation() {
        return new EvaluationOJB();
    }

    public IPersistentEvaluationExecutionCourse getIPersistentEvaluationExecutionCourse() {
        return new EvaluationExecutionCourseOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentEmployee()
     */
    public IPersistentEmployee getIPersistentEmployee() {
        return new EmployeeOJB();
    }

    public IPersistentEquivalentEnrolmentForEnrolmentEquivalence getIPersistentEquivalentEnrolmentForEnrolmentEquivalence() {
        return new EquivalentEnrolmentForEnrolmentEquivalenceOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentSummary()
     */
    public IPersistentSummary getIPersistentSummary() {
        return new SummaryOJB();
    }

    public IPersistentQualification getIPersistentQualification() {
        return new QualificationOJB();
    }

    public IPersistentCandidateEnrolment getIPersistentCandidateEnrolment() {
        return new CandidateEnrolmentOJB();
    }

    public IPersistentStudentGroup getIPersistentStudentGroup() {
        return new StudentGroupOJB();
    }

    public IPersistentStudentGroupAttend getIPersistentStudentGroupAttend() {
        return new StudentGroupAttendOJB();
    }

    public IPersistentGroupProperties getIPersistentGroupProperties() {
        return new GroupPropertiesOJB();
    }

    public IPersistentCurricularCourseEquivalence getIPersistentCurricularCourseEquivalence() {
        return new CurricularCourseEquivalenceOJB();
    }

    //by gedl AT rnl DOT ist DOT utl DOT pt (July the 25th, 2003)
    public IPersistentSeminaryModality getIPersistentSeminaryModality() {
        return new ModalityOJB();
    }

    // by Barbosa (October 28th, 2003)
    public IPersistentGrantOwner getIPersistentGrantOwner() {
        return new GrantOwnerOJB();
    }

    // by Barbosa (November 18th, 2003)
    public IPersistentGrantContract getIPersistentGrantContract() {
        return new GrantContractOJB();
    }

    //By Barbosa (November 19th, 2003)
    public IPersistentGrantType getIPersistentGrantType() {
        return new GrantTypeOJB();
    }

    //By Barbosa (November 20th, 2003)
    public IPersistentGrantOrientationTeacher getIPersistentGrantOrientationTeacher() {
        return new GrantOrientationTeacherOJB();
    }

    public IPersistentMasterDegreeThesis getIPersistentMasterDegreeThesis() {
        return new MasterDegreeThesisOJB();
    }

    public IPersistentMasterDegreeThesisDataVersion getIPersistentMasterDegreeThesisDataVersion() {
        return new MasterDegreeThesisDataVersionOJB();
    }

    public IPersistentMasterDegreeProofVersion getIPersistentMasterDegreeProofVersion() {
        return new MasterDegreeProofVersionOJB();
    }

    public IPersistentExternalPerson getIPersistentExternalPerson() {
        return new ExternalPersonOJB();
    }

    public IPersistentUniversity getIPersistentUniversity() {
        return new UniversityOJB();
    }

    public IPersistentEvaluationMethod getIPersistentEvaluationMethod() {
        return new EvaluationMethodOJB();
    }

    public IPersistentCoordinator getIPersistentCoordinator() {
        return new CoordinatorOJB();
    }

    public IPersistentDegreeInfo getIPersistentDegreeInfo() {
        return new DegreeInfoOJB();
    }

    public IPersistentCourseReport getIPersistentCourseReport() {
        return new CourseReportOJB();
    }

    public IPersistentCategory getIPersistentCategory() {
        return new CategoryOJB();
    }

    public IPersistentCareer getIPersistentCareer() {
        return new CareerOJB();
    }

    public IPersistentWeeklyOcupation getIPersistentWeeklyOcupation() {
        return new WeeklyOcupationOJB();
    }

    public IPersistentExternalActivity getIPersistentExternalActivity() {
        return new ExternalActivityOJB();
    }

    public IPersistentServiceProviderRegime getIPersistentServiceProviderRegime() {
        return new ServiceProviderRegimeOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentShiftProfessorship()
     */
    public IPersistentShiftProfessorship getIPersistentShiftProfessorship() {
        return new ShiftProfessorshipOJB();
    }

    public IPersistentReimbursementGuide getIPersistentReimbursementGuide() {

        return new ReimbursementGuideOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentOrientation()
     */
    public IPersistentOrientation getIPersistentOrientation() {
        return new OrientationOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentPublicationsNumber()
     */
    public IPersistentPublicationsNumber getIPersistentPublicationsNumber() {
        return new PublicationsNumberOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentOldPublication()
     */
    public IPersistentOldPublication getIPersistentOldPublication() {
        return new OldPublicationOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentGaugingTestResult()
     */
    public IPersistentGaugingTestResult getIPersistentGaugingTestResult() {
        return new GaugingTestResultOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentSupportLesson()
     */
    public IPersistentSupportLesson getIPersistentSupportLesson() {
        return new SupportLessonOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentTeacherDegreeFinalProjectStudent()
     */
    public IPersistentTeacherDegreeFinalProjectStudent getIPersistentTeacherDegreeFinalProjectStudent() {
        return new TeacherDegreeFinalProjectStudentOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentTeacherInstitutionWorkingTime()
     */
    public IPersistentTeacherInstitutionWorkingTime getIPersistentTeacherInstitutionWorkingTime() {
        return new TeacherInstitutionWorkingTimeOJB();
    }

    //Nuno Correia & Ricardo Rodrigues
    public IPersistentCurricularCourseGroup getIPersistentCurricularCourseGroup() {
        return new CurricularCourseGroupOJB();
    }

    public IPersistentScientificArea getIPersistentScientificArea() {
        return new ScientificAreaOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentCampus()
     */
    public IPersistentCampus getIPersistentCampus() {
        return new CampusOJB();
    }

    //by gedl AT rnl DOT ist DOT utl DOT pt (July the 28th, 2003)
    public IPersistentSeminaryTheme getIPersistentSeminaryTheme() {
        return new ThemeOJB();
    }

    //	   by gedl AT rnl DOT ist DOT utl DOT pt (July the 28th, 2003)
    public IPersistentSeminary getIPersistentSeminary() {
        return new SeminaryOJB();
    }

    //	   by gedl AT rnl DOT ist DOT utl DOT pt (July the 28th, 2003)
    public IPersistentSeminaryCaseStudy getIPersistentSeminaryCaseStudy() {
        return new CaseStudyOJB();
    }

    //	   by gedl AT rnl DOT ist DOT utl DOT pt (July the 29th, 2003)
    public IPersistentSeminaryCandidacy getIPersistentSeminaryCandidacy() {
        return new CandidacyOJB();
    }

    //	   by gedl AT rnl DOT ist DOT utl DOT pt (July the 29th, 2003)
    public IPersistentSeminaryCaseStudyChoice getIPersistentSeminaryCaseStudyChoice() {
        return new CaseStudyChoiceOJB();
    }

    //	   by gedl AT rnl DOT ist DOT utl DOT pt (August the 4th, 2003)
    public IPersistentSeminaryCurricularCourseEquivalency getIPersistentSeminaryCurricularCourseEquivalency() {
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

    public IPersistentDistributedTest getIPersistentDistributedTest() {
        return new DistributedTestOJB();
    }

    public IPersistentStudentTestQuestion getIPersistentStudentTestQuestion() {
        return new StudentTestQuestionOJB();
    }

    public IPersistentStudentTestLog getIPersistentStudentTestLog() {
        return new StudentTestLogOJB();
    }

    public IPersistentOnlineTest getIPersistentOnlineTest() {
        return new OnlineTestOJB();
    }

    public IPersistentTestScope getIPersistentTestScope() {
        return new TestScopeOJB();
    }

    public IPersistentDistributedTestAdvisory getIPersistentDistributedTestAdvisory() {
        return new DistributedTestAdvisoryOJB();
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

    public IPersistentWorkLocation getIPersistentWorkLocation() {
        return new WorkLocationOJB();
    }

    public void beginTransaction() throws StorageException {
        try {
            this.iniciarTransaccao();
        } catch (ExcepcaoPersistencia e) {
            throw new StorageException("error in wrapping method", e);
        }

    }

    //	   by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
    public void commitTransaction() throws StorageException {
        try {
            this.confirmarTransaccao();
        } catch (ExcepcaoPersistencia e) {
            throw new StorageException("error in wrapping method", e);
        }

    }

    //	   by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
    public void abortTransaction() throws StorageException {
        try {
            this.cancelarTransaccao();
        } catch (ExcepcaoPersistencia e) {
            throw new StorageException("error in wrapping method", e);
        }

    }

    //	   by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
    public void lockRead(List list) throws StorageException {
        try {

            Transaction tx = _odmg.currentTransaction();

            if (tx == null)
                throw new StorageException("No current transaction!");
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    lockRead(list.get(i));
                }
            }
        } catch (ODMGRuntimeException ex) {
            throw new StorageException(ExcepcaoPersistencia.READ_LOCK, ex);
        }
    }

    //	   by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
    public void lockRead(Object obj) throws StorageException {
        try {
            Transaction tx = _odmg.currentTransaction();
            tx.lock(obj, Transaction.READ);

        } catch (ODMGRuntimeException ex) {
            throw new StorageException(ExcepcaoPersistencia.READ_LOCK, ex);
        }
    }

    //	   by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
    public void lockWrite(Object obj) throws StorageException {
        try {
            Transaction tx = _odmg.currentTransaction();
            tx.lock(obj, Transaction.WRITE);

        } catch (ODMGRuntimeException ex) {
            throw new StorageException(ExcepcaoPersistencia.UPGRADE_LOCK, ex);
        }
    }

    //	   by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
    public PersistenceBroker currentBroker() throws StorageException {
        Transaction tx = this._odmg.currentTransaction();
        if (tx == null)
            throw new StorageException(StorageException.NO_TRANSACTION_IN_COURSE);
        return ((HasBroker) tx).getBroker();
    }

    public IPersistentGratuityValues getIPersistentGratuityValues() {
        return new GratuityValuesOJB();
    }

    public IPersistentGratuitySituation getIPersistentGratuitySituation() {
        return new GratuitySituationOJB();
    }

    public IPersistentPaymentPhase getIPersistentPaymentPhase() {
        return new PaymentPhaseOJB();
    }

    public IPersistentCreditsInAnySecundaryArea getIPersistentCreditsInAnySecundaryArea() {
        return new CreditsInAnySecundaryAreaOJB();
    }

    public IPersistentCreditsInSpecificScientificArea getIPersistentCreditsInSpecificScientificArea() {
        return new CreditsInSpecificScientificAreaOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantCostCenter()
     */
    public IPersistentGrantCostCenter getIPersistentGrantCostCenter() {
        return new GrantCostCenterOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantPart()
     */
    public IPersistentGrantPart getIPersistentGrantPart() {
        return new GrantPartOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantPaymentEntity()
     */
    public IPersistentGrantPaymentEntity getIPersistentGrantPaymentEntity() {
        return new GrantPaymentEntityOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantSubsidy()
     */
    public IPersistentGrantSubsidy getIPersistentGrantSubsidy() {
        return new GrantSubsidyOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantContractRegime()
     */
    public IPersistentGrantContractRegime getIPersistentGrantContractRegime() {
        return new GrantContractRegimeOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantContractRegime()
     */
    public IPersistentGrantInsurance getIPersistentGrantInsurance() {
        return new GrantInsuranceOJB();
    }

    public IPersistentGrantContractMovement getIPersistentGrantContractMovement() {
        return new GrantContractMovementOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentTutor()
     */
    public IPersistentTutor getIPersistentTutor() {
        return new TutorOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentCourseHistoric()
     */
    public IPersistentCourseHistoric getIPersistentCourseHistoric() {
        return new CourseHistoricOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentStudentCourseReport()
     */
    public IPersistentStudentCourseReport getIPersistentStudentCourseReport() {
        return new StudentCourseReportOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentDelegate()
     */
    public IPersistentDelegate getIPersistentDelegate() {
        return new DelegateOJB();
    }

    public IPersistentOtherTypeCreditLine getIPersistentOtherTypeCreditLine() {
        return new OtherTypeCreditLineOJB();
    }

    public IPersistentServiceExemptionCreditLine getIPersistentServiceExemptionCreditLine() {
        return new ServiceExemptionCreditLineOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentManagementPosistionCreditLine()
     */
    public IPersistentManagementPositionCreditLine getIPersistentManagementPositionCreditLine() {
        return new ManagementPositionCreditLineOJB();
    }

    public IPersistentFinalDegreeWork getIPersistentFinalDegreeWork() {
        return new FinalDegreeWorkOJB();
    }

    //	Ana e Ricardo
    public IPersistentRoomOccupation getIPersistentRoomOccupation() {
        return new RoomOccupationOJB();
    }

    public IPersistentPeriod getIPersistentPeriod() {
        return new PeriodOJB();
    }

    public IPersistentWrittenTest getIPersistentWrittenTest() {
        return new WrittenTestOJB();
    }

    public IPersistentReimbursementGuideEntry getIPersistentReimbursementGuideEntry() {
        return new ReimbursementGuideEntryOJB();

    }
/*
    public IPersistentWrittenEvaluationCurricularCourseScope getIPersistentWrittenEvaluationCurricularCourseScope() {
        return new WrittenEvaluationCurricularCourseScopeOJB();

    }
*/
    public IPersistentSentSms getIPersistentSentSms() {
        return new SentSmsOJB();
    }

    //TJBF & PFON
    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentPublication()
     */
    public IPersistentPublication getIPersistentPublication() {
        return new PublicationOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentPublicationType()
     */
    public IPersistentPublicationType getIPersistentPublicationType() {
        return new PublicationTypeOJB();
    }

    // Nuno Correia & Ricardo Rodrigues
    public IPersistentPersonalDataUseInquiryAnswers getIPersistentPersonalDataUseInquiryAnswers() {
        return new PersonalDataUseInquiryAnswersOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentPublicationAttribute()
     */
    public IPersistentPublicationAttribute getIPersistentPublicationAttribute() {
        return new PublicationAttributeOJB();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.ISuportePersistente#getIPersistentPublicationFormat()
     */
    public IPersistentPublicationFormat getIPersistentPublicationFormat() {
        return new PublicationFormatOJB();
    }

    public IPersistentSibsPaymentFile getIPersistentSibsPaymentFile() {
        return new SibsPaymentFileOJB();
    }

    public IPersistentSibsPaymentFileEntry getIPersistentSibsPaymentFileEntry() {
        return new SibsPaymentFileEntryOJB();
    }

    public IPersistentObject getIPersistentObject() {
        return new PersistentObjectOJB();
    }

    public IPersistentGratuityTransaction getIPersistentGratuityTransaction() {
        return new GratuityTransactionOJB();
    }

    public IPersistentResidenceCandidacies getIPersistentResidenceCandidacies() {
        return new ResidenceCandidaciesOJB();
    }

    public IPersistentReimbursementTransaction getIPersistentReimbursementTransaction() {
        return new ReimbursementTransactionOJB();
    }

    public IPersistentSmsTransaction getIPersistentSmsTransaction() {
        return new SmsTransactionOJB();
    }

    public IPersistentInsuranceTransaction getIPersistentInsuranceTransaction() {
        return new InsuranceTransactionOJB();
    }

    public IPersistentPersonAccount getIPersistentPersonAccount() {
        return new PersonAccountOJB();
    }

    public IPersistentTransaction getIPersistentTransaction() {
        return new TransactionOJB();
    }

    public IPersistentInsuranceValue getIPersistentInsuranceValue() {
        return new InsuranceValueOJB();
    }

    public IPersistentFAQSection getIPersistentFAQSection() {
        return new FAQSectionOJB();
    }

    public IPersistentFAQEntries getIPersistentFAQEntries() {
        return new FAQEntriesOJB();
    }

    public IPersistentGlossaryEntries getIPersistentGlossaryEntries() {
        return new GlossaryEntriesOJB();
    }
    
    public IPersistentGroupPropertiesExecutionCourse getIPersistentGroupPropertiesExecutionCourse() {
        return new GroupPropertiesExecutionCourseOJB();
    }

    public IPersistentAttendInAttendsSet getIPersistentAttendInAttendsSet() {
    	return new AttendInAttendsSetOJB();
    }

    public IPersistentAttendsSet getIPersistentAttendsSet() {
    	return new AttendsSetOJB();
    }
    
    public IPersistentEnrolmentLog getIPersistentEnrolmentLog() {
        return new EnrolmentLogOJB();
    }

    // Rita Ferreira e Jo�o Fialho
	public IPersistentOldInquiriesSummary getIPersistentOldInquiriesSummary() {
		return new OldInquiriesSummaryOJB();
	}
	public IPersistentOldInquiriesTeachersRes getIPersistentOldInquiriesTeachersRes() {
	    return new OldInquiriesTeachersResOJB();
	}
	public IPersistentOldInquiriesCoursesRes getIPersistentOldInquiriesCoursesRes() {
	    return new OldInquiriesCoursesResOJB();
	}
	public IPersistentInquiriesCourse getIPersistentInquiriesCourse() {
		return new InquiriesCourseOJB();
	}
	public IPersistentInquiriesRegistry getIPersistentInquiriesRegistry() {
		return new InquiriesRegistryOJB();
	}
	public IPersistentInquiriesRoom getIPersistentInquiriesRoom() {
		return new InquiriesRoomOJB();
	}
	public IPersistentInquiriesTeacher getIPersistentInquiriesTeacher() {
		return new InquiriesTeacherOJB();
	}
	//
    public IPersistentPublicationTeacher getIPersistentPublicationTeacher(){
    	return new PublicationTeacherOJB();
    }
    public IPersistentCostCenter getIPersistentCostCenter() {
        return new CostCenterOJB();
    }
    
    public IPersistentMoneyCostCenter getIPersistentMoneyCostCenter() {
        return new MoneyCostCenterOJB();
    }
    
    public IPersistentExtraWork getIPersistentExtraWork() {
        return new ExtraWorkOJB();
    }

    public IPersistentExtraWorkRequests getIPersistentExtraWorkRequests() {
        return new ExtraWorkResquestsOJB();
    }    
       public IPersistentPaymentTransaction getIPersistentPaymentTransaction() {
        return new PaymentTransactionOJB();
    }

    public IPersistentSecretaryEnrolmentStudent getIPersistentSecretaryEnrolmentStudent() {
        return new SecretaryEnrolmentStudentOJB();
    }
    
    public IPersistentExtraWorkCompensation getIPersistentExtraWorkCompensation() {
        return new ExtraWorkCompensationOJB();
    }

    public IPersistentExtraWorkHistoric getIPersistentExtraWorkHistoric() {
        return new ExtraWorkHistoricOJB();
    }
    
    public IPersistentProjectAccess getIPersistentProjectAccess() {
        return new ProjectAccessOJB();
    }

    public IPersistentBuilding getIPersistentBuilding() {
        return new BuildingOJB();
    }
    
    public IPersistentInstitution getIPersistentInstitution() {
        return new InstitutionOJB();
    }

    public IPersistentNonAffiliatedTeacher getIPersistentNonAffiliatedTeacher() {
        return new NonAffiliatedTeacherOJB();
    }

    public IPersistentAuthorship getIPersistentAuthorship() {
        return new AuthorshipOJB();
    }

}