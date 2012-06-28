package net.sourceforge.fenixedu.presentationTier.Action.phd;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PhdProgramsMultipleProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object current) {
	return RootDomainObject.getInstance().getPhdPrograms();
    }

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }
}
