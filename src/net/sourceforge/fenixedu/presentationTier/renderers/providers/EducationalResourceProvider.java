package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.presentationTier.Action.manager.FileItemCreationBean.EducationalResourceType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class EducationalResourceProvider implements DataProvider {

	 public Object provide(Object source, Object currentValue) {
	        List<EducationalResourceType> types = new ArrayList<EducationalResourceType>();

	        for (EducationalResourceType type : EducationalResourceType.values()) {
	        	if(!type.equals(EducationalResourceType.PROJECT_SUBMISSION) && !type.equals(EducationalResourceType.SITE_CONTENT)) { 
	        		types.add(type);
	        	}
	        }

	        Collections.sort(types,new Comparator<Enum>() {

				public int compare(Enum o1, Enum o2) {
					return RenderUtils.getEnumString(o1).compareTo(RenderUtils.getEnumString(o2));
				}
	        });
	        
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
