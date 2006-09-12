package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean;
import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean.SummaryType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ShiftLessonsToSummariesManagementProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        SummariesManagementBean bean = (SummariesManagementBean) source;
        Shift shift = bean.getShift();
        SummaryType summaryType = bean.getSummaryType();
        Set<Lesson> lessons = new TreeSet<Lesson>(Lesson.LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME);        
        if(shift != null && summaryType != null && summaryType.equals(SummaryType.NORMAL_SUMMARY)) {
           lessons.addAll(shift.getAssociatedLessons());
        }
        return lessons;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
