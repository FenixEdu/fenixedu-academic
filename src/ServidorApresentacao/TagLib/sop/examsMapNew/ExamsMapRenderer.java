/*
 * Created on Oct 27, 2003
 *
*/

package ServidorApresentacao.TagLib.sop.examsMapNew;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import DataBeans.InfoExam;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoRoomOccupation;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.TagLib.sop.examsMapNew.renderers.ExamsMapSlotContentRenderer;
import Util.Season;

/*
 * @author Ana & Ricardo
 * 
*/

public class ExamsMapRenderer implements IExamsMapRenderer
{

    private String[] daysOfWeek = { "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado" };
    private int numberOfWeks;

    private ExamsMap examsMap;
    private ExamsMapSlotContentRenderer examsMapSlotContentRenderer;

    private String user;

    public ExamsMapRenderer(
        ExamsMap examsMap,
        ExamsMapSlotContentRenderer examsMapSlotContentRenderer,
        String typeUser)
    {
        setExamsMap(examsMap);
        setExamsMapSlotContentRenderer(examsMapSlotContentRenderer);
        numberOfWeks = examsMap.getDays().size() / 6;
        setUser(typeUser);
    }

    public StringBuffer render()
    {
        StringBuffer strBuffer = new StringBuffer("");

        // Generate maps for the specified years.
        int numberOfCurricularYearsToDisplay = this.examsMap.getCurricularYears().size();
        for (int i = 0; i < numberOfCurricularYearsToDisplay; i++)
        {
            Integer year1 = (Integer) this.examsMap.getCurricularYears().get(i);
            Integer year2 = null;
            if (i + 1 < numberOfCurricularYearsToDisplay
                && year1.intValue() + 1
                    == ((Integer) this.examsMap.getCurricularYears().get(i + 1)).intValue())
            {
                year2 = (Integer) this.examsMap.getCurricularYears().get(i + 1);

                i++;
            }

            strBuffer.append(
                "<table class='examMapContainer' cellspacing='0' cellpadding='3' width='95%'>");
            strBuffer.append("<tr>");

            // Generate Exam Map
            strBuffer.append("<td width='100%'>");
            if (year2 == null)
            {
                strBuffer.append("<strong>" + year1 + "º<strong> ano");
            }
            else
            {
                strBuffer.append("<strong>" + year1 + "º</strong>");
                strBuffer.append(" e <strong>" + year2 + "º</strong> ano");
            }

            renderExamsMapForFilteredYears(strBuffer, year1, year2);
            strBuffer.append("</td>");

            strBuffer.append("<tr>");

            // Generate Exam Map Side Lable
            strBuffer.append("<td class='courseList'>");
            strBuffer.append("<br />");
            renderExecutionCourseListForYear(strBuffer, year1);
            if (year2 != null)
            {
                strBuffer.append("<br />");
                renderExecutionCourseListForYear(strBuffer, year2);
            }
            strBuffer.append("</td>");

            strBuffer.append("</tr>");

            strBuffer.append("</tr>");
            strBuffer.append("</table>");

            if (i < numberOfCurricularYearsToDisplay - 1)
            {
                strBuffer.append("<br />");
                strBuffer.append("<br />");
            }
        }

        return strBuffer;
    }

    private String getCurricularYearsArgs()
    {
        String result = "";

        List curricularYears = examsMap.getCurricularYears();
        for (int i = 0; i < curricularYears.size(); i++)
        {
            if (curricularYears.get(i).equals(new Integer(1)))
            {
                result += "&amp;" + SessionConstants.CURRICULAR_YEARS_1 + "=1";
            }
            if (curricularYears.get(i).equals(new Integer(2)))
            {
                result += "&amp;" + SessionConstants.CURRICULAR_YEARS_2 + "=2";
            }
            if (curricularYears.get(i).equals(new Integer(3)))
            {
                result += "&amp;" + SessionConstants.CURRICULAR_YEARS_3 + "=3";
            }
            if (curricularYears.get(i).equals(new Integer(4)))
            {
                result += "&amp;" + SessionConstants.CURRICULAR_YEARS_4 + "=4";
            }
            if (curricularYears.get(i).equals(new Integer(5)))
            {
                result += "&amp;" + SessionConstants.CURRICULAR_YEARS_5 + "=5";
            }
        }
        return result;
    }

