/*
 * Created on 24/Jul/2003
 *
 */
package ServidorAplicacao.strategy.groupEnrolment.strategys;

import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author asnr and scpo
 *
 */

public class IndividualGroupEnrolmentStrategy extends GroupEnrolmentStrategy implements IGroupEnrolmentStrategy {
	
	

	public IndividualGroupEnrolmentStrategy(){
	
	}
	
		
	public boolean enrolmentPolicyNewGroup(IGroupProperties groupProperties,int numberOfStudentsToEnrole,ITurno shift){
		
		boolean result = false;
		
		if(checkNumberOfGroups(groupProperties,shift))
			result=true;
		
		return result;			
				
		}

	
	public boolean checkNumberOfGroupElements(IGroupProperties groupProperties,IStudentGroup studentGroup) throws ExcepcaoPersistencia {
		return true;
	}
}


