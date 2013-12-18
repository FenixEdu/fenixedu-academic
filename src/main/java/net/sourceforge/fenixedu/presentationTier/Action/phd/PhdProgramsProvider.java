package net.sourceforge.fenixedu.presentationTier.Action.phd;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class PhdProgramsProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object current) {
        return Bennu.getInstance().getPhdProgramsSet();
    }
}
