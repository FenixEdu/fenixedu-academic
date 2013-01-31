/**
 * Aug 6, 2005
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlot;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlotContentRendererShift;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ShiftEnrollmentTimeTableLessonContentRenderer implements LessonSlotContentRendererShift {
	private String studentID;

	private String application;

	private String action;

	private String classID;

	private String executionCourseID;

	public ShiftEnrollmentTimeTableLessonContentRenderer(String studentID, String application, String classID,
			String executionCourseID, String action) {
		setStudentID(studentID);
		setApplication(application);
		setClassID(classID);
		setExecutionCourseID(executionCourseID);
		setAction(action);
	}

	@Override
	public StringBuilder render(String context, LessonSlot lessonSlot) {
		StringBuilder strBuffer = new StringBuilder();
		InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

		if (showOccupation instanceof InfoLesson) {
			InfoLesson lesson = (InfoLesson) showOccupation;

			final InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
			final Site site = infoExecutionCourse.getExecutionCourse().getSite();

			strBuffer.append("<span class=\"float-left\">");
			// CONTENT / CHECKSUM prefixes have to be right before <a> tag
			if (site.isPublic()) {
				strBuffer
						.append(pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX);
			} else {
				strBuffer.append(pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX);
			}
			strBuffer.append("<a href=\"").append(context);
			strBuffer.append(site.getReversePath());
			strBuffer.append("\">");
			strBuffer.append(infoExecutionCourse.getSigla()).append("</a>");
			strBuffer.append("&nbsp;").append("&nbsp;(").append(lesson.getInfoShift().getShiftTypesCodePrettyPrint())
					.append(")&nbsp;");

			if (lesson.getInfoRoomOccupation() != null) {
				strBuffer.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome());
			}
			strBuffer.append("</span>");

			return strBuffer;

		} else if (showOccupation instanceof InfoLessonInstance) {

			InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;
			final InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
			final Site site = infoExecutionCourse.getExecutionCourse().getSite();

			strBuffer.append("<span class=\"float-left\">");
			// CONTENT / CHECKSUM prefixes have to be right before <a> tag
			if (site.isPublic()) {
				strBuffer
						.append(pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX);
			} else {
				strBuffer.append(pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX);
			}
			strBuffer.append("<a href=\"").append(context);
			strBuffer.append(infoExecutionCourse.getExecutionCourse().getSite().getReversePath());
			strBuffer.append("\">");
			strBuffer.append("&nbsp;").append("&nbsp;(").append(lesson.getShiftTypeCodesPrettyPrint()).append(")&nbsp;");

			if (lesson.getInfoRoomOccupation() != null) {
				strBuffer.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome());
			}
			strBuffer.append("</span>");

			return strBuffer;
		}

		return new StringBuilder("");
	}

	@Override
	public StringBuilder lastRender(LessonSlot lessonSlot, String context) {
		StringBuilder strBuffer = new StringBuilder();
		InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();
		if (showOccupation instanceof InfoLesson) {
			InfoLesson lesson = (InfoLesson) showOccupation;
			strBuffer.append(getURL(lesson, context));
			strBuffer.append("<img src=\"").append(context).append("/images/").append(getImage()).append("\"/>").append("</a>");
			return strBuffer;
		}
		return strBuffer.append("");
	}

	/**
	 * @return
	 */
	private Object getImage() {
		StringBuilder strBuffer = new StringBuilder();
		if (getAction().equalsIgnoreCase("add")) {
			strBuffer.append("add1.gif\" title=\"Adicionar\"");
		} else if (getAction().equalsIgnoreCase("remove")) {
			strBuffer.append("remove1.gif\" title=\"Remover\"");
		} else if (getAction().equalsIgnoreCase("removeram")) {
			strBuffer.append("remove1.gif\" title=\"Remover\"");
		} else if (getAction().equalsIgnoreCase("addram")) {
			strBuffer.append("add1.gif\" title=\"Adicionar\"");
		}

		return strBuffer;
	}

	/**
	 * @param lesson
	 * @return
	 */
	private StringBuilder getURL(InfoLesson lesson, String context) {
		StringBuilder strBuffer = new StringBuilder();

		if (getAction().equalsIgnoreCase("add")) {
			strBuffer.append("<a href=\"" + context + "/student/enrollStudentInShifts.do?registrationOID=");
		} else if (getAction().equalsIgnoreCase("remove")) {
			strBuffer.append("<a href=\"" + context
					+ "/student/studentShiftEnrollmentManager.do?method=unEnroleStudentFromShift&registrationOID=");
		} else if (getAction().equalsIgnoreCase("addram")) {
			strBuffer.append("<a href=\"" + context + "/resourceAllocationManager/enrollStudentInShifts.do?registrationOID=");
		} else if (getAction().equalsIgnoreCase("removeram")) {
			strBuffer
					.append("<a href=\""
							+ context
							+ "/resourceAllocationManager/studentShiftEnrollmentManager.do?method=unEnroleStudentFromShift&registrationOID=");
		}

		strBuffer.append(getStudentID()).append("&shiftId=").append(lesson.getInfoShift().getIdInternal());
		strBuffer.append("&classId=").append(getClassID()).append("&executionCourseID=").append(getExecutionCourseID())
				.append("\">");
		return strBuffer;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getClassID() {
		return classID;
	}

	public void setClassID(String classID) {
		this.classID = classID;
	}

	public String getExecutionCourseID() {
		return executionCourseID;
	}

	public void setExecutionCourseID(String executionCourseID) {
		this.executionCourseID = executionCourseID;
	}
}
