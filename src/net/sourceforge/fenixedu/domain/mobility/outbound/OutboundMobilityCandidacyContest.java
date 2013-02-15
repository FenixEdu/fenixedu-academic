package net.sourceforge.fenixedu.domain.mobility.outbound;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;

public class OutboundMobilityCandidacyContest extends OutboundMobilityCandidacyContest_Base implements
        Comparable<OutboundMobilityCandidacyContest> {

    public OutboundMobilityCandidacyContest(final OutboundMobilityCandidacyPeriod outboundMobilityCandidacyPeriod,
            final ExecutionDegree executionDegree, final Unit unit, final String code, final Integer vacancies) {
        setRootDomainObject(RootDomainObject.getInstance());
        setOutboundMobilityCandidacyPeriod(outboundMobilityCandidacyPeriod);
        setExecutionDegree(executionDegree);
        setUnit(unit);
        setCode(code);
        setVacancies(vacancies);
    }

    @Override
    public int compareTo(final OutboundMobilityCandidacyContest o) {
        final int edc =
                ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME_AND_EXECUTION_YEAR.compare(
                        getExecutionDegree(), o.getExecutionDegree());
        if (edc == 0) {
            final int uc = Unit.COMPARATOR_BY_NAME_AND_ID.compare(getUnit(), o.getUnit());
            return uc == 0 ? getExternalId().compareTo(o.getExternalId()) : uc;
        }
        return edc;
    }

    public boolean isAcceptingCandidacies() {
        return getOutboundMobilityCandidacyPeriod().isAcceptingCandidacies();
    }

    public boolean isAcceptingCandidacies(final Student student) {
        return isAcceptingCandidacies() && findBestRegistration(student) != null;
    }

    public void apply(final Student student) {
        if (isAcceptingCandidacies()) {
            final Registration registration = findBestRegistration(student);
            if (registration == null) {
                throw new DomainException("error.OutboundMobilityCandidacyContest.not.accepting.for.studnt");
            } else {
                apply(registration);
            }
        } else {
            throw new DomainException("error.CandidacyPeriod.not.accepting.candidacies");
        }
    }

    private void apply(final Registration registration) {
        OutboundMobilityCandidacySubmission.apply(this, registration);
    }

    private Registration findBestRegistration(final Student student) {
        for (final Registration registration : student.getRegistrationsSet()) {
            if (getExecutionDegree().getDegree() == registration.getDegree()) {
                final ExecutionYear executionYear = (ExecutionYear) getOutboundMobilityCandidacyPeriod().getExecutionInterval();
                final RegistrationState registrationState = registration.getLastRegistrationState(executionYear);
                if (registrationState != null && registrationState.getStateType().isActive()) {
                    return registration;
                }
            }
        }
        return null;
    }

    public boolean hasCandidacy(final Student student) {
        for (final Registration registration : student.getRegistrationsSet()) {
            for (final OutboundMobilityCandidacySubmission submission : registration.getOutboundMobilityCandidacySubmissionSet()) {
                for (final OutboundMobilityCandidacy candidacy : submission.getOutboundMobilityCandidacySet()) {
                    if (candidacy.getOutboundMobilityCandidacyContest() == this) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
