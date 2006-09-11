package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean;
import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean.SummaryType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.YearMonthDayConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.joda.time.YearMonthDay;

public class PossibleDatesToSummariesManagementProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        SummariesManagementBean bean = (SummariesManagementBean) source;
        Lesson lesson = bean.getLesson();
        SummaryType summaryType = bean.getSummaryType();
        List<YearMonthDay> possibleSummaryDates = new ArrayList<YearMonthDay>();
        if (lesson != null && summaryType.equals(SummaryType.NORMAL_SUMMARY)) {
             possibleSummaryDates.addAll(lesson.getPossibleDatesToInsertSummary());
        }        
        if(bean.getSummary() != null && bean.getSummaryDate() != null && lesson.isDateValid(bean.getSummaryDate())) {
            possibleSummaryDates.add(bean.getSummaryDate());
        }
        Collections.reverse(possibleSummaryDates);
        return possibleSummaryDates;
    }

    public Converter getConverter() {
        return new YearMonthDayConverter();
    }
}
