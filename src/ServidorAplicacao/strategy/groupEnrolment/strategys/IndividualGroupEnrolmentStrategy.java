/*
 * Created on 24/Jul/2003
 *
 */
package ServidorAplicacao.strategy.groupEnrolment.strategys;

import Dominio.IGroupProperties;
import Dominio.ITurno;

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

	}
	
//	public boolean enrolmentPolicyExistingGroup(
//			IGroupProperties groupProperties,
//			int numberOfStudentsToEnrole,
//			IStudentGroup studentGroup,
//			ITurno shift)
//			throws ExcepcaoPersistencia{return true;}


