package net.sourceforge.fenixedu.persistenceTier.credits;

import java.util.Date;
import java.util.List;

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
    List readByTeacherAndExecutionPeriod(Integer teacherId, Date executionPeriodBeginDate,
            Date executionPeriodEndDate) throws ExcepcaoPersistencia;

    List readByTeacher(Integer teacher) throws ExcepcaoPersistencia;

}