    private void renderExecutionCourseListForYear(StringBuffer strBuffer, Integer year)
    {
        strBuffer.append("<strong>Disciplinas do " + year + "º ano:</strong><br />");

        for (int i = 0; i < examsMap.getExecutionCourses().size(); i++)
        {
            InfoExecutionCourse infoExecutionCourse =
                (InfoExecutionCourse) examsMap.getExecutionCourses().get(i);

            strBuffer.append("<table border='1' cellspacing='0' cellpadding='3' width='95%'>");
            strBuffer.append("<tr>");

            if (infoExecutionCourse.getCurricularYear().equals(year))
            {
                boolean showCreateExamLink = infoExecutionCourse.getAssociatedInfoExams().size() < 2;
                strBuffer.append("<td colspan='6'>");

                if (user.equals("public"))
                {
                    strBuffer.append(
                        "<a href='viewSite.do?method=firstPage&amp;objectCode="
                            + infoExecutionCourse.getIdInternal()
                            + "'>");
                }
                else if (showCreateExamLink && user.equals("sop"))
                {
                    strBuffer.append(
                        "<a href='showExamsManagement.do?method=createByCourse&amp;"
                            + SessionConstants.EXECUTION_COURSE_OID
                            + "="
                            + infoExecutionCourse.getIdInternal()
                            + "&amp;"
                            + SessionConstants.EXECUTION_PERIOD_OID
                            + "="
                            + infoExecutionCourse.getInfoExecutionPeriod().getIdInternal()
                            + "&amp;"
                            + SessionConstants.EXECUTION_DEGREE_OID
                            + "="
                            + examsMap.getInfoExecutionDegree().getIdInternal()
                            + "&amp;"
                            + SessionConstants.CURRICULAR_YEAR_OID
                            + "="
                            + year.toString()
                            + "'>");
                }

                strBuffer.append(infoExecutionCourse.getSigla());
                strBuffer.append(" - ");
                strBuffer.append(infoExecutionCourse.getNome());

                if (showCreateExamLink || user.equals("public"))
                {
                    strBuffer.append("</a>");
                }

                if (infoExecutionCourse.getComment() != null
                    && infoExecutionCourse.getComment().length() > 0)
                {

                    strBuffer.append(" : ");
                    if (user.equals("sop"))
                    {
                        strBuffer.append("<a href='defineComment.do?executionCourseCode=");
                        strBuffer.append(infoExecutionCourse.getSigla());
                        strBuffer.append(
                            "&amp;executionPeriodName="
                                + infoExecutionCourse.getInfoExecutionPeriod().getName());
                        strBuffer.append(
                            "&amp;executionYear="
                                + infoExecutionCourse
                                    .getInfoExecutionPeriod()
                                    .getInfoExecutionYear()
                                    .getYear());
                        strBuffer.append("&amp;comment=" + infoExecutionCourse.getComment());
                        strBuffer.append("&amp;method=prepare");
                        strBuffer.append("&amp;indexExecutionCourse=" + i);
                        strBuffer.append(
                            "&amp;"
                                + SessionConstants.EXECUTION_PERIOD_OID
                                + "="
                                + infoExecutionCourse.getInfoExecutionPeriod().getIdInternal()
                                + "&amp;"
                                + SessionConstants.EXECUTION_DEGREE_OID
                                + "="
                                + examsMap.getInfoExecutionDegree().getIdInternal()
                                + getCurricularYearsArgs());
                        strBuffer.append("'>");
                    }

                    strBuffer.append(infoExecutionCourse.getComment());

                    if (user.equals("sop"))
                    {
                        strBuffer.append("</a>");
                    }

                }
                else
                {
                    if (user.equals("sop"))
                    {
                        strBuffer.append(" : ");
                        strBuffer.append("<a href='defineComment.do?executionCourseCode=");
                        strBuffer.append(infoExecutionCourse.getSigla());
                        strBuffer.append(
                            "&amp;executionPeriodName="
                                + infoExecutionCourse.getInfoExecutionPeriod().getName());
                        strBuffer.append(
                            "&amp;executionYear="
                                + infoExecutionCourse
                                    .getInfoExecutionPeriod()
                                    .getInfoExecutionYear()
                                    .getYear());
                        strBuffer.append("&amp;method=prepare");
                        strBuffer.append("&amp;indexExecutionCourse=" + i);
                        strBuffer.append(
                            "&amp;"
                                + SessionConstants.EXECUTION_PERIOD_OID
                                + "="
                                + infoExecutionCourse.getInfoExecutionPeriod().getIdInternal()
                                + "&amp;"
                                + SessionConstants.EXECUTION_DEGREE_OID
                                + "="
                                + examsMap.getInfoExecutionDegree().getIdInternal()
                                + getCurricularYearsArgs());
                        strBuffer.append("'>");
                        strBuffer.append("Definir comentário");
                        strBuffer.append("</a>");
                    }
                }
                strBuffer.append("</td>");

                // Get 1st season exam
                InfoExam season1Exam =
                    (
                        InfoExam) CollectionUtils
                            .find(infoExecutionCourse.getAssociatedInfoExams(), new Predicate()
                {
                    public boolean evaluate(Object obj)
                    {
                        InfoExam infoExam = (InfoExam) obj;
                        return infoExam.getSeason().equals(new Season(Season.SEASON1));
                    }
                });
                // Get 2nd season exam				
                InfoExam season2Exam =
                    (
                        InfoExam) CollectionUtils
                            .find(infoExecutionCourse.getAssociatedInfoExams(), new Predicate()
                {
                    public boolean evaluate(Object obj)
                    {
                        InfoExam infoExam = (InfoExam) obj;
                        return infoExam.getSeason().equals(new Season(Season.SEASON2));
                    }
                });

                if ((season1Exam != null)
                    //					&& season1Exam.getAssociatedRooms() != null
                    //					&& season1Exam.getAssociatedRooms().size() > 0)
                    || (season2Exam != null))
                    //						&& season2Exam.getAssociatedRooms() != null
                    //						&& season2Exam.getAssociatedRooms().size() > 0)) 
                {
                    if (season1Exam != null)
                    {
                        strBuffer.append("<tr>");

                        strBuffer.append("<td>");
                        strBuffer.append(
                            "<a href='showExamsManagement.do?method=edit&amp;"
                                + SessionConstants.EXECUTION_COURSE_OID
                                + "="
                                + infoExecutionCourse.getIdInternal()
                                + "&amp;"
                                + SessionConstants.EXECUTION_PERIOD_OID
                                + "="
                                + infoExecutionCourse.getInfoExecutionPeriod().getIdInternal()
                                + "&amp;"
                                + SessionConstants.EXECUTION_DEGREE_OID
                                + "="
                                + examsMap.getInfoExecutionDegree().getIdInternal()
                                + "&amp;"
                                + SessionConstants.CURRICULAR_YEAR_OID
                                + "="
                                + year.toString()
                                + "&amp;"
                                + SessionConstants.EXAM_OID
                                + "="
                                + season1Exam.getIdInternal()
                                + "'>");

                        strBuffer.append("1ª Época");
                        strBuffer.append("</a>");
                        strBuffer.append("</td>");

                        strBuffer.append("<td>");
                        strBuffer.append(season1Exam.getDate());
                        strBuffer.append("<br>");
                        strBuffer.append(season1Exam.getBeginningHour());
                        strBuffer.append("-");
                        strBuffer.append(season1Exam.getEndHour());
                        strBuffer.append("</td>");

                        strBuffer.append("<td>");

                        Integer numAlunos = infoExecutionCourse.getNumberOfAttendingStudents();
                        strBuffer.append(numAlunos);
                        strBuffer.append(" alunos inscritos");
                        strBuffer.append("</td>");

                        strBuffer.append("<td>");
                        strBuffer.append("Faltam ");

                        //Obter o num de lugares de exame das salas
                        List roomOccupation = season1Exam.getAssociatedRoomOccupation();
                        int numLugaresSalas = 0;

                        for (int iterRO = 0; iterRO < roomOccupation.size(); iterRO++)
                        {
                            InfoRoomOccupation infoRO = (InfoRoomOccupation) roomOccupation.get(iterRO);

                            numLugaresSalas += infoRO.getInfoRoom().getCapacidadeExame().intValue();
                        }

                        int numLugaresAPreencher = numAlunos.intValue() - numLugaresSalas;

                        if (numLugaresAPreencher < 0)
                            numLugaresAPreencher = 0;

                        strBuffer.append(numLugaresAPreencher);
                        strBuffer.append(" lugares");
                        strBuffer.append("</td>");

                        strBuffer.append("<td>");

                        List infoRoomOccupations = season1Exam.getAssociatedRoomOccupation();

                        if (infoRoomOccupations != null && infoRoomOccupations.size() > 0)
                        {
                            strBuffer.append("Salas:");
                            strBuffer.append("<br>");
                            for (int iterIRO = 0; iterIRO < infoRoomOccupations.size(); iterIRO++)
                            {
                                InfoRoomOccupation infoRoomOccupation =
                                    (InfoRoomOccupation) infoRoomOccupations.get(iterIRO);

                                strBuffer.append(infoRoomOccupation.getInfoRoom().getNome() + "; ");
                            }
                        }
                        else
                            strBuffer.append("-");

                        strBuffer.append("</td>");

                        strBuffer.append("<td>");
                        /* TODO */
                        strBuffer.append(
                            "<a href='showExamsManagement.do?method=delete&amp;"
                                + SessionConstants.EXECUTION_COURSE_OID
                                + "="
                                + infoExecutionCourse.getIdInternal()
                                + "&amp;"
                                + SessionConstants.EXECUTION_PERIOD_OID
                                + "="
                                + infoExecutionCourse.getInfoExecutionPeriod().getIdInternal()
                                + "&amp;"
                                + SessionConstants.EXECUTION_DEGREE_OID
                                + "="
                                + examsMap.getInfoExecutionDegree().getIdInternal()
                                + "&amp;"
                                + SessionConstants.CURRICULAR_YEAR_OID
                                + "="
                                + year.toString()
                                + "&amp;"
                                + SessionConstants.EXAM_OID
                                + "="
                                + season1Exam.getIdInternal()
                                + "'>");
                        strBuffer.append("Apagar");
                        strBuffer.append("</a>");
                        strBuffer.append("</td>");
                    }

                    if (season2Exam != null)
                    {
                        strBuffer.append("<tr>");

                        strBuffer.append("<td>");
                        strBuffer.append(
                            "<a href='showExamsManagement.do?method=edit&amp;"
                                + SessionConstants.EXECUTION_COURSE_OID
                                + "="
                                + infoExecutionCourse.getIdInternal()
                                + "&amp;"
                                + SessionConstants.EXECUTION_PERIOD_OID
                                + "="
                                + infoExecutionCourse.getInfoExecutionPeriod().getIdInternal()
                                + "&amp;"
                                + SessionConstants.EXECUTION_DEGREE_OID
                                + "="
                                + examsMap.getInfoExecutionDegree().getIdInternal()
                                + "&amp;"
                                + SessionConstants.CURRICULAR_YEAR_OID
                                + "="
                                + year.toString()
                                + "&amp;"
                                + SessionConstants.EXAM_OID
                                + "="
                                + season2Exam.getIdInternal()
                                + "'>");
                        strBuffer.append("2ª Época");
                        strBuffer.append("</a>");
                        strBuffer.append("</td>");

                        strBuffer.append("<td>");
                        strBuffer.append(season2Exam.getDate());
                        strBuffer.append("<br>");
                        strBuffer.append(season2Exam.getBeginningHour());
                        strBuffer.append("-");
                        strBuffer.append(season2Exam.getEndHour());
                        strBuffer.append("</td>");

                        strBuffer.append("<td>");

                        Integer numAlunos = infoExecutionCourse.getNumberOfAttendingStudents();
                        strBuffer.append(numAlunos);
                        strBuffer.append(" alunos inscritos");
                        strBuffer.append("</td>");

                        strBuffer.append("<td>");
                        strBuffer.append("Faltam ");

                        //Obter o num de lugares de exame das salas
                        List roomOccupation = season2Exam.getAssociatedRoomOccupation();
                        int numLugaresSalas = 0;

                        for (int iterRO = 0; iterRO < roomOccupation.size(); iterRO++)
                        {
                            InfoRoomOccupation infoRO = (InfoRoomOccupation) roomOccupation.get(iterRO);

                            numLugaresSalas += infoRO.getInfoRoom().getCapacidadeExame().intValue();
                        }

                        int numLugaresAPreencher = numAlunos.intValue() - numLugaresSalas;

                        if (numLugaresAPreencher < 0)
                            numLugaresAPreencher = 0;

                        strBuffer.append(numLugaresAPreencher);
                        strBuffer.append(" lugares");
                        strBuffer.append("</td>");

                        strBuffer.append("<td>");
                        List infoRoomOccupations = season2Exam.getAssociatedRoomOccupation();

                        if (infoRoomOccupations != null && infoRoomOccupations.size() > 0)
                        {
                            strBuffer.append("Salas:");
                            strBuffer.append("<br>");
                            for (int iterIRO = 0; iterIRO < infoRoomOccupations.size(); iterIRO++)
                            {
                                InfoRoomOccupation infoRoomOccupation =
                                    (InfoRoomOccupation) infoRoomOccupations.get(iterIRO);

                                strBuffer.append(infoRoomOccupation.getInfoRoom().getNome() + "; ");
                            }
                        }
                        else
                            strBuffer.append("-");

                        strBuffer.append("</td>");

                        strBuffer.append("<td>");
                        /* TODO */
						strBuffer.append(
							"<a href='showExamsManagement.do?method=delete&amp;"
								+ SessionConstants.EXECUTION_COURSE_OID
								+ "="
								+ infoExecutionCourse.getIdInternal()
								+ "&amp;"
								+ SessionConstants.EXECUTION_PERIOD_OID
								+ "="
								+ infoExecutionCourse.getInfoExecutionPeriod().getIdInternal()
								+ "&amp;"
								+ SessionConstants.EXECUTION_DEGREE_OID
								+ "="
								+ examsMap.getInfoExecutionDegree().getIdInternal()
								+ "&amp;"
								+ SessionConstants.CURRICULAR_YEAR_OID
								+ "="
								+ year.toString()
								+ "&amp;"
								+ SessionConstants.EXAM_OID
								+ "="
								+ season1Exam.getIdInternal()
								+ "'>");
                        strBuffer.append("Apagar");
                        strBuffer.append("</a>");
                        strBuffer.append("</td>");
                    }
                }
            }

            strBuffer.append("</tr>");
            strBuffer.append("</table>");
            strBuffer.append("<br />");
        }
    }

