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
import java.util.Locale;

import net.sourceforge.fenixedu.domain.institutionalRelations.academic.Program;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class MobilityProgram extends MobilityProgram_Base implements Comparable<MobilityProgram> {

    public static final Comparator<MobilityProgram> COMPARATOR_BY_REGISTRATION_AGREEMENT = new Comparator<MobilityProgram>() {

        @Override
        public int compare(MobilityProgram o1, MobilityProgram o2) {
            return o1.getRegistrationAgreement().compareTo(o2.getRegistrationAgreement());
        }

    };

    private MobilityProgram() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public MobilityProgram(RegistrationAgreement agreement) {
        this();
        setRegistrationAgreement(agreement);
    }

    public void delete() {
        setRootDomainObject(null);
        getMobilityAgreements().clear();
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

    public MultiLanguageString getName() {
        RegistrationAgreement registrationAgreement = getRegistrationAgreement();
        String englishDescription = registrationAgreement.getDescription(Locale.ENGLISH);

        Locale localePT = new Locale("pt", "PT");
        String portugueseDescription = registrationAgreement.getDescription(localePT);

        return (new MultiLanguageString(MultiLanguageString.pt, portugueseDescription)).with(MultiLanguageString.en, englishDescription);
    }

    @Override
    public boolean isMobility() {
        return true;
    }

    public MobilityAgreement getMobilityAgreementByUniversityUnit(final UniversityUnit unit) {
        Collection<MobilityAgreement> mobilityAgreements = getMobilityAgreements();

        for (MobilityAgreement mobilityAgreement : mobilityAgreements) {
            if (mobilityAgreement.getUniversityUnit() == unit) {
                return mobilityAgreement;
            }
        }

        return null;
    }

    public static MobilityProgram getByRegistrationAgreement(RegistrationAgreement registrationAgreement) {
        Collection<Program> programs = Bennu.getInstance().getProgramsSet();
        for (Program program : programs) {
            if (program instanceof MobilityProgram) {
                MobilityProgram mob = ((MobilityProgram) program);
                if (mob.getRegistrationAgreement() == registrationAgreement) {
                    return mob;
                }
            }
        }
        return null;
    }

    @Override
    public int compareTo(final MobilityProgram o) {
        int rac = getRegistrationAgreement().compareTo(o.getRegistrationAgreement());
        return rac == 0 ? getExternalId().compareTo(o.getExternalId()) : rac;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityAgreement> getMobilityAgreements() {
        return getMobilityAgreementsSet();
    }

    @Deprecated
    public boolean hasAnyMobilityAgreements() {
        return !getMobilityAgreementsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityEmailTemplate> getEmailTemplates() {
        return getEmailTemplatesSet();
    }

    @Deprecated
    public boolean hasAnyEmailTemplates() {
        return !getEmailTemplatesSet().isEmpty();
    }

}
