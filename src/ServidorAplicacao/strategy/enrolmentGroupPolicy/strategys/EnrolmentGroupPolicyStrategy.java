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
	int numberOfStudentsToEnrole;
	private IStudentGroup studentGroup = null;
	
	public IGroupProperties getGroupProperties() {
		return groupProperties;
	}

	public void setGroupProperties(IGroupProperties groupProperties) {
		this.groupProperties = groupProperties;
	}

	public int getNumberOfStudentsToEnrole() {
		return numberOfStudentsToEnrole;
	}

	public void setNumberOfStudentsToEnrole(int numberOfStudentsToEnrole) {
		this.numberOfStudentsToEnrole = numberOfStudentsToEnrole;
	}

	public IStudentGroup getStudentGroup() {
		return studentGroup;
	}

	public void setStudentGroup(IStudentGroup studentGroup) {
		this.studentGroup = studentGroup;
	}
	
	public boolean checkNumberOfGroups(IGroupProperties groupProperties)
		{
			boolean result = false;
			try
			{
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				IPersistentStudentGroup persistentStudentGroup = sp.getIPersistentStudentGroup();
				List groups = persistentStudentGroup.readAllStudentGroupByGroupProperties(groupProperties);
				int numberOfGroups = groups.size();
				if(numberOfGroups < groupProperties.getGroupMaximumNumber().intValue())
					result = true;
			
			}catch (ExcepcaoPersistencia e) {
			 }
			return result;
		}
		
	public abstract boolean enrolmentPolicy(IGroupProperties groupProperties,int numberOfStudentsToEnrole,IStudentGroup studentGroup);
}
