/*
 * Created on 24/Jul/2003
 */
 
package ServidorAplicacao.strategy.groupEnrolment.strategys;

import java.util.Calendar;

import Dominio.IGroupProperties;
import Dominio.ITurno;

/**
 * @author asnr and scpo
 *
 */

public class AtomicGroupEnrolmentStrategy extends GroupEnrolmentStrategy implements IGroupEnrolmentStrategy {
	
	
	public AtomicGroupEnrolmentStrategy(){
	}

	public boolean enrolmentPolicyNewGroup(IGroupProperties groupProperties,int numberOfStudentsToEnrole,ITurno shift)
	{
		boolean result = false;
		if(checkNumberOfGroups(groupProperties,shift))
		{
			if(numberOfStudentsToEnrole >= groupProperties.getMinimumCapacity().intValue()&& numberOfStudentsToEnrole <= groupProperties.getMaximumCapacity().intValue())
				result = true;
		}			
		return result;			
		
	}
	
}		
//	public boolean enrolmentPolicyExistingGroup(IGroupProperties groupProperties,int numberOfStudentsToEnrole,IStudentGroup studentGroup,ITurno shift)
//	throws ExcepcaoPersistencia
//	{
//		boolean result = false;
//		
//		if(checkEnrolmentDate(groupProperties,Calendar.getInstance()))
//		{
//			List listStudentGroupAttend = null;
//			try
//			{
//				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
//				listStudentGroupAttend = sp.getIPersistentStudentGroupAttend().readAllByStudentGroup(studentGroup);
//				
//				
//			} catch (ExcepcaoPersistencia ex) {
//				ex.printStackTrace();
//			}
//			int nrOfElements = listStudentGroupAttend.size();
//			if(nrOfElements+numberOfStudentsToEnrole<=groupProperties.getMaximumCapacity().intValue())
//				return true;
//					
//		}
//		return result;			
//				
//		
//	}

