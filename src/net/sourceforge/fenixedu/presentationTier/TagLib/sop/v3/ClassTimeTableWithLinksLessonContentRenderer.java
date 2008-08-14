/*
 * Created on 18:52:01,20/Out/2004
 *
 * by gedl@rnl.ist.utl.pt
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;

/**
 * @author gedl@rnl.ist.utl.pt
 * 
 *         18:52:01,20/Out/2004
 */
public class ClassTimeTableWithLinksLessonContentRenderer implements LessonSlotContentRenderer {

    private String application;

    public ClassTimeTableWithLinksLessonContentRenderer(String application) {
	setApplication(application);
    }

    public StringBuilder render(String context, LessonSlot lessonSlot) {

	StringBuilder strBuffer = new StringBuilder();
	InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

	if (showOccupation instanceof InfoLesson) {

	    InfoLesson lesson = (InfoLesson) showOccupation;
	    final InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
	    final Site site = infoExecutionCourse.getExecutionCourse().getSite();

	    if (site.isPublic()) {
		strBuffer.append(ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX);
	    } else {
		strBuffer.append(ContentInjectionRewriter.HAS_CONTEXT_PREFIX);
	    }
	    strBuffer.append("<a href=\"").append(context);
	    strBuffer.append(site.getReversePath());
	    strBuffer.append("\">");
	    strBuffer.append(infoExecutionCourse.getSigla()).append("</a>");
	    strBuffer.append("&nbsp;").append("&nbsp;(").append(lesson.getInfoShift().getShiftTypesCodePrettyPrint()).append(
		    ")&nbsp;");

	    if (lesson.getInfoRoomOccupation() != null) {
		strBuffer.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome());
	    }

	    return strBuffer;

	} else if (showOccupation instanceof InfoLessonInstance) {

	    InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;
	    final InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
	    final Site site = infoExecutionCourse.getExecutionCourse().getSite();

	    if (site.isPublic()) {
		strBuffer.append(ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX);
	    } else {
		strBuffer.append(ContentInjectionRewriter.HAS_CONTEXT_PREFIX);
	    }
	    strBuffer.append("<a href=\"").append(context);
	    strBuffer.append(infoExecutionCourse.getExecutionCourse().getSite().getReversePath());
	    strBuffer.append("\">");
	    strBuffer.append("&nbsp;").append("&nbsp;(").append(lesson.getShiftTypeCodesPrettyPrint()).append(")&nbsp;");

	    if (lesson.getInfoRoomOccupation() != null) {
		strBuffer.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome());
	    }

	    return strBuffer;
	}

	return new StringBuilder("");
    }

    public String getApplication() {
	return application;
    }

    public void setApplication(String application) {
	this.application = application;
    }
}
