/*
 * Created on 17/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import DataBeans.InfoSiteAllGroups;
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
 * @author lmac
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class EditStudentGroup implements IServico {

	private IPersistentStudentGroup persistentStudentGroup = null;
	
	private static EditStudentGroup service = new EditStudentGroup();

	/**
		* The singleton access method of this class.
		*/
	public static EditStudentGroup getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private EditStudentGroup() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "EditStudentGroup";
	}

	
	/**
	 * Executes the service.
	 */

	public Boolean run(List studentCodes,Integer studentGroupCode)
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
			//IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperites.readByOId(new GroupProperties(groupPropertiesCode),false);		
			
			persistentShift = persistentSupport.getITurnoPersistente();
			//ITurno shift = (ITurno) persistentShift.readByOId(new Turno(shiftCode),false);
						
			persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
			//IStudentGroup newStudentGroup = new StudentGroup(groupNumber, groupProperties, shift);
			//persistentStudentGroup.lockWrite(newStudentGroup);
					
			persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();
			//IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(executionCourseCode),false);
			
			persistentStudent = persistentSupport.getIPersistentStudent();
			persistentAttend = persistentSupport.getIFrequentaPersistente();
			persistentStudentGroupAttend = persistentSupport.getIPersistentStudentGroupAttend();
			
			Iterator iterator = studentCodes.iterator();
			
			while (iterator.hasNext()) 
			{
				IStudent student = (IStudent) persistentStudent.readByOId(new Student((Integer) iterator.next()),false);
						
			//	IFrequenta attend = persistentAttend.readByAlunoAndDisciplinaExecucao(student,executionCourse);
						
			//	IStudentGroupAttend newStudentGroupAttend = new StudentGroupAttend(newStudentGroup, attend);
											
			//	persistentStudentGroupAttend.lockWrite(newStudentGroupAttend);	
		
			}
				
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());			
		  }
		//  catch (ExistingServiceException excepcao) {
			 //throw new FenixServiceException(excepcao.getMessage());
			// return new Boolean(false);			
		  //}
		return new Boolean(true);
	}
}
