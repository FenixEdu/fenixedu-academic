/*
 * Created on 24/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.strategy.enrolmentGroupPolicy.strategys;

import Dominio.IGroupProperties;
import Dominio.IStudentGroup;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

public class AtomicGroupStrategy extends EnrolmentGroupPolicyStrategy implements IEnrolmentGroupPolicyStrategy {
	
	
	public AtomicGroupStrategy(){
	}

	public boolean enrolmentPolicy(IGroupProperties groupProperties,int numberOfStudentsToEnrole,IStudentGroup studentGroup)
	{
		boolean result = false;
		if(checkNumberOfGroups(groupProperties))
		{
			if(numberOfStudentsToEnrole >= groupProperties.getMinimumCapacity().intValue()&& numberOfStudentsToEnrole <= groupProperties.getMaximumCapacity().intValue())
				result = true;
		}			
		return result;			
				
	}
	
}
