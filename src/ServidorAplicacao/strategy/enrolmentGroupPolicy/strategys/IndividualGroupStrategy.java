/*
 * Created on 24/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.strategy.enrolmentGroupPolicy.strategys;

import java.util.List;

import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class IndividualGroupStrategy extends EnrolmentGroupPolicyStrategy implements IEnrolmentGroupPolicyStrategy {
	
	private IGroupProperties groupProperties = null;

	public IndividualGroupStrategy(){
		
		
		}
	
	public IGroupProperties getGroupProperties() {
			return groupProperties;
		}

	public void setGroupProperties(IGroupProperties groupProperties) {
			this.groupProperties = groupProperties;
		}
		
	public boolean enrolmentPolicy(IGroupProperties groupProperties,List listOfStudentsToEnrole,IStudentGroup studentGroup)
	{
		boolean result = false;
		if(checkNumberOfGroups(groupProperties,listOfStudentsToEnrole))
		{
			if(studentGroup==null)
				result=true;
			else
			{
				
				List listStudentGroupAttend=null;
				try
				{
					ISuportePersistente sp = SuportePersistenteOJB.getInstance();
					listStudentGroupAttend = sp.getIPersistentStudentGroupAttend().readAllByStudentGroup(studentGroup);
			
				} catch (ExcepcaoPersistencia ex) {
					ex.printStackTrace();
				}
				int nrOfElements = listStudentGroupAttend.size();
				if(nrOfElements < groupProperties.getMaximumCapacity().intValue())
				result=true;
			}
		}			
		return result;			
				
	}
	
	
		
}
