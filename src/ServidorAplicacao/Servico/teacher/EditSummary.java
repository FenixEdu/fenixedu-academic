/*
 * Created on 21/Jul/2003
 *
 * 
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;

import Dominio.ISummary;
import Dominio.Summary;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
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
public class EditSummary implements IServico {

	private static EditSummary service = new EditSummary();

	public static EditSummary getService() {

		return service;
	}

	/**
	 * 
	 */
	public EditSummary() {
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "EditSummary";
	}

	public void run(
	Integer executionCourseId,
	Integer summaryId,
	Calendar summaryDate,
	Calendar summaryHour,
	Integer summaryType,
	String title,
	String summaryText ) throws FenixServiceException {

		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			
		
			IPersistentSummary persistentSummary =
				persistentSuport.getIPersistentSummary();
			
			ISummary summary= new Summary(summaryId);	
			summary=(ISummary) persistentSummary.readByOId(summary,true);	
			
			
			summaryHour.set(Calendar.SECOND,0);
			summary.setSummaryDate(summaryDate);
			summary.setSummaryHour(summaryHour);
			summary.setSummaryType(new TipoAula(summaryType));
			summary.setTitle(title);
			summary.setSummaryText(summaryText);		
			summary.setLastModifiedDate(null);			
			
			

		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}

}
