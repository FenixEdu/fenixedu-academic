package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.EducationArea;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class EducationAreaProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		return new ArrayList<EducationArea>(RootDomainObject.getInstance().getEducationAreas());
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
