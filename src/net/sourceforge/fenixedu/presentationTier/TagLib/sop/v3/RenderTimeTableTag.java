package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.MessageResources;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.colorPickers.ClassTimeTableColorPicker;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.colorPickers.ExecutionCourseTimeTableColorPicker;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.colorPickers.RoomTimeTableColorPicker;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers.ClassTimeTableLessonContentRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers.ClassTimeTableWithoutLinksLessonContentRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers.ExecutionCourseTimeTableLessonContentRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers.RoomTimeTableLessonContentRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers.ShiftTimeTableLessonContentRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers.SopClassRoomTimeTableLessonContentRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers.SopClassTimeTableLessonContentRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers.SopRoomTimeTableLessonContentRenderer;

public final class RenderTimeTableTag extends TagSupport {

    private LessonSlotContentRenderer lessonSlotContentRenderer = new ClassTimeTableLessonContentRenderer();

    private int type = 1;

    private String application = "";

    private final Integer startTimeTableHour = new Integer(8);

    private final Integer endTimeTableHour = new Integer(24);

    private final Integer slotSizeMinutes = new Integer(30);

    //private final int HORA_MINIMA = 8;
    //private final int HORA_MAXIMA = 24;

    // Factor de divisão das celulas.
    //private final int COL_SPAN_FACTOR = 24;

    private ColorPicker colorPicker;

    // Nome do atributo que contém a lista de aulas.
    private String name;

    // Mensagens de erro.
    protected static MessageResources messages = MessageResources
            .getMessageResources("ApplicationResources");

    private InfoCurricularYear infoCurricularYear = null;

    private InfoExecutionDegree infoExecutionDegree = null;

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int doStartTag() throws JspException {

        try {
            infoCurricularYear = (InfoCurricularYear) pageContext
                    .findAttribute(SessionConstants.CURRICULAR_YEAR);
            infoExecutionDegree = (InfoExecutionDegree) pageContext
                    .findAttribute(SessionConstants.EXECUTION_DEGREE);
        } catch (ClassCastException e) {
            infoCurricularYear = null;
            infoExecutionDegree = null;
        }

        setLessonSlotRendererAndColorPicker();
        // Obtem a lista de aulas.
        List infoLessonList = null;
        try {
            infoLessonList = (ArrayList) pageContext.findAttribute(name);
        } catch (ClassCastException e) {
            infoLessonList = null;
        }
        if (infoLessonList == null)
            throw new JspException(messages.getMessage("gerarHorario.listaAulas.naoExiste", name));

        if ((String) pageContext.findAttribute("application") != null) {

            setApplication((String) pageContext.findAttribute("application"));
        }

        // Gera o horário a partir da lista de aulas.
        JspWriter writer = pageContext.getOut();
        TimeTable timeTable = generateTimeTable(infoLessonList);

        TimeTableRenderer renderer = new TimeTableRenderer(timeTable, lessonSlotContentRenderer,
                this.slotSizeMinutes, this.startTimeTableHour, this.endTimeTableHour, colorPicker);

        try {
            writer.print(renderer.render());
            writer.print(legenda(infoLessonList));
        } catch (IOException e) {
            throw new JspException(messages.getMessage("gerarHorario.io", e.toString()));
        }
        return (SKIP_BODY);
    }

