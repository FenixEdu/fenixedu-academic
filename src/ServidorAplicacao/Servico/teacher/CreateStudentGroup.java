/*
 * Created on 8/Ago/2003
 *
 */

package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import Dominio.DisciplinaExecucao;
import Dominio.GroupProperties;
import Dominio.IDisciplinaExecucao;
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
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author ansr & scpo
 *
 */

public class CreateStudentGroup implements IServico {

	private IPersistentStudentGroup persistentStudentGroup = null;
	
	private static CreateStudentGroup service = new CreateStudentGroup();

	/**
		* The singleton access method of this class.
		*/
	public static CreateStudentGroup getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private CreateStudentGroup() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "CreateStudentGroup";
	}

	private void checkIfStudentGroupExists(
		Integer groupNumber,
		IGroupProperties groupProperties)
		throws FenixServiceException {

		IStudentGroup studentGroup = null;
		
		try {
			
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
			
			studentGroup =
				persistentStudentGroup
					.readStudentGroupByGroupPropertiesAndGroupNumber(
					groupProperties,
					groupNumber);
		
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}

		if (studentGroup != null)
			throw new ExistingServiceException();		
	}

	/**
	 * Executes the service.
	 */

	public void run(Integer groupNumber, Integer groupPropertiesCode, Integer shiftCode,List studentCodes,Integer executionCourseCode)
		throws FenixServiceException {

		IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
		IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
		IPersistentGroupProperties persistentGroupProperites = null;
		IPersistentStudent persistentStudent = null;
		ITurnoPersistente persistentShift = null;
		IFrequentaPersistente persistentAttend = null;
		IPersistentStudentGroup persistentStudentGroup = null;
		
		
		try {
		
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
		
			persistentGroupProperites = persistentSupport.getIPersistentGroupProperties();
			IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperites.readByOId(new GroupProperties(groupPropertiesCode),false);		
			
			ITurno shift = null;
			if(shiftCode!=null)
			{
				persistentShift = persistentSupport.getITurnoPersistente();
				shift = (ITurno) persistentShift.readByOId(new Turno(shiftCode),false);
			}
						
			checkIfStudentGroupExists(groupNumber, groupProperties);
			
			
			persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
			IStudentGroup newStudentGroup = new StudentGroup(groupNumber, groupProperties, shift);
			persistentStudentGroup.lockWrite(newStudentGroup);
					
			persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(executionCourseCode),false);
			
			persistentStudent = persistentSupport.getIPersistentStudent();
			persistentAttend = persistentSupport.getIFrequentaPersistente();
			persistentStudentGroupAttend = persistentSupport.getIPersistentStudentGroupAttend();
			
			Iterator iterator = studentCodes.iterator();
			
			while (iterator.hasNext()) 
			{
				IStudent student = (IStudent) persistentStudent.readByOId(new Student((Integer) iterator.next()),false);
				IFrequenta attend = persistentAttend.readByAlunoAndDisciplinaExecucao(student,executionCourse);
				IStudentGroupAttend newStudentGroupAttend = new StudentGroupAttend(newStudentGroup, attend);
				persistentStudentGroupAttend.lockWrite(newStudentGroupAttend);	
		
			}
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());			
		  }
		 }
}
