/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.persistenceTier.credits;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author jpvl
 */
public interface IPersistentServiceExemptionCreditLine extends IPersistentObject {

    /**
     * @param teacher
     * @param executionPeriod
     */
    List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    List readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia;

}