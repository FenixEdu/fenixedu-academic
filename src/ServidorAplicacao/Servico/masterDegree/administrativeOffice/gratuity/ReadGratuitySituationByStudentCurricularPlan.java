/*
 * Created on 10/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import DataBeans.util.Cloner;
import Dominio.IGratuitySituation;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;


/**
 * @author Tânia Pousão
 *
 */
public class ReadGratuitySituationByStudentCurricularPlan implements IServico
{

    private static ReadGratuitySituationByStudentCurricularPlan servico = new ReadGratuitySituationByStudentCurricularPlan();

    /**
	 * The singleton access method of this class.
	 */
    public static ReadGratuitySituationByStudentCurricularPlan getService()
    {
        return servico;
    }

    /**
	 * The actor of this class.
	 */
    private ReadGratuitySituationByStudentCurricularPlan()
    {
    }

    /**
	 * Returns The Service Name
	 */

    public final String getNome()
    {
        return "ReadGratuitySituationByStudentCurricularPlan";
    }

    public Object run(Integer studentCurricularPlanID) throws FenixServiceException
    {
    	ISuportePersistente sp = null;
    	IGratuitySituation gratuitySituation = null;
    	try {
			sp = SuportePersistenteOJB.getInstance();
			
			IPersistentGratuitySituation persistentGratuitySituation = sp.getIPersistentGratuitySituation();
    	
			IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
			studentCurricularPlan.setIdInternal(studentCurricularPlanID);
						
			gratuitySituation = persistentGratuitySituation.readGratuitySituatuionByStudentCurricularPlan(studentCurricularPlan);
			
    	} catch (ExcepcaoPersistencia e) {
    		e.printStackTrace();
			throw new FenixServiceException("error.impossible.insertExemptionGratuity");			
		}
    	if(gratuitySituation == null){
    		throw new NonExistingServiceException("error.impossible.insertExemptionGratuity");
    	}
    	
		return Cloner.copyIGratuitySituation2InfoGratuitySituation(gratuitySituation);
    }
}
