/*
 * Created on 24/Jul/2003
 *
 */
package ServidorAplicacao.strategy.groupEnrolment.strategys;

import java.util.Calendar;
import java.util.List;

import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */

public class IndividualGroupEnrolmentStrategy extends GroupEnrolmentStrategy implements IGroupEnrolmentStrategy {
	

	public IndividualGroupEnrolmentStrategy(){
	
	}
	
		
	public boolean enrolmentPolicy(IGroupProperties groupProperties,int numberOfStudentsToEnrole,IStudentGroup studentGroup,ITurno shift){
		boolean result = false;
		
		if(checkEnrolmentDate(groupProperties,Calendar.getInstance()))
		{
			if(studentGroup == null)
			{
				if(checkNumberOfGroups(groupProperties,shift) )
					result=true;
			}
			else
			{	
				List listStudentGroupAttend = null;
				try
				{
					ISuportePersistente sp = SuportePersistenteOJB.getInstance();
					listStudentGroupAttend = sp.getIPersistentStudentGroupAttend().readAllByStudentGroup(studentGroup);
				
				
				} catch (ExcepcaoPersistencia ex) {
					ex.printStackTrace();
				  }
				int nrOfElements = listStudentGroupAttend.size();
				if(numberOfStudentsToEnrole==1 && nrOfElements < groupProperties.getMaximumCapacity().intValue())
					result = true;
			}
		}
			return result;			
				
			}
		
}
