package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.ConvokeBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.YearMonthDay;

public class WrittenEvaluationForGivenVigilantGroup implements DataProvider {

    public Object provide(Object source, Object currentValue) {

        ConvokeBean bean = (ConvokeBean) source;
        VigilantGroup selectedGroup = bean.getSelectedVigilantGroup();
        List<WrittenEvaluation> evaluationsAfterCurrentDate = new ArrayList<WrittenEvaluation>();
        if (selectedGroup != null) {
            List<WrittenEvaluation> writtenEvaluationsForExecutionCourse = selectedGroup
                    .getAllAssocitedWrittenEvaluations();
            List<WrittenEvaluation> writtenEvaluations = getOpenWrittenEvaluations(writtenEvaluationsForExecutionCourse);
            evaluationsAfterCurrentDate.addAll(writtenEvaluations);
            Collections.sort(evaluationsAfterCurrentDate, new BeanComparator("name"));
        }

        return evaluationsAfterCurrentDate;

    }

    private List<WrittenEvaluation> getOpenWrittenEvaluations(List<WrittenEvaluation> writtenEvaluations) {
        YearMonthDay currentDate = new YearMonthDay();
        List<WrittenEvaluation> evaluations = new ArrayList<WrittenEvaluation>(writtenEvaluations);
        List<WrittenEvaluation> evaluationsAfterCurrentDate = new ArrayList<WrittenEvaluation>();

        for (WrittenEvaluation writtenEvaluation : evaluations) {
            if (writtenEvaluation.getDayDateYearMonthDay().isAfter(currentDate)) {
                evaluationsAfterCurrentDate.add(writtenEvaluation);
            }
        }
        return evaluationsAfterCurrentDate;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
