package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import DataBeans.ISiteComponent;
import DataBeans.InfoExam;
import DataBeans.InfoStudentSiteExams;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.IExam;
import Dominio.IFrequenta;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.utils.ExamsNotEnrolledPredicate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 */

public class ReadExamsByStudent implements IServico {

	private static ReadExamsByStudent _servico = new ReadExamsByStudent();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadExamsByStudent getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadExamsByStudent() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ReadExamsByStudent";
	}

	//TODO: filtrar os exames por período de inscrição

	public Object run(String username) {

		List examsToEnroll = new ArrayList();

		List infoExamsEnrolled = new ArrayList();
		List infoExamsToEnroll = new ArrayList();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IStudent student =
				(IStudent) sp.getIPersistentStudent().readByUsername(username);

			if (student != null) {
				List examsEnrolled = student.getExamsEnrolled();

				Iterator iter1 = examsEnrolled.iterator();
				while (iter1.hasNext()) {
					IExam exam = (IExam) iter1.next();

					if (isInDate(exam)) {
						InfoExam infoExam = Cloner.copyIExam2InfoExam(exam);
						infoExam.setInfoExecutionCourse(
							Cloner.copyIExecutionCourse2InfoExecutionCourse(
								(IDisciplinaExecucao) exam
									.getAssociatedExecutionCourses()
									.get(
									0)));
						infoExamsEnrolled.add(infoExam);
					}

				}

				List attends =
					sp.getIFrequentaPersistente().readByStudentId(
						student.getNumber());
				Iterator iter2 = attends.iterator();
				while (iter2.hasNext()) {
					examsToEnroll.addAll(
						((IFrequenta) iter2.next())
							.getDisciplinaExecucao()
							.getAssociatedExams());
				}

				CollectionUtils.filter(
					examsToEnroll,
					new ExamsNotEnrolledPredicate(examsEnrolled));

				Iterator iter3 = examsToEnroll.iterator();
				while (iter3.hasNext()) {
					IExam exam = (IExam) iter3.next();

					if (isInDate(exam)) {
						InfoExam infoExam = Cloner.copyIExam2InfoExam(exam);
						infoExam.setInfoExecutionCourse(
							Cloner.copyIExecutionCourse2InfoExecutionCourse(
								(IDisciplinaExecucao) exam
									.getAssociatedExecutionCourses()
									.get(
									0)));
						infoExamsToEnroll.add(infoExam);
					}
				}

			}

		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		}

		ISiteComponent component =
			new InfoStudentSiteExams(infoExamsToEnroll, infoExamsEnrolled);
		SiteView siteView = new SiteView(component);
		return siteView;

	}

	/**
	 * @param examEnrollment
	 * @return
	 */
	private boolean isInDate(IExam exam) {
		if (exam.getEnrollmentBeginDay() == null
			|| exam.getEnrollmentEndDay() == null
			|| exam.getEnrollmentBeginTime() == null
			|| exam.getEnrollmentEndTime() == null) {
			return false;
		} else {
		Calendar begin = Calendar.getInstance();
		begin.set(Calendar.YEAR,exam.getEnrollmentBeginDay().get(Calendar.YEAR));
		begin.set(Calendar.MONTH,exam.getEnrollmentBeginDay().get(Calendar.MONTH));
		begin.set(Calendar.DAY_OF_MONTH,exam.getEnrollmentBeginDay().get(Calendar.DAY_OF_MONTH));
		begin.set(Calendar.HOUR_OF_DAY,exam.getEnrollmentBeginTime().get(Calendar.HOUR_OF_DAY));
		begin.set(Calendar.MINUTE,exam.getEnrollmentBeginTime().get(Calendar.MINUTE));
		
		Calendar end = Calendar.getInstance();
		end.set(Calendar.YEAR,exam.getEnrollmentEndDay().get(Calendar.YEAR));
		end.set(Calendar.MONTH,exam.getEnrollmentEndDay().get(Calendar.MONTH));
		end.set(Calendar.DAY_OF_MONTH,exam.getEnrollmentEndDay().get(Calendar.DAY_OF_MONTH));
		end.set(Calendar.HOUR_OF_DAY,exam.getEnrollmentEndTime().get(Calendar.HOUR_OF_DAY));
		end.set(Calendar.MINUTE,exam.getEnrollmentEndTime().get(Calendar.MINUTE));
			return (
				Calendar.getInstance().getTimeInMillis()
					< end.getTimeInMillis()
					&& Calendar.getInstance().getTimeInMillis()
						> begin.getTimeInMillis());
		}
	}

}
