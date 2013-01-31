package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class ExternalPhdProgramsProviderForPublicCandidacies extends AbstractDomainObjectProvider {

	@Override
	public Object provide(Object source, Object currentValue) {

		if (source instanceof PhdProgramCandidacyProcessBean) {
			PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) source;
			if (bean.getFocusArea() == null) {
				return Collections.EMPTY_LIST;
			}

			return bean.getFocusArea().getAssociatedExternalPhdProgramsForCollaborationType(bean.getCollaborationType());
		}

		if (source instanceof PhdIndividualProgramProcessBean) {
			PhdIndividualProgramProcessBean bean = (PhdIndividualProgramProcessBean) source;

			if (bean.getFocusArea() == null) {
				return Collections.EMPTY_LIST;
			}

			return bean.getFocusArea().getAssociatedExternalPhdProgramsForCollaborationType(bean.getCollaborationType());
		}

		return null;
	}
}
