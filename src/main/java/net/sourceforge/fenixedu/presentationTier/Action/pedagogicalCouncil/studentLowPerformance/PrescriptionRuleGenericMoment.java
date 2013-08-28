package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.student.Registration;

abstract class PrescriptionRuleGenericMoment extends AbstractPrescriptionRule {

    public PrescriptionRuleGenericMoment() {
    }

    @Override
    public boolean isPrescript(Registration registration, BigDecimal ects, int numberOfEntriesStudentInSecretary,
            ExecutionYear executionYear) {
        return ects.compareTo(getMinimumEcts()) < 0
                && numberOfEntriesStudentInSecretary == getNumberOfEntriesStudentInSecretary()
                && isForAdmission(registration.getIngression()) && registration.isFullRegime(executionYear);
    }

    protected boolean isForAdmission(Ingression ingression) {
        return ingression != null
                && (ingression.equals(Ingression.CNA01) || ingression.equals(Ingression.CNA02)
                        || ingression.equals(Ingression.CNA03) || ingression.equals(Ingression.CNA04)
                        || ingression.equals(Ingression.CNA05) || ingression.equals(Ingression.CNA06) || ingression
                            .equals(Ingression.CNA07));
    }

}
