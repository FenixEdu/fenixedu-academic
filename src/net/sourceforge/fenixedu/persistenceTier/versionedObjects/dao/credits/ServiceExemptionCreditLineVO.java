package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.credits;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.credits.IServiceExemptionCreditLine;
import net.sourceforge.fenixedu.domain.credits.ServiceExemptionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.credits.IPersistentServiceExemptionCreditLine;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author jpvl
 */
public class ServiceExemptionCreditLineVO extends VersionedObjectsBase implements
        IPersistentServiceExemptionCreditLine {

    public List readByTeacherAndExecutionPeriod(Integer teacherId, Date executionPeriodBeginDate,
            Date executionPeriodEndDate) throws ExcepcaoPersistencia {

        List<IServiceExemptionCreditLine> seCreditLines = (List<IServiceExemptionCreditLine>) readAll(ServiceExemptionCreditLine.class);
        List<IServiceExemptionCreditLine> result = null;

        for (IServiceExemptionCreditLine line : seCreditLines) {
            if (line.getTeacher().getIdInternal().equals(teacherId)
                    && line.getEnd().getTime() > executionPeriodBeginDate.getTime()
                    && line.getStart().getTime() < executionPeriodEndDate.getTime()) {
                result.add(line);
            }
        }

        return result;
    }

    public List readByTeacher(Integer teacherId) throws ExcepcaoPersistencia {
        List<IServiceExemptionCreditLine> seCreditLines = (List<IServiceExemptionCreditLine>) readAll(ServiceExemptionCreditLine.class);
        List<IServiceExemptionCreditLine> result = null;

        for (IServiceExemptionCreditLine line : seCreditLines) {
            if (line.getTeacher().getIdInternal().equals(teacherId)) {
                result.add(line);
            }
        }

        return result;
    }

}
