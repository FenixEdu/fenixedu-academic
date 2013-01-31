package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.space.RoomClassification;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class RoomClassificationsProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		return RoomClassification.readClassificationsWithParentSortedByCode();
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}
}
