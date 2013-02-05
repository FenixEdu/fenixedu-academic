package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram.institution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea;
import net.sourceforge.fenixedu.domain.phd.candidacy.InstitutionPhdCandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class PhdProgramFocusAreasProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object current) {

        if (source instanceof PhdProgramCandidacyProcessBean) {
            PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) source;

            Set<PhdProgramFocusArea> focusAreaSet = new HashSet<PhdProgramFocusArea>();
            InstitutionPhdCandidacyPeriod phdCandidacyPeriod = (InstitutionPhdCandidacyPeriod) bean.getPhdCandidacyPeriod();

            for (PhdProgram phdProgram : phdCandidacyPeriod.getPhdPrograms()) {
                focusAreaSet.addAll(phdProgram.getPhdProgramFocusAreas());
            }

            List<PhdProgramFocusArea> focusAreaList = new ArrayList<PhdProgramFocusArea>();
            focusAreaList.addAll(focusAreaSet);

            return focusAreaList;

        } else if (source instanceof PhdIndividualProgramProcessBean) {
            PhdIndividualProgramProcessBean bean = (PhdIndividualProgramProcessBean) source;
            Set<PhdProgramFocusArea> focusAreaSet = new HashSet<PhdProgramFocusArea>();

            InstitutionPhdCandidacyPeriod phdCandidacyPeriod =
                    (InstitutionPhdCandidacyPeriod) bean.getIndividualProgramProcess().getCandidacyProcess()
                            .getPublicPhdCandidacyPeriod();

            for (PhdProgram phdProgram : phdCandidacyPeriod.getPhdPrograms()) {
                focusAreaSet.addAll(phdProgram.getPhdProgramFocusAreas());
            }

            List<PhdProgramFocusArea> focusAreaList = new ArrayList<PhdProgramFocusArea>();
            focusAreaList.addAll(focusAreaSet);

            return focusAreaList;
        }

        return Collections.EMPTY_LIST;
    }

}
