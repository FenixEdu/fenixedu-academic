/*
 * Created on 23/Abr/2003
 *
 * 
 */
package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.EvaluationMethod;

/**
 * @author João Mota
 * 
 *  
 */
public interface IPersistentEvaluationMethod extends IPersistentObject {

    public EvaluationMethod readByIdExecutionCourse(Integer executionCourseOID)
            throws ExcepcaoPersistencia;

}