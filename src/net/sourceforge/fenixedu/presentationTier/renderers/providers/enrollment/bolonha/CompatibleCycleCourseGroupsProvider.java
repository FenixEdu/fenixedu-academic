package net.sourceforge.fenixedu.presentationTier.renderers.providers.enrollment.bolonha;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.CycleEnrolmentBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CompatibleCycleCourseGroupsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	final CycleEnrolmentBean cycleEnrolmentBean = (CycleEnrolmentBean) source;

	return cycleEnrolmentBean.getCycleDestinationAffinities();

    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

}
