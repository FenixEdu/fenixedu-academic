package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean.ResultPublicationType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class ResultPublicationTypeProvider implements DataProvider {
	
	public Object provide(Object source, Object currentValue) {
        List<ResultPublicationType> types = new ArrayList<ResultPublicationType>();

        for (ResultPublicationType type : ResultPublicationType.values()) {
            types.add(type);
        }
        Collections.sort(types, new Comparator<ResultPublicationType>() {

			public int compare(ResultPublicationType o1, ResultPublicationType o2) {
				return RenderUtils.getEnumString(o1).compareTo(RenderUtils.getEnumString(o2));
			}
        });
        
        return types;
    }

    public Converter getConverter() {
        return new Converter() {

            @Override
            public Object convert(Class type, Object value) {
                return ResultPublicationType.valueOf((String) value);

            }

        };
    }
}
