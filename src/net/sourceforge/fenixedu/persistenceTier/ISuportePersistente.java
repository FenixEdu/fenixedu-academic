package net.sourceforge.fenixedu.persistenceTier;


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

public interface ISuportePersistente {
    
	public void iniciarTransaccao() throws ExcepcaoPersistencia;

	public void confirmarTransaccao() throws ExcepcaoPersistencia;

	public void cancelarTransaccao() throws ExcepcaoPersistencia;

	public void clearCache();

	public Integer getNumberCachedItems();
	
	public IPersistentMasterDegreeCandidate getIPersistentMasterDegreeCandidate();

	public IPersistentCurricularCourseScope getIPersistentCurricularCourseScope();

	public IPersistentEnrolmentPeriod getIPersistentEnrolmentPeriod();

    public IPersistentMetadata getIPersistentMetadata();

	public IPersistentQuestion getIPersistentQuestion();

	public IPersistentDistributedTest getIPersistentDistributedTest();

	public IPersistentStudentTestQuestion getIPersistentStudentTestQuestion();

	public IPersistentDistributedTestAdvisory getIPersistentDistributedTestAdvisory();

	public IPersistentGrantOwner getIPersistentGrantOwner();

	public IPersistentGrantContract getIPersistentGrantContract();

	public IPersistentGrantOrientationTeacher getIPersistentGrantOrientationTeacher();

	public IPersistentGrantCostCenter getIPersistentGrantCostCenter();

	public IPersistentMasterDegreeThesisDataVersion getIPersistentMasterDegreeThesisDataVersion();

	public IPersistentMasterDegreeProofVersion getIPersistentMasterDegreeProofVersion();

	public IPersistentGratuitySituation getIPersistentGratuitySituation();

	public IPersistentFinalDegreeWork getIPersistentFinalDegreeWork();

	@Deprecated
    public IPersistentObject getIPersistentObject();

	public IPersistentInsuranceTransaction getIPersistentInsuranceTransaction();

	public IPersistentCMS getIPersistentCms();

	public IPersistentMailingList getIPersistentMailingList();
            
}
