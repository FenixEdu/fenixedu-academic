package net.sourceforge.fenixedu.persistenceTier;


import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;

public interface ISuportePersistente {
    
	public void iniciarTransaccao() throws ExcepcaoPersistencia;

	public void confirmarTransaccao() throws ExcepcaoPersistencia;

	public void cancelarTransaccao() throws ExcepcaoPersistencia;

	public void clearCache();

	public Integer getNumberCachedItems();

    public IPersistentStudentTestQuestion getIPersistentStudentTestQuestion();

	public IPersistentGrantContract getIPersistentGrantContract();

	@Deprecated
    public IPersistentObject getIPersistentObject();
      
}
