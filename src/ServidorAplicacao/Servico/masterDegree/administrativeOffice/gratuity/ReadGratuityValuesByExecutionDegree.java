/*
 * Created on 10/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import Dominio.IGratuityValues;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuityValues;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;


/**
 * @author Tânia Pousão
 *
 */
public class ReadGratuityValuesByExecutionDegree implements IServico
{

    private static ReadGratuityValuesByExecutionDegree servico = new ReadGratuityValuesByExecutionDegree();

    /**
	 * The singleton access method of this class.
	 */
    public static ReadGratuityValuesByExecutionDegree getService()
    {
        return servico;
    }

    /**
	 * The actor of this class.
	 */
    private ReadGratuityValuesByExecutionDegree()
    {
    }

    /**
	 * Returns The Service Name
	 */

    public final String getNome()
    {
        return "ReadGratuityValuesByExecutionDegree";
    }

    public Object run(Integer executionDegreeID) throws FenixServiceException
    {
    	ISuportePersistente sp = null;
    	   	
    	try {
			sp = SuportePersistenteOJB.getInstance();
			IPersistentGratuityValues persistentGratuityValues = sp.getIPersistentGrtuityValues();
			
			ICursoExecucao executionDegree = new CursoExecucao();
			executionDegree.setIdInternal(executionDegreeID);
			
			IGratuityValues gratuityValues = persistentGratuityValues.readGratuityValuesByExecutionDegree(executionDegree);
			
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		}
    	
		return null;
    }
}
