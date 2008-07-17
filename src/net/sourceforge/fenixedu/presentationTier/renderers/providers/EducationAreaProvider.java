package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.EducationArea;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;


public class EducationAreaProvider implements DataProvider {

	public Object provide(Object source, Object currentValue) {
		return new ArrayList<EducationArea>(RootDomainObject.getInstance().getEducationAreas());
	}

	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}
	
}
