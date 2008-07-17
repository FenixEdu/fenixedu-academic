package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;

import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.publicAccess.AlumniPublicAccessBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class BusinessAreaChildProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	if (source.getClass().equals(AlumniJobBean.class)) {
	    AlumniJobBean jobBean = (AlumniJobBean) source;
	    if (jobBean.getParentBusinessArea() != null) {
		return jobBean.getParentBusinessArea().getChildAreas();
	    }
	}

	if (source.getClass().equals(AlumniPublicAccessBean.class)) {
	    AlumniPublicAccessBean publicBean = (AlumniPublicAccessBean) source;
	    if (publicBean.getJobBean().getParentBusinessArea() != null) {
		return publicBean.getJobBean().getParentBusinessArea().getChildAreas();
	    }
	}

	return Collections.EMPTY_LIST;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
