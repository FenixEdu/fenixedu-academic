package net.sourceforge.fenixedu.presentationTier.renderers.providers.spaceManager;

import net.sourceforge.fenixedu.domain.space.Space;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AllBuildingsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	return Space.getAllActiveBuildingsOrderedByName();
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }
}
