/*
 * Created on 28/Ago/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.Frequenta;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadExecutionCoursesByStudent implements IServico {

	private static ReadExecutionCoursesByStudent service =
		new ReadExecutionCoursesByStudent();

	public static ReadExecutionCoursesByStudent getService() {
		return service;
	}

	public String getNome() {
		return "ReadExecutionCoursesByStudent";
	}

	public Object run(String userName) throws FenixServiceException {
		List result = new ArrayList();
		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			IStudent student =
				persistentSuport.getIPersistentStudent().readByUsername(
					userName);

			List attendList =
				persistentSuport
					.getIFrequentaPersistente()
					.readByStudentNumber(
					student.getNumber());

			Iterator it = attendList.iterator();
			while (it.hasNext()) {
				IFrequenta attend = (Frequenta) it.next();
				IExecutionCourse executionCourse =
					attend.getDisciplinaExecucao();
				result.add(
					Cloner.get(
						executionCourse));
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return result;
	}
}
