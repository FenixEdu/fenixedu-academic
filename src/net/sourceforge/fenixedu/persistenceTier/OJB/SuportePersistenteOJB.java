/*
 * SuportePersistenteOJB.java
 * 
 * Created on 19 de Agosto de 2002, 1:18
 */


package net.sourceforge.fenixedu.persistenceTier.OJB;


/**
 * @author ars
 */

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCandidateSituation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCreditsInAnySecundaryArea;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCreditsInSpecificScientificArea;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseEquivalence;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularSemester;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluationMethod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExportGrouping;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExternalPerson;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFAQEntries;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFAQSection;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuityValues;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGuide;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGuideEntry;
import net.sourceforge.fenixedu.persistenceTier.IPersistentInsuranceValue;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMark;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeProofVersion;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeThesis;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPersonalDataUseInquiryAnswers;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPrice;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResidenceCandidacies;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRestriction;
import net.sourceforge.fenixedu.persistenceTier.IPersistentScientificArea;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSecretaryEnrolmentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentShiftProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentKind;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTutor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSite;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteItem;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.CandidacyOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.CaseStudyChoiceOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.CaseStudyOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.EquivalencyOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.ModalityOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.SeminaryOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries.ThemeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.cms.PersistentCMSOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.cms.PersistentMailAddressAliasOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.cms.PersistentMailingListOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.credits.ManagementPositionCreditLineOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.credits.OtherTypeCreditLineOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.degree.finalProject.TeacherDegreeFinalProjectStudentOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.gaugingTests.physics.GaugingTestResultOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.gaugingTests.physics.IPersistentGaugingTestResult;
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
import net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness.CostCenterOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness.ExtraWorkCompensationOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness.ExtraWorkHistoricOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness.ExtraWorkOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness.ExtraWorkResquestsOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness.MoneyCostCenterOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.DistributedTestAdvisoryOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.DistributedTestOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.MetadataOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.OnlineTestOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.QuestionOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.StudentTestLogOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.StudentTestQuestionOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.TestOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.TestQuestionOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests.TestScopeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.projectsManagement.ProjectAccessOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.publication.AuthorshipOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.publication.PublicationAttributeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.publication.PublicationFormatOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.publication.PublicationOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.publication.PublicationTeacherOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.publication.PublicationTypeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.sms.SentSmsOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.student.DelegateOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.ExternalActivityOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.OldPublicationOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.OrientationOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.PublicationsNumberOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.ServiceProviderRegimeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.TeacherPersonalExpectationOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.WeeklyOcupationOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.professorship.NonAffiliatedTeacherOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.professorship.ShiftProfessorshipOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.professorship.SupportLessonOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.service.DegreeTeachingServiceOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.service.InstitutionWorkTimeOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.service.OtherServiceOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.service.TeacherMasterDegreeServiceOJB;
import net.sourceforge.fenixedu.persistenceTier.OJB.teacher.service.TeacherPastServiceOJB;
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
import net.sourceforge.fenixedu.persistenceTier.cache.FenixCache;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentCMS;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailAddressAlias;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailingList;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentManagementPositionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentOtherTypeCreditLine;
import net.sourceforge.fenixedu.persistenceTier.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;
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
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTestAdvisory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentOnlineTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestScope;
import net.sourceforge.fenixedu.persistenceTier.projectsManagement.IPersistentProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthorship;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationAttribute;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationFormat;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import net.sourceforge.fenixedu.persistenceTier.sms.IPersistentSentSms;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentDelegate;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOldPublication;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOrientation;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentPublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentServiceProviderRegime;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentTeacherPersonalExpectation;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentWeeklyOcupation;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentNonAffiliatedTeacher;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;
import net.sourceforge.fenixedu.persistenceTier.teacher.service.IPersistentDegreeTeachingService;
import net.sourceforge.fenixedu.persistenceTier.teacher.service.IPersistentInstitutionWorkTime;
import net.sourceforge.fenixedu.persistenceTier.teacher.service.IPersistentOtherService;
import net.sourceforge.fenixedu.persistenceTier.teacher.service.IPersistentTeacherMasterDegreeService;
import net.sourceforge.fenixedu.persistenceTier.teacher.service.IPersistentTeacherPastService;
import net.sourceforge.fenixedu.persistenceTier.teacher.workingTime.IPersistentTeacherInstitutionWorkingTime;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentGratuityTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentPaymentTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentReimbursementTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentSmsTransaction;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentTransaction;
import net.sourceforge.fenixedu.stm.OJBFunctionalSetWrapper;
import net.sourceforge.fenixedu.stm.Transaction;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.CollectionDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.MetadataManager;
import org.apache.ojb.broker.metadata.ObjectReferenceDescriptor;

