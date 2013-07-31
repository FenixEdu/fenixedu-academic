package net.sourceforge.fenixedu.applicationTier.Servico.phd;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import pt.ist.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import pt.ist.fenixframework.FenixFramework;

public class SearchPhdParticipantsByProcess implements AutoCompleteProvider<PhdParticipant> {

    @Override
    public Collection<PhdParticipant> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        return getPhdIndividualProgramProcess(argsMap).getParticipants();
    }

    private PhdIndividualProgramProcess getPhdIndividualProgramProcess(Map<String, String> arguments) {
        return FenixFramework.getDomainObject(arguments.get("phdProcessOID"));
    }
}
