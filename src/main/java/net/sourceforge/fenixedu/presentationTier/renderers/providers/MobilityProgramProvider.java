package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import org.fenixedu.commons.StringNormalizer;

import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityProgram;
import net.sourceforge.fenixedu.domain.institutionalRelations.academic.Program;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;

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
