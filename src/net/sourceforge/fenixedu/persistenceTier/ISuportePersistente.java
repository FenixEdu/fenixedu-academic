package net.sourceforge.fenixedu.persistenceTier;


import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentCMS;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailAddressAlias;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailingList;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantPaymentEntity;
import net.sourceforge.fenixedu.persistenceTier.guide.IPersistentReimbursementGuide;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTestAdvisory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationAttribute;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationFormat;
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

    public IPersistentGrantPaymentEntity getIPersistentGrantPaymentEntity();

    public IPersistentGrantContractRegime getIPersistentGrantContractRegime();

    public IPersistentMasterDegreeThesisDataVersion getIPersistentMasterDegreeThesisDataVersion();

    public IPersistentMasterDegreeProofVersion getIPersistentMasterDegreeProofVersion();

    public IPersistentReimbursementGuide getIPersistentReimbursementGuide();

    public IPersistentGratuitySituation getIPersistentGratuitySituation();

    public IPersistentFinalDegreeWork getIPersistentFinalDegreeWork();

    public IPersistentPublicationAttribute getIPersistentPublicationAttribute();

    public IPersistentPublicationFormat getIPersistentPublicationFormat();

    public IPersistentObject getIPersistentObject();

    public IPersistentInsuranceTransaction getIPersistentInsuranceTransaction();

    public IPersistentCMS getIPersistentCms();

    public IPersistentMailAddressAlias getIPersistentMailAdressAlias();

    public IPersistentMailingList getIPersistentMailingList();

}
