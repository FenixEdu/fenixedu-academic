/*
 * Created on Apr 3, 2003
 *  
 */

package net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMap;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMap.renderers.ExamsMapSlotContentRenderer;
import net.sourceforge.fenixedu.util.Season;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/*
 * @author Luis Cruz & Sara Ribeiro
 *  
 */

public class ExamsMapRenderer implements IExamsMapRenderer {

    private String[] daysOfWeek = { "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado" };

    private int numberOfWeks;

    private ExamsMap examsMap;

    private ExamsMapSlotContentRenderer examsMapSlotContentRenderer;

    private String user;
    
    private Locale locale;

    public ExamsMapRenderer(ExamsMap examsMap, ExamsMapSlotContentRenderer examsMapSlotContentRenderer,
            String typeUser,Locale locale) {
        
        setExamsMap(examsMap);
        setExamsMapSlotContentRenderer(examsMapSlotContentRenderer);
        numberOfWeks = examsMap.getDays().size() / 6;
        setUser(typeUser);
        setLocale(locale);
    }

    public StringBuilder render(Locale locale) {
        StringBuilder strBuffer = new StringBuilder("");
        ResourceBundle bundle = ResourceBundle
                .getBundle("resources.PublicDegreeInformation",locale);
        // Generate maps for the specified years.
        int numberOfCurricularYearsToDisplay = this.examsMap.getCurricularYears().size();
        for (int i = 0; i < numberOfCurricularYearsToDisplay; i++) {
            Integer year1 = (Integer) this.examsMap.getCurricularYears().get(i);
            Integer year2 = null;
            if (i + 1 < numberOfCurricularYearsToDisplay
                    && year1.intValue() + 1 == ((Integer) this.examsMap.getCurricularYears().get(i + 1))
                            .intValue()) {
                year2 = (Integer) this.examsMap.getCurricularYears().get(i + 1);

                i++;
            }

            strBuffer
                    .append("<table class='examMapContainer' cellspacing='0' cellpadding='3' width='95%'>");
            strBuffer.append("<tr>");

            // Generate Exam Map
            strBuffer.append("<td width='100%'>");
            if (year2 == null) {
                
                strBuffer.append("<strong>" + year1 + "º<strong> ");
                strBuffer.append(bundle.getString("label.year"));
            } else {
                strBuffer.append("<strong>" + year1 + "º</strong>");
                strBuffer.append(" e <strong>" + year2 + "º</strong> ");
                strBuffer.append(bundle.getString("label.year"));
            }

            renderExamsMapForFilteredYears(strBuffer, year1, year2);
            strBuffer.append("</td>");

            strBuffer.append("<tr>");

            // Generate Exam Map Side Lable
            strBuffer.append("<td class='courseList'>");
            strBuffer.append("<br />");
            renderExecutionCourseListForYear(strBuffer, year1);
            if (year2 != null) {
                strBuffer.append("<br />");
                renderExecutionCourseListForYear(strBuffer, year2);
            }
            strBuffer.append("</td>");

            strBuffer.append("</tr>");

            strBuffer.append("</tr>");
            strBuffer.append("</table>");

            if (i < numberOfCurricularYearsToDisplay - 1) {
                strBuffer.append("<br />");
                strBuffer.append("<br />");
            }
        }

        return strBuffer;
    }

    private String getCurricularYearsArgs() {
        String result = "";

        List curricularYears = examsMap.getCurricularYears();
        for (int i = 0; i < curricularYears.size(); i++) {
            if (curricularYears.get(i).equals(new Integer(1))) {
                result += "&amp;" + SessionConstants.CURRICULAR_YEARS_1 + "=1";
            }
            if (curricularYears.get(i).equals(new Integer(2))) {
                result += "&amp;" + SessionConstants.CURRICULAR_YEARS_2 + "=2";
            }
            if (curricularYears.get(i).equals(new Integer(3))) {
                result += "&amp;" + SessionConstants.CURRICULAR_YEARS_3 + "=3";
            }
            if (curricularYears.get(i).equals(new Integer(4))) {
                result += "&amp;" + SessionConstants.CURRICULAR_YEARS_4 + "=4";
            }
            if (curricularYears.get(i).equals(new Integer(5))) {
                result += "&amp;" + SessionConstants.CURRICULAR_YEARS_5 + "=5";
            }
        }
        return result;
    }

