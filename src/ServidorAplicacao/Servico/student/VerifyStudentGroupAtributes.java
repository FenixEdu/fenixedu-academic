/*
 * Created on 29/Jul/2003
 *
 */

package ServidorAplicacao.Servico.student;

import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.StudentGroup;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
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

	private boolean checkGroupStudentEnrolment(Integer studentGroupCode, String username) throws FenixServiceException {

		boolean result = false;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IStudentGroup studentGroup =
				(IStudentGroup) sp.getIPersistentStudentGroup().readByOId(new StudentGroup(studentGroupCode), false);
			
			if (studentGroup == null)
				throw new FenixServiceException();

			IGroupProperties groupProperties = studentGroup.getGroupProperties();
			IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
			IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

			result = strategy.checkAlreadyEnroled(groupProperties, username);
			if (result)
				throw new InvalidSituationServiceException();

			result = strategy.checkPossibleToEnrolInExistingGroup(groupProperties, studentGroup, studentGroup.getShift());
			if (!result)
				throw new InvalidArgumentsServiceException();

		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}
		return true;
	}

	private boolean checkGroupEnrolment(Integer GroupPropertiesCode, String username) throws FenixServiceException {

		boolean result = false;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IGroupProperties groupProperties =
				(IGroupProperties) sp.getIPersistentStudentGroup().readByOId(new GroupProperties(GroupPropertiesCode), false);
			
			IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
			IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

			result = strategy.checkAlreadyEnroled(groupProperties, username);
			
			if (result)
				throw new InvalidSituationServiceException();

		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}
		return true;
	}

	private boolean checkUnEnrollStudentInGroup(Integer studentGroupCode, String username) throws FenixServiceException {

		boolean result = false;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IStudentGroup studentGroup =
				(IStudentGroup) sp.getIPersistentStudentGroup().readByOId(new StudentGroup(studentGroupCode), false);
			
			if (studentGroup == null)
				throw new FenixServiceException();

			IGroupProperties groupProperties = studentGroup.getGroupProperties();
			IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
			IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

			result = strategy.checkNotEnroledInGroup(groupProperties, studentGroup, username);
			if (result)
				throw new InvalidSituationServiceException();

			result = strategy.checkNumberOfGroupElements(groupProperties, studentGroup);
			if (!result)
				throw new InvalidArgumentsServiceException();

		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}
		return true;
	}

	private boolean checkEditStudentGroupShift(Integer studentGroupCode, String username) throws FenixServiceException {

		boolean result = false;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IStudentGroup studentGroup =
				(IStudentGroup) sp.getIPersistentStudentGroup().readByOId(new StudentGroup(studentGroupCode), false);
			
			if (studentGroup == null)
				throw new FenixServiceException();

			IGroupProperties groupProperties = studentGroup.getGroupProperties();
			IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
			IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

			result = strategy.checkNotEnroledInGroup(groupProperties, studentGroup, username);
			if (result)
				throw new InvalidSituationServiceException();
				

		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}
		return true;
	}

	/**
	 * Executes the service.
	 **/

	public boolean run(Integer groupPropertiesCode,Integer studentGroupCode, String username, Integer option)
		throws FenixServiceException {

		boolean result = false;

		switch (option.intValue()) {

			case 1 :
				result = checkGroupStudentEnrolment(studentGroupCode, username);
				return result;

			case 2 :
				result = checkGroupEnrolment(groupPropertiesCode, username);
				return result;

			case 3 :
				result = checkUnEnrollStudentInGroup(studentGroupCode, username);
				return result;

			case 4 :
				result = checkEditStudentGroupShift(studentGroupCode, username);
				return result;
		}

		return result;
	}

}
