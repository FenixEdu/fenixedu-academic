/*
 * Created on 17/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.StudentGroup;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */

public class PrepareEditStudentGroupMembers  implements IServico {

	private static PrepareEditStudentGroupMembers service =
			new PrepareEditStudentGroupMembers();

	/**
		* The singleton access method of this class.
		*/
	public static PrepareEditStudentGroupMembers getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private PrepareEditStudentGroupMembers() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "PrepareEditStudentGroupMembers";
	}

	/**
	 * Executes the service.
	 */

	public List run(Integer executionCourseCode,Integer studentGroupCode)
		throws FenixServiceException {

		IFrequentaPersistente persistentAttend = null;
		IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
		IPersistentStudentGroup persistentStudentGroup = null;
		IPersistentExecutionCourse persistentExecutionCourse = null;
		List frequentas = new ArrayList();
		List infoStudentList = new ArrayList();
				
		try {
			
			ISuportePersistente ps = SuportePersistenteOJB.getInstance();
			persistentExecutionCourse = ps.getIDisciplinaExecucaoPersistente();
			persistentAttend = ps.getIFrequentaPersistente();
			persistentStudentGroup = ps.getIPersistentStudentGroup();
			persistentStudentGroupAttend = ps.getIPersistentStudentGroupAttend();
			
			IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(new DisciplinaExecucao(executionCourseCode),false);
			IStudentGroup studentGroup = (IStudentGroup)ps.getIPersistentStudentGroup().readByOId(new StudentGroup(studentGroupCode),false);
			
			
			frequentas = persistentAttend.readByExecutionCourse(executionCourse);
			
			List allStudentsGroups =persistentStudentGroup.readAllStudentGroupByGroupProperties(studentGroup.getGroupProperties());
			
			List allStudentGroupAttend=null;
			
			Iterator iterator = allStudentsGroups.iterator();
			while (iterator.hasNext()) 
			{
				
				allStudentGroupAttend = persistentStudentGroupAttend.readAllByStudentGroup((IStudentGroup) iterator.next()); 
				
				Iterator iterator2 = allStudentGroupAttend.iterator();
				IFrequenta frequenta = null;
				while(iterator2.hasNext())
				{
				
					frequenta =((IStudentGroupAttend)iterator2.next()).getAttend();
					if(frequentas.contains(frequenta))
					{
						frequentas.remove(frequenta);
					}
				}
			}
				
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		  }
		
		IStudent student = null;
		Iterator iterator3 = frequentas.iterator();

		while (iterator3.hasNext()) 
		{
			
			student = ((IFrequenta) iterator3.next()).getAluno();
			infoStudentList.add(Cloner.copyIStudent2InfoStudent(student));		
		}
		return infoStudentList;
		
	}
}

