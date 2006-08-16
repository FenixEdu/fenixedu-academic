package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

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
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers.ShiftEnrollmentTimeTableLessonContentRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers.ShiftTimeTableLessonContentRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers.SopClassRoomTimeTableLessonContentRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers.SopClassTimeTableLessonContentRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers.SopRoomTimeTableLessonContentRenderer;

import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

public final class RenderTimeTableTag extends TagSupport {

    private LessonSlotContentRenderer lessonSlotContentRenderer = null;

    private int type = 1;

    private String application = "";

    private String studentID = "";

    private String classID = "";

    private String executionCourseID = "";

    private String action = "";

    private String endTime = "";

    private final Integer startTimeTableHour = new Integer(8);

    private Integer endTimeTableHour = new Integer(24);

    private final Integer slotSizeMinutes = new Integer(30);

    // private final int HORA_MINIMA = 8;
    // private final int HORA_MAXIMA = 24;

    // Factor de divisão das celulas.
    // private final int COL_SPAN_FACTOR = 24;
    
    private boolean definedWidth = true;

    private ColorPicker colorPicker;

    // Nome do atributo que contém a lista de aulas.
    private String name;

    // Mensagens de erro.
    protected static MessageResources messages = MessageResources
            .getMessageResources("PublicDegreeInformation");

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
        	e.printStackTrace();
            infoLessonList = null;
        }
        if (infoLessonList == null)
            throw new JspException(messages.getMessage("gerarHorario.listaAulas.naoExiste", name));

//        if ((String) pageContext.findAttribute("application") != null) {
//
//            setApplication((String) pageContext.findAttribute("application"));
//        }
//
//        if ((String) pageContext.findAttribute("studentID") != null) {
//            setStudentID((String) pageContext.findAttribute("studentID"));
//        }
//
//        if ((String) pageContext.findAttribute("classID") != null) {
//            setClassID((String) pageContext.findAttribute("classID"));
//        }
//
//        if ((String) pageContext.findAttribute("executionCourseID") != null) {
//            setExecutionCourseID((String) pageContext.findAttribute("executionCourseID"));
//        }
//
//        if ((String) pageContext.findAttribute("endTime") != null) {
//            setEndTime((String) pageContext.findAttribute("endTime"));
//        }

        // Gera o horário a partir da lista de aulas.
        Locale locale = (Locale) pageContext.findAttribute(Globals.LOCALE_KEY);
        JspWriter writer = pageContext.getOut();
        TimeTable timeTable = generateTimeTable(infoLessonList, locale, pageContext);

        TimeTableRenderer renderer = new TimeTableRenderer(timeTable, lessonSlotContentRenderer,
                this.slotSizeMinutes, this.startTimeTableHour, this.endTimeTableHour, colorPicker);

        try {
            writer.print(renderer.render(locale, pageContext, getDefinedWidth()));
            writer.print(legenda(infoLessonList, locale));
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
    private TimeTable generateTimeTable(List lessonList, Locale locale, PageContext pageContext) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, this.startTimeTableHour.intValue());
        calendar.set(Calendar.MINUTE, 0);

        Integer numberOfDays = new Integer(6);
        Integer numberOfHours = new Integer(
                (endTimeTableHour.intValue() - startTimeTableHour.intValue())
                        * (60 / slotSizeMinutes.intValue()));

        TimeTable timeTable = new TimeTable(numberOfHours, numberOfDays, calendar, slotSizeMinutes,
                locale, pageContext);

        Iterator lessonIterator = lessonList.iterator();

        while (lessonIterator.hasNext()) {

            // InfoLesson infoLesson = (InfoLesson) lessonIterator.next();
            // timeTable.addLesson(infoLesson);
            InfoShowOccupation infoShowOccupation = (InfoShowOccupation) lessonIterator.next();
            timeTable.addLesson(infoShowOccupation);
        }

        return timeTable;
    }

    public int doEndTag() {
        return (EVAL_PAGE);
    }

    public void release() {
        super.release();
        
        this.application = null;
        this.studentID = null;
        this.classID = null;
        this.executionCourseID = null;
        this.action = null;
        this.endTime = null;
    }

    private String getMessageResource(PageContext pageContext, String key) {
        try {
            return RequestUtils.message(pageContext, "PUBLIC_DEGREE_INFORMATION", Globals.LOCALE_KEY,
                    key);
        } catch (JspException e) {
            return "???" + key + "???";
        }
    }

    private StringBuilder legenda(List listaAulas, Locale locale) {
        StringBuilder result = new StringBuilder("");
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
            result.append("<br/><b>");
            result.append(getMessageResource(pageContext, "public.degree.information.label.legend"));
            result
                    .append("</b><br /><br /><table cellpadding='0' cellspacing='0' style='margin-left:5px'>");
            for (int i = 0; i < listaAuxiliar.size(); i++) {
                SubtitleEntry elem = (SubtitleEntry) listaAuxiliar.get(i);
                boolean oddElement = (i % 2 == 1);
                if (!oddElement) {
                    result.append("<tr>\r\n");
                }
                result.append("<td width='60'><b>");
                result.append(elem.getKey());
                result.append("</b></td><td  style='vertical-align:top'>-</td><td>");
                result.append(elem.getValue());
                result.append("</td>");
                if (oddElement) {
                    result.append("</tr>\r\n");
                }
            }
            if (listaAuxiliar.size() % 2 == 1) {
                result.append("<td colspan='3'>&nbsp;</td></tr>");
            }

            // TODO(rspl): Will it stay like this the interface for showing
            // the legend of a quinzenal lesson?
            result.append("<tr><td style='vertical-align:top'><b>[Q]</b></td>");
            result.append("<td  style='vertical-align:top'>-</td>");
            result.append("<td>");
            result.append(getMessageResource(pageContext, "public.degree.information.label.biweekly"));
            result.append("</td></tr>");

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
        case TimeTableType.SHIFT_ENROLLMENT_TIMETABLE:
            this.lessonSlotContentRenderer = new ShiftEnrollmentTimeTableLessonContentRenderer(
                    getStudentID(), getApplication(), getClassID(), getExecutionCourseID(), getAction());
            this.colorPicker = new ClassTimeTableColorPicker();
            Integer defaultTime = new Integer(19);
            Integer endTime = defaultTime;            
            if (!getEndTime().equals("")) {
                endTime = new Integer(getEndTime());                
                if (endTime < defaultTime) {
                    endTime = defaultTime;
                }
            }
            this.endTimeTableHour = endTime;
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

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean getDefinedWidth() {
        return definedWidth;
    }

    public void setDefinedWidth(boolean definedWidth) {
        this.definedWidth = definedWidth;
    }

}
