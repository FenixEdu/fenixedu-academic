/*
 * Created on 2003/10/28  
 */

package net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew.renderers;

import java.util.Calendar;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew.ExamsMap;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew.ExamsMapSlot;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.struts.Globals;
import org.apache.struts.util.RequestUtils;

/**
 * @author Ana e Ricardo
 */
public class ExamsMapContentRenderer implements ExamsMapSlotContentRenderer {

    private ExamsMap examsMap;

    public StringBuilder renderDayLabel(ExamsMapSlot examsMapSlot, ExamsMap examsMap, String typeUser,PageContext pageContext) {
        this.examsMap = examsMap;
        StringBuilder strBuffer = new StringBuilder();

        boolean isFirstDayOfSeason = ((examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH) == examsMap
                .getFirstDayOfSeason().get(Calendar.DAY_OF_MONTH))
                && (examsMapSlot.getDay().get(Calendar.MONTH) == examsMap.getFirstDayOfSeason().get(
                        Calendar.MONTH)) && (examsMapSlot.getDay().get(Calendar.YEAR) == examsMap
                .getFirstDayOfSeason().get(Calendar.YEAR)));

        boolean isSecondDayOfMonthAndFirstDayWasASunday = (examsMapSlot.getDay().get(
                Calendar.DAY_OF_MONTH) == 2 && examsMapSlot.getDay().get(Calendar.DAY_OF_WEEK) == 2);

