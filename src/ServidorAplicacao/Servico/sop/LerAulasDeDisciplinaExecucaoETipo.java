/*
 * LerAulasDeDisciplinaExecucaoETipo.java
 *
 * Created on 28 de Outubro de 2002, 18:03
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerAulasDeDisciplinaExecucaoETipo.
 *
 * @author tfc130
 **/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ExecutionCourseKeyAndLessonType;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.IExecutionCourse;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerAulasDeDisciplinaExecucaoETipo implements IServico {

	private static LerAulasDeDisciplinaExecucaoETipo _servico =
		new LerAulasDeDisciplinaExecucaoETipo();
	/**
	 * The singleton access method of this class.
	 **/
	public static LerAulasDeDisciplinaExecucaoETipo getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private LerAulasDeDisciplinaExecucaoETipo() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "LerAulasDeDisciplinaExecucaoETipo";
	}

	public Object run(ExecutionCourseKeyAndLessonType tipoAulaAndKeyDisciplinaExecucao, InfoExecutionCourse infoExecutionCourse) {

		ArrayList infoAulas = null;

		try {
			
			IExecutionCourse executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			List aulas =
				sp.getIAulaPersistente().readByExecutionCourseAndLessonType(
					executionCourse,
					tipoAulaAndKeyDisciplinaExecucao.getTipoAula());

			Iterator iterator = aulas.iterator();
			infoAulas = new ArrayList();
			while (iterator.hasNext()) {
				IAula lesson = (IAula) iterator.next();
				InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(lesson);
				infoAulas.add(infoLesson);
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}
		return infoAulas;
	}

}