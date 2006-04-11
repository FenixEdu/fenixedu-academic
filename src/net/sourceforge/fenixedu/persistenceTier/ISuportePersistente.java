package net.sourceforge.fenixedu.persistenceTier;



public interface ISuportePersistente {
    
	public void iniciarTransaccao() throws ExcepcaoPersistencia;

	public void confirmarTransaccao() throws ExcepcaoPersistencia;

	public void cancelarTransaccao() throws ExcepcaoPersistencia;

	public void clearCache();

	public Integer getNumberCachedItems();

	@Deprecated
    public IPersistentObject getIPersistentObject();
      
}
