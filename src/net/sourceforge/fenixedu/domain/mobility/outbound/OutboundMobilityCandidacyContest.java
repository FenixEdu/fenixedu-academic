package net.sourceforge.fenixedu.domain.mobility.outbound;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityAgreement;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;

public class OutboundMobilityCandidacyContest extends OutboundMobilityCandidacyContest_Base implements
        Comparable<OutboundMobilityCandidacyContest> {

    public OutboundMobilityCandidacyContest(final OutboundMobilityCandidacyPeriod outboundMobilityCandidacyPeriod,
            final ExecutionDegree executionDegree, final MobilityAgreement mobilityAgreement, final String code,
            final Integer vacancies) {
        setRootDomainObject(RootDomainObject.getInstance());
        setOutboundMobilityCandidacyPeriod(outboundMobilityCandidacyPeriod);
        if (executionDegree != null) {
            addExecutionDegree(executionDegree);
        }
        setMobilityAgreement(mobilityAgreement);
        setCode(code);
        setVacancies(vacancies);
    }

    @Override
    public int compareTo(final OutboundMobilityCandidacyContest o) {
        final int edc = compareDegrees(o);
        if (edc == 0) {
            final int cc = compareCountries(o);
            if (cc == 0) {
                final int uc = compareUniversities(o);
                if (uc == 0) {
                    final int pc = comparePrograms(o);
                    return pc == 0 ? getExternalId().compareTo(o.getExternalId()) : pc;
                }
                return uc;
            }
            return cc;
        }
        return edc;
    }

    private int compareCountries(final OutboundMobilityCandidacyContest o) {
        final Country c1 = getMobilityAgreement().getUniversityUnit().getCountry();
        final Country c2 = o.getMobilityAgreement().getUniversityUnit().getCountry();
        return c1 == c2 ? 0 : (c1 != null && c2 != null ? c1.getName().compareTo(c2.getName()) : (c1 == null ? -1 : 1));
    }

    private int comparePrograms(final OutboundMobilityCandidacyContest o) {
        return getMobilityAgreement().getMobilityProgram().getRegistrationAgreement().getDescription()
                .compareTo(o.getMobilityAgreement().getMobilityProgram().getRegistrationAgreement().getDescription());
    }

    private int compareUniversities(final OutboundMobilityCandidacyContest o) {
        return Unit.COMPARATOR_BY_NAME_AND_ID.compare(getMobilityAgreement().getUniversityUnit(), o.getMobilityAgreement()
                .getUniversityUnit());
    }

    private int compareDegrees(final OutboundMobilityCandidacyContest o) {
        return executionDegreesCompareHash(o.getExecutionDegreeSet()).compareTo(
                executionDegreesCompareHash(getExecutionDegreeSet()));
    }

    private String executionDegreesCompareHash(final Set<ExecutionDegree> executionDegreeSet) {
        final SortedSet<String> strings = new TreeSet<String>();
        for (final ExecutionDegree executionDegree : executionDegreeSet) {
            strings.add(executionDegree.getDegree().getSigla());
        }
        final StringBuilder builder = new StringBuilder();
        for (final String string : strings) {
            builder.append(string);
        }
        return builder.toString();
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
            for (final ExecutionDegree executionDegree : getExecutionDegreeSet()) {
                if (executionDegree.getDegree() == registration.getDegree()) {
                    final ExecutionYear executionYear =
                            (ExecutionYear) getOutboundMobilityCandidacyPeriod().getExecutionInterval();
                    final RegistrationState registrationState = registration.getLastRegistrationState(executionYear);
                    if (registrationState != null && registrationState.getStateType().isActive()) {
                        return registration;
                    }
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
