/*
 * Created on 28/Ago/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.GroupProperties;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentGroupPolicyType;

/**
 * @author asnr and scpo
 *
 */
public class ReadStudentsWithoutGroup implements IServico {

	private static ReadStudentsWithoutGroup service =
		new ReadStudentsWithoutGroup();

	/**
		* The singleton access method of this class.
		*/
	public static ReadStudentsWithoutGroup getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private ReadStudentsWithoutGroup() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "ReadStudentsWithoutGroup";
	}

	/**
	 * Executes the service.
	 */

	public List run(Integer groupPropertiesCode,String username)
		throws FenixServiceException {
		

		List frequentas = new ArrayList();
		IStudent userStudent = null;
		List infoStudentList = new ArrayList();		
		try {
		
			ISuportePersistente ps = SuportePersistenteOJB.getInstance();
			IPersistentStudent persistentStudent = ps.getIPersistentStudent();
			IFrequentaPersistente persistentAttend = ps.getIFrequentaPersistente();
			IPersistentStudentGroup persistentStudentGroup = ps.getIPersistentStudentGroup();
			IPersistentStudentGroupAttend  persistentStudentGroupAttend = ps.getIPersistentStudentGroupAttend();
			System.out.println("ENTRA NO Servico READ STUDENTS WITHOUT GROUP");
			
			IGroupProperties groupProperties = (IGroupProperties)ps.getIPersistentGroupProperties().readByOId(new GroupProperties(groupPropertiesCode),false);
			System.out.println("POLITICA"+groupProperties.getEnrolmentPolicy().toString());
			
			
			if(groupProperties.getEnrolmentPolicy().equals(new EnrolmentGroupPolicyType(1)))
			{
				System.out.println("ENTRA NO IF DO SERVICO READ STUDENTS WITHOUT GROUP");
				frequentas = persistentAttend.readByExecutionCourse(groupProperties.getExecutionCourse());

				System.out.println("FREQUENTAS"+frequentas.size());
				List allStudentsGroups =persistentStudentGroup.readAllStudentGroupByGroupProperties(groupProperties);
				System.out.println("ALL-STUDENTS-GROUP"+allStudentsGroups.size());
				
				userStudent = persistentStudent.readByUsername(username);
				//IFrequenta userAttend = persistentAttend.readByAlunoAndDisciplinaExecucao(userStudent,groupProperties.getExecutionCourse());
				System.out.println("USER_STUDENT"+userStudent.getNumber());
				
				List allStudentGroupAttend = new ArrayList();
				Iterator iterator = allStudentsGroups.iterator();
				IFrequenta frequenta = null;
				while (iterator.hasNext()) 
				{
					allStudentGroupAttend = persistentStudentGroupAttend.readAllByStudentGroup((IStudentGroup) iterator.next()); 
					System.out.println("ALL-STUDENTS-BY-GROUP"+allStudentGroupAttend.size());
					Iterator iter = allStudentGroupAttend.iterator();
					while(iter.hasNext())
					{
						frequenta =((IStudentGroupAttend)iter.next()).getAttend();
						if(frequentas.contains(frequenta))
						{
							frequentas.remove(frequenta);
						}
					}
				}
				System.out.println("frequentas depois"+frequentas.size());
				IStudent student = null;
				Iterator iterStudent = frequentas.iterator();
		
				while (iterStudent.hasNext()) 
				{
					student = ((IFrequenta) iterStudent.next()).getAluno();
					if(!student.equals(userStudent))
						infoStudentList.add(Cloner.copyIStudent2InfoStudent(student));		
				}
			}
		
			} catch (ExcepcaoPersistencia excepcaoPersistencia) {
				throw new FenixServiceException(excepcaoPersistencia.getMessage());
		  	}

		return infoStudentList;
	}
}
