/*
 * Created on 21/Mar/2003
 *
 * 
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.odmg.QueryException;

import Dominio.Gratuity;
import Dominio.IGratuity;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuity;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GratuityOJB extends ObjectFenixOJB implements IPersistentGratuity {
    
	public GratuityOJB() {}
    	
	public void write(IGratuity gratuity) throws ExcepcaoPersistencia {

		if (gratuity == null){
			return;
		}
		
		IGratuity gratuityFromBD = this.readByStudentCurricularPlanIDAndState(gratuity.getStudentCurricularPlan().getIdInternal(), gratuity.getState());

		if (gratuityFromBD == null){
			super.lockWrite(gratuity);
			return;	
		}
		if (((gratuity) instanceof Gratuity) &&
				   (((Gratuity) gratuityFromBD).getIdInternal().equals(
				   ((Gratuity) gratuity).getIdInternal()))){
		   	super.lockWrite(gratuity);
		   	return; 
		}
		throw new ExistingPersistentException();
	} 

	public List readByStudentCurricularPlanID(Integer studentCurricularPlanID) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Gratuity.class.getName()
							+ " where keyStudentCurricularPlan = $1";
			query.create(oqlQuery);
			query.bind(studentCurricularPlanID);
			List result = (List) query.execute();
			super.lockRead(result);
			return result;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public IGratuity readByStudentCurricularPlanIDAndState(Integer studentCurricularPlanID, State state) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + Gratuity.class.getName()
							+ " where keyStudentCurricularPlan = $1"
							+ " and state = $2"; 
			query.create(oqlQuery);
			query.bind(studentCurricularPlanID);
			query.bind(state);
			List result = (List) query.execute();
			
			if (result.size() == 0){
				return null;
			}
			super.lockRead(result);
			return (IGratuity) result.get(0);
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}


	}

}
