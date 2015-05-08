package org.fenixedu.academic.ui.renderers.providers.candidacy;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import org.fenixedu.academic.dto.student.IRegistrationBean;
import org.fenixedu.academic.ui.renderers.providers.AbstractDomainObjectProvider;

public class ProgramConclusionProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        if (source instanceof DocumentRequestCreateBean) {
            return provide(((DocumentRequestCreateBean) source).getRegistration(), currentValue);
        }

        if (source instanceof IRegistrationBean) {
            return provide(((IRegistrationBean) source).getRegistration(), currentValue);
        }

        if (source instanceof Registration) {
            return ProgramConclusion.conclusionsFor((Registration) source).collect(Collectors.toList());
        }

        if (source instanceof DegreeCurricularPlan) {
            return ProgramConclusion.conclusionsFor((DegreeCurricularPlan) source).collect(Collectors.toList());
        }

        return new ArrayList<ProgramConclusion>();
    }
}
