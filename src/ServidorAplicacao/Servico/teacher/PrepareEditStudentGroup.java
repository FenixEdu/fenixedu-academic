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

import Dominio.IDisciplinaExecucao;
import Dominio.IFrequenta;

import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.StudentGroup;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */

public class PrepareEditStudentGroup  implements IServico {

	private static PrepareEditStudentGroup service =
			new PrepareEditStudentGroup();

	/**
		* The singleton access method of this class.
		*/
	public static PrepareEditStudentGroup getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private PrepareEditStudentGroup() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "PrepareEditStudentGroup";
	}

	/**
	 * Executes the service.
	 */

	public List run(Integer executionCourseCode,Integer studentGroupCode)
		throws FenixServiceException {

		IFrequentaPersistente persistentAttend = null;
		IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
		IPersistentGroupProperties persistentGroupProperties = null;
		IPersistentStudentGroup persistentStudentGroup = null;
		IDisciplinaExecucaoPersistente persistentExecutionCourse = null;
		List frequentas = new ArrayList();
				
		try {
			ISuportePersistente ps = SuportePersistenteOJB.getInstance();
			persistentExecutionCourse = ps.getIDisciplinaExecucaoPersistente();
			persistentAttend = ps.getIFrequentaPersistente();
			persistentStudentGroup = ps.getIPersistentStudentGroup();
			persistentStudentGroupAttend = ps.getIPersistentStudentGroupAttend();
			
			IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(executionCourseCode),false);
			IStudentGroup studentGroup = (IStudentGroup)ps.getIPersistentStudentGroup().readByOId(new StudentGroup(studentGroupCode),false);
			
			frequentas = persistentAttend.readByExecutionCourse(executionCourse);
			List allStudentsGroups =persistentStudentGroup.readAllStudentGroupByGroupProperties(studentGroup.getGroupProperties());
			
			List allStudentAttend = new ArrayList();
			Iterator iterator = allStudentsGroups.iterator();
			List allStudentGroupAttend;
			int i;
			IFrequenta frequenta = null;
			while (iterator.hasNext()) 
			{
				i=0;
				allStudentGroupAttend = persistentStudentGroupAttend.readAllByStudentGroup((IStudentGroup) iterator.next()); 
				
				while(i<allStudentGroupAttend.size())
				{
					frequenta =((IStudentGroupAttend)allStudentGroupAttend.get(i)).getAttend();
					if(frequentas.contains(frequenta))
					{
						frequentas.remove(frequenta);
					}
					i++;
				}
			}
				
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		  }

		IStudent student = null;
		List infoStudentList = new ArrayList();
		Iterator iterator = frequentas.iterator();

		while (iterator.hasNext()) 
		{
			student = ((IFrequenta) iterator.next()).getAluno();
			infoStudentList.add(Cloner.copyIStudent2InfoStudent(student));		
		}

		return infoStudentList;
	}
}
