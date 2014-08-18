/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.candidacyProcess.mobility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.institutionalRelations.academic.Program;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;

// TODO : On next major version of fenix remove the parent domain class
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
        super.setRegistrationAgreement(protocol.getRegistrationAgreement());
    }

    @Deprecated
    public MobilityProgram(RegistrationAgreement agreement) {
        this(RegistrationProtocol.serveRegistrationProtocol(agreement));
    }

    public void delete() {
        setRootDomainObject(null);
        getMobilityAgreementsSet().clear();
        setRegistrationProtocol(null);
        deleteDomainObject();
    }

    public static List<MobilityProgram> getAllMobilityPrograms() {
        List<MobilityProgram> result = new ArrayList<MobilityProgram>();

        Collection<Program> programs = Bennu.getInstance().getProgramsSet();

        for (Program program : programs) {
            if (program.isMobility()) {
                result.add((MobilityProgram) program);
            }
        }

        return result;
    }

    public LocalizedString getName() {
        return getRegistrationProtocol().getDescription();
    }

    @Override
    public boolean isMobility() {
        return true;
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

    @Deprecated
    public static MobilityProgram getByRegistrationAgreement(RegistrationAgreement registrationAgreement) {
        return getByRegistrationProtocol(RegistrationProtocol.serveRegistrationProtocol(registrationAgreement));
    }

    public static MobilityProgram getByRegistrationProtocol(RegistrationProtocol registrationProtocol) {
        Collection<Program> programs = Bennu.getInstance().getProgramsSet();
        return programs.stream().filter(p -> p instanceof MobilityProgram).map(p -> (MobilityProgram) p)
                .filter(mp -> mp.getRegistrationProtocol() == registrationProtocol).findAny().get();
    }

    @Override
    public int compareTo(final MobilityProgram o) {
        int rac = getRegistrationProtocol().compareTo(o.getRegistrationProtocol());
        return rac == 0 ? getExternalId().compareTo(o.getExternalId()) : rac;
    }

    @Deprecated
    @Atomic
    public static void connectToRegistrationProtocols() {
        for (final Program program : Bennu.getInstance().getProgramsSet()) {
            if (program.isMobility()) {
                final MobilityProgram mobilityProgram = (MobilityProgram) program;
                if (mobilityProgram.getRegistrationProtocol() == null) {
                    final RegistrationAgreement agreement = mobilityProgram.getRegistrationAgreementFromBase();
                    final RegistrationProtocol protocol = RegistrationProtocol.serveRegistrationProtocol(agreement);
                    mobilityProgram.setRegistrationProtocol(protocol);
                }
            }
        }
    }

    @Deprecated
    private RegistrationAgreement getRegistrationAgreementFromBase() {
        return super.getRegistrationAgreement();
    }

    @Deprecated
    @Override
    public RegistrationAgreement getRegistrationAgreement() {
        return getRegistrationProtocol().getRegistrationAgreement();
    }

    @Deprecated
    @Override
    public void setRegistrationAgreement(RegistrationAgreement registrationAgreement) {
        throw new Error("This is not the method you are looking for. Go back to the dark side from whence you came.");
    }

    @Override
    public void setRegistrationProtocol(final RegistrationProtocol registrationProtocol) {
        super.setRegistrationProtocol(registrationProtocol);
        super.setRegistrationAgreement(registrationProtocol == null ? null : registrationProtocol.getRegistrationAgreement());
    }

}
