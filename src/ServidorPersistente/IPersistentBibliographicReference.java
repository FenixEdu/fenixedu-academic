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
import Dominio.IExecutionCourse;

public interface IPersistentBibliographicReference extends IPersistentObject{

    public IBibliographicReference readBibliographicReference(IExecutionCourse executionCourse,String title,String authors,String reference,String year)throws ExcepcaoPersistencia;
    public void lockWrite(IBibliographicReference  bibliographicReference) throws ExcepcaoPersistencia;
    public void delete(IBibliographicReference bibliographicReference ) throws ExcepcaoPersistencia ;
    public void deleteAll() throws ExcepcaoPersistencia;  	
	public List readBibliographicReference(IExecutionCourse executionCourse)throws ExcepcaoPersistencia;
}
