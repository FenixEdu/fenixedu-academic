package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoGenericEvent;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlot;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlotContentRenderer;

public class SpaceManagerRoomTimeTableLessonContentRenderer implements LessonSlotContentRenderer {

    private static final ResourceBundle spaceManagerResourceBundle = ResourceBundle.getBundle("resources.SpaceResources",
	    new Locale("pt"));

    public StringBuilder render(String context, LessonSlot lessonSlot) {

	StringBuilder strBuffer = new StringBuilder();
	InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

	if (showOccupation instanceof InfoLesson) {

	    InfoLesson lesson = (InfoLesson) showOccupation;
	    strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
	    strBuffer.append("&nbsp;(").append(lesson.getInfoShift().getShiftTypesCodePrettyPrint()).append(")");
	    if (lesson.getFrequency().equals(FrequencyType.BIWEEKLY)) {
		strBuffer.append("&nbsp;&nbsp;[Q]");
	    }

	} else if (showOccupation instanceof InfoLessonInstance) {

	    InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;
	    strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
	    strBuffer.append("&nbsp;(").append(lesson.getShiftTypeCodesPrettyPrint()).append(")");

	} else if (showOccupation instanceof InfoExam) {

	    InfoExam infoExam = (InfoExam) showOccupation;

	    strBuffer.append(spaceManagerResourceBundle.getString("label.written.exam")).append(" ");
	    strBuffer.append(infoExam.getSeason().getSeason());
	    strBuffer.append(spaceManagerResourceBundle.getString("label.written.exam.season")).append(" - ");

	    for (int iterEC = 0; iterEC < infoExam.getAssociatedExecutionCourse().size(); iterEC++) {
		InfoExecutionCourse infoEC = (InfoExecutionCourse) infoExam.getAssociatedExecutionCourse().get(iterEC);
		if (iterEC != 0) {
		    strBuffer.append(", ");
		}
		strBuffer.append(infoEC.getSigla());

	    }

	} else if (showOccupation instanceof InfoWrittenTest) {

	    InfoWrittenTest infoWrittenTest = (InfoWrittenTest) showOccupation;

	    strBuffer.append("<span title=\"").append(infoWrittenTest.getDescription()).append("\">");
	    strBuffer.append(spaceManagerResourceBundle.getString("label.written.test")).append(" - ");

	    for (int iterEC = 0; iterEC < infoWrittenTest.getAssociatedExecutionCourse().size(); iterEC++) {
		InfoExecutionCourse infoEC = (InfoExecutionCourse) infoWrittenTest.getAssociatedExecutionCourse().get(iterEC);
		if (iterEC != 0) {
		    strBuffer.append(", ");
		}
		strBuffer.append(infoEC.getSigla());
	    }

	    strBuffer.append("</span>");

	} else if (showOccupation instanceof InfoGenericEvent) {

	    InfoGenericEvent infoGenericEvent = (InfoGenericEvent) showOccupation;
	    strBuffer.append("<span title=\"").append(infoGenericEvent.getDescription()).append("\">");
	    strBuffer.append(spaceManagerResourceBundle.getString("label.punctual.occupation")).append(" - ");
	    strBuffer.append(infoGenericEvent.getTitle());
	    strBuffer.append("</span>");
	}

	return strBuffer;
    }

}
