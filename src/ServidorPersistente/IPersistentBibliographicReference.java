package ServidorPersistente;

/**
 * @author PTRLV
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.util.List;

import Dominio.IBibliographicReference;
import Dominio.IDisciplinaExecucao;

public interface IPersistentBibliographicReference {

    public IBibliographicReference readBibliographicReference(IDisciplinaExecucao executionCourse,String title,String authors,String reference,String year)throws ExcepcaoPersistencia;
    public void lockWrite(IBibliographicReference  bibliographicReference) throws ExcepcaoPersistencia;
    public void delete(IBibliographicReference bibliographicReference ) throws ExcepcaoPersistencia ;
    public void deleteAll() throws ExcepcaoPersistencia;  	
	public List readBibliographicReference(IDisciplinaExecucao executionCourse)throws ExcepcaoPersistencia;
}
