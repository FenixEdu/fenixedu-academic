package net.sourceforge.fenixedu.presentationTier.renderers.providers.pedagogicalCouncil;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class DegreeTypesForBolonhaDegrees implements DataProvider{
	
	public Object provide(Object source, Object currentValue) {
		List<DegreeType> degreeTypes = new ArrayList<DegreeType>();
		
		degreeTypes.add(DegreeType.BOLONHA_DEGREE);
		degreeTypes.add(DegreeType.BOLONHA_MASTER_DEGREE);
		degreeTypes.add(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
    	
		return degreeTypes;
    }

	public Converter getConverter() {
		return new Converter() {

			@Override
			public Object convert(Class type, Object value) {
				return DegreeType.valueOf((String) value);
			}

		};
	}
}
