/*
 * Created on 29/Jul/2003
 *
 */
 
package ServidorAplicacao.Servico.student;

import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.StudentGroup;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
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

	public Boolean run(Integer studentGroupCode,String userName) 
	throws FenixServiceException{
		
		boolean result= false;
		
		IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
		try{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			
			IStudentGroup studentGroup =(IStudentGroup)sp.getIPersistentStudentGroup().readByOId(new StudentGroup(studentGroupCode),false);
			IGroupProperties groupProperties = studentGroup.getGroupProperties();
			
			IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);
			result = strategy.checkPossibleToEnrolInExistingGroup(groupProperties,studentGroup,studentGroup.getShift());
		
		//boolean result = strategy.enrolmentPolicy(groupProperties,numberOfStudentsToEnrole.intValue(),studentGroup,studentGroup.getShift());
		
		} catch (ExcepcaoPersistencia ex) {
		ex.printStackTrace();
		}
		return new Boolean(result);
	}

}
