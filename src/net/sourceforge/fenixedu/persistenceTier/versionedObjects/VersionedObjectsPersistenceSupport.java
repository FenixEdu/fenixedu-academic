package net.sourceforge.fenixedu.persistenceTier.versionedObjects;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeProofVersion;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentCMS;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailingList;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTestAdvisory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.cms.CMSVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.cms.MailingListVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.CurricularCourseScopeVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.EnrolmentPeriodVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.GratuitySituationVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.InsuranceTransactionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.MasterDegreeProofVersionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.MasterDegreeThesisDataVersionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantContractVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantCostCenterVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantOrientationTeacherVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.owner.GrantOwnerVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.DistributedTestAdvisoryVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.DistributedTestVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.MetadataVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.QuestionVO;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests.StudentTestQuestionVO;

public class VersionedObjectsPersistenceSupport implements ISuportePersistente {

    private static final VersionedObjectsPersistenceSupport instance = new VersionedObjectsPersistenceSupport();

    static {
    }

    private VersionedObjectsPersistenceSupport() {
    }

    public static VersionedObjectsPersistenceSupport getInstance() {
        return instance;
    }

    public IPersistentGrantOrientationTeacher getIPersistentGrantOrientationTeacher() {
        return new GrantOrientationTeacherVO();
    }

    public IPersistentStudentTestQuestion getIPersistentStudentTestQuestion() {
        return new StudentTestQuestionVO();
    }

    public IPersistentMasterDegreeCandidate getIPersistentMasterDegreeCandidate() {
        return null;
    }

    public IPersistentGrantCostCenter getIPersistentGrantCostCenter() {
        return new GrantCostCenterVO();
    }

    public void confirmarTransaccao() throws ExcepcaoPersistencia {
        return;
    }

    public void iniciarTransaccao() throws ExcepcaoPersistencia {
        return;
    }

    public IPersistentMetadata getIPersistentMetadata() {
        return new MetadataVO();
    }

    public IPersistentGrantOwner getIPersistentGrantOwner() {
        return new GrantOwnerVO();
    }

    public IPersistentDistributedTestAdvisory getIPersistentDistributedTestAdvisory() {
        return new DistributedTestAdvisoryVO();
    }

    public IPersistentGrantContract getIPersistentGrantContract() {
        return new GrantContractVO();
    }

    public IPersistentGratuitySituation getIPersistentGratuitySituation() {
        return new GratuitySituationVO();
    }

    public IPersistentEnrolmentPeriod getIPersistentEnrolmentPeriod() {
        return new EnrolmentPeriodVO();
    }

    public IPersistentQuestion getIPersistentQuestion() {
        return new QuestionVO();
    }

    public Integer getNumberCachedItems() {
        return null;
    }

    public IPersistentFinalDegreeWork getIPersistentFinalDegreeWork() {
        return null;
    }

    public IPersistentInsuranceTransaction getIPersistentInsuranceTransaction() {
        return new InsuranceTransactionVO();
    }

    public IPersistentMasterDegreeProofVersion getIPersistentMasterDegreeProofVersion() {
        return new MasterDegreeProofVersionVO();
    }

    public void cancelarTransaccao() throws ExcepcaoPersistencia {
        return;
    }

    public IPersistentMasterDegreeThesisDataVersion getIPersistentMasterDegreeThesisDataVersion() {
        return new MasterDegreeThesisDataVersionVO();
    }

    public void clearCache() {
        return;
    }

    public IPersistentObject getIPersistentObject() {
        return null;
    }

    public IPersistentDistributedTest getIPersistentDistributedTest() {
        return new DistributedTestVO();
    }

    public IPersistentCurricularCourseScope getIPersistentCurricularCourseScope() {
        return new CurricularCourseScopeVO();
    }

    public IPersistentCMS getIPersistentCms() {
		return new CMSVO();
	}
	
	public IPersistentMailingList getIPersistentMailingList()
	{
		return new MailingListVO();
	}	
	    
}
