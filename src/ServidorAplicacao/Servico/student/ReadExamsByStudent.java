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
import Dominio.IExamEnrollment;
import Dominio.IFrequenta;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.utils.ExamsNotEnrolledPredicate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExamEnrollment;
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
			IPersistentExamEnrollment persistentExamEnrollment =
				sp.getIPersistentExamEnrollment();
			if (student != null) {
				List examsEnrolled = student.getExamsEnrolled();

				Iterator iter1 = examsEnrolled.iterator();
				while (iter1.hasNext()) {
					IExam exam = (IExam) iter1.next();

					IExamEnrollment examEnrollment =
						persistentExamEnrollment.readIExamEnrollmentByExam(
							exam);
					if (isInDate(examEnrollment)) {
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
					IExamEnrollment examEnrollment =
						persistentExamEnrollment.readIExamEnrollmentByExam(
							exam);
					if (examEnrollment != null && isInDate(examEnrollment)) {
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
	private boolean isInDate(IExamEnrollment examEnrollment) {

		return (
			Calendar.getInstance().getTimeInMillis()
				< examEnrollment.getEndDate().getTimeInMillis()
				&& Calendar.getInstance().getTimeInMillis()
					> examEnrollment.getBeginDate().getTimeInMillis());
	}

}
