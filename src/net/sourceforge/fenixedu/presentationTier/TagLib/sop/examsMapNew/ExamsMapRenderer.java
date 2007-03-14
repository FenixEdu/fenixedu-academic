/*
 * Created on Oct 27, 2003
 *  
 */

package net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew.renderers.ExamsMapSlotContentRenderer;
import net.sourceforge.fenixedu.util.Season;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.Globals;
import org.apache.struts.util.RequestUtils;

/*
 * @author Ana & Ricardo
 * 
 */

public class ExamsMapRenderer implements IExamsMapRenderer {

    private String[] daysOfWeek = { "Segunda", "Ter&ccedil;a", "Quarta", "Quinta", "Sexta",
            "S&aacute;bado" };

    private int numberOfWeks;

    private ExamsMap examsMap;

    private ExamsMapSlotContentRenderer examsMapSlotContentRenderer;

    private String user;

    private String mapType;

    private Locale locale;

    private PageContext pageContext;
    
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public ExamsMapRenderer(ExamsMap examsMap, ExamsMapSlotContentRenderer examsMapSlotContentRenderer,
            String typeUser, String mapType, Locale locale) {
        setExamsMap(examsMap);
        setExamsMapSlotContentRenderer(examsMapSlotContentRenderer);
        numberOfWeks = examsMap.getDays().size() / 6;
        setUser(typeUser);
        setMapType(mapType);
        setLocale(locale);
    }

    private String getMessageResource(PageContext pageContext, String key) {
        try {
            return RequestUtils.message(pageContext, "PUBLIC_DEGREE_INFORMATION", Globals.LOCALE_KEY, key);
        } catch (JspException e) {
            return "???" + key + "???";
        }
    }

