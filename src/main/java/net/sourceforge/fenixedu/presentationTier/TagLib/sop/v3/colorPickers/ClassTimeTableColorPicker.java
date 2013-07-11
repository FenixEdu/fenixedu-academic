package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.colorPickers;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.InfoGenericEvent;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstanceAggregation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.ColorPicker;
import pt.ist.fenixframework.FenixFramework;

public class ClassTimeTableColorPicker extends ColorPicker {

    @Override
    protected String getColorKeyFromInfoLesson(final InfoShowOccupation infoShowOccupation) {
        if (infoShowOccupation instanceof InfoLesson) {
            return key((InfoLesson) infoShowOccupation);
        }
        if (infoShowOccupation instanceof InfoLessonInstance) {
            return key((InfoLessonInstance) infoShowOccupation);
        }
        if (infoShowOccupation instanceof InfoLessonInstanceAggregation) {
            return key((InfoLessonInstanceAggregation) infoShowOccupation);
        }
        if (infoShowOccupation instanceof InfoWrittenEvaluation) {
            return key((InfoWrittenEvaluation) infoShowOccupation);
        }
        if (infoShowOccupation instanceof InfoGenericEvent) {
            return "GenericEvent";
        }
        return "Other";
    }

    private String key(final Lesson lesson) {
        return lesson.getExecutionCourse().getExternalId();
    }

    private String key(final InfoLesson infoLesson) {
        return key(infoLesson.getLesson());
    }

    private String key(final InfoLessonInstance infoLessonInstance) {
        return key(infoLessonInstance.getLessonInstance().getLesson());
    }

    private String key(final InfoLessonInstanceAggregation infoLessonInstanceAggregation) {
        return infoLessonInstanceAggregation.getShift().getExecutionCourse().getExternalId();
    }

    private String key(final InfoWrittenEvaluation infoWrittenEvaluation) {
        final StringBuilder stringBuilder = new StringBuilder();
        final WrittenEvaluation writtenEvaluation = infoWrittenEvaluation.getWrittenEvaluation();
        for (final ExecutionCourse executionCourse : sort(writtenEvaluation.getAssociatedExecutionCoursesSet())) {
            stringBuilder.append(executionCourse.getExternalId());
        }
        return stringBuilder.toString();
    }

    private SortedSet<ExecutionCourse> sort(final Set<ExecutionCourse> associatedExecutionCoursesSet) {
        final SortedSet<ExecutionCourse> result = new TreeSet<ExecutionCourse>(AbstractDomainObject.COMPARATOR_BY_ID);
        result.addAll(associatedExecutionCoursesSet);
        return result;
    }

}
