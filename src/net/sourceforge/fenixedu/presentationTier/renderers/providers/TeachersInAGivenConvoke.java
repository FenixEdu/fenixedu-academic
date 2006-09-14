package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.ConvokeBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class TeachersInAGivenConvoke implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        ConvokeBean bean = (ConvokeBean) source;
        return bean.getTeachersForAGivenCourse();
    }

    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

}
