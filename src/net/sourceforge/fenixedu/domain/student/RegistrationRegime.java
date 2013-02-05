package net.sourceforge.fenixedu.domain.student;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.MaximumNumberOfCreditsForEnrolmentPeriod;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class RegistrationRegime extends RegistrationRegime_Base {

    static public Comparator<RegistrationRegime> COMPARATOR_BY_EXECUTION_YEAR = new Comparator<RegistrationRegime>() {
        @Override
        public int compare(RegistrationRegime o1, RegistrationRegime o2) {
            return o1.getExecutionYear().compareTo(o2.getExecutionYear());
        }
    };

    private RegistrationRegime() {
        super();
        super.setRootDomainObject(RootDomainObject.getInstance());
        super.setWhenCreated(new DateTime());
    }

    public RegistrationRegime(final Registration registration, final ExecutionYear executionYear,
            final RegistrationRegimeType type) {

        this();
        checkParameters(registration, executionYear, type);

        super.setRegistration(registration);
        super.setExecutionYear(executionYear);
        super.setRegimeType(type);
    }

    private void checkParameters(final Registration registration, final ExecutionYear executionYear,
            final RegistrationRegimeType type) {
        if (registration == null) {
            throw new DomainException("error.RegistrationRegime.invalid.registration");
        }
        if (executionYear == null) {
            throw new DomainException("error.RegistrationRegime.invalid.executionYear");
        }
        if (type == null) {
            throw new DomainException("error.RegistrationRegime.invalid.regimeType");
        }

        if (registration.hasRegistrationRegime(executionYear, type)) {
            throw new DomainException("error.RegistrationRegime.already.has.regime.type.in.given.executionYear");
        }

        if (registration.getDegree().isEmpty()) {
            throw new DomainException("error.RegistrationRegime.cannot.apply.to.empty.degrees");
        }

        checkEctsCredits(registration, executionYear);
    }

    private void checkEctsCredits(final Registration registration, final ExecutionYear executionYear) {
        final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();

        double enroledEctsCredits = 0d;
        for (final ExecutionSemester semester : executionYear.getExecutionPeriods()) {
            enroledEctsCredits += studentCurricularPlan.getAccumulatedEctsCredits(semester);
        }

        if (enroledEctsCredits > MaximumNumberOfCreditsForEnrolmentPeriod.MAXIMUM_NUMBER_OF_CREDITS_PARTIAL_TIME) {
            throw new DomainException("error.RegistrationRegime.semester.has.more.ects.than.maximum.allowed",
                    String.valueOf(enroledEctsCredits), executionYear.getQualifiedName(),
                    String.valueOf(MaximumNumberOfCreditsForEnrolmentPeriod.MAXIMUM_NUMBER_OF_CREDITS_PARTIAL_TIME));
        }
    }

    public boolean isPartTime() {
        return getRegimeType() == RegistrationRegimeType.PARTIAL_TIME;
    }

    public void delete() {
        removeRegistration();
        removeExecutionYear();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

    public boolean isFor(final ExecutionYear executionYear) {
        return getExecutionYear() == executionYear;
    }

    public boolean hasRegime(final RegistrationRegimeType type) {
        return getRegimeType() == type;
    }
}
