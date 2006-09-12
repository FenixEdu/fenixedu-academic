package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.HashSet;

import net.sourceforge.fenixedu.dataTransferObject.gesdis.CreateLessonPlanningBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class ExecutionCourseShiftTypesToCreateLessonPlanningProvider implements DataProvider{

    public ExecutionCourse getExecutionCourse(Object source) {
	return ((CreateLessonPlanningBean)source).getExecutionCourse();
    }
    
    public Object provide(Object source, Object currentValue) {
        ExecutionCourse executionCourse = getExecutionCourse(source);        
        return (getExecutionCourse(source) != null) ? executionCourse.getShiftTypes() : new HashSet<ShiftType>();        
    }

    public Converter getConverter() {
        return new EnumConverter();       
    }
}
