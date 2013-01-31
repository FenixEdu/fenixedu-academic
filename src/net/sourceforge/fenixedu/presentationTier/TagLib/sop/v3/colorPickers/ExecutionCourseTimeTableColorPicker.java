package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.colorPickers;

import net.sourceforge.fenixedu.dataTransferObject.InfoGenericEvent;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenEvaluation;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.ColorPicker;

public class ExecutionCourseTimeTableColorPicker extends ColorPicker {

	@Override
	protected String getColorKeyFromInfoLesson(final InfoShowOccupation infoShowOccupation) {
		if (infoShowOccupation instanceof InfoLesson) {
			return key((InfoLesson) infoShowOccupation);
		}
		if (infoShowOccupation instanceof InfoLessonInstance) {
			return key((InfoLessonInstance) infoShowOccupation);
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
		return lesson.getShift().getExternalId();
	}

	private String key(final InfoLesson infoLesson) {
		return key(infoLesson.getLesson());
	}

	private String key(final InfoLessonInstance infoLessonInstance) {
		return key(infoLessonInstance.getLessonInstance().getLesson());
	}

	private String key(final InfoWrittenEvaluation infoWrittenEvaluation) {
		return infoWrittenEvaluation.getWrittenEvaluation().getExternalId();
	}

}