        if (examsMap.getInfoExecutionDegree() != null && typeUser.equals("sop")) {
            strBuffer.append("<a href='showExamsManagement.do?method=createByDay" + "&amp;"
                    + SessionConstants.EXECUTION_DEGREE_OID + "="
                    + examsMap.getInfoExecutionDegree().getIdInternal() + "&amp;"
                    + SessionConstants.EXECUTION_PERIOD_OID + "="
                    + examsMap.getInfoExecutionPeriod().getIdInternal() + "&amp;"
                    + SessionConstants.CURRICULAR_YEAR_OID + "=" + examsMap.getCurricularYears().get(0)
                    + "&amp;" + SessionConstants.DAY + "="
                    + examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH) + "&amp;"
                    + SessionConstants.MONTH + "=" + (examsMapSlot.getDay().get(Calendar.MONTH) + 1)
                    + "&amp;" + SessionConstants.YEAR + "=" + examsMapSlot.getDay().get(Calendar.YEAR)
                    + "'>");
        }
        strBuffer.append(examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH));
        if ((examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH) == 1) || isFirstDayOfSeason
                || isSecondDayOfMonthAndFirstDayWasASunday) {
                strBuffer.append(" ");
                strBuffer.append(getMessageResource(pageContext, "public.degree.information.label.of"));
                strBuffer.append(" ");
            Locale locale = pageContext.getRequest().getLocale();
            strBuffer.append(monthToString(examsMapSlot.getDay().get(Calendar.MONTH),locale));
        }
        if (examsMapSlot.getDay().get(Calendar.DAY_OF_YEAR) == 1) {
            strBuffer.append(getMessageResource(pageContext, "public.degree.information.label.comma"));
            strBuffer.append(examsMapSlot.getDay().get(Calendar.YEAR));
        }

        if (examsMap.getInfoExecutionDegree() != null && typeUser.equals("sop")) {
            strBuffer.append("</a>");
        }

        strBuffer.append("<br />");

        return strBuffer;
    }

    private String getMessageResource(PageContext pageContext, String key) {
        try {
            return RequestUtils.message(pageContext, "PUBLIC_DEGREE_INFORMATION", Globals.LOCALE_KEY, key);
        } catch (JspException e) {
            return "???" + key + "???"; 
        }
    }

    public StringBuilder renderDayContents(ExamsMapSlot examsMapSlot, Integer year1, Integer year2,
            String typeUser,PageContext pageContext) {
        StringBuilder strBuffer = new StringBuilder();

        for (int i = 0; i < examsMapSlot.getExams().size(); i++) {
            InfoExam infoExam = (InfoExam) examsMapSlot.getExams().get(i);
            Integer curicularYear = infoExam.getInfoExecutionCourse().getCurricularYear();

            if (curicularYear.equals(year1) || curicularYear.equals(year2)) {
                boolean isOnValidWeekDay = onValidWeekDay(infoExam);

                InfoExecutionCourse infoExecutionCourse = infoExam.getInfoExecutionCourse();
                String courseInitials = infoExam.getInfoExecutionCourse().getSigla();

                if (typeUser.equals("sop")) {
                    strBuffer.append("<a href='showExamsManagement.do?method=edit&amp;"
                            + SessionConstants.EXECUTION_COURSE_OID + "="
                            + infoExecutionCourse.getIdInternal() + "&amp;"
                            + SessionConstants.EXECUTION_PERIOD_OID + "="
                            + infoExecutionCourse.getInfoExecutionPeriod().getIdInternal() + "&amp;"
                            + SessionConstants.EXECUTION_DEGREE_OID + "="
                            + examsMap.getInfoExecutionDegree().getIdInternal() + "&amp;"
                            + SessionConstants.CURRICULAR_YEAR_OID + "=" + curicularYear.toString()
                            + "&amp;" + SessionConstants.EXAM_OID + "=" + infoExam.getIdInternal()
                            + "'>");
                    if (isOnValidWeekDay) {
                        strBuffer.append(courseInitials);
                    } else {
                        strBuffer.append("<span class='redtxt'>" + courseInitials + "</span>");
                    }

                } else if (typeUser.equals("public")) {
                    strBuffer.append("<a href='executionCourse.do?method=firstPage&amp;executionCourseID="
                            + infoExecutionCourse.getIdInternal()
                            + "'>");
                    strBuffer.append(courseInitials);
                }
                strBuffer.append("</a>");
                if (infoExam.getBeginning() != null) {
                    boolean isAtValidHour = atValidHour(infoExam);
                    String hoursText = infoExam.getBeginning().get(Calendar.HOUR_OF_DAY) + "h"
                            + DateFormatUtils.format(infoExam.getBeginning().getTime(), "mm");
                    strBuffer.append(" ");
                    strBuffer.append(getMessageResource(pageContext, "public.degree.information.label.as"));
                    strBuffer.append(" ");
                
                    if (isAtValidHour || !typeUser.equals("sop")) {
                        strBuffer.append(hoursText);
                    } else {
                        strBuffer.append("<span class='redtxt'>" + hoursText + "</span>");
                    }
                }

                strBuffer.append("<br />");
            }
        }

        strBuffer.append("<br />");

        return strBuffer;
    }

    /**
     * @param infoExam
     * @return
     */
    private boolean atValidHour(InfoExam infoExam) {
        int curricularYear = infoExam.getInfoExecutionCourse().getCurricularYear().intValue();
        int beginning = infoExam.getBeginning().get(Calendar.HOUR_OF_DAY);
        int weekDay = infoExam.getDay().get(Calendar.DAY_OF_WEEK);

        return ((curricularYear == 1 || curricularYear == 2) && (beginning == 9))
                || (curricularYear == 3 && beginning == 17)
                || (curricularYear == 4 && (((weekDay == Calendar.TUESDAY || weekDay == Calendar.THURSDAY) && beginning == 17) || (weekDay == Calendar.SATURDAY && beginning == 9)))
                || (curricularYear == 5 && beginning == 13);
    }

    private boolean onValidWeekDay(InfoExam infoExam) {
        int curricularYear = infoExam.getInfoExecutionCourse().getCurricularYear().intValue();
        int weekDay = infoExam.getDay().get(Calendar.DAY_OF_WEEK);

        return ((curricularYear == 1 || curricularYear == 3 || curricularYear == 5) && (weekDay == Calendar.MONDAY
                || weekDay == Calendar.WEDNESDAY || weekDay == Calendar.FRIDAY))
                || ((curricularYear == 2 || curricularYear == 4) && (weekDay == Calendar.TUESDAY
                        || weekDay == Calendar.THURSDAY || weekDay == Calendar.SATURDAY));
    }

    private String monthToString(int month, Locale locale) {
        switch (month) {

        case Calendar.JANUARY:
            if (locale.getLanguage().equals("pt"))
                return "Janeiro";
            return "January";
        case Calendar.FEBRUARY:
            if (locale.getLanguage().equals("pt"))
                return "Fevereiro";
            return "February";
        case Calendar.MARCH:
            if (locale.getLanguage().equals("pt"))
                return "Março";
            return "March";
        case Calendar.APRIL:
            if (locale.getLanguage().equals("pt"))
                return "Abril";
            return "April";
        case Calendar.MAY:
            if (locale.getLanguage().equals("pt"))
                return "Maio";
            return "May";
        case Calendar.JUNE:
            if (locale.getLanguage().equals("pt"))
                return "Junho";
            return "June";
        case Calendar.JULY:
            if (locale.getLanguage().equals("pt"))
                return "Julho";
            return "July";
        case Calendar.AUGUST:
            if (locale.getLanguage().equals("pt"))
                return "Agosto";
            return "August";
        case Calendar.SEPTEMBER:
            if (locale.getLanguage().equals("pt"))
                return "Setembro";
            return "September";
        case Calendar.OCTOBER:
            if (locale.getLanguage().equals("pt"))
                return "Outubro";
            return "October";
        case Calendar.NOVEMBER:
            if (locale.getLanguage().equals("pt"))
                return "Novembro";
            return "November";
        case Calendar.DECEMBER:
            if (locale.getLanguage().equals("pt"))
                return "Dezembro";
            return "December";
        case Calendar.UNDECIMBER:
            return "Undecember";
        default:
            return "Error";
        }
    }

    public StringBuilder renderDayContents(ExamsMapSlot examsMapSlot, ExamsMap examsMap, String typeUser,PageContext pageContext) {
        StringBuilder strBuffer = new StringBuilder();

        for (int i = 0; i < examsMapSlot.getExams().size(); i++) {
            InfoExam infoExam = (InfoExam) examsMapSlot.getExams().get(i);
            //InfoExecutionCourse infoExecutionCourse =
            //	infoExam.getInfoExecutionCourse();
            String courseInitials = infoExam.getInfoExecutionCourse().getSigla();

            strBuffer.append("<b><span class='redtxt'>");

            /*
             * strBuffer.append( " <a
             * href='showExamsManagement.do?method=edit&amp;" +
             * SessionConstants.EXECUTION_COURSE_OID + "=" +
             * infoExam.getInfoExecutionCourse().getIdInternal() + "&amp;" +
             * SessionConstants.EXECUTION_PERIOD_OID + "=" +
             * infoExam.getInfoExecutionCourse().getInfoExecutionPeriod().getIdInternal() +
             * "&amp;" + SessionConstants.EXECUTION_DEGREE_OID + "=" +
             * examsMap.getInfoExecutionDegree().getIdInternal() + "&amp;" +
             * SessionConstants.CURRICULAR_YEAR_OID + "=" +
             * infoExam.getInfoExecutionCourse().getCurricularYear() + "&amp;" +
             * SessionConstants.EXAM_OID + "=" + infoExam.getIdInternal() +
             * "'>");
             */
            strBuffer.append(courseInitials);
            //			strBuffer.append("</a>");

            if (infoExam.getBeginning() != null) {
                String hoursText = infoExam.getBeginning().get(Calendar.HOUR_OF_DAY) + "H";
                strBuffer.append(" ");
                strBuffer.append(getMessageResource(pageContext, "public.degree.information.label.as"));
                strBuffer.append(" ");
                strBuffer.append(hoursText);
            }

            strBuffer.append("</span></b>");

            strBuffer.append("<br />");
        }

        strBuffer.append("<br />");
        strBuffer.append("<br />");
        strBuffer.append("<br />");
        strBuffer.append("<br />");
        strBuffer.append("<br />");

        return strBuffer;
    }

}