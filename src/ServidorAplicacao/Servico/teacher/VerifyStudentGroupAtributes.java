/*
 * Created on 29/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoStudentGroup;
import DataBeans.util.Cloner;
import Dominio.IGroupProperties;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ISuportePersistente;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class VerifyStudentGroupAtributes implements IServico {
	private ISuportePersistente persistentSupport = null;
	
	//private IPersistentGroupProperties persistentGroupProperties = null;

	private static VerifyStudentGroupAtributes service = new VerifyStudentGroupAtributes();
	/**
	 * The singleton access method of this class.
	 */
	public static VerifyStudentGroupAtributes getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private VerifyStudentGroupAtributes() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "VerifyStudentGroupAtributes";
	}

	
	/**
	 * Executes the service.
	 **/

	public Boolean run(InfoStudentGroup infoStudentGroup,Integer numberOfStudentsToEnrole) throws FenixServiceException{

		IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
		IGroupProperties groupProperties = Cloner.copyInfoGroupProperties2IGroupProperties(infoStudentGroup.getInfoGroupProperties());
		IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);
		
		
		boolean result = strategy.enrolmentPolicy(groupProperties,numberOfStudentsToEnrole.intValue(),Cloner.copyInfoStudentGroup2IStudentGroup(infoStudentGroup));
		
		return new Boolean(result);
	}

}
