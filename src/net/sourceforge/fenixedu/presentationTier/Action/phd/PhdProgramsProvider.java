package net.sourceforge.fenixedu.presentationTier.Action.phd;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class PhdProgramsProvider extends AbstractDomainObjectProvider {

	@Override
	public Object provide(Object source, Object current) {
		return RootDomainObject.getInstance().getPhdPrograms();
	}
}
