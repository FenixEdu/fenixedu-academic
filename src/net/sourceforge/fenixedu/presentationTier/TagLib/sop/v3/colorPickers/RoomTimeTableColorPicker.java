package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.colorPickers;

import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGenericEvent;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.ColorPicker;

public class RoomTimeTableColorPicker extends ColorPicker {

    protected String getColorKeyFromInfoLesson(InfoShowOccupation infoShowOccupation) {
	StringBuilder strBuffer = new StringBuilder();

	if (infoShowOccupation instanceof InfoLesson) {
	    InfoLesson infoLesson = (InfoLesson) infoShowOccupation;
	    strBuffer.append(infoLesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
	} else {
	    if (infoShowOccupation instanceof InfoGenericEvent || infoShowOccupation instanceof InfoEvaluation) {
		strBuffer.append("GenericEvent");
	    } else {
		strBuffer.append("EXAM");
	    }
	}
	return strBuffer.toString();
    }

}
