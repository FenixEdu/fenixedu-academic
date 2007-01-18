package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.presentationTier.Action.manager.FileItemCreationBean.EducationalResourceType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class EducationalResourceProvider implements DataProvider {

	 public Object provide(Object source, Object currentValue) {
	        List<EducationalResourceType> types = new ArrayList<EducationalResourceType>();

	        for (EducationalResourceType type : EducationalResourceType.values()) {
	        	if(!type.equals(EducationalResourceType.PROJECT_SUBMISSION)) { 
	        		types.add(type);
	        	}
	        }

	        return types;
	    }

	    public Converter getConverter() {
	        return new Converter() {

	            @Override
	            public Object convert(Class type, Object value) {
	                List<EducationalResourceType> types = new ArrayList<EducationalResourceType>();
	            	String[] flatTypes = (String[]) value;
	                for(int i=0; i < flatTypes.length;i++) {
	                	types.add( EducationalResourceType.valueOf(flatTypes[i]));
	                }
	            	return types;

	            }

	        };
	    }

}