    private void renderExamsMapForFilteredYears(StringBuffer strBuffer, Integer year1, Integer year2)
    {
        strBuffer.append("<table class='examMap' cellspacing='0' cellpadding='3' width='95%'>");

        strBuffer.append("<tr>");
        renderHeader(strBuffer);
        strBuffer.append("</tr>");

        for (int week = 0; week < numberOfWeks; week++)
        {
            strBuffer.append("<tr>");
            renderLabelsForRowOfDays(strBuffer, week);
            strBuffer.append("</tr>\r\n");
            strBuffer.append("<tr>");
            renderExamsForRowOfDays(strBuffer, week, year1, year2);
            strBuffer.append("</tr>\r\n");
        }

        strBuffer.append("</table>");
    }

    private void renderExamsForRowOfDays(StringBuffer strBuffer, int week, Integer year1, Integer year2)
    {
        for (int slot = 0; slot < daysOfWeek.length; slot++)
        {
            ExamsMapSlot examsMapSlot =
                (ExamsMapSlot) examsMap.getDays().get(week * daysOfWeek.length + slot);

            String classCSS = "exam_cell_content";
            if (examsMapSlot.getDay().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
            {
                classCSS += "_first";
            }

            if (week == numberOfWeks - 1)
            {
                classCSS += "_bottom";
            }

            strBuffer.append("<td ").append("class='").append(classCSS).append("'>");
            strBuffer.append(
                examsMapSlotContentRenderer.renderDayContents(
                    (ExamsMapSlot) examsMap.getDays().get(week * daysOfWeek.length + slot),
                    year1,
                    year2,
                    user));
            strBuffer.append("</td>");
        }
    }

    private void renderLabelsForRowOfDays(StringBuffer strBuffer, int week)
    {
        for (int slot = 0; slot < daysOfWeek.length; slot++)
        {
            ExamsMapSlot examsMapSlot =
                (ExamsMapSlot) examsMap.getDays().get(week * daysOfWeek.length + slot);

            String classCSS = "exam_cell_day";
            if (examsMapSlot.getDay().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
            {
                classCSS += "_first";
            }

            strBuffer.append("<td ").append("class='").append(classCSS).append("'>");
            strBuffer.append(examsMapSlotContentRenderer.renderDayLabel(examsMapSlot, examsMap));
            strBuffer.append("</td>");
        }
    }

    private void renderHeader(StringBuffer strBuffer)
    {
        for (int index = 0; index < this.daysOfWeek.length; index++)
        {
            StringBuffer classCSS = new StringBuffer("examMap_header");
            if (index == 0)
            {
                classCSS.append("_first");
            }
            strBuffer.append("<td class='").append(classCSS).append("'>\r\n").append(
                daysOfWeek[index]).append(
                "</td>\r\n");
        }
    }

    /**
     * @param map
     */
    private void setExamsMap(ExamsMap map)
    {
        examsMap = map;
    }

    /**
     * @param renderer
     */
    private void setExamsMapSlotContentRenderer(ExamsMapSlotContentRenderer renderer)
    {
        examsMapSlotContentRenderer = renderer;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String string)
    {
        user = string;
    }

}
