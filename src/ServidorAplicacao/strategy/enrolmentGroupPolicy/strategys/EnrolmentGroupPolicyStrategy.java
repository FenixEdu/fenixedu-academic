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
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class EnrolmentGroupPolicyStrategy implements IEnrolmentGroupPolicyStrategy{

	private IGroupProperties groupProperties = null;
	List listOfStudentsToEnrole = null;
	
	public IGroupProperties getGroupProperties() {
		return groupProperties;
	}

	public void setGroupProperties(IGroupProperties groupProperties) {
		this.groupProperties = groupProperties;
	}

	public List getListOfStudentsToEnrole() {
		return listOfStudentsToEnrole;
	}

	public void setListOfStudentsToEnrole(List listOfStudentsToEnrole) {
		this.listOfStudentsToEnrole = listOfStudentsToEnrole;
	}



	public boolean checkNumberOfGroups(IGroupProperties groupProperties, List ListOfStudentsToEnrole)
		{
			boolean result = false;
			try{
			
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				IPersistentStudentGroup persistentStudentGroup = sp.getIPersistentStudentGroup();
				List groups = persistentStudentGroup.readAllStudentGroupByGroupProperties(groupProperties);
				int numberOfGroups = groups.size();
				if(numberOfGroups<groupProperties.getGroupMaximumNumber().intValue())
					result = true;
					
				}
				catch (ExcepcaoPersistencia e) {
				}
			return result;
		}
		
	public abstract boolean enrolmentPolicy(IGroupProperties groupProperties,List listOfStudentsToEnrole,IStudentGroup studentGroup);
}
