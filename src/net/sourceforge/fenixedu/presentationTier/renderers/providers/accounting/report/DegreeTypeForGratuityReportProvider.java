package net.sourceforge.fenixedu.presentationTier.renderers.providers.accounting.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreeTypeForGratuityReportProvider implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {
		return Arrays.asList(DegreeType.DEGREE, DegreeType.BOLONHA_DEGREE, DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE,
				DegreeType.BOLONHA_MASTER_DEGREE, DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA);
	}

	@Override
	public Converter getConverter() {
		return new Converter() {
			/**
	     * 
	     */
			private static final long serialVersionUID = 1L;

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
