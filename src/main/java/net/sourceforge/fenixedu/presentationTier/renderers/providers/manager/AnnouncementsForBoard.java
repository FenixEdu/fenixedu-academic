package net.sourceforge.fenixedu.presentationTier.renderers.providers.manager;

import net.sourceforge.fenixedu.presentationTier.Action.manager.executionCourseManagement.ExecutionCourseBean;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AnnouncementsForBoard implements DataProvider {

    @Override
    public Converter getConverter() {
        return null;
    }

    @Override
    public Object provide(Object arg0, Object arg1) {
        ExecutionCourseBean bean = (ExecutionCourseBean) arg0;
        return bean.getSourceExecutionCourse().getBoard().getAnnouncements();
    }

}
