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
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityProgram;
import net.sourceforge.fenixedu.domain.institutionalRelations.academic.Program;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import org.fenixedu.commons.StringNormalizer;

public class MobilityProgramProvider implements AutoCompleteProvider<MobilityProgram> {

    @Override
    public Collection<MobilityProgram> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final String nvalue = StringNormalizer.normalize(value);
        final List<MobilityProgram> result = new ArrayList<MobilityProgram>();
        for (final Program program : Bennu.getInstance().getProgramsSet()) {
            if (program.isMobility()) {
                final MobilityProgram mobilityProgram = (MobilityProgram) program;
                final RegistrationAgreement registrationAgreement = mobilityProgram.getRegistrationAgreement();
                final String name = StringNormalizer.normalize(registrationAgreement.getQualifiedName());
                if (name.indexOf(nvalue) >= 0) {
                    result.add(mobilityProgram);
                }
            }
        }
        return result;
    }

}
