/*
 * ReadExamsByExecutionCourse.java
 *
 * Created on 2003/05/26
 */

package ServidorAplicacao.Servico.sop;

/**
  * @author Luis Cruz & Sara Ribeiro
  * 
 **/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.InfoViewClassSchedule;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.IExecutionPeriod;
import Dominio.ITurma;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurnoAulaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

public class ReadAllClassesLessons implements IServico {

	private static ReadAllClassesLessons _servico = new ReadAllClassesLessons();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadAllClassesLessons getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadAllClassesLessons() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ReadAllClassesLessons";
	}

	public List run(InfoExecutionPeriod infoExecutionPeriod) {

		List infoViewClassScheduleList = new ArrayList();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					infoExecutionPeriod);

			ITurmaPersistente classDAO = sp.getITurmaPersistente();
			IAulaPersistente lessonDAO = sp.getIAulaPersistente();			
			ITurnoAulaPersistente shiftLessonDAO = sp.getITurnoAulaPersistente();
			
			List classes =
				classDAO.readByExecutionPeriodAndDegreeType(
					executionPeriod,
					new TipoCurso(TipoCurso.LICENCIATURA));

			for (int i = 0; i < classes.size(); i++) {
				InfoViewClassSchedule infoViewClassSchedule =
					new InfoViewClassSchedule();
				ITurma turma = (ITurma) classes.get(i);
     
     			// read class lessons
				List shiftList = sp.getITurmaTurnoPersistente().readByClass(turma);
				Iterator iterator = shiftList.iterator();
				List infoLessonList = new ArrayList();
				while (iterator.hasNext()) {
				  ITurno shift = (ITurno) iterator.next();
				  InfoShift infoShift = Cloner.copyIShift2InfoShift(shift);
				  List lessonList = shiftLessonDAO.readByShift(shift);
				  Iterator lessonIterator = lessonList.iterator();		
				  while(lessonIterator.hasNext()) {
					IAula elem = (IAula)lessonIterator.next();
					InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);
					infoLesson.getInfoShiftList().add(infoShift);
					infoLessonList.add(infoLesson);
				  }
				}

				infoViewClassSchedule.setInfoClass(Cloner.copyClass2InfoClass(turma));
				infoViewClassSchedule.setClassLessons(infoLessonList);				
				infoViewClassScheduleList.add(infoViewClassSchedule);
			}

	} catch (ExcepcaoPersistencia ex) {
		ex.printStackTrace();
	}
	return infoViewClassScheduleList;
}
}