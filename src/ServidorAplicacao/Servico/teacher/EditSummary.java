/*
 * Created on 21/Jul/2003
 * 
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ISummary;
import Dominio.Summary;
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
 * 21/Jul/2003 fenix-head ServidorAplicacao.Servico.teacher
 *  
 */
public class EditSummary implements IService
{

    private static EditSummary service = new EditSummary();

    public static EditSummary getService()
    {

        return service;
    }

    /**
	 *  
	 */
    public EditSummary()
    {
    }

    public void run(
        Integer executionCourseId,
        Integer summaryId,
        Calendar summaryDate,
        Calendar summaryHour,
        Integer summaryType,
        String title,
        String summaryText)
        throws FenixServiceException
    {

        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            //OJB cannot upgrade the read lock to a write lock, so its necessary to open and close 
            //the transaction
//            persistentSuport.confirmarTransaccao();
//            persistentSuport.iniciarTransaccao();
            
            IPersistentSummary persistentSummary = persistentSuport.getIPersistentSummary();

            ISummary  summary  = (ISummary) persistentSummary.readByOId(new Summary(summaryId), true);
            System.out.println("service->"+summary);
            summaryHour.set(Calendar.SECOND, 0);
            summary.setSummaryDate(summaryDate);
            summary.setSummaryHour(summaryHour);
            summary.setSummaryType(new TipoAula(summaryType));
            summary.setTitle(title);
            summary.setSummaryText(summaryText);
            summary.setLastModifiedDate(null);

        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }

}