    private void renderExecutionCourseListForYear(StringBuilder strBuffer, Integer year) {
        strBuffer.append("<strong>Disciplinas do " + year + "º ano:</strong><br />");
        for (int i = 0; i < examsMap.getExecutionCourses().size(); i++) {
            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) examsMap
                    .getExecutionCourses().get(i);

            if (infoExecutionCourse.getCurricularYear().equals(year)) {
                boolean showCreateExamLink = infoExecutionCourse.getAssociatedInfoExams().size() < 2;

                if (user.equals("public")) {
                    strBuffer.append("<a href='executionCourse.do?method=firstPage&amp;executionCourseID="
                            + infoExecutionCourse.getIdInternal() + "'>");
                } else if (showCreateExamLink && user.equals("sop")) {
                    strBuffer.append("<a href='viewExamsMap.do?method=create&amp;indexExecutionCourse="
                            + i + "&amp;" + SessionConstants.EXECUTION_PERIOD_OID + "="
                            + infoExecutionCourse.getInfoExecutionPeriod().getIdInternal() + "&amp;"
                            + SessionConstants.EXECUTION_DEGREE_OID + "="
                            + examsMap.getInfoExecutionDegree().getIdInternal()
                            + getCurricularYearsArgs() + "'>");
                }

                strBuffer.append(infoExecutionCourse.getSigla());
                strBuffer.append(" - ");
                strBuffer.append(infoExecutionCourse.getNome());

                if (showCreateExamLink || user.equals("public")) {
                    strBuffer.append("</a>");
                }

                if (infoExecutionCourse.getComment() != null
                        && infoExecutionCourse.getComment().length() > 0) {

                    strBuffer.append(" : ");
                    if (user.equals("sop")) {
                        strBuffer.append("<a href='defineExamComment.do?executionCourseCode=");
                        strBuffer.append(infoExecutionCourse.getSigla());
                        strBuffer.append("&amp;executionPeriodName="
                                + infoExecutionCourse.getInfoExecutionPeriod().getName());
                        strBuffer.append("&amp;executionYear="
                                + infoExecutionCourse.getInfoExecutionPeriod().getInfoExecutionYear()
                                        .getYear());
                        strBuffer.append("&amp;comment=" + infoExecutionCourse.getComment());
                        strBuffer.append("&amp;method=prepare");
                        strBuffer.append("&amp;indexExecutionCourse=" + i);
                        strBuffer.append("&amp;" + SessionConstants.EXECUTION_PERIOD_OID + "="
                                + infoExecutionCourse.getInfoExecutionPeriod().getIdInternal() + "&amp;"
                                + SessionConstants.EXECUTION_DEGREE_OID + "="
                                + examsMap.getInfoExecutionDegree().getIdInternal()
                                + getCurricularYearsArgs());
                        strBuffer.append("'>");
                    }

                    strBuffer.append(infoExecutionCourse.getComment());

                    if (user.equals("sop")) {
                        strBuffer.append("</a>");
                    }

                } else {
                    if (user.equals("sop")) {
                        strBuffer.append(" : ");
                        strBuffer.append("<a href='defineExamComment.do?executionCourseCode=");
                        strBuffer.append(infoExecutionCourse.getSigla());
                        strBuffer.append("&amp;executionPeriodName="
                                + infoExecutionCourse.getInfoExecutionPeriod().getName());
                        strBuffer.append("&amp;executionYear="
                                + infoExecutionCourse.getInfoExecutionPeriod().getInfoExecutionYear()
                                        .getYear());
                        strBuffer.append("&amp;method=prepare");
                        strBuffer.append("&amp;indexExecutionCourse=" + i);
                        strBuffer.append("&amp;" + SessionConstants.EXECUTION_PERIOD_OID + "="
                                + infoExecutionCourse.getInfoExecutionPeriod().getIdInternal() + "&amp;"
                                + SessionConstants.EXECUTION_DEGREE_OID + "="
                                + examsMap.getInfoExecutionDegree().getIdInternal()
                                + getCurricularYearsArgs());
                        strBuffer.append("'>");
                        strBuffer.append("Definir comentário");
                        strBuffer.append("</a>");
                    }
                }
                strBuffer.append("<br />");

                // Get 1st season exam
                InfoExam season1Exam = (InfoExam) CollectionUtils.find(infoExecutionCourse
                        .getAssociatedInfoExams(), new Predicate() {
                    public boolean evaluate(Object obj) {
                        InfoExam infoExam = (InfoExam) obj;
                        return infoExam.getSeason().equals(new Season(Season.SEASON1));
                    }
                });
                // Get 2nd season exam
                InfoExam season2Exam = (InfoExam) CollectionUtils.find(infoExecutionCourse
                        .getAssociatedInfoExams(), new Predicate() {
                    public boolean evaluate(Object obj) {
                        InfoExam infoExam = (InfoExam) obj;
                        return infoExam.getSeason().equals(new Season(Season.SEASON2));
                    }
                });

                if ((season1Exam != null && season1Exam.getAssociatedRooms() != null && season1Exam
                        .getAssociatedRooms().size() > 0)
                        || (season2Exam != null && season2Exam.getAssociatedRooms() != null && season2Exam
                                .getAssociatedRooms().size() > 0)) {

                    strBuffer.append("<table>");

                    strBuffer.append("<tr>");
                    strBuffer.append("<td colspan='2'>");
                    strBuffer.append("Salas");
                    strBuffer.append("</td>");
                    strBuffer.append("</tr>");

                    // Print rooms for 1st season exam
                    if (season1Exam != null) {
                        List infoRooms = season1Exam.getAssociatedRooms();
                        if (infoRooms != null && infoRooms.size() > 0) {
                            strBuffer.append("<tr>");
                            strBuffer.append("<td nowrap='nowrap' valign='top'>");
                            strBuffer.append("1ª Época: ");
                            strBuffer.append("</td>");
                            strBuffer.append("<td>");
                            for (int r = 0; r < infoRooms.size(); r++) {
                                InfoRoom infoRoom = (InfoRoom) infoRooms.get(r);
                                strBuffer.append(infoRoom.getNome() + "; ");
                            }
                            strBuffer.append("</td>");
                            strBuffer.append("</tr>");

                        }
                    }
                    // Print rooms for 2nd season exam
                    if (season2Exam != null) {
                        List infoRooms = season2Exam.getAssociatedRooms();
                        if (infoRooms != null && infoRooms.size() > 0) {
                            strBuffer.append("<tr>");
                            strBuffer.append("<td nowrap='nowrap' valign='top'>");
                            strBuffer.append("2ª Época: ");
                            strBuffer.append("</td>");

                            strBuffer.append("<td>");
                            for (int r = 0; r < infoRooms.size(); r++) {
                                InfoRoom infoRoom = (InfoRoom) infoRooms.get(r);
                                strBuffer.append(infoRoom.getNome() + "; ");
                            }
                            strBuffer.append("</td>");
                            strBuffer.append("</tr>");

                            strBuffer.append("<br />");
                        }
                    }

                    strBuffer.append("</table>");
                }

                strBuffer.append("<br />");
            }
        }
        strBuffer.append("<br />");
    }

    private void renderExamsMapForFilteredYears(StringBuilder strBuffer, Integer year1, Integer year2) {
        strBuffer.append("<table class='examMap' cellspacing='0' cellpadding='3' width='95%'>");

        strBuffer.append("<tr>");
        renderHeader(strBuffer);
        strBuffer.append("</tr>");

        for (int week = 0; week < numberOfWeks; week++) {
            strBuffer.append("<tr>");
            renderLabelsForRowOfDays(strBuffer, week);
            strBuffer.append("</tr>\r\n");
            strBuffer.append("<tr>");
            renderExamsForRowOfDays(strBuffer, week, year1, year2);
            strBuffer.append("</tr>\r\n");
        }

        strBuffer.append("</table>");
    }

    private void renderExamsForRowOfDays(StringBuilder strBuffer, int week, Integer year1, Integer year2) {
        for (int slot = 0; slot < daysOfWeek.length; slot++) {
            ExamsMapSlot examsMapSlot = (ExamsMapSlot) examsMap.getDays().get(
                    week * daysOfWeek.length + slot);

            String classCSS = "exam_cell_content";
            if (examsMapSlot.getDay().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                classCSS += "_first";
            }

            if (week == numberOfWeks - 1) {
                classCSS += "_bottom";
            }

            strBuffer.append("<td ").append("class='").append(classCSS).append("'>");
            strBuffer.append(examsMapSlotContentRenderer.renderDayContents((ExamsMapSlot) examsMap
                    .getDays().get(week * daysOfWeek.length + slot), year1, year2, user));
            strBuffer.append("</td>");
        }
    }

    private void renderLabelsForRowOfDays(StringBuilder strBuffer, int week) {
        for (int slot = 0; slot < daysOfWeek.length; slot++) {
            ExamsMapSlot examsMapSlot = (ExamsMapSlot) examsMap.getDays().get(
                    week * daysOfWeek.length + slot);

            String classCSS = "exam_cell_day";
            if (examsMapSlot.getDay().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                classCSS += "_first";
            }

            strBuffer.append("<td ").append("class='").append(classCSS).append("'>");
            strBuffer.append(examsMapSlotContentRenderer.renderDayLabel(examsMapSlot, examsMap));
            strBuffer.append("</td>");
        }
    }

    private void renderHeader(StringBuilder strBuffer) {
        for (int index = 0; index < this.daysOfWeek.length; index++) {
            StringBuilder classCSS = new StringBuilder("examMap_header");
            if (index == 0) {
                classCSS.append("_first");
            }
            strBuffer.append("<td class='").append(classCSS).append("'>\r\n").append(daysOfWeek[index])
                    .append("</td>\r\n");
        }
    }

    /**
     * @param map
     */
    private void setExamsMap(ExamsMap map) {
        examsMap = map;
    }

    /**
     * @param renderer
     */
    private void setExamsMapSlotContentRenderer(ExamsMapSlotContentRenderer renderer) {
        examsMapSlotContentRenderer = renderer;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String string) {
        user = string;
    }

    public Locale getLocale() {
        return locale;
    }
    

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    

}