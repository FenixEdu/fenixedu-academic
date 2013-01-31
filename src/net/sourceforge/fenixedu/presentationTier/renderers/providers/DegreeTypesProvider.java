package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * @deprecated Use {@link #DegreeTypeProvider}
 */
@Deprecated
public class DegreeTypesProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		return new ArrayList<DegreeType>(DegreeType.NOT_EMPTY_VALUES);
	}

	@Override
	public Converter getConverter() {
		return new Converter() {
			@Override
			public Object convert(Class type, Object value) {
				final List<DegreeType> degreeTypes = new ArrayList<DegreeType>();
				for (final String o : (String[]) value) {
					degreeTypes.add(DegreeType.valueOf(o));
				}
				return degreeTypes;
			}
		};
	}

}
