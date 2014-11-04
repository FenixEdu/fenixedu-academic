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
package org.fenixedu.academic.domain.candidacyProcess.mobility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

public class MobilityProgram extends MobilityProgram_Base implements Comparable<MobilityProgram> {

    public static final Comparator<MobilityProgram> COMPARATOR_BY_REGISTRATION_AGREEMENT = new Comparator<MobilityProgram>() {

        @Override
        public int compare(MobilityProgram o1, MobilityProgram o2) {
            return o1.getRegistrationProtocol().compareTo(o2.getRegistrationProtocol());
        }

    };

    private MobilityProgram() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public MobilityProgram(RegistrationProtocol protocol) {
        this();
        setRegistrationProtocol(protocol);
    }

    public void delete() {
        setRootDomainObject(null);
        getMobilityAgreementsSet().clear();
        setRegistrationProtocol(null);
        deleteDomainObject();
    }

    public static List<MobilityProgram> getAllMobilityPrograms() {
        return new ArrayList<MobilityProgram>(Bennu.getInstance().getProgramsSet());
    }

    public LocalizedString getName() {
        return getRegistrationProtocol().getDescription();
    }

    public MobilityAgreement getMobilityAgreementByUniversityUnit(final UniversityUnit unit) {
        Collection<MobilityAgreement> mobilityAgreements = getMobilityAgreementsSet();

        for (MobilityAgreement mobilityAgreement : mobilityAgreements) {
            if (mobilityAgreement.getUniversityUnit() == unit) {
                return mobilityAgreement;
            }
        }

        return null;
    }

    public static MobilityProgram getByRegistrationProtocol(RegistrationProtocol registrationProtocol) {
        Collection<MobilityProgram> programs = Bennu.getInstance().getProgramsSet();
        return programs.stream().filter(mp -> mp.getRegistrationProtocol() == registrationProtocol).findAny().get();
    }

    @Override
    public int compareTo(final MobilityProgram o) {
        int rac = getRegistrationProtocol().compareTo(o.getRegistrationProtocol());
        return rac == 0 ? getExternalId().compareTo(o.getExternalId()) : rac;
    }

    @Override
    public void setRegistrationProtocol(final RegistrationProtocol registrationProtocol) {
        super.setRegistrationProtocol(registrationProtocol);
    }

}
