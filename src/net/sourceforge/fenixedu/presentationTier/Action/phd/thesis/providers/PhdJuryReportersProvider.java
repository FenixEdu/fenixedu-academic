package net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.providers;

import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class PhdJuryReportersProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        PhdThesisProcessBean bean = (PhdThesisProcessBean) source;

        return bean.getThesisProcess().getReportThesisJuryElements();
    }

}
