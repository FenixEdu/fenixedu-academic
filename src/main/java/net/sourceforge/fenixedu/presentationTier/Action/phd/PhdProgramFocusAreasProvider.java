package net.sourceforge.fenixedu.presentationTier.Action.phd;

import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;
import pt.ist.bennu.core.domain.Bennu;

public class PhdProgramFocusAreasProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object current) {
        return Bennu.getInstance().getPhdProgramFocusAreasSet();
    }
}
