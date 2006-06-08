package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.renderers.UpdateObjects;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class EditCreditsPeriods extends UpdateObjects {

    @Override
    protected void afterRun(Collection<Object> touchedObjects) {
        super.afterRun(touchedObjects);
        ExecutionPeriod executionPeriod = (ExecutionPeriod) touchedObjects.iterator().next();
        if (executionPeriod.getTeacherCreditsPeriodBegin() != null
                && executionPeriod.getTeacherCreditsPeriodEnd() != null
                && !executionPeriod.getTeacherCreditsPeriodBegin().isBefore(
                        executionPeriod.getTeacherCreditsPeriodEnd())) {
            throw new DomainException("message.error.invalid.dates");
        }
        if (executionPeriod.getDepartmentAdmOfficeCreditsPeriodBegin() != null
                && executionPeriod.getDepartmentAdmOfficeCreditsPeriodEnd() != null
                && !executionPeriod.getDepartmentAdmOfficeCreditsPeriodBegin().isBefore(
                        executionPeriod.getDepartmentAdmOfficeCreditsPeriodEnd())) {
            throw new DomainException("message.error.invalid.dates");
        }
    }
}
