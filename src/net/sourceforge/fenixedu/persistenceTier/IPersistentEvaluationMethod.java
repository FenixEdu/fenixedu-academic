/*
 * Created on 23/Abr/2003
 *
 * 
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IEvaluationMethod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;

/**
 * @author João Mota
 * 
 *  
 */
public interface IPersistentEvaluationMethod extends IPersistentObject {

    public IEvaluationMethod readByExecutionCourse(IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia;

    public IEvaluationMethod readByIdExecutionCourse(IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public void delete(IEvaluationMethod evaluation) throws ExcepcaoPersistencia;

}