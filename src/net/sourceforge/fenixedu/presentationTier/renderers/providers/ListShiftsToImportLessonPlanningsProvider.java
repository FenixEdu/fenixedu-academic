package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.teacher.ImportLessonPlanningsBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ListShiftsToImportLessonPlanningsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        ImportLessonPlanningsBean bean = (ImportLessonPlanningsBean) source;
        Set<Shift> shifts = new TreeSet<Shift>(Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);
        ExecutionCourse executionCourseFrom = bean.getExecutionCourse();
        ExecutionCourse executionCourseTo = bean.getExecutionCourseTo();                      
        if(executionCourseFrom != null && executionCourseTo != null) {              
            Set<ShiftType> shiftTypesTo = executionCourseTo.getShiftTypes();
            for (Shift shift :executionCourseFrom.getAssociatedShiftsSet()) {
                if(shiftTypesTo.contains(shift.getTipo())) {
                    shifts.add(shift);
                }
            }            
        }
        return shifts;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
