package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoExam;
import DataBeans.InfoSiteTeacherStudentsEnrolledList;
import DataBeans.InfoStudent;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.Exam;
import Dominio.IExam;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 */
public class ReadStudentsEnrolledInExam implements IServico {
	private static ReadStudentsEnrolledInExam service =
		new ReadStudentsEnrolledInExam();

	/**
	 * The singleton access method of this class.
	 */
	public static ReadStudentsEnrolledInExam getService() {
		return service;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadStudentsEnrolledInExam";
	}

	public Object run(Integer executionCourseCode, Integer examCode)
		throws FenixServiceException {
		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExam persistentExam = sp.getIPersistentExam();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			IExam exam = new Exam();
			exam.setIdInternal(examCode);
			exam = (IExam) persistentExam.readByOId(exam);
			List students = exam.getStudentsEnrolled();
			List infoStudents = new ArrayList();
			Iterator iter = students.iterator();
			while (iter.hasNext()) {
				IStudent student = (IStudent) iter.next();
				InfoStudent infoStudent =
					Cloner.copyIStudent2InfoStudent(student);
				infoStudents.add(infoStudent);
			}
			InfoExam infoExam = Cloner.copyIExam2InfoExam(exam);
			ISiteComponent component =
				new InfoSiteTeacherStudentsEnrolledList(infoStudents,infoExam);

			SiteView siteView = new SiteView(component);
			return siteView;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}
}
