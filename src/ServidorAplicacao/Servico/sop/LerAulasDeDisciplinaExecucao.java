/*
 * LerAulasDeDisciplinaExecucao.java
 *
 * Created on 27 de Outubro de 2002, 23:09
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerAulasDeDisciplinaExecucao.
 *
 * @author tfc130
 **/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerAulasDeDisciplinaExecucao implements IServico {

	private static LerAulasDeDisciplinaExecucao _servico =
		new LerAulasDeDisciplinaExecucao();
	/**
	 * The singleton access method of this class.
	 **/
	public static LerAulasDeDisciplinaExecucao getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private LerAulasDeDisciplinaExecucao() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "LerAulasDeDisciplinaExecucao";
	}

	public Object run(InfoExecutionCourse infoExecutionCourse) {

		ArrayList infoLessonList = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse executionCourseDAO =
				sp.getIPersistentExecutionCourse();

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					infoExecutionCourse.getInfoExecutionPeriod());

			IExecutionCourse executionCourse =
				executionCourseDAO
					.readByExecutionCourseInitialsAndExecutionPeriod(
					infoExecutionCourse.getSigla(),
					executionPeriod);

//			List aulas = sp.getIAulaPersistente().readByExecutionCourse(executionCourse);
		  	List aulas = new ArrayList();
			
		    List shifts = sp.getITurnoPersistente().readByExecutionCourse(executionCourse);
		    for (int i=0; i < shifts.size(); i++)
		    {
		    	ITurno shift = (ITurno) shifts.get(i);
			  	List aulasTemp = sp.getIAulaPersistente().readLessonsByShift(shift);
             	
			  	aulas.addAll(aulasTemp);  
		    }
			
			Iterator iterator = aulas.iterator();
			infoLessonList = new ArrayList();
			while (iterator.hasNext()) {
				IAula elem = (IAula) iterator.next();
				InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);
				ITurno shift = elem.getShift();
				InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
				infoLesson.setInfoShift(infoShift);
				
				infoLessonList.add(infoLesson);
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}

		return infoLessonList;
	}

}