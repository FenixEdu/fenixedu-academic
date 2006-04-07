package net.sourceforge.fenixedu.persistenceTier;


import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentCMS;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentMailingList;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;

public interface ISuportePersistente {
    
	public void iniciarTransaccao() throws ExcepcaoPersistencia;

	public void confirmarTransaccao() throws ExcepcaoPersistencia;

	public void cancelarTransaccao() throws ExcepcaoPersistencia;

	public void clearCache();

	public Integer getNumberCachedItems();

	public IPersistentCurricularCourseScope getIPersistentCurricularCourseScope();

    public IPersistentMetadata getIPersistentMetadata();

    public IPersistentStudentTestQuestion getIPersistentStudentTestQuestion();

	public IPersistentGrantContract getIPersistentGrantContract();

	public IPersistentGratuitySituation getIPersistentGratuitySituation();

	public IPersistentFinalDegreeWork getIPersistentFinalDegreeWork();

	@Deprecated
    public IPersistentObject getIPersistentObject();

	public IPersistentInsuranceTransaction getIPersistentInsuranceTransaction();

	public IPersistentCMS getIPersistentCms();

	public IPersistentMailingList getIPersistentMailingList();
            
}
