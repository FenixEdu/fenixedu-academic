package net.sourceforge.fenixedu.presentationTier.renderers.providers;


import pt.utl.ist.fenix.tools.file.FileSearchCriteria.SearchField;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class SearchFieldsForResultPublicationsProvider implements DataProvider {

	public Converter getConverter() {
		return new Converter() {
			@Override
			public Object convert(Class type, Object value) {
				return SearchField.valueOf((String)value);
			}
			
		};
	}

	public Object provide(Object source, Object currentValue) {
		return SearchField.getSearchFieldsInResearchPublications();
	}

}
