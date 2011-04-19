package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram.institution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.candidacy.InstitutionPhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class PhdProgramsProviderForPublicCandidacy extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object current) {
	// return fromFocusArea(source);

	if (source instanceof PhdProgramCandidacyProcessBean) {
	    PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) source;
	    InstitutionPhdCandidacyPeriod phdCandidacyPeriod = (InstitutionPhdCandidacyPeriod) bean.getPhdCandidacyPeriod();

	    return phdCandidacyPeriod.getPhdPrograms();

	} else if (source instanceof PhdIndividualProgramProcessBean) {
	    PhdIndividualProgramProcessBean bean = (PhdIndividualProgramProcessBean) source;
	    InstitutionPhdCandidacyPeriod publicPhdCandidacyPeriod = (InstitutionPhdCandidacyPeriod) bean
		    .getIndividualProgramProcess().getCandidacyProcess()
		    .getPublicPhdCandidacyPeriod();

	    return publicPhdCandidacyPeriod.getPhdPrograms();
	}

	return Collections.EMPTY_LIST;
    }

    protected Object fromFocusArea(Object source) {
	PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) source;
	List<PhdProgram> activePhdProgramList = new ArrayList<PhdProgram>();
	InstitutionPhdCandidacyPeriod phdCandidacyPeriod = (InstitutionPhdCandidacyPeriod) bean.getPhdCandidacyPeriod();

	if (bean.getFocusArea() == null) {
	    return Collections.EMPTY_LIST;
	}

	for (PhdProgram phdProgram : bean.getFocusArea().getPhdPrograms()) {
	    if (phdCandidacyPeriod.getPhdPrograms().contains(phdProgram)) {
		activePhdProgramList.add(phdProgram);
	    }
	}

	return activePhdProgramList;
    }

}
