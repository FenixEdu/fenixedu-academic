package net.sourceforge.fenixedu.presentationTier.Action.phd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class ActivePhdProgramsOnExecutionYearProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        PhdProgramCandidacyProcessBean bean = (PhdProgramCandidacyProcessBean) source;

        if (bean.getExecutionYear() == null) {
            return Collections.EMPTY_LIST;
        }

        List<PhdProgram> phdProgramsList = new ArrayList<PhdProgram>();

        for (PhdProgram phdProgram : AcademicAuthorizationGroup.getPhdProgramsForOperation(AccessControl.getPerson(),
                AcademicOperationType.MANAGE_PHD_PROCESSES)) {
            if (phdProgram.isActive(bean.getExecutionYear())) {
                phdProgramsList.add(phdProgram);
            }
        }

        return phdProgramsList;
    }
}
