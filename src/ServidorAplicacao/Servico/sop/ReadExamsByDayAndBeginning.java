/*
 * ReadExamsByDayAndBeginning.java
 *
 * Created on 2003/03/19
 */

package ServidorAplicacao.Servico.sop;

/**
  * @author Luis Cruz & Sara Ribeiro
  * 
 **/
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import DataBeans.InfoExam;
import DataBeans.InfoViewExamByDayAndShift;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import Dominio.IExam;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadExamsByDayAndBeginning implements IServico {

	private static ReadExamsByDayAndBeginning _servico =
		new ReadExamsByDayAndBeginning();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadExamsByDayAndBeginning getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadExamsByDayAndBeginning() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ReadExamsByDayAndBeginning";
	}

	public List run(Date day, Calendar beginning) {
		ArrayList infoViewExams = new ArrayList();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			// Read exams on requested day and start-time
			List exams = sp.getIPersistentExam().readBy(day, beginning);
			
			IExam tempExam = null;
			InfoExam tempInfoExam = null;
			List tempAssociatedCurricularCourses = null;
			ICurso tempDegree = null;
			List tempInfoDegrees = null;
			Integer numberStudentesAttendingCourse = null;

			// For every exam return:
			//    - exam info
			//    - degrees associated with exam
			//    - number of students attending course (potentially attending exam)
			for (int i = 0; i < exams.size(); i++) {
				// prepare exam info
				tempExam = (IExam) exams.get(i);
				tempInfoExam = Cloner.copyIExam2InfoExam(tempExam);
				
				// prepare degrees associated with exam
				tempInfoDegrees = new ArrayList();
				tempAssociatedCurricularCourses = tempExam.getExecutionCourse().getAssociatedCurricularCourses();
				for (int j = 0; j < tempAssociatedCurricularCourses.size(); j++) {
					tempDegree = ((ICurricularCourse) tempAssociatedCurricularCourses.get(j)).getDegreeCurricularPlan().getDegree();
					tempInfoDegrees.add(Cloner.copyIDegree2InfoDegree(tempDegree));
				}

				// determine number of students attending course
				numberStudentesAttendingCourse = sp.getIFrequentaPersistente().countStudentsAttendingExecutionCourse(tempExam.getExecutionCourse());

				// add exam and degree info to result list
				infoViewExams.add(new InfoViewExamByDayAndShift(tempInfoExam, tempInfoDegrees, numberStudentesAttendingCourse));
			}

		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}

		return infoViewExams;
	}

}