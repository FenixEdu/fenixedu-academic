/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorAplicacao.Servico.masterDegree.coordinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus;
import Dominio.ICursoExecucao;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadCoordinatedDegrees implements IServico {
    
    private static ReadCoordinatedDegrees servico = new ReadCoordinatedDegrees();
    
    /**
     * The singleton access method of this class.
     **/
    public static ReadCoordinatedDegrees getService() {
        return servico;
    }
    
    /**
     * The actor of this class.
     **/
    private ReadCoordinatedDegrees() { 
    }
    
    /**
     * Returns The Service Name */
    
    public final String getNome() {
        return "ReadCoordinatedDegrees";
    }
    
    
    public Object run(UserView userView)
	    throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;
        
        List degrees = null;
         
        try {
            sp = SuportePersistenteOJB.getInstance();
            
            // Read the Teacher
            
            ITeacher teacher = sp.getIPersistentTeacher().readTeacherByUsername(userView.getUtilizador());
			if (teacher == null)
				throw new ExcepcaoInexistente("No Teachers Found !!");

			degrees = sp.getIPersistentCoordinator().readExecutionDegreesByTeacher(teacher);		
          //  degrees = sp.getICursoExecucaoPersistente().readByTeacher(teacher);
          
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error",ex);            
            throw newEx;
        } 
		if (degrees == null)
			throw new ExcepcaoInexistente("No Degrees Found !!");	
		
		Iterator iterator = degrees.iterator();
		List result = new ArrayList();
		while(iterator.hasNext()){
			ICursoExecucao executionDegree = (ICursoExecucao) iterator.next(); 
			
			//CLONER
			//result.add(Cloner.get(executionDegree));
			result
                    .add(InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus
                            .newInfoFromDomain(executionDegree));
		}

		return result;
    }
}