package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.colorPickers;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.ColorPicker;

public class ExecutionCourseTimeTableColorPicker extends ColorPicker {

    protected String getColorKeyFromInfoLesson(InfoShowOccupation infoShowOccupation) {
        StringBuilder strBuffer = new StringBuilder();

        if (infoShowOccupation instanceof InfoLesson) {
            InfoLesson infoLesson = (InfoLesson) infoShowOccupation;

            strBuffer.append(infoLesson.getInfoShift().getShiftTypesCodePrettyPrint());
        } else {
            strBuffer.append("EXAM");
        }

        return strBuffer.toString();
    }

}
