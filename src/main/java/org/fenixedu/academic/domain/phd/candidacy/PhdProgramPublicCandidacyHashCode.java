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
package org.fenixedu.academic.domain.phd.candidacy;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PublicCandidacyHashCode;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.academic.util.phd.PhdProperties;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;

import org.joda.time.LocalDate;
import pt.ist.fenixframework.Atomic;

public class PhdProgramPublicCandidacyHashCode extends PhdProgramPublicCandidacyHashCode_Base {

    private PhdProgramPublicCandidacyHashCode() {
        super();
    }

    @Override
    public boolean hasCandidacyProcess() {
        return getPhdProgramCandidacyProcess() != null;
    }

    @Override
    final public boolean isFromPhdProgram() {
        return true;
    }

    static public PhdProgramPublicCandidacyHashCode getOrCreatePhdProgramCandidacyHashCode(final String email) {
        final PhdProgramPublicCandidacyHashCode hashCode = getPhdProgramCandidacyHashCode(email, null, new LocalDate());
        if (hashCode != null) {
            return hashCode;
        }
        return create(email);
    }

    @Atomic
    static private PhdProgramPublicCandidacyHashCode create(final String email) {
        final PhdProgramPublicCandidacyHashCode hash = new PhdProgramPublicCandidacyHashCode();
        hash.setEmail(email);
        hash.setValue(UUID.randomUUID().toString());

        /* Disable alerts */
        // new PublicPhdMissingCandidacyAlert(hash);
        return hash;
    }

    static public PhdProgramPublicCandidacyHashCode getPhdProgramCandidacyHashCode(final String email) {
        if (StringUtils.isEmpty(email)) {
            throw new IllegalArgumentException();
        }

        for (final PublicCandidacyHashCode hashCode : Bennu.getInstance().getCandidacyHashCodesSet()) {
            if (hashCode.isFromPhdProgram() && hashCode.getEmail().equals(email)) {
                return (PhdProgramPublicCandidacyHashCode) hashCode;
            }
        }
        return null;
    }

    static public PhdProgramPublicCandidacyHashCode getPhdProgramCandidacyHashCode(final String email, final PhdProgram program, final LocalDate candidacyDate) {
        if (StringUtils.isEmpty(email)) {
            throw new IllegalArgumentException();
        }

        for (final PublicCandidacyHashCode hashCode : Bennu.getInstance().getCandidacyHashCodesSet()) {
            if (hashCode.getEmail().equals(email) && hashCode.isFromPhdProgram()) {
                PhdProgramPublicCandidacyHashCode phdHashCode = (PhdProgramPublicCandidacyHashCode) hashCode;
                if (phdHashCode.getPhdProgramCandidacyProcess() != null
                        && (program == null || phdHashCode.getPhdProgramCandidacyProcess().getPhdProgram() == program)
                        && (candidacyDate == null ||
                        phdHashCode.getPhdProgramCandidacyProcess().getPublicPhdCandidacyPeriod().contains(candidacyDate.toDateTimeAtCurrentTime()))) {
                    return phdHashCode;
                }
            }
        }
        
        return null;
    }

    public PhdIndividualProgramProcess getIndividualProgramProcess() {
        return getPhdProgramCandidacyProcess().getIndividualProgramProcess();
    }

    public Person getPerson() {
        return getPhdProgramCandidacyProcess().getPerson();
    }

    public String getAccessLink() {
        return PhdProperties.getPublicCandidacyAccessLink() + getValue();
    }

    @Override
    public Optional<User> getUser() {
        try {
            return Optional.ofNullable(getPhdProgramCandidacyProcess().getPerson().getUser());
        } catch (NullPointerException npe) {
            return Optional.empty();
        }
    }

}
