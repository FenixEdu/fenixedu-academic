package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidMarksServiceException;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMark;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.InvalidMarkDomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class WriteMarks implements IService {

    public void run(final Integer executioCourseOID, final Integer evaluationOID, final Map<Integer, String> marks)
            throws ExcepcaoPersistencia, FenixServiceException {
    	final List<DomainException> exceptionList = new ArrayList<DomainException>(); 
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentEvaluation persistentEvaluation = persistentSupport.getIPersistentEvaluation();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
        
        final IEvaluation evaluation = (IEvaluation) persistentEvaluation.readByOID(Evaluation.class, evaluationOID);
        final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, executioCourseOID);
        
        for (final Entry<Integer, String> entry : marks.entrySet()) {
            final Integer studentNumber = entry.getKey();
            final String markValue = entry.getValue();
            
            final IAttends attends = findStudentAttends(executionCourse, studentNumber);

            if(attends != null) {            
	            final IMark mark = findExistingMark(attends.getAssociatedMarks(), evaluation);
	            
	            if(markValue == null || markValue.length() == 0) {
	            	if(mark != null) {
	            		mark.delete();
	            	}
	            } else {
		            try {
		            	if (mark == null) {
			                evaluation.addNewMark(attends, markValue);
			            } else {
			                mark.setMark(markValue);
			            }
		            } catch(InvalidMarkDomainException e) {
		            	exceptionList.add(e);
		            }
	            }
            } else {
            	exceptionList.add(new DomainException("errors.noStudent", studentNumber.toString()));
            }
        }
        if(!exceptionList.isEmpty()) {
        	throw new InvalidMarksServiceException(exceptionList);
        }
   }

    private IAttends findStudentAttends(final IExecutionCourse executionCourse, final Integer studentNumber) {
		for(final IAttends attends: executionCourse.getAttends()) {
			if(attends.getAluno().getNumber().equals(studentNumber)) {
				return attends;
			}
		}
		return null;
	}


	private IMark findExistingMark(final List<IMark> marks, IEvaluation evaluation) {
        for (final IMark mark : marks) {
            final IEvaluation markEvaluation = mark.getEvaluation();
            if (markEvaluation.equals(evaluation)) {
                return mark;
            }
        }
        return null;
    }

}