/*
 * Created on 21/Jul/2003
 *
 * 
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ISummary;
import Dominio.Summary;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

/**
 * @author João Mota
 * @author Susana Fernandes
 *
 * 21/Jul/2003
 * fenix-head
 * ServidorAplicacao.Servico.teacher
 * 
 */
public class InsertSummary implements IServico {

	private static InsertSummary service = new InsertSummary();

	public static InsertSummary getService() {

		return service;
	}

	/**
	 * 
	 */
	public InsertSummary() {
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "InsertSummary";
	}

	public boolean run(Integer executionCourseId,
	Calendar summaryDate,
	Calendar summaryHour,
	Integer summaryType,
	String title,
	String summaryText ) throws FenixServiceException {

		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			IPersistentExecutionCourse persistentExecutionCourse =
				persistentSuport.getIPersistentExecutionCourse();
			IExecutionCourse executionCourse =
				new ExecutionCourse(executionCourseId);
			executionCourse =
				(IExecutionCourse) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			if (executionCourse == null) {
				throw new InvalidArgumentsServiceException();
			}
			IPersistentSummary persistentSummary =
				persistentSuport.getIPersistentSummary();
				
			ISummary summary = new Summary();
			summary.setExecutionCourse(executionCourse);
			summaryHour.set(Calendar.SECOND,0);
			summary.setSummaryDate(summaryDate);
			summary.setSummaryHour(summaryHour);
			summary.setSummaryType(new TipoAula(summaryType));
			summary.setTitle(title);
			summary.setSummaryText(summaryText);		
			summary.setLastModifiedDate(null);			
			
			persistentSummary.simpleLockWrite(summary);
			return true;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

}
