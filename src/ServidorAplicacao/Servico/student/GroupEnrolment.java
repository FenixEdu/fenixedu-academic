/*
 * Created on 28/Ago/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import Dominio.GroupProperties;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.ITurno;
import Dominio.Student;
import Dominio.StudentGroup;
import Dominio.StudentGroupAttend;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */
public class GroupEnrolment implements IServico {

	private static GroupEnrolment _servico = new GroupEnrolment();
	/**
	 * The singleton access method of this class.
	 **/
	public static GroupEnrolment getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private GroupEnrolment() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "GroupEnrolment";
	}

	public Integer run(Integer groupPropertiesCode,Integer shiftCode,List studentCodes,String username)
		throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentStudentGroupAttend persistentStudentGroupAttend = sp.getIPersistentStudentGroupAttend();
			IPersistentStudentGroup persistentStudentGroup = sp.getIPersistentStudentGroup();
			IPersistentStudent persistentStudent = sp.getIPersistentStudent();
			IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
			
			
			IGroupProperties groupProperties =(IGroupProperties)sp.getIPersistentGroupProperties().readByOId(new GroupProperties(groupPropertiesCode),false);
			ITurno shift = (ITurno)sp.getITurnoPersistente().readByOId(new Turno(shiftCode),false);
			
			IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
			IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);
			
			boolean result = strategy.checkNumberOfGroups(groupProperties,shift);
			if(!result)
				return new Integer(-1);
			
			result =strategy.enrolmentPolicyNewGroup(groupProperties,studentCodes.size(),shift);
			if(!result)
				return new Integer(-2);
			
			
			List allStudentGroup = new ArrayList();
			allStudentGroup = persistentStudentGroup.readAllStudentGroupByGroupProperties(groupProperties);
			
			Integer groupNumber = new Integer(1);
			if(allStudentGroup.size()!=0)
			{
				Collections.sort(allStudentGroup,new BeanComparator("groupNumber"));
				Integer lastGroupNumber =((IStudentGroup)allStudentGroup.get(allStudentGroup.size()-1)).getGroupNumber();
				groupNumber = new Integer(lastGroupNumber.intValue()+1);
			
			}
			
			IStudentGroup newStudentGroup =persistentStudentGroup.readStudentGroupByGroupPropertiesAndGroupNumber(groupProperties,groupNumber);
			
			if(newStudentGroup!=null)
				return new Integer(0);
			
			newStudentGroup = new StudentGroup(groupNumber,groupProperties,shift);
			persistentStudentGroup.lockWrite(newStudentGroup);
			
			IStudentGroupAttend newStudentGroupAttend =null;
			
			Iterator iterator = studentCodes.iterator();
			while (iterator.hasNext()) 
			{
				IStudent student = (IStudent) persistentStudent.readByOId(new Student((Integer) iterator.next()),false);
						
				IFrequenta attend = persistentAttend.readByAlunoAndDisciplinaExecucao(student,groupProperties.getExecutionCourse());
				
					
				newStudentGroupAttend = persistentStudentGroupAttend.readBy(newStudentGroup,attend);
				if(newStudentGroupAttend!=null)
					return new Integer(0);
					
				newStudentGroupAttend = new StudentGroupAttend(newStudentGroup, attend);
											
				persistentStudentGroupAttend.lockWrite(newStudentGroupAttend);	
		
			}
			IStudent userStudent = (IStudent) sp.getIPersistentStudent().readByUsername(username);
			IFrequenta userAttend = (IFrequenta)sp.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(userStudent,groupProperties.getExecutionCourse());
			
			newStudentGroupAttend = persistentStudentGroupAttend.readBy(newStudentGroup,userAttend);
			if(newStudentGroupAttend!=null)
				return new Integer(0);
					
			newStudentGroupAttend = new StudentGroupAttend(newStudentGroup, userAttend);
											
			persistentStudentGroupAttend.lockWrite(newStudentGroupAttend);	
		
			
			} catch (ExcepcaoPersistencia ex) {
				ex.printStackTrace();
			}
	
		return new Integer(1);
	}

}
