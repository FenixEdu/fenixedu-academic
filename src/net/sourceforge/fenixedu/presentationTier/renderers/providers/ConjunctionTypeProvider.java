package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import pt.utl.ist.fenix.tools.file.FilesetMetadataQuery.ConjunctionType;

public class ConjunctionTypeProvider implements DataProvider {

	 public Object provide(Object source, Object currentValue) {
	        List<ConjunctionType> types = new ArrayList<ConjunctionType>();

	        for (ConjunctionType type : ConjunctionType.values()) {
	            types.add(type);
	        }

	        return types;
	    }

	    public Converter getConverter() {
	        return new Converter() {

	            @Override
	            public Object convert(Class type, Object value) {
	                return ConjunctionType.valueOf((String) value);

	            }

	        };
	    }

}
