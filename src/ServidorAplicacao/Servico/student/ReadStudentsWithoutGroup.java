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
		List infoStudentList = null;		
		try {
		
			ISuportePersistente ps = SuportePersistenteOJB.getInstance();
			IPersistentStudent persistentStudent = ps.getIPersistentStudent();
			IFrequentaPersistente persistentAttend = ps.getIFrequentaPersistente();
			IPersistentStudentGroup persistentStudentGroup = ps.getIPersistentStudentGroup();
			IPersistentStudentGroupAttend  persistentStudentGroupAttend = ps.getIPersistentStudentGroupAttend();
			
			IGroupProperties groupProperties = (IGroupProperties)ps.getIPersistentGroupProperties().readByOId(new GroupProperties(groupPropertiesCode),false);
			
				
			if(groupProperties.getEnrolmentPolicy().equals(new EnrolmentGroupPolicyType(2)))
				return null;
			else
			{
				frequentas = persistentAttend.readByExecutionCourse(groupProperties.getExecutionCourse());

				List allStudentsGroups =persistentStudentGroup.readAllStudentGroupByGroupProperties(groupProperties);
				
				userStudent = persistentStudent.readByUsername(username);
				
				List allStudentGroupAttend = new ArrayList();
				Iterator iterator = allStudentsGroups.iterator();
				IFrequenta frequenta = null;
				while (iterator.hasNext()) 
				{
					allStudentGroupAttend = persistentStudentGroupAttend.readAllByStudentGroup((IStudentGroup) iterator.next()); 
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
				IStudent student = null;
				Iterator iterStudent = frequentas.iterator();
				infoStudentList = new ArrayList();
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
