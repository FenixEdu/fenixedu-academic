/*
 * Created on 12/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.GroupProperties;
import Dominio.IDisciplinaExecucao;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
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
 * @author ansr and scpo
 *
 */
public class PrepareCreateStudentGroup implements IServico {

	private static PrepareCreateStudentGroup service =
		new PrepareCreateStudentGroup();

	/**
		* The singleton access method of this class.
		*/
	public static PrepareCreateStudentGroup getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private PrepareCreateStudentGroup() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "PrepareCreateStudentGroup";
	}

	/**
	 * Executes the service.
	 */

	public List run(Integer executionCourseCode,Integer groupPropertiesCode)
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
			IGroupProperties groupProperties = (IGroupProperties)ps.getIPersistentGroupProperties().readByOId(new GroupProperties(groupPropertiesCode),false);
			
			frequentas = persistentAttend.readByExecutionCourse(executionCourse);
			List allStudentsGroups =persistentStudentGroup.readAllStudentGroupByGroupProperties(groupProperties);
			
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
