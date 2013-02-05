package net.sourceforge.fenixedu.applicationTier.Servico.phd;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class SearchPhdParticipantsByProcess implements AutoCompleteSearchService, IService {

    @Override
    public Collection<PhdParticipant> run(Class type, String value, int limit, Map<String, String> arguments) {
        return getPhdIndividualProgramProcess(arguments).getParticipants();
    }

    private PhdIndividualProgramProcess getPhdIndividualProgramProcess(Map<String, String> arguments) {
        return PhdIndividualProgramProcess.fromExternalId(arguments.get("phdProcessOID"));
    }
}
