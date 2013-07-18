package net.sourceforge.fenixedu.applicationTier.Servico.phd;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AutoCompleteProvider;

public class SearchPhdParticipantsByProcess implements AutoCompleteProvider<PhdParticipant> {

    @Override
    public Collection<PhdParticipant> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        return getPhdIndividualProgramProcess(argsMap).getParticipants();
    }

    private PhdIndividualProgramProcess getPhdIndividualProgramProcess(Map<String, String> arguments) {
        return PhdIndividualProgramProcess.fromExternalId(arguments.get("phdProcessOID"));
    }
}
