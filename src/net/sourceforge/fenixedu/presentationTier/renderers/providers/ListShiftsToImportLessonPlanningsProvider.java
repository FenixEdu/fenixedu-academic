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
	if (executionCourseFrom != null && executionCourseTo != null) {
	    Set<ShiftType> shiftTypesTo = executionCourseTo.getShiftTypes();
	    if (shiftTypesTo.isEmpty()) {
		if (executionCourseTo.getTheoreticalHours() != null
			&& executionCourseTo.getTheoreticalHours().doubleValue() != 0.0) {
		    shiftTypesTo.add(ShiftType.TEORICA);
		}
		if (executionCourseTo.getPraticalHours() != null && executionCourseTo.getPraticalHours().doubleValue() != 0.0) {
		    shiftTypesTo.add(ShiftType.PRATICA);
		}

		if (executionCourseTo.getTheoPratHours() != null && executionCourseTo.getTheoPratHours().doubleValue() != 0.0) {
		    shiftTypesTo.add(ShiftType.TEORICO_PRATICA);
		}

		if (executionCourseTo.getLabHours() != null && executionCourseTo.getLabHours().doubleValue() != 0.0) {
		    shiftTypesTo.add(ShiftType.LABORATORIAL);
		}

		if (executionCourseTo.getSeminaryHours() != null && executionCourseTo.getSeminaryHours().doubleValue() != 0.0) {
		    shiftTypesTo.add(ShiftType.SEMINARY);
		}

		if (executionCourseTo.getProblemsHours() != null && executionCourseTo.getProblemsHours().doubleValue() != 0.0) {
		    shiftTypesTo.add(ShiftType.PROBLEMS);
		}

		if (executionCourseTo.getFieldWorkHours() != null && executionCourseTo.getFieldWorkHours().doubleValue() != 0.0) {
		    shiftTypesTo.add(ShiftType.FIELD_WORK);
		}

		if (executionCourseTo.getTrainingPeriodHours() != null
			&& executionCourseTo.getTrainingPeriodHours().doubleValue() != 0.0) {
		    shiftTypesTo.add(ShiftType.TRAINING_PERIOD);
		}

		if (executionCourseTo.getTutorialOrientationHours() != null
			&& executionCourseTo.getTutorialOrientationHours().doubleValue() != 0.0) {
		    shiftTypesTo.add(ShiftType.TUTORIAL_ORIENTATION);
		}
	    }
	    for (Shift shift : executionCourseFrom.getAssociatedShiftsSet()) {
		if (shiftTypesTo.contains(shift.getTipo())) {
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