import pt.utl.ist.berserk.storage.ITransactionBroker;
import pt.utl.ist.berserk.storage.exceptions.StorageException;

public class SuportePersistenteOJB implements ISuportePersistente, ITransactionBroker
{
	private static SuportePersistenteOJB _instance = null;

	private static HashMap descriptorMap = null;

	public void setDescriptor(DescriptorRepository descriptorRepository, String hashName)
	{
		descriptorMap.put(hashName, descriptorRepository);
	}

	public DescriptorRepository getDescriptor(String hashName)
	{

		return (DescriptorRepository) descriptorMap.get(hashName);
	}

	public static PersistenceBroker getCurrentPersistenceBroker()
	{
		return Transaction.getOJBBroker();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#clearCache()
	 */
	public void clearCache()
	{
		getCurrentPersistenceBroker().clearCache();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#clearCache()
	 */
	public Integer getNumberCachedItems()
	{
		return new Integer(FenixCache.getNumberOfCachedItems());
	}

	public static synchronized SuportePersistenteOJB getInstance() throws ExcepcaoPersistencia
	{
		if (_instance == null)
		{
			_instance = new SuportePersistenteOJB();
		}
		if (descriptorMap == null)
		{
			descriptorMap = new HashMap();

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
	private SuportePersistenteOJB()
	{
	}

	protected void finalize() throws Throwable
	{
	}

	public void iniciarTransaccao()
	{
		// commit any current transaction
		if (Transaction.current() != null)
		{
			Transaction.commit();
		}
		Transaction.begin();
	}

	public void confirmarTransaccao()
	{
		Transaction.checkpoint();
		Transaction.currentFenixTransaction().setReadOnly();
	}

	public void cancelarTransaccao()
	{
		Transaction.abort();
	}

	public ISalaPersistente getISalaPersistente()
	{
		return new SalaOJB();
	}

	public ITurmaPersistente getITurmaPersistente()
	{
		return new TurmaOJB();
	}

	public IFrequentaPersistente getIFrequentaPersistente()
	{
		return new FrequentaOJB();
	}

	public IPersistentEnrolmentEvaluation getIPersistentEnrolmentEvaluation()
	{
		return new EnrolmentEvaluationOJB();
	}

	public IPersistentCurricularCourse getIPersistentCurricularCourse()
	{
		return new CurricularCourseOJB();
	}

	public IPersistentExecutionCourse getIPersistentExecutionCourse()
	{
		return new ExecutionCourseOJB();
	}

	public IPessoaPersistente getIPessoaPersistente()
	{
		return new PessoaOJB();
	}

	public IPersistentExecutionDegree getIPersistentExecutionDegree()
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

	public IPersistentStudentCurricularPlan getIStudentCurricularPlanPersistente()
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
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentCurriculum()
	 */
	public IPersistentCurriculum getIPersistentCurriculum()
	{
		return new CurriculumOJB();
	}

	public IPersistentExam getIPersistentExam()
	{
		return new ExamOJB();
	}

	public IPersistentCurricularYear getIPersistentCurricularYear()
	{
		return new CurricularYearOJB();
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

	public IPersistentCurricularCourseScope getIPersistentCurricularCourseScope()
	{
		return new CurricularCourseScopeOJB();
	}

	public IPersistentRestriction getIPersistentRestriction()
	{
		return new RestrictionOJB();
	}

	public IPersistentEnrolmentPeriod getIPersistentEnrolmentPeriod()
	{
		return new PersistentEnrolmentPeriod();
	}

	public IPersistentStudentKind getIPersistentStudentKind()
	{
		return new StudentKindOJB();
	}

	public IPersistentShiftProfessorship getIPersistentTeacherShiftPercentage()
	{
		return new ShiftProfessorshipOJB();
	}

	public IPersistentMark getIPersistentMark()
	{
		return new MarkOJB();
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

	public IPersistentCurricularCourseEquivalence getIPersistentCurricularCourseEquivalence()
	{
		return new CurricularCourseEquivalenceOJB();
	}

	// by gedl AT rnl DOT ist DOT utl DOT pt (July the 25th, 2003)
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

	// By Barbosa (November 19th, 2003)
	public IPersistentGrantType getIPersistentGrantType()
	{
		return new GrantTypeOJB();
	}

	// By Barbosa (November 20th, 2003)
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

	// Nuno Correia & Ricardo Rodrigues
	public IPersistentCurricularCourseGroup getIPersistentCurricularCourseGroup()
	{
		return new CurricularCourseGroupOJB();
	}

	public IPersistentScientificArea getIPersistentScientificArea()
	{
		return new ScientificAreaOJB();
	}

	// by gedl AT rnl DOT ist DOT utl DOT pt (July the 28th, 2003)
	public IPersistentSeminaryTheme getIPersistentSeminaryTheme()
	{
		return new ThemeOJB();
	}

	// by gedl AT rnl DOT ist DOT utl DOT pt (July the 28th, 2003)
	public IPersistentSeminary getIPersistentSeminary()
	{
		return new SeminaryOJB();
	}

	// by gedl AT rnl DOT ist DOT utl DOT pt (July the 28th, 2003)
	public IPersistentSeminaryCaseStudy getIPersistentSeminaryCaseStudy()
	{
		return new CaseStudyOJB();
	}

	// by gedl AT rnl DOT ist DOT utl DOT pt (July the 29th, 2003)
	public IPersistentSeminaryCandidacy getIPersistentSeminaryCandidacy()
	{
		return new CandidacyOJB();
	}

	// by gedl AT rnl DOT ist DOT utl DOT pt (July the 29th, 2003)
	public IPersistentSeminaryCaseStudyChoice getIPersistentSeminaryCaseStudyChoice()
	{
		return new CaseStudyChoiceOJB();
	}

	// by gedl AT rnl DOT ist DOT utl DOT pt (August the 4th, 2003)
	public IPersistentSeminaryCurricularCourseEquivalency getIPersistentSeminaryCurricularCourseEquivalency()
	{
		return new EquivalencyOJB();
	}

	public IPersistentMetadata getIPersistentMetadata()
	{
		return new MetadataOJB();
	}

	public IPersistentQuestion getIPersistentQuestion()
	{
		return new QuestionOJB();
	}

	public IPersistentTest getIPersistentTest()
	{
		return new TestOJB();
	}

	public IPersistentTestQuestion getIPersistentTestQuestion()
	{
		return new TestQuestionOJB();
	}

	public IPersistentDistributedTest getIPersistentDistributedTest()
	{
		return new DistributedTestOJB();
	}

	public IPersistentStudentTestQuestion getIPersistentStudentTestQuestion()
	{
		return new StudentTestQuestionOJB();
	}

	public IPersistentStudentTestLog getIPersistentStudentTestLog()
	{
		return new StudentTestLogOJB();
	}

	public IPersistentOnlineTest getIPersistentOnlineTest()
	{
		return new OnlineTestOJB();
	}

	public IPersistentTestScope getIPersistentTestScope()
	{
		return new TestScopeOJB();
	}

	public IPersistentDistributedTestAdvisory getIPersistentDistributedTestAdvisory()
	{
		return new DistributedTestAdvisoryOJB();
	}

	public IPersistentWebSite getIPersistentWebSite()
	{
		return new WebSiteOJB();
	}

	public IPersistentWebSiteSection getIPersistentWebSiteSection()
	{
		return new WebSiteSectionOJB();
	}

	public IPersistentWebSiteItem getIPersistentWebSiteItem()
	{
		return new WebSiteItemOJB();
	}

	public void beginTransaction()
	{
		this.iniciarTransaccao();
	}

	// by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	public void commitTransaction()
	{
		this.confirmarTransaccao();
	}

	// by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	public void abortTransaction() throws StorageException
	{
		this.cancelarTransaccao();
	}

	// by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	public void lockRead(List list) throws StorageException
	{
	}

	// by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	public void lockRead(Object obj) throws StorageException
	{
	}

	// by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	public void lockWrite(Object obj) throws StorageException
	{
	}

	// by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Oct/2003
	public PersistenceBroker currentBroker()
	{
		return getCurrentPersistenceBroker();
	}

	public IPersistentGratuityValues getIPersistentGratuityValues()
	{
		return new GratuityValuesOJB();
	}

	public IPersistentGratuitySituation getIPersistentGratuitySituation()
	{
		return new GratuitySituationOJB();
	}

	public IPersistentCreditsInAnySecundaryArea getIPersistentCreditsInAnySecundaryArea()
	{
		return new CreditsInAnySecundaryAreaOJB();
	}

	public IPersistentCreditsInSpecificScientificArea getIPersistentCreditsInSpecificScientificArea()
	{
		return new CreditsInSpecificScientificAreaOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantCostCenter()
	 */
	public IPersistentGrantCostCenter getIPersistentGrantCostCenter()
	{
		return new GrantCostCenterOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantPart()
	 */
	public IPersistentGrantPart getIPersistentGrantPart()
	{
		return new GrantPartOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantPaymentEntity()
	 */
	public IPersistentGrantPaymentEntity getIPersistentGrantPaymentEntity()
	{
		return new GrantPaymentEntityOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantSubsidy()
	 */
	public IPersistentGrantSubsidy getIPersistentGrantSubsidy()
	{
		return new GrantSubsidyOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantContractRegime()
	 */
	public IPersistentGrantContractRegime getIPersistentGrantContractRegime()
	{
		return new GrantContractRegimeOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentGrantContractRegime()
	 */
	public IPersistentGrantInsurance getIPersistentGrantInsurance()
	{
		return new GrantInsuranceOJB();
	}

	public IPersistentGrantContractMovement getIPersistentGrantContractMovement()
	{
		return new GrantContractMovementOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentTutor()
	 */
	public IPersistentTutor getIPersistentTutor()
	{
		return new TutorOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentDelegate()
	 */
	public IPersistentDelegate getIPersistentDelegate()
	{
		return new DelegateOJB();
	}

	public IPersistentOtherTypeCreditLine getIPersistentOtherTypeCreditLine()
	{
		return new OtherTypeCreditLineOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentManagementPosistionCreditLine()
	 */
	public IPersistentManagementPositionCreditLine getIPersistentManagementPositionCreditLine()
	{
		return new ManagementPositionCreditLineOJB();
	}

	public IPersistentFinalDegreeWork getIPersistentFinalDegreeWork()
	{
		return new FinalDegreeWorkOJB();
	}

	public IPersistentPeriod getIPersistentPeriod()
	{
		return new PeriodOJB();
	}

	public IPersistentReimbursementGuideEntry getIPersistentReimbursementGuideEntry()
	{
		return new ReimbursementGuideEntryOJB();

	}

	public IPersistentSentSms getIPersistentSentSms()
	{
		return new SentSmsOJB();
	}

	// TJBF & PFON
	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentPublication()
	 */
	public IPersistentPublication getIPersistentPublication()
	{
		return new PublicationOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentPublicationType()
	 */
	public IPersistentPublicationType getIPersistentPublicationType()
	{
		return new PublicationTypeOJB();
	}

	// Nuno Correia & Ricardo Rodrigues
	public IPersistentPersonalDataUseInquiryAnswers getIPersistentPersonalDataUseInquiryAnswers()
	{
		return new PersonalDataUseInquiryAnswersOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentPublicationAttribute()
	 */
	public IPersistentPublicationAttribute getIPersistentPublicationAttribute()
	{
		return new PublicationAttributeOJB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.ISuportePersistente#getIPersistentPublicationFormat()
	 */
	public IPersistentPublicationFormat getIPersistentPublicationFormat()
	{
		return new PublicationFormatOJB();
	}

	public IPersistentSibsPaymentFile getIPersistentSibsPaymentFile()
	{
		return new SibsPaymentFileOJB();
	}

	public IPersistentSibsPaymentFileEntry getIPersistentSibsPaymentFileEntry()
	{
		return new SibsPaymentFileEntryOJB();
	}

	public IPersistentObject getIPersistentObject()
	{
		return new PersistentObjectOJB();
	}

	public IPersistentGratuityTransaction getIPersistentGratuityTransaction()
	{
		return new GratuityTransactionOJB();
	}

	public IPersistentResidenceCandidacies getIPersistentResidenceCandidacies()
	{
		return new ResidenceCandidaciesOJB();
	}

	public IPersistentReimbursementTransaction getIPersistentReimbursementTransaction()
	{
		return new ReimbursementTransactionOJB();
	}

	public IPersistentSmsTransaction getIPersistentSmsTransaction()
	{
		return new SmsTransactionOJB();
	}

	public IPersistentInsuranceTransaction getIPersistentInsuranceTransaction()
	{
		return new InsuranceTransactionOJB();
	}

	public IPersistentTransaction getIPersistentTransaction()
	{
		return new TransactionOJB();
	}

	public IPersistentInsuranceValue getIPersistentInsuranceValue()
	{
		return new InsuranceValueOJB();
	}

	public IPersistentFAQSection getIPersistentFAQSection()
	{
		return new FAQSectionOJB();
	}

	public IPersistentFAQEntries getIPersistentFAQEntries()
	{
		return new FAQEntriesOJB();
	}

	public IPersistentExportGrouping getIPersistentExportGrouping()
	{
		return new ExportGroupingOJB();
	}

	// Rita Ferreira e Jo�o Fialho
	public IPersistentOldInquiriesSummary getIPersistentOldInquiriesSummary()
	{
		return new OldInquiriesSummaryOJB();
	}

	public IPersistentOldInquiriesTeachersRes getIPersistentOldInquiriesTeachersRes()
	{
		return new OldInquiriesTeachersResOJB();
	}

	public IPersistentOldInquiriesCoursesRes getIPersistentOldInquiriesCoursesRes()
	{
		return new OldInquiriesCoursesResOJB();
	}

	public IPersistentInquiriesCourse getIPersistentInquiriesCourse()
	{
		return new InquiriesCourseOJB();
	}

	public IPersistentInquiriesRegistry getIPersistentInquiriesRegistry()
	{
		return new InquiriesRegistryOJB();
	}

	public IPersistentInquiriesRoom getIPersistentInquiriesRoom()
	{
		return new InquiriesRoomOJB();
	}

	public IPersistentInquiriesTeacher getIPersistentInquiriesTeacher()
	{
		return new InquiriesTeacherOJB();
	}

	//
	public IPersistentPublicationTeacher getIPersistentPublicationTeacher()
	{
		return new PublicationTeacherOJB();
	}

	public IPersistentCostCenter getIPersistentCostCenter()
	{
		return new CostCenterOJB();
	}

	public IPersistentMoneyCostCenter getIPersistentMoneyCostCenter()
	{
		return new MoneyCostCenterOJB();
	}

	public IPersistentExtraWork getIPersistentExtraWork()
	{
		return new ExtraWorkOJB();
	}

	public IPersistentExtraWorkRequests getIPersistentExtraWorkRequests()
	{
		return new ExtraWorkResquestsOJB();
	}

	public IPersistentPaymentTransaction getIPersistentPaymentTransaction()
	{
		return new PaymentTransactionOJB();
	}

	public IPersistentSecretaryEnrolmentStudent getIPersistentSecretaryEnrolmentStudent()
	{
		return new SecretaryEnrolmentStudentOJB();
	}

	public IPersistentExtraWorkCompensation getIPersistentExtraWorkCompensation()
	{
		return new ExtraWorkCompensationOJB();
	}

	public IPersistentExtraWorkHistoric getIPersistentExtraWorkHistoric()
	{
		return new ExtraWorkHistoricOJB();
	}

	public IPersistentProjectAccess getIPersistentProjectAccess()
	{
		return new ProjectAccessOJB();
	}

	public IPersistentNonAffiliatedTeacher getIPersistentNonAffiliatedTeacher()
	{
		return new NonAffiliatedTeacherOJB();
	}

	public IPersistentAuthorship getIPersistentAuthorship()
	{
		return new AuthorshipOJB();
	}

	public static void fixDescriptors()
	{
		final MetadataManager metadataManager = MetadataManager.getInstance();
		final Collection<ClassDescriptor> classDescriptors = (Collection<ClassDescriptor>) metadataManager.getGlobalRepository().getDescriptorTable().values();

		for (ClassDescriptor classDescriptor : classDescriptors)
		{
			for (ObjectReferenceDescriptor rd : (Collection<ObjectReferenceDescriptor>) classDescriptor.getObjectReferenceDescriptors())
			{
				rd.setCascadingStore(ObjectReferenceDescriptor.CASCADE_LINK);
				rd.setCascadeRetrieve(false);
				rd.setLazy(false);
			}

			for (CollectionDescriptor cod : (Collection<CollectionDescriptor>) classDescriptor.getCollectionDescriptors())
			{
				cod.setCascadingStore(ObjectReferenceDescriptor.CASCADE_NONE);
				cod.setCollectionClass(OJBFunctionalSetWrapper.class);
				cod.setCascadeRetrieve(false);
				cod.setLazy(false);
			}
		}
	}

	public IPersistentTeacherPersonalExpectation getIPersistentTeacherPersonalExpectation()
	{
		return new TeacherPersonalExpectationOJB();
	}

	public IPersistentCMS getIPersistentCms()
	{
		return new PersistentCMSOJB();
	}

	public IPersistentMailAddressAlias getIPersistentMailAdressAlias()
	{
		return new PersistentMailAddressAliasOJB();
	}

	public IPersistentMailingList getIPersistentMailingList()
	{
		return new PersistentMailingListOJB();
	}   
    
    public IPersistentTeacherMasterDegreeService getIPersistentTeacherMasterDegreeService() {
        return new TeacherMasterDegreeServiceOJB();
    }
    
    public IPersistentDegreeTeachingService getIPersistentDegreeTeachingService() {
        return new DegreeTeachingServiceOJB();
    }
    
    public IPersistentTeacherPastService getIPersistentTeacherPastService() {
        return new TeacherPastServiceOJB();
    }
    
    public IPersistentInstitutionWorkTime getIPersistentInstitutionWorkTime() {
        return new InstitutionWorkTimeOJB();
    }
    
    public IPersistentOtherService getIPersistentOtherService() {
        return new OtherServiceOJB();
    }  

}
