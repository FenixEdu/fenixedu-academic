package net.sourceforge.fenixedu.presentationTier.renderers.providers.assiduousness;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatus;
import net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class AssidousnessStatusAsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	List<AssiduousnessStatus> assiduousnessStatusList = new ArrayList<AssiduousnessStatus>();
	for (AssiduousnessStatus assiduousnessStatus : RootDomainObject.getInstance()
		.getAssiduousnessStatus()) {
	    if (assiduousnessStatus.getState().equals(AssiduousnessState.ACTIVE)) {
		assiduousnessStatusList.add(assiduousnessStatus);
	    }
	}
	return assiduousnessStatusList;
    }

    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }
}
