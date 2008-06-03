package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Summary;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class LessonTypesToSummariesManagementProvider implements DataProvider{

    public Object provide(Object source, Object currentValue) {
	
	SummariesManagementBean bean = (SummariesManagementBean) source;
	Lesson lesson = bean.getLesson();
	Summary summary = bean.getSummary();
	Set<ShiftType> shiftTypes = new HashSet<ShiftType>();
	
	if(summary != null && summary.getSummaryType() != null) {
	    shiftTypes.add(summary.getSummaryType());
	}
	
	if(lesson != null) {
	    shiftTypes.addAll(lesson.getShift().getTypes());
	}
	
	return shiftTypes;
    }

    public Converter getConverter() {
	 return new EnumConverter();
    }
}
