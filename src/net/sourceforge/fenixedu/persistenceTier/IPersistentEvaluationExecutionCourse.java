package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IEvalutionExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;

/**
 * 
 * @author Tânia Pousão
 */
public interface IPersistentEvaluationExecutionCourse extends IPersistentObject {
    public IEvalutionExecutionCourse readBy(IEvaluation evaluation, IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public void delete(IEvalutionExecutionCourse evalutionExecutionCourse) throws ExcepcaoPersistencia;

    public void delete(IEvaluation evaluation) throws ExcepcaoPersistencia;

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia;

}