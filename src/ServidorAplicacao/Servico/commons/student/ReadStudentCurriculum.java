
/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorAplicacao.Servico.commons.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.DegreeCurricularPlan;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

public class ReadStudentCurriculum implements IServico {
    
    private static ReadStudentCurriculum servico = new ReadStudentCurriculum();
    
    /**
     * The singleton access method of this class.
     **/
    public static ReadStudentCurriculum getService() {
        return servico;
    }
    
    /**
     * The actor of this class.
     **/
    private ReadStudentCurriculum() { 
    }
    
    /**
     * Returns The Service Name */
    
    public final String getNome() {
        return "ReadStudentCurriculum";
    }
    
    
    public List run(Integer studentCurricularPlanID) throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;
        
		IStudentCurricularPlan studentCurricularPlan = null;
         
        try {
            sp = SuportePersistenteOJB.getInstance();
            
            // The student Curricular plan
            
            IStudentCurricularPlan studentCurricularPlanTemp = new StudentCurricularPlan();
            studentCurricularPlanTemp.setIdInternal(studentCurricularPlanID);

            studentCurricularPlan = (IStudentCurricularPlan) sp.getIStudentCurricularPlanPersistente().readByOId(studentCurricularPlanTemp, false);
          
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        } 

		if (studentCurricularPlan == null){
			throw new NonExistingServiceException();
		}
				
		Iterator iterator = studentCurricularPlan.getEnrolments().iterator();
		List result = new ArrayList();
//		while(iterator.hasNext()){
//			IEnrolment
//
//			result.add(Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan));
//		}
		
		return null;
    }
}