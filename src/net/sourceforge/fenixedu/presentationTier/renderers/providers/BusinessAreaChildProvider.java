package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;

import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean;
import net.sourceforge.fenixedu.dataTransferObject.alumni.publicAccess.AlumniPublicAccessBean;
import net.sourceforge.fenixedu.domain.JobBean;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class BusinessAreaChildProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {

	if (source.getClass().equals(AlumniJobBean.class)) {
	    final AlumniJobBean jobBean = (AlumniJobBean) source;
	    if (jobBean.getParentBusinessArea() != null) {
		return jobBean.getParentBusinessArea().getChildAreas();
	    }

	} else if (source.getClass().equals(AlumniPublicAccessBean.class)) {
	    final AlumniPublicAccessBean publicBean = (AlumniPublicAccessBean) source;
	    if (publicBean.getJobBean().getParentBusinessArea() != null) {
		return publicBean.getJobBean().getParentBusinessArea().getChildAreas();
	    }

	} else if (source.getClass().equals(JobBean.class)) {
	    final JobBean jobBean = (JobBean) source;
	    if (jobBean.hasParentBusinessArea()) {
		return jobBean.getParentBusinessArea().getChildAreas();
	    }
	}

	return Collections.emptyList();
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
