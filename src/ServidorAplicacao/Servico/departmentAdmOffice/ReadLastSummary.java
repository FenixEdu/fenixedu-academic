/*
 * Created on Nov 18, 2004
 */

package ServidorAplicacao.Servico.departmentAdmOffice;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoSummary;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ISummary;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.utils.summary.SummaryUtils;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author mrsp and jdnf
 */

public class ReadLastSummary implements IService {

	public ReadLastSummary(){}
	
	
	public InfoSummary run(Integer executionCourseId, Integer shiftId) throws FenixServiceException {
		InfoSummary summary = null;
		
		try {
			ISuportePersistente suportePersistente = SuportePersistenteOJB.getInstance();
			IPersistentSummary persistentSummary = suportePersistente.getIPersistentSummary();
			IPersistentExecutionCourse persistentExecutionCourse = suportePersistente.getIPersistentExecutionCourse();
			ITurnoPersistente persistentShift = suportePersistente.getITurnoPersistente();
			
			ITurno shift = (ITurno) persistentShift.readByOID(Turno.class, shiftId);
			if(shift == null)
				throw new FenixServiceException("no.shift");
			
			IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new FenixServiceException("no.executioncourse");
            }
            
			List summaries = persistentSummary.readByShift(executionCourse, shift);
			if(summaries != null || summaries.size() > 0){
				Comparator comparator = new BeanComparator("summaryDate.timeInMillis");
				Collections.sort(summaries, comparator);
				ISummary summary1 = (ISummary) summaries.get(summaries.size() - 1);
				
				summary = InfoSummary.newInfoFromDomain(summary1);
			}
			
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
		
		return summary;
	}
}
