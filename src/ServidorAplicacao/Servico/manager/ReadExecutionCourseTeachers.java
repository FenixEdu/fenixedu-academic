/*
 * Created on 16/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadExecutionCourseTeachers implements IServico {

	private static ReadExecutionCourseTeachers service = new ReadExecutionCourseTeachers();

	/**
	 * The singleton access method of this class.
	 */
	public static ReadExecutionCourseTeachers getService() {
		return service;
	}

	/**
	 * The constructor of this class.
	 */
	private ReadExecutionCourseTeachers() {
	}

	/**
	 * Service name
	 */
	public final String getNome() {
		return "ReadExecutionCourseTeachers";
	}

	/**
	 * Executes the service. Returns the current collection of infoCurricularCourses.
	 */

	//ver excepcoes
	public List run(Integer executionCourseId) throws FenixServiceException {
		ISuportePersistente sp;
		
		List infoTeachers = new ArrayList();
		try {
			sp = SuportePersistenteOJB.getInstance();
//			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			
			//como todos os prof responsaveis leccionam obrigatóriamente basta listar os que leccionam
			//list of Professorships
			List professorShips =
				(List) sp.getIDisciplinaExecucaoPersistente().readExecutionCourseTeachers(
					executionCourseId);
					
			Iterator professorShipsIter = professorShips.iterator();
			
			while(professorShipsIter.hasNext()){
				ITeacher teacher = ((IProfessorship) professorShipsIter.next()).getTeacher();
				infoTeachers.add(Cloner.copyITeacher2InfoTeacher(teacher));
			}
			



//System.out.println("AAAAAAAAAAAAaateachers"+teachers);
//
//			List teachersInChargeIds =
//				(List) sp
//					.getIDisciplinaExecucaoPersistente()
//					.readExecutionCourseTeachersInCharge(
//					executionCourseId);
//			System.out.println("AAAAAAAAAAAAteachersInChargeIds"+teachersInChargeIds);
//	
//			Iterator iter1 = teachers.iterator();
//
//			while (iter1.hasNext()) {
//
//				Teacher teacherToRead = new Teacher();
//				teacherToRead.setIdInternal((Integer) iter1.next());
//				infoTeachers.add(
//					Cloner.copyITeacher2InfoTeacher(
//						(ITeacher) persistentTeacher.readByOId(teacherToRead, false)));
//
//				Iterator iter2 = teachersInChargeIds.iterator();
//
//				while (iter2.hasNext()) {
//
//					if (!(((Integer) iter1.next()).equals((Integer) iter2.next()))) {
//						teacherToRead.setIdInternal((Integer) iter2.next());
//						infoTeachers.add(
//							Cloner.copyITeacher2InfoTeacher(
//								(ITeacher) persistentTeacher.readByOId(teacherToRead, false)));
//
//					}
//
//				}
//
//			}

			

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
		
		return infoTeachers;
	}
}