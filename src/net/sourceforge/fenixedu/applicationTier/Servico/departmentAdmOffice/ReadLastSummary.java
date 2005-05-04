/*
 * Created on Nov 18, 2004
 */

package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author mrsp and jdnf
 */

public class ReadLastSummary implements IService {

	public ReadLastSummary(){}
	
	
	public InfoSummary run(Integer executionCourseId, Integer shiftId, Integer lessonID) throws FenixServiceException {
		InfoSummary summary = null;
		
		try {
			ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
			IPersistentSummary persistentSummary = suportePersistente.getIPersistentSummary();
			IPersistentExecutionCourse persistentExecutionCourse = suportePersistente.getIPersistentExecutionCourse();
			ITurnoPersistente persistentShift = suportePersistente.getITurnoPersistente();
			IAulaPersistente aulaPersistente = suportePersistente.getIAulaPersistente();
			
			IShift shift = (IShift) persistentShift.readByOID(Shift.class, shiftId);
			if(shift == null)
				throw new FenixServiceException("no.shift");		
						
			ILesson aula = (ILesson) aulaPersistente.readByOID(Lesson.class, lessonID);								
			if(aula == null)
			    throw new FenixServiceException("no.lesson");
									
			IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new FenixServiceException("no.executioncourse");
            }
            
			List summaries = persistentSummary.readByShift(executionCourse, shift);
			if(summaries != null && summaries.size() > 0){
				Comparator comparator = new BeanComparator("summaryDate.time");
				Collections.sort(summaries, comparator);
				DiaSemana diaSemana = aula.getDiaSemana();
				Calendar summaryDate = Calendar.getInstance();
				int summaryDayOfWeek;
				for(int i = summaries.size() - 1; i >= 0; i--){
				    ISummary summary1 = (ISummary) summaries.get(i);
				    summaryDate.setTime(summary1.getSummaryDate());
				    summaryDayOfWeek = summaryDate.get(Calendar.DAY_OF_WEEK);
				    if(summaryDayOfWeek == diaSemana.getDiaSemana().intValue()){
				        summary = InfoSummary.newInfoFromDomain(summary1);
				        break;	
				    }
				}				
			}
			
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return summary;
	}
}
