package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.colorPickers;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.ColorPicker;

public class ExecutionCourseTimeTableColorPicker extends ColorPicker {

    protected String getColorKeyFromInfoLesson(InfoShowOccupation infoShowOccupation) {
	return infoShowOccupation instanceof InfoLesson ?
		((InfoLesson) infoShowOccupation).getLesson().getShift().getNome() : "EXAM";
    }

}
