package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.credits;

import java.util.List;

import net.sourceforge.fenixedu.domain.credits.IOtherTypeCreditLine;
import net.sourceforge.fenixedu.domain.credits.OtherTypeCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentOtherTypeCreditLine;

public class OtherTypeCreditLineVO extends PersistentObjectOJB implements IPersistentOtherTypeCreditLine {

    public List readByTeacherAndExecutionPeriod(Integer teacherId, Integer executionPeriodId)
            throws ExcepcaoPersistencia {

        List<IOtherTypeCreditLine> otCreditLines = (List<IOtherTypeCreditLine>) readAll(OtherTypeCreditLine.class);
        List<IOtherTypeCreditLine> result = null;

        for (IOtherTypeCreditLine line : otCreditLines) {
            if (line.getTeacher().getIdInternal().equals(teacherId)
                    && line.getExecutionPeriod().getIdInternal().equals(executionPeriodId)) {
                result.add(line);
            }
        }

        return result;
    }

}