package ServidorPersistente;

/**
 * @author PTRLV
 *
 */

import java.util.List;

import Dominio.IBibliographicReference;
import Dominio.IExecutionCourse;

public interface IPersistentBibliographicReference extends IPersistentObject{

    public IBibliographicReference readBibliographicReference(IExecutionCourse executionCourse,String title,String authors,String reference,String year)throws ExcepcaoPersistencia;
    public void lockWrite(IBibliographicReference  bibliographicReference) throws ExcepcaoPersistencia;
    public void delete(IBibliographicReference bibliographicReference ) throws ExcepcaoPersistencia ;
     	
	public List readBibliographicReference(IExecutionCourse executionCourse)throws ExcepcaoPersistencia;
}
