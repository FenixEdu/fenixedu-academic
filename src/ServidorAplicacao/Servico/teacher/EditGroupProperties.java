/*
 * Created on 17/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoGroupProperties;
import Dominio.DisciplinaExecucao;
import Dominio.GroupProperties;
import Dominio.IDisciplinaExecucao;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author asnr and scpo
 */
public class EditGroupProperties implements IServico{
	
	private static EditGroupProperties service = new EditGroupProperties();

	/**
	* The singleton access method of this class.
	**/
	
	public static EditGroupProperties getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private EditGroupProperties() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "EditGroupProperties";
	}



	private boolean checkIfIsPossibleToEdit(InfoGroupProperties infoGroupProperties, IGroupProperties groupProperties)
		throws FenixServiceException {

		IPersistentStudentGroup persistentStudentGroup = null;
		IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
		try {
			ISuportePersistente ps = SuportePersistenteOJB.getInstance();
			persistentStudentGroup = ps.getIPersistentStudentGroup();
			persistentStudentGroupAttend = ps.getIPersistentStudentGroupAttend();
			List allStudentsGroup = persistentStudentGroup.readAllStudentGroupByGroupProperties(groupProperties);

			if(allStudentsGroup.size()>infoGroupProperties.getGroupMaximumNumber().intValue())
			{
				System.out.println("<---ENTRA NO IF");
				return false;
			}				
			Iterator iterGroups = allStudentsGroup.iterator();
			List allStudents = null;
			while (iterGroups.hasNext()) 
			{
				allStudents = new ArrayList();
				
				IStudentGroup studentGroup = (IStudentGroup) iterGroups.next();
				allStudents = persistentStudentGroupAttend.readAllByStudentGroup(studentGroup);
				System.out.println("allStudents_size"+allStudents.size());
				System.out.println("studentGroup"+studentGroup.getIdInternal());
				if(allStudents.size()>infoGroupProperties.getMaximumCapacity().intValue()||allStudents.size()<infoGroupProperties.getMinimumCapacity().intValue())
				{
					System.out.println("<---ENTRA NO 2º IF");
					return false;
				}
			}
			
		}
		catch (ExistingPersistentException excepcaoPersistencia) {
			throw new ExistingServiceException(excepcaoPersistencia);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {

			throw new FenixServiceException(excepcaoPersistencia);
		}
		return true;
	}

	
	/**
	 * Executes the service.
	 */

	public Boolean run (Integer objectCode,InfoGroupProperties infoGroupProperties)
	throws FenixServiceException {
		

		System.out.println("<-----------------ENTRA NO SERVICO");
		boolean result =false;
		try {
			ISuportePersistente ps =
				SuportePersistenteOJB.getInstance();
			
		
			IPersistentGroupProperties persistentGroupProperties =ps.getIPersistentGroupProperties();
			IDisciplinaExecucaoPersistente persistentExecutionCourse = ps.getIDisciplinaExecucaoPersistente();
			
			IGroupProperties groupProperties= new GroupProperties(infoGroupProperties.getIdInternal());	
			groupProperties=(IGroupProperties) persistentGroupProperties.readByOId(groupProperties,false);
			

			System.out.println("<-----------------ANTES DO CHECK");
			result = checkIfIsPossibleToEdit(infoGroupProperties,groupProperties);

			System.out.println("<-----------------DP DO CHECK");	
			if(result)
			{
				IDisciplinaExecucao executionCourse = (IDisciplinaExecucao)persistentExecutionCourse.readByOId(new DisciplinaExecucao(objectCode),false);
			
			
				groupProperties.setEnrolmentBeginDay(infoGroupProperties.getEnrolmentBeginDay());
				groupProperties.setEnrolmentEndDay(infoGroupProperties.getEnrolmentEndDay());
				groupProperties.setEnrolmentPolicy(infoGroupProperties.getEnrolmentPolicy());
				groupProperties.setExecutionCourse(executionCourse);
				groupProperties.setGroupMaximumNumber(infoGroupProperties.getGroupMaximumNumber());
				groupProperties.setIdealCapacity(infoGroupProperties.getIdealCapacity());
				groupProperties.setIdInternal(infoGroupProperties.getIdInternal());
				groupProperties.setMaximumCapacity(infoGroupProperties.getMaximumCapacity());
				groupProperties.setMinimumCapacity(infoGroupProperties.getMinimumCapacity());
				groupProperties.setName(infoGroupProperties.getName());
				groupProperties.setShiftType(infoGroupProperties.getShiftType());
				persistentGroupProperties.lockWrite(groupProperties);
			}
			
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		System.out.println("<-----------------SAI DO SERVICO-resultado"+result);
	return new Boolean(result);
	}
}