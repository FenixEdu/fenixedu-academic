/*
 * Created on 28/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.strategy.enrolmentGroupPolicy.strategys;

import java.util.List;

import Dominio.IGroupProperties;
import Dominio.IStudentGroup;

/**
 * @author lmac
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IEnrolmentGroupPolicyStrategy {
	
	public boolean checkNumberOfGroups(IGroupProperties groupProperties,List ListOfStudentsToEnrole);
	public boolean enrolmentPolicy(IGroupProperties groupProperties,List listOfStudentsToEnrole,IStudentGroup studentGroup);

}
