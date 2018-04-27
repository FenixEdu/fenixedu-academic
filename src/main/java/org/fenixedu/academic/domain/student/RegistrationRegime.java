/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.student;

import java.util.Comparator;
import java.util.function.Supplier;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.MaximumNumberOfCreditsForEnrolmentPeriod;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
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
        super.setRootDomainObject(Bennu.getInstance());
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

        registrationRegimeVerifier.checkEctsCredits(registration, executionYear, type);
    }

    private static Supplier<RegistrationRegimeVerifier> REGISTRATION_REGIME_VERIFIER = () -> new RegistrationRegimeVerifier() {

        public void checkEctsCredits(final Registration registration, final ExecutionYear executionYear,
                final RegistrationRegimeType type) {

            final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();

            double enroledEctsCredits = 0d;
            for (final ExecutionSemester semester : executionYear.getExecutionPeriodsSet()) {
                enroledEctsCredits += studentCurricularPlan.getAccumulatedEctsCredits(semester);
            }

            if (enroledEctsCredits > MaximumNumberOfCreditsForEnrolmentPeriod.MAXIMUM_NUMBER_OF_CREDITS_PARTIAL_TIME) {
                throw new DomainException("error.RegistrationRegime.semester.has.more.ects.than.maximum.allowed",
                        String.valueOf(enroledEctsCredits), executionYear.getQualifiedName(),
                        String.valueOf(MaximumNumberOfCreditsForEnrolmentPeriod.MAXIMUM_NUMBER_OF_CREDITS_PARTIAL_TIME));
            }
        }

    };

    public static interface RegistrationRegimeVerifier {

        void checkEctsCredits(final Registration registration, final ExecutionYear executionYear,
                final RegistrationRegimeType type);
    }

    private RegistrationRegimeVerifier registrationRegimeVerifier = REGISTRATION_REGIME_VERIFIER.get();

    public static RegistrationRegimeVerifier getRegistrationRegimeVerifier() {
        return REGISTRATION_REGIME_VERIFIER.get();
    }

    public static void setRegistrationRegimeVerifier(final Supplier<RegistrationRegimeVerifier> input) {
        REGISTRATION_REGIME_VERIFIER = input;
    }

    public boolean isPartTime() {
        return getRegimeType() == RegistrationRegimeType.PARTIAL_TIME;
    }

    public void delete() {
        setRegistration(null);
        setExecutionYear(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public boolean isFor(final ExecutionYear executionYear) {
        return getExecutionYear() == executionYear;
    }

    public boolean hasRegime(final RegistrationRegimeType type) {
        return getRegimeType() == type;
    }

}
