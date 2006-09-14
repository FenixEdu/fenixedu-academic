package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilancyCourseGroupBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ExecutionCoursesInGroup implements DataProvider {

    public Object provide(Object source, Object currentValue) {

        VigilancyCourseGroupBean bean = (VigilancyCourseGroupBean) source;
        VigilantGroup group = bean.getSelectedVigilantGroup();
        return group.getExecutionCourses();

    }

    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

}