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
	
		
	public Integer enrolmentPolicyNewGroup(IGroupProperties groupProperties,int numberOfStudentsToEnrole,ITurno shift){
		
		if(checkNumberOfGroups(groupProperties,shift)){
			return new Integer(1);}
		
			return new Integer(-1);			
				
	}

	
	public boolean checkNumberOfGroupElements(IGroupProperties groupProperties,IStudentGroup studentGroup) throws ExcepcaoPersistencia {
		return true;
	}
}


