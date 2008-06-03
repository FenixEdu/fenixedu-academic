package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.teacher.ImportLessonPlanningsBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ListShiftsToImportLessonPlanningsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	
	ImportLessonPlanningsBean bean = (ImportLessonPlanningsBean) source;	
	Set<Shift> shifts = new TreeSet<Shift>(Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);
	
	ExecutionCourse executionCourseFrom = bean.getExecutionCourse();
	ExecutionCourse executionCourseTo = bean.getExecutionCourseTo();
	
	if (executionCourseFrom != null && executionCourseTo != null) {
	    
	    List<ShiftType> shiftTypesTo = new ArrayList<ShiftType>();	    	  
	    for (ShiftType shiftType : executionCourseTo.getShiftTypes()) {					
		if (executionCourseTo.hasCourseLoadForType(shiftType)) {
		    shiftTypesTo.add(shiftType);
		}
	    }
	    	   	   
	    for (Shift shift : executionCourseFrom.getAssociatedShifts()) {
		if (CollectionUtils.containsAny(shiftTypesTo, shift.getTypes())) {
		    shifts.add(shift);
		}
	    }
	    
	    if(shifts.isEmpty()) {
		return executionCourseFrom.getAssociatedShifts();
	    }
	}
	
	return shifts;
    }

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }
}
