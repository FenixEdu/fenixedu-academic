/*
 * Created on 12/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package ServidorAplicacao.Servico.gesdis;

import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.gesdis.ICourseReport;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.gesdis.IPersistentCourseReport;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class EditCourseInformation implements IServico {

	private static EditCourseInformation service = new EditCourseInformation();
	
	/**
	 * The singleton access method of this class.
	 */
	public static EditCourseInformation getService() {
		return service;
	}
	
	/**
	 * The constructor of this class.
	 */
	private EditCourseInformation() {
	}
	
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "EditCourseInformation";
	}

	/**
	 * Executes the service.
	 */
	public Boolean run(Integer executionCourseId, String report)
		throws FenixServiceException {

		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();

			IDisciplinaExecucao executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					new DisciplinaExecucao(executionCourseId),
					false);

			IPersistentCourseReport persistentCourseReport =
				sp.getIPersistentCourseReport();

			ICourseReport courseReport = persistentCourseReport.readCourseReportByExecutionCourse(executionCourse);

			persistentCourseReport.simpleLockWrite(courseReport);
			courseReport.setReport(report);
			

			return new Boolean(true);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}
