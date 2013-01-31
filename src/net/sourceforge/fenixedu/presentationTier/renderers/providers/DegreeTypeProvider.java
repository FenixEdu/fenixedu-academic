package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeTypeProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		List<DegreeType> result = new ArrayList<DegreeType>(DegreeType.NOT_EMPTY_VALUES);
		Collections.sort(result);
		return result;
	}

	@Override
	public Converter getConverter() {
		return null;
	}

}