    public StringBuilder render(Locale locale, PageContext pageContext) {
        this.pageContext = pageContext;
        StringBuilder strBuffer = new StringBuilder("");

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

            strBuffer.append("<td>");
            if (!mapType.equals("DegreeAndYear")) {
                // PRINT EXECUTION DEGREE
                if (user.equals("sop")) {
                    strBuffer.append("<strong>"
                            + examsMap.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getName()
                            + "</strong><br/>");
                }
                if (year2 == null) {
                    strBuffer.append("<h2>");
                    strBuffer.append(getMessageResource(pageContext,
                            "public.degree.information.label.year"));
                    strBuffer.append(" ");
                    strBuffer.append(year1);
                    strBuffer.append("</h2>");
                } else {
                    strBuffer.append("<h2>");
                    strBuffer.append(getMessageResource(pageContext,
                            "public.degree.information.label.years"));
                    strBuffer.append(" ");
                    strBuffer.append(year1);
                    strBuffer.append(" ");
                    strBuffer.append(getMessageResource(pageContext,
                            "public.degree.information.label.and"));
                    strBuffer.append(" ");
                    strBuffer.append(year2);
                    strBuffer.append("</h2>");
                }

                renderExamsMapForFilteredYears(strBuffer, year1, year2, pageContext);
            }
            strBuffer.append("</td>");
            strBuffer.append("</tr>");
            strBuffer.append("<tr>");

            // Generate Exam Map Side Lable
            strBuffer.append("<td class='courseList'>");
            if (mapType.equals("DegreeAndYear")) {
                strBuffer.append("<h2>");
                strBuffer.append(getMessageResource(pageContext,
                        "public.degree.information.label.examsCalendar"));
                strBuffer.append("</h2><br />");
                renderExamsExecutionCourseTableForYear(strBuffer, year1, pageContext);
            } else {
                renderExecutionCourseListForYear(strBuffer, year1, pageContext);
            }

            if (year2 != null) {
                strBuffer.append("</td>");
                strBuffer.append("</tr>");
                strBuffer.append("<tr>");
                strBuffer.append("<td class='courseList'>");
                strBuffer.append("<br style='page-break-before:always;' />");
                if (mapType.equals("DegreeAndYear")) {
                    strBuffer.append("<h2>");
                    strBuffer.append(getMessageResource(pageContext,
                            "public.degree.information.label.examsCalendar"));
                    strBuffer.append("</h2><br />");
                    renderExamsExecutionCourseTableForYear(strBuffer, year2, pageContext);
                } else {
                    renderExecutionCourseListForYear(strBuffer, year2, pageContext);
                }
                strBuffer.append("</td>");
                strBuffer.append("</tr>");
                strBuffer.append("</table>");
                strBuffer.append("<br style='page-break-before:always;' />");

                if ((i < numberOfCurricularYearsToDisplay - 1) && user.equals("sop")) {
                    strBuffer.append("<br />");
                    strBuffer.append("<br />");
                }
            } else {
                strBuffer.append("</td>");
                strBuffer.append("</tr>");
                strBuffer.append("</table>");
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

    private void renderExecutionCourseListForYear(StringBuilder strBuffer, Integer curricularYear,
            PageContext pageContext) {
        if (!examsMap.hasExecutionCoursesForGivenCurricularYear(curricularYear)) {
            strBuffer.append("<i>").append(getMessageResource(pageContext, "no.execution.courses.for.curricular.year")).append(curricularYear).append("</i>");
        } else {
            if (user.equals("public")) {
                strBuffer.append("<table class='tab_exams_details' cellspacing='0'>");
                strBuffer.append("<tr><th rowspan='2' width='250' class='ordYear'>");
                strBuffer
                        .append(getMessageResource(pageContext, "public.degree.information.label.year"));
                strBuffer.append(" ");
                strBuffer.append(curricularYear);
                strBuffer.append("</th>");
                strBuffer.append("<th colspan='3' width='250'>");
                strBuffer
                        .append(getMessageResource(pageContext, "public.degree.information.label.times"));
                strBuffer.append(" 1");
                strBuffer.append("</th>");
                strBuffer.append("<th colspan='3'>");
                strBuffer
                        .append(getMessageResource(pageContext, "public.degree.information.label.times"));
                strBuffer.append(" 2");
                strBuffer.append("</th></tr>");
                strBuffer.append("<tr><td class='subheader' width='70'>");
                strBuffer.append(getMessageResource(pageContext, "public.degree.information.label.day"));
                strBuffer.append("</td><td class='subheader' width='50'>");
                strBuffer
                        .append(getMessageResource(pageContext, "public.degree.information.label.hour"));
                strBuffer.append("</td><td class='subheader' width='130'>");
                strBuffer
                        .append(getMessageResource(pageContext, "public.degree.information.label.room"));
                strBuffer.append("</td>");
                strBuffer.append("<td class='subheader' width='70'>");
                strBuffer.append(getMessageResource(pageContext, "public.degree.information.label.day"));
                strBuffer.append("</td>" + "<td class='subheader' width='50'>");
                strBuffer
                        .append(getMessageResource(pageContext, "public.degree.information.label.hour"));
                strBuffer.append("</td><td class='subheader' width='130'>");
                strBuffer
                        .append(getMessageResource(pageContext, "public.degree.information.label.room"));
                strBuffer.append("</td></tr>");
            } else {
                strBuffer.append("<strong>");
                strBuffer.append(getMessageResource(pageContext,
                        "public.degree.information.label.courseOf"));
                strBuffer.append(curricularYear + "&ordm; ano:</strong>");
            }

            Collections.sort(examsMap.getExecutionCourses(), new BeanComparator("sigla"));
            String rowClass = "notFirstRow"; // used for CSS style
            for (int i = 0; i < examsMap.getExecutionCourses().size(); i++) {
                InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) examsMap
                        .getExecutionCourses().get(i);

                if (user.equals("sop")) {
                    strBuffer.append("<table border='1' cellspacing='0' cellpadding='3' width='95%'>");
                    strBuffer.append("<tr>");
                }

                if (i == 0) {
                    rowClass = "firstRow";
                }

                if (infoExecutionCourse.getCurricularYear().equals(curricularYear)) {
                    boolean showCreateExamLink = infoExecutionCourse.getAssociatedInfoExams().size() < 2;
                    if (user.equals("sop")) {
                        strBuffer.append("<td colspan='6'>");
                    }

                    if (user.equals("public")) {
                        strBuffer.append("<tr valign='top'>");
                        strBuffer.append("<td class='" + rowClass + "'>");

                        strBuffer.append("<a href='" + addPublicPrefix("executionCourse.do") + "?method=firstPage&amp;executionCourseID="
                                + infoExecutionCourse.getIdInternal()
                                + "'>");
                    } else if (showCreateExamLink && user.equals("sop")) {
                        strBuffer.append("<a href='" + addPublicPrefix("showExamsManagement.do") + "?method=createByCourse&amp;"
                                + SessionConstants.EXECUTION_COURSE_OID + "="
                                + infoExecutionCourse.getIdInternal() + "&amp;"
                                + SessionConstants.EXECUTION_PERIOD_OID + "="
                                + infoExecutionCourse.getInfoExecutionPeriod().getIdInternal() + "&amp;"
                                + SessionConstants.EXECUTION_DEGREE_OID + "="
                                + examsMap.getInfoExecutionDegree().getIdInternal() + "&amp;"
                                + SessionConstants.CURRICULAR_YEAR_OID + "=" + curricularYear.toString()
                                + "'>");
                    }

                    strBuffer.append(infoExecutionCourse.getSigla());
                    strBuffer.append(" - ");
                    strBuffer.append(infoExecutionCourse.getNome());

                    if (user.equals("public")) {
                        strBuffer.append("</a>");
                        strBuffer.append("</td>");
                    }

                    boolean hasComment = infoExecutionCourse.getComment() != null
                            && infoExecutionCourse.getComment().length() > 0
                            && !infoExecutionCourse.getComment().equals(" ");

                    if (hasComment) {
                        if (user.equals("sop")) {
                            strBuffer.append(" : ");
                            strBuffer.append("<a href='" + addPublicPrefix("defineComment.do") + "?executionCourseCode=");
                            strBuffer.append(infoExecutionCourse.getSigla());
                            strBuffer.append("&amp;executionPeriodName="
                                    + infoExecutionCourse.getInfoExecutionPeriod().getName());
                            strBuffer.append("&amp;executionYear="
                                    + infoExecutionCourse.getInfoExecutionPeriod()
                                            .getInfoExecutionYear().getYear());
                            strBuffer.append("&amp;comment=" + infoExecutionCourse.getComment());
                            strBuffer.append("&amp;method=prepare");
                            strBuffer.append("&amp;indexExecutionCourse=" + i);
                            strBuffer.append("&amp;" + SessionConstants.EXECUTION_PERIOD_OID + "="
                                    + infoExecutionCourse.getInfoExecutionPeriod().getIdInternal()
                                    + "&amp;" + SessionConstants.EXECUTION_DEGREE_OID + "="
                                    + examsMap.getInfoExecutionDegree().getIdInternal()
                                    + getCurricularYearsArgs());
                            strBuffer.append("'>");
                        } else if (user.equals("public")) {
                            strBuffer.append("<td colspan='6' class='" + rowClass + "'><i>");
                        }

                        strBuffer.append(infoExecutionCourse.getComment());

                        if (user.equals("sop")) {
                            strBuffer.append("</a>");
                        } else if (user.equals("public")) {
                            strBuffer.append("</i></td>");
                        }

                    } else {
                        if (user.equals("sop")) {
                            strBuffer.append(" : ");
                            strBuffer.append("<a href='" + addPublicPrefix("defineComment.do") + "?executionCourseCode=");
                            strBuffer.append(infoExecutionCourse.getSigla());
                            strBuffer.append("&amp;executionPeriodName="
                                    + infoExecutionCourse.getInfoExecutionPeriod().getName());
                            strBuffer.append("&amp;executionYear="
                                    + infoExecutionCourse.getInfoExecutionPeriod()
                                            .getInfoExecutionYear().getYear());
                            strBuffer.append("&amp;method=prepare");
                            strBuffer.append("&amp;indexExecutionCourse=" + i);
                            strBuffer.append("&amp;" + SessionConstants.EXECUTION_PERIOD_OID + "="
                                    + infoExecutionCourse.getInfoExecutionPeriod().getIdInternal()
                                    + "&amp;" + SessionConstants.EXECUTION_DEGREE_OID + "="
                                    + examsMap.getInfoExecutionDegree().getIdInternal()
                                    + getCurricularYearsArgs());
                            strBuffer.append("'>");
                            strBuffer.append("Definir coment&aacute;rio");
                            strBuffer.append("</a>");
                        }
                    }

                    if (user.equals("sop")) {
                        strBuffer.append("</td>");
                        strBuffer.append("</tr>");
                    }

                    if (infoExecutionCourse.getAssociatedInfoExams().isEmpty() && !hasComment) {
                        strBuffer.append("<td colspan='6' align='center' class='" + rowClass + "'>");
                        strBuffer.append("<i>").append(getMessageResource(pageContext, "no.exams.for.curricular.year")).append("</i>");
                        strBuffer.append("</td>");
                    } else {
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

                        if (user.equals("public")) {
                            if (season1Exam == null && !hasComment) {
                                strBuffer.append("<td colspan='3' class='" + rowClass + "'>&nbsp;</td>");
                            }
                            if (season2Exam == null && !hasComment) {
                                strBuffer.append("<td colspan='3' class='" + rowClass + "'>&nbsp;</td>");
                            }
                        }

                        if ((season1Exam != null)
                        // && season1Exam.getAssociatedRooms() != null
                                // && season1Exam.getAssociatedRooms().size() >
                                // 0)
                                || (season2Exam != null))
                        // && season2Exam.getAssociatedRooms() != null
                        // && season2Exam.getAssociatedRooms().size() > 0))
                        {
                            if (season1Exam != null) {
                                if (user.equals("sop")) {
                                    strBuffer.append("<tr>");
                                    strBuffer.append("<td>");

                                    strBuffer.append("<a href='" + addPublicPrefix("showExamsManagement.do") + "?method=edit&amp;"
                                            + SessionConstants.EXECUTION_COURSE_OID
                                            + "="
                                            + infoExecutionCourse.getIdInternal()
                                            + "&amp;"
                                            + SessionConstants.EXECUTION_PERIOD_OID
                                            + "="
                                            + infoExecutionCourse.getInfoExecutionPeriod()
                                                    .getIdInternal() + "&amp;"
                                            + SessionConstants.EXECUTION_DEGREE_OID + "="
                                            + examsMap.getInfoExecutionDegree().getIdInternal()
                                            + "&amp;" + SessionConstants.CURRICULAR_YEAR_OID + "="
                                            + curricularYear.toString() + "&amp;"
                                            + SessionConstants.EXAM_OID + "="
                                            + season1Exam.getIdInternal() + "'>");
                                }

                                if (user.equals("sop")) {
                                    strBuffer.append("1&ordf; &Eacute;poca");
                                    strBuffer.append("</a>");
                                    strBuffer.append("</td>");
                                    strBuffer.append("<td>");
                                    strBuffer.append(season1Exam.getDate());
                                    strBuffer.append("<br/>");
                                    strBuffer.append(season1Exam.getBeginningHour());
                                    strBuffer.append("-");
                                    strBuffer.append(season1Exam.getEndHour());
                                } else if (user.equals("public")) {
                                    strBuffer.append("<td class='" + rowClass + "'>");
                                    strBuffer.append(season1Exam.getDate());
                                    strBuffer.append("</td>");
                                    strBuffer.append("<td class='" + rowClass + "'>");
                                    strBuffer.append(season1Exam.getBeginningHour());
                                    strBuffer.append("</td>");
                                    // strBuffer.append("-");
                                    // strBuffer.append(season1Exam.getEndHour());
                                }

                                if (user.equals("sop")) {
                                    strBuffer.append("</td>");
                                    strBuffer.append("<td>");

                                    // Integer numAlunos =
                                    // infoExecutionCourse.getNumberOfAttendingStudents();

                                    Integer numAlunos = season1Exam.getEnrolledStudents();

                                    strBuffer.append(numAlunos);
                                    strBuffer.append(getMessageResource(pageContext,
                                            "public.degree.information.label.enrolledPupils"));
                                    strBuffer.append("</td>");
                                    strBuffer.append("<td>");
                                    strBuffer.append(getMessageResource(pageContext,
                                            "public.degree.information.label.lack"));

                                    // Obter o num de lugares de exame das salas
                                    List roomOccupation = season1Exam.getAssociatedRoomOccupation();
                                    int numLugaresSalas = 0;

                                    for (int iterRO = 0; iterRO < roomOccupation.size(); iterRO++) {
                                        InfoRoomOccupation infoRO = (InfoRoomOccupation) roomOccupation
                                                .get(iterRO);

                                        numLugaresSalas += infoRO.getInfoRoom().getCapacidadeExame()
                                                .intValue();
                                    }

                                    int numLugaresAPreencher = numAlunos.intValue() - numLugaresSalas;

                                    // if (numLugaresAPreencher < 0)
                                    // numLugaresAPreencher = 0;

                                    strBuffer.append(numLugaresAPreencher);
                                    strBuffer.append(getMessageResource(pageContext,
                                            "public.degree.information.label.places"));

                                    strBuffer.append("</td>");
                                    strBuffer.append("<td>");
                                }

                                List infoRoomOccupations = season1Exam.getAssociatedRoomOccupation();

                                if (infoRoomOccupations != null && infoRoomOccupations.size() > 0) {
                                    if (user.equals("sop")) {
                                        strBuffer.append(getMessageResource(pageContext,
                                                "public.degree.information.label.rooms"));
                                        strBuffer.append("<br/>");
                                    } else if (user.equals("public")) {
                                        strBuffer.append("<td class='" + rowClass + "'>");
                                    }

                                    for (int iterIRO = 0; iterIRO < infoRoomOccupations.size(); iterIRO++) {
                                        InfoRoomOccupation infoRoomOccupation = (InfoRoomOccupation) infoRoomOccupations
                                                .get(iterIRO);

                                        strBuffer.append(infoRoomOccupation.getInfoRoom().getNome()
                                                + "; ");
                                    }
                                } else {
                                    if (user.equals("sop")) {
                                        strBuffer.append("-");
                                    } else if (user.equals("public")) {
                                        strBuffer.append("<td class='" + rowClass + "'>");
                                        strBuffer.append("<i>");
                                        strBuffer.append(getMessageResource(pageContext,
                                                "public.degree.information.label.noRoomsAttributed"));
                                        strBuffer.append("</i>");
                                    }
                                }

                                if (user.equals("sop")) {
                                    strBuffer.append("</td>");
                                    strBuffer.append("<td>");

                                    strBuffer
                                            .append("<a href='" + addPublicPrefix("showExamsManagement.do") + "?method=delete&amp;"
                                                    + SessionConstants.EXECUTION_COURSE_OID
                                                    + "="
                                                    + infoExecutionCourse.getIdInternal()
                                                    + "&amp;"
                                                    + SessionConstants.EXECUTION_PERIOD_OID
                                                    + "="
                                                    + infoExecutionCourse.getInfoExecutionPeriod()
                                                            .getIdInternal() + "&amp;"
                                                    + SessionConstants.EXECUTION_DEGREE_OID + "="
                                                    + examsMap.getInfoExecutionDegree().getIdInternal()
                                                    + "&amp;" + SessionConstants.CURRICULAR_YEAR_OID
                                                    + "=" + curricularYear.toString() + "&amp;"
                                                    + SessionConstants.EXAM_OID + "="
                                                    + season1Exam.getIdInternal() + "'>");
                                    strBuffer.append(getMessageResource(pageContext,
                                            "public.degree.information.label.delete"));
                                    strBuffer.append("</a>");
                                    strBuffer.append("</td>");
                                }
                            }

                            if (season2Exam != null) {
                                if (user.equals("sop")) {
                                    strBuffer.append("<tr>");

                                    strBuffer.append("<td>");

                                    strBuffer.append("<a href='" + addPublicPrefix("showExamsManagement.do") + "?method=edit&amp;"
                                            + SessionConstants.EXECUTION_COURSE_OID
                                            + "="
                                            + infoExecutionCourse.getIdInternal()
                                            + "&amp;"
                                            + SessionConstants.EXECUTION_PERIOD_OID
                                            + "="
                                            + infoExecutionCourse.getInfoExecutionPeriod()
                                                    .getIdInternal() + "&amp;"
                                            + SessionConstants.EXECUTION_DEGREE_OID + "="
                                            + examsMap.getInfoExecutionDegree().getIdInternal()
                                            + "&amp;" + SessionConstants.CURRICULAR_YEAR_OID + "="
                                            + curricularYear.toString() + "&amp;"
                                            + SessionConstants.EXAM_OID + "="
                                            + season2Exam.getIdInternal() + "'>");
                                }

                                if (user.equals("sop")) {
                                    strBuffer.append("2&ordf; ");
                                    strBuffer.append(getMessageResource(pageContext,
                                            "public.degree.information.label.times"));
                                    strBuffer.append("</a>");
                                    strBuffer.append("</td>");
                                    strBuffer.append("<td>");
                                    strBuffer.append(season2Exam.getDate());
                                    strBuffer.append("<br/>");
                                    strBuffer.append(season2Exam.getBeginningHour());
                                    strBuffer.append("-");
                                    strBuffer.append(season2Exam.getEndHour());
                                } else if (user.equals("public")) {
                                    strBuffer.append("<td class='" + rowClass + "'>");
                                    strBuffer.append(season2Exam.getDate());
                                    strBuffer.append("</td>");
                                    strBuffer.append("<td class='" + rowClass + "'>");
                                    strBuffer.append(season2Exam.getBeginningHour());
                                    strBuffer.append("</td>");
                                    // strBuffer.append("-");
                                    // strBuffer.append(season2Exam.getEndHour());
                                }

                                if (user.equals("sop")) {
                                    strBuffer.append("</td>");
                                    strBuffer.append("<td>");
                                    // Integer numAlunos =
                                    // infoExecutionCourse.getNumberOfAttendingStudents();
                                    Integer numAlunos = season2Exam.getEnrolledStudents();

                                    strBuffer.append(numAlunos);
                                    strBuffer.append(getMessageResource(pageContext,
                                            "public.degree.information.label.enrolledPupils"));

                                    strBuffer.append("</td>");
                                    strBuffer.append("<td>");
                                    strBuffer.append(getMessageResource(pageContext,
                                            "public.degree.information.label.lack"));

                                    // Obter o num de lugares de exame das salas
                                    List roomOccupation = season2Exam.getAssociatedRoomOccupation();
                                    int numLugaresSalas = 0;

                                    for (int iterRO = 0; iterRO < roomOccupation.size(); iterRO++) {
                                        InfoRoomOccupation infoRO = (InfoRoomOccupation) roomOccupation
                                                .get(iterRO);

                                        numLugaresSalas += infoRO.getInfoRoom().getCapacidadeExame()
                                                .intValue();
                                    }

                                    int numLugaresAPreencher = numAlunos.intValue() - numLugaresSalas;

                                    // if (numLugaresAPreencher < 0)
                                    // numLugaresAPreencher = 0;

                                    strBuffer.append(numLugaresAPreencher);
                                    strBuffer.append(getMessageResource(pageContext,
                                            "public.degree.information.label.places"));
                                    strBuffer.append("</td>");

                                    strBuffer.append("<td>");
                                }

                                List infoRoomOccupations = season2Exam.getAssociatedRoomOccupation();

                                if (infoRoomOccupations != null && infoRoomOccupations.size() > 0) {
                                    if (user.equals("sop")) {
                                        strBuffer.append(getMessageResource(pageContext,
                                                "public.degree.information.label.rooms")
                                                + ":");
                                        strBuffer.append("<br/>");
                                    } else if (user.equals("public")) {
                                        strBuffer.append("<td class='" + rowClass + "'>");
                                    }

                                    for (int iterIRO = 0; iterIRO < infoRoomOccupations.size(); iterIRO++) {
                                        InfoRoomOccupation infoRoomOccupation = (InfoRoomOccupation) infoRoomOccupations
                                                .get(iterIRO);

                                        strBuffer.append(infoRoomOccupation.getInfoRoom().getNome()
                                                + "; ");
                                    }

                                    if (user.equals("public")) {
                                        strBuffer.append("</td>");
                                    }
                                } else {
                                    if (user.equals("sop")) {
                                        strBuffer.append("-");
                                    } else if (user.equals("public")) {
                                        strBuffer.append("<td class='" + rowClass + "'>");
                                        strBuffer.append("<i>");
                                        strBuffer.append(getMessageResource(pageContext,
                                                "public.degree.information.label.noRoomsAttributed"));
                                        strBuffer.append("</i>");
                                        strBuffer.append("</td>");
                                    }
                                }

                                if (user.equals("sop")) {
                                    strBuffer.append("</td>");
                                    strBuffer.append("<td>");
                                    strBuffer
                                            .append("<a href='" + addPublicPrefix("showExamsManagement.do") + "?method=delete&amp;"
                                                    + SessionConstants.EXECUTION_COURSE_OID
                                                    + "="
                                                    + infoExecutionCourse.getIdInternal()
                                                    + "&amp;"
                                                    + SessionConstants.EXECUTION_PERIOD_OID
                                                    + "="
                                                    + infoExecutionCourse.getInfoExecutionPeriod()
                                                            .getIdInternal() + "&amp;"
                                                    + SessionConstants.EXECUTION_DEGREE_OID + "="
                                                    + examsMap.getInfoExecutionDegree().getIdInternal()
                                                    + "&amp;" + SessionConstants.CURRICULAR_YEAR_OID
                                                    + "=" + curricularYear.toString() + "&amp;"
                                                    + SessionConstants.EXAM_OID + "="
                                                    + season2Exam.getIdInternal() + "'>");
                                    strBuffer.append(getMessageResource(pageContext,
                                            "public.degree.information.label.delete"));
                                    strBuffer.append("</a>");
                                    strBuffer.append("</td>");
                                }
                            }
                        }
                    }

                    if (user.equals("public")) {
                        strBuffer.append("</tr>");
                        if (rowClass == "firstRow") {
                            rowClass = "notFirstRow";
                        }
                    }
                }

                if (user.equals("sop")) {
                    strBuffer.append("</tr>");
                    strBuffer.append("</table>");
                    strBuffer.append("<br />");
                }
            }

            if (user.equals("public")) {
                strBuffer.append("</table>");
            }
        }
    }

    private void renderExamsMapForFilteredYears(StringBuilder strBuffer, Integer year1, Integer year2, PageContext pageContext) {
        if (numberOfWeks != 0) {
            strBuffer.append("<table class='examMap' cellspacing='0' cellpadding='3' width='100%'>");

            strBuffer.append("<tr>");
            renderHeader(strBuffer, pageContext);
            strBuffer.append("</tr>");

            for (int week = 0; week < numberOfWeks; week++) {
                strBuffer.append("<tr>");

                renderLabelsForRowOfDays(strBuffer, week, pageContext);
                strBuffer.append("</tr>\r\n");
                strBuffer.append("<tr>");
                renderExamsForRowOfDays(strBuffer, week, year1, year2, pageContext);
                strBuffer.append("</tr>\r\n");
            }

            strBuffer.append("</table>");
            strBuffer.append("<br />");
        }
    }

    private void renderExamsForRowOfDays(StringBuilder strBuffer, int week, Integer year1,
            Integer year2, PageContext pageContext) {
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
                    .getDays().get(week * daysOfWeek.length + slot), year1, year2, user, pageContext));
            strBuffer.append("</td>");
        }
    }

    private void renderLabelsForRowOfDays(StringBuilder strBuffer, int week, PageContext pageContext) {
        for (int slot = 0; slot < daysOfWeek.length; slot++) {
            ExamsMapSlot examsMapSlot = (ExamsMapSlot) examsMap.getDays().get(
                    week * daysOfWeek.length + slot);
            String classCSS = "exam_cell_day";
            if (examsMapSlot.getDay().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                classCSS += "_first";
            }

            strBuffer.append("<td ").append("class='").append(classCSS).append("'>");
            examsMapSlotContentRenderer.renderDayLabel(examsMapSlot, examsMap, user, pageContext);
            strBuffer.append(examsMapSlotContentRenderer.renderDayLabel(examsMapSlot, examsMap, user,
                    pageContext));
            strBuffer.append("</td>");
        }
    }

    private void renderHeader(StringBuilder strBuffer, PageContext pageContext) {
        String makeLocale = getMessageResource(pageContext, "public.degree.information.label.monday")
                + "," + getMessageResource(pageContext, "public.degree.information.label.tusday") + ","
                + getMessageResource(pageContext, "public.degree.information.label.wednesday") + ","
                + getMessageResource(pageContext, "public.degree.information.label.thursday") + ","
                + getMessageResource(pageContext, "public.degree.information.label.friday") + ","
                + getMessageResource(pageContext, "public.degree.information.label.saturday");

        String[] daysOfWeek = makeLocale.split(",");
        // Segunda", "Ter&ccedil;a", "Quarta", "Quinta", "Sexta",
        // "S&aacute;bado" };

        for (int index = 0; index < this.daysOfWeek.length; index++) {
            StringBuilder classCSS = new StringBuilder("examMap_header");
            if (index == 0) {
                classCSS.append("_first");
            }
            strBuffer.append("<td class='").append(classCSS).append("'>\r\n").append(daysOfWeek[index]).append("</td>\r\n");
        }
    }

    private void renderExamsExecutionCourseTableForYear(StringBuilder strBuffer, Integer year,
            PageContext pageContext) {
        // PRINT EXECUTION DEGREE
        strBuffer.append("<strong>"
                + examsMap.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getInfoDegree()
                        .getNome() + "</strong><br />");
        strBuffer.append("<strong>" + year + "&ordm; ");
        strBuffer.append(getMessageResource(pageContext, "public.degree.information.label.year"));
        strBuffer.append("</strong>");
        if(!examsMap.getExecutionCourses().isEmpty()){                   
            strBuffer.append(" - <strong>" + ((InfoExecutionCourse) examsMap.getExecutionCourses().get(0)).getInfoExecutionPeriod().getSemester() + "&ordm; ");
            strBuffer.append(getMessageResource(pageContext, "public.degree.information.label.semester"));
            strBuffer.append("</strong>");
        }
        strBuffer.append(" - <strong>" + examsMap.getInfoExecutionDegree().getInfoExecutionYear().getYear() + "</strong><br />");
        strBuffer.append("<table border='1' cellspacing='0' cellpadding='3' width='95%'>");

        renderExamsTableHeader(strBuffer, pageContext);

        Collections.sort(examsMap.getExecutionCourses(), new BeanComparator("nome"));

        for (int i = 0; i < examsMap.getExecutionCourses().size(); i++) {
            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) examsMap
                    .getExecutionCourses().get(i);
            if (infoExecutionCourse.getCurricularYear().equals(year)
                    && !infoExecutionCourse.getAssociatedInfoExams().isEmpty()) {
                strBuffer.append("<tr>");
                strBuffer.append("<td rowspan='2'>");
                strBuffer.append(infoExecutionCourse.getNome());
                strBuffer.append("</td>");

                writeLineForExecutionCourseAndExamOfSeason(strBuffer, infoExecutionCourse,
                        Season.SEASON1);
                strBuffer.append("</tr>");

                strBuffer.append("<tr>");
                writeLineForExecutionCourseAndExamOfSeason(strBuffer, infoExecutionCourse,
                        Season.SEASON2);
                strBuffer.append("</tr>");
            }
        }

        strBuffer.append("</table>");
    }

    private void writeLineForExecutionCourseAndExamOfSeason(StringBuilder strBuffer,
            InfoExecutionCourse infoExecutionCourse, int season) {
        for (int j = 0; j < infoExecutionCourse.getAssociatedInfoExams().size(); j++) {
            InfoExam infoExam = (InfoExam) infoExecutionCourse.getAssociatedInfoExams().get(j);
            if (infoExam.getSeason().getSeason().intValue() == season) {
                strBuffer.append("<td>" + season + "&ordf; </td>");
                strBuffer.append("<td>" + infoExam.getDate() + "</td>");
                strBuffer.append("<td>" + infoExam.getBeginningHour() + "</td>");
                strBuffer.append("<td>" + infoExam.getEndHour() + "</td>");
                strBuffer.append("<td>");
                ComparatorChain comparatorChain = new ComparatorChain();
                comparatorChain.addComparator(new BeanComparator("infoRoom.capacidadeExame"), true);
                comparatorChain.addComparator(new BeanComparator("infoRoom.nome"));
                Collections.sort(infoExam.getAssociatedRoomOccupation(), comparatorChain);
                for (int k = 0; k < infoExam.getAssociatedRoomOccupation().size(); k++) {
                    if (k > 0) {
                        strBuffer.append(" ");
                    }

                    InfoRoomOccupation infoRoomOccupation = (InfoRoomOccupation) infoExam
                            .getAssociatedRoomOccupation().get(k);
                    strBuffer.append(infoRoomOccupation.getInfoRoom().getNome());
                }
                strBuffer.append("</td>");

            }
        }
    }

    private void renderExamsTableHeader(StringBuilder strBuffer, PageContext pageContext) {
        strBuffer.append("<tr>");
        strBuffer.append("<td> ");
        strBuffer.append(getMessageResource(pageContext, "public.degree.information.label.course"));
        strBuffer.append("</td>");
        strBuffer.append("<td> ");
        strBuffer.append(getMessageResource(pageContext, "public.degree.information.label.times"));
        strBuffer.append("</td>");
        strBuffer.append("<td> ");
        strBuffer.append(getMessageResource(pageContext, "public.degree.information.label.date"));
        strBuffer.append("</td>");
        strBuffer.append("<td> ");
        strBuffer.append(getMessageResource(pageContext, "public.degree.information.label.inicialHour"));
        strBuffer.append("</td>");
        strBuffer.append("<td> ");
        strBuffer.append(getMessageResource(pageContext, "public.degree.information.label.endHour"));
        strBuffer.append("</td>");
        strBuffer.append("<td> ");
        strBuffer.append(getMessageResource(pageContext, "public.degree.information.label.rooms"));
        strBuffer.append("</td>");
        strBuffer.append("</tr>");
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

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String string) {
        mapType = string;
    }
    
    public String addPublicPrefix(String url) {
        String contextPath = ((HttpServletRequest) this.pageContext.getRequest()).getContextPath();
        
        return contextPath + "/publico/" + url;
    }
}