    /**
     * Method generateTimeTable.
     * 
     * @param listaAulas
     * @return TimeTable
     */
    private TimeTable generateTimeTable(List lessonList) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, this.startTimeTableHour.intValue());
        calendar.set(Calendar.MINUTE, 0);

        Integer numberOfDays = new Integer(6);
        Integer numberOfHours = new Integer(
                (endTimeTableHour.intValue() - startTimeTableHour.intValue())
                        * (60 / slotSizeMinutes.intValue()));

        TimeTable timeTable = new TimeTable(numberOfHours, numberOfDays, calendar, slotSizeMinutes);

        Iterator lessonIterator = lessonList.iterator();

        while (lessonIterator.hasNext()) {

            //InfoLesson infoLesson = (InfoLesson) lessonIterator.next();
            //timeTable.addLesson(infoLesson);
            InfoShowOccupation infoShowOccupation = (InfoShowOccupation) lessonIterator.next();
            timeTable.addLesson(infoShowOccupation);
        }

        return timeTable;
    }

    public int doEndTag() throws JspException {
        return (EVAL_PAGE);
    }

    public void release() {
        super.release();
    }

    private StringBuffer legenda(List listaAulas) {
        StringBuffer result = new StringBuffer("");
        List listaAuxiliar = new ArrayList();
        Iterator iterator = listaAulas.iterator();
        while (iterator.hasNext()) {
            InfoShowOccupation elem = (InfoShowOccupation) iterator.next();
            if (elem instanceof InfoLesson) {
                SubtitleEntry subtitleEntry = new SubtitleEntry(elem.getInfoShift()
                        .getInfoDisciplinaExecucao().getSigla(), elem.getInfoShift()
                        .getInfoDisciplinaExecucao().getNome());
                if (!listaAuxiliar.contains(subtitleEntry))
                    listaAuxiliar.add(subtitleEntry);
            }
        }

        if (listaAuxiliar.size() > 1) {
            Collections.sort(listaAuxiliar);
            result
                    .append("<br/><b>Legenda:</b><br /><br /><table cellpadding='0' cellspacing='0' style='margin-left:5px'>");
            for (int i = 0; i < listaAuxiliar.size(); i++) {
                SubtitleEntry elem = (SubtitleEntry) listaAuxiliar.get(i);
                boolean oddElement = (i % 2 == 1);
                if (!oddElement) {
                    result.append("<tr>\r\n");
                }
                result.append("<td width='60px'><b>");
                result.append(elem.getKey());
                result.append("</b></td><td  style='vertical-align:top'>-</td><td wrap='wrap'>");
                result.append(elem.getValue());
                result.append("</td>");
                if (oddElement) {
                    result.append("</tr>\r\n");
                }
            }
            if (listaAuxiliar.size() % 2 == 1) {
                result.append("<td colspan='3'>&nbsp;</td></tr>");
            }

            //TODO(rspl): Will it stay like this the interface for showing
            // the legend of a quinzenal lesson?
            result.append("<tr><td style='vertical-align:top'><b>[Q]</b></td>");
            result.append("<td  style='vertical-align:top'>-</td>");
            result.append("<td wrap='wrap'>Identifica uma aula como sendo quinzenal</td></tr>");

            result.append("</table>");

        }
        return result;
    }

    /**
     * Returns the type.
     * 
     * @return int
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type
     *            The type to set
     */
    public void setType(int timeTableType) {
        this.type = timeTableType;
        setLessonSlotRendererAndColorPicker();
    }

    private void setLessonSlotRendererAndColorPicker() {
        switch (this.type) {
        case TimeTableType.SHIFT_TIMETABLE:
            this.lessonSlotContentRenderer = new ShiftTimeTableLessonContentRenderer();
            this.colorPicker = new ClassTimeTableColorPicker();
            break;
        case TimeTableType.EXECUTION_COURSE_TIMETABLE:
            this.lessonSlotContentRenderer = new ExecutionCourseTimeTableLessonContentRenderer();
            this.colorPicker = new ExecutionCourseTimeTableColorPicker();
            break;
        case TimeTableType.ROOM_TIMETABLE:
            this.lessonSlotContentRenderer = new RoomTimeTableLessonContentRenderer();
            this.colorPicker = new RoomTimeTableColorPicker();
            break;

        case TimeTableType.SOP_CLASS_TIMETABLE:
            this.lessonSlotContentRenderer = new SopClassTimeTableLessonContentRenderer(
                    infoExecutionDegree, infoCurricularYear);
            this.colorPicker = new ClassTimeTableColorPicker();
            break;

        case TimeTableType.SOP_ROOM_TIMETABLE:
            this.lessonSlotContentRenderer = new SopRoomTimeTableLessonContentRenderer();
            this.colorPicker = new ClassTimeTableColorPicker();
            break;
        case TimeTableType.SOP_CLASS_ROOM_TIMETABLE:
            this.lessonSlotContentRenderer = new SopClassRoomTimeTableLessonContentRenderer();
            this.colorPicker = new ClassTimeTableColorPicker();
            break;
        case TimeTableType.CLASS_TIMETABLE_WITHOUT_LINKS:
            this.lessonSlotContentRenderer = new ClassTimeTableWithoutLinksLessonContentRenderer();
            this.colorPicker = new ClassTimeTableColorPicker();
            break;
        case TimeTableType.CLASS_TIMETABLE:
            this.lessonSlotContentRenderer = new ClassTimeTableWithLinksLessonContentRenderer(
                    getApplication());
            this.colorPicker = new ClassTimeTableColorPicker();
            break;

        default:
            this.lessonSlotContentRenderer = new ClassTimeTableLessonContentRenderer();
            this.colorPicker = new ClassTimeTableColorPicker();
            break;
        }
    }

    /**
     * @return Returns the application.
     */
    public String getApplication() {
        return application;
    }

    /**
     * @param application
     *            The application to set.
     */
    public void setApplication(String application) {
        this.application = application;
    }
}
