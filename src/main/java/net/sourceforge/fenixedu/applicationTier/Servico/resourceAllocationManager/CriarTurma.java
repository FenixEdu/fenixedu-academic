/*
 * CriarTurma.java
 *
 * Created on 25 de Outubro de 2002, 18:34
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CriarTurma {

    @Atomic
    public static Object run(final String className, final Integer curricularYear, final InfoExecutionDegree infoExecutionDegree,
            final AcademicInterval academicInterval) throws ExistingServiceException {

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(infoExecutionDegree.getExternalId());

        final SchoolClass schoolClass = new SchoolClass(executionDegree, academicInterval, className, curricularYear);
        return InfoClass.newInfoFromDomain(schoolClass);
    }

    @Atomic
    public static Object run(final String className, final Integer curricularYear, final InfoExecutionDegree infoExecutionDegree,
            final InfoExecutionPeriod infoExecutionPeriod) throws ExistingServiceException {

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(infoExecutionDegree.getExternalId());
        final ExecutionSemester executionSemester =
                FenixFramework.getDomainObject(infoExecutionPeriod.getExternalId());

        final SchoolClass schoolClass = new SchoolClass(executionDegree, executionSemester, className, curricularYear);
        return InfoClass.newInfoFromDomain(schoolClass);
    }
}