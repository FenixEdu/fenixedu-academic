package net.sourceforge.fenixedu.presentationTier.Action.phd;

import net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class PhdProgramFocusAreasProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object current) {
        return PhdProgramFocusArea.getActivePhdProgramFocusAreas();
    }
}
