/*
 * Created on 29/Fev/2004
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
public interface IPersistentOtherTypeCreditLine extends IPersistentObject {
    List readByTeacherAndExecutionPeriod(ITeacher teacher, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;
}