/*
 * Created on Nov 18, 2004
 */

package ServidorAplicacao.Servico.departmentAdmOffice;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoSummary;
import Dominio.Aula;
import Dominio.ExecutionCourse;
import Dominio.IAula;
import Dominio.IExecutionCourse;
import Dominio.ISummary;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DiaSemana;

/**
 * @author mrsp and jdnf
 */

public class ReadLastSummary implements IService {

	public ReadLastSummary(){}
	
	
	public InfoSummary run(Integer executionCourseId, Integer shiftId, Integer lessonID) throws FenixServiceException {
		InfoSummary summary = null;
		
		try {
			ISuportePersistente suportePersistente = SuportePersistenteOJB.getInstance();
			IPersistentSummary persistentSummary = suportePersistente.getIPersistentSummary();
			IPersistentExecutionCourse persistentExecutionCourse = suportePersistente.getIPersistentExecutionCourse();
			ITurnoPersistente persistentShift = suportePersistente.getITurnoPersistente();
			IAulaPersistente aulaPersistente = suportePersistente.getIAulaPersistente();
			
			ITurno shift = (ITurno) persistentShift.readByOID(Turno.class, shiftId);
			if(shift == null)
				throw new FenixServiceException("no.shift");		
						
			IAula aula = (IAula) aulaPersistente.readByOID(Aula.class, lessonID);								
			if(aula == null)
			    throw new FenixServiceException("no.lesson");
									
			IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new FenixServiceException("no.executioncourse");
            }
            
			List summaries = persistentSummary.readByShift(executionCourse, shift);
			if(summaries != null && summaries.size() > 0){
				Comparator comparator = new BeanComparator("summaryDate.timeInMillis");
				Collections.sort(summaries, comparator);
				DiaSemana diaSemana = aula.getDiaSemana();
				int summaryDayOfWeek;
				for(int i = summaries.size() - 1; i >= 0; i--){
				    ISummary summary1 = (ISummary) summaries.get(i);
				    summary = InfoSummary.newInfoFromDomain(summary1);
				    summaryDayOfWeek = summary.getSummaryDate().get(Calendar.DAY_OF_WEEK);
				    if(summaryDayOfWeek == diaSemana.getDiaSemana().intValue())
				        break;	
				}				
			}
			
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		
		return summary;
	}
}
