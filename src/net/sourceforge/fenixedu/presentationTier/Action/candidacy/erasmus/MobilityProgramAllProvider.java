package net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus;

import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityProgram;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class MobilityProgramAllProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object arg0, Object arg1) {
	return MobilityProgram.getAllMobilityPrograms();
    }

}
