package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class ExecutionCourseShiftTypesProvider implements DataProvider{

    public Object provide(Object source, Object currentValue) {
        ExecutionCourse executionCourse = ((LessonPlanning)source).getExecutionCourse();        
        return executionCourse.getShiftTypes();
    }

    public Converter getConverter() {        
        return new EnumConverter();
    }

}
