/*
 * Created on Apr 3, 2003  
 */

package net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMap.renderers;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMap.ExamsMap;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMap.ExamsMapSlot;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ExamsMapContentRenderer implements ExamsMapSlotContentRenderer {

    private ExamsMap examsMap;

    public StringBuilder renderDayLabel(ExamsMapSlot examsMapSlot, ExamsMap examsMap) {
        this.examsMap = examsMap;
        StringBuilder strBuffer = new StringBuilder();

        boolean isFirstDayOfSeason = ((examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH) == examsMap
                .getFirstDayOfSeason().get(Calendar.DAY_OF_MONTH))
                && (examsMapSlot.getDay().get(Calendar.MONTH) == examsMap.getFirstDayOfSeason().get(
                        Calendar.MONTH)) && (examsMapSlot.getDay().get(Calendar.YEAR) == examsMap
                .getFirstDayOfSeason().get(Calendar.YEAR)));

        boolean isSecondDayOfMonthAndFirstDayWasASunday = (examsMapSlot.getDay().get(
                Calendar.DAY_OF_MONTH) == 2 && examsMapSlot.getDay().get(Calendar.DAY_OF_WEEK) == 2);

        strBuffer.append(examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH));
        if ((examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH) == 1) || isFirstDayOfSeason
                || isSecondDayOfMonthAndFirstDayWasASunday) {
            strBuffer.append(" de ");
            strBuffer.append(monthToString(examsMapSlot.getDay().get(Calendar.MONTH)));
        }
        if (examsMapSlot.getDay().get(Calendar.DAY_OF_YEAR) == 1) {
            strBuffer.append(", ");
            strBuffer.append(examsMapSlot.getDay().get(Calendar.YEAR));
        }
        strBuffer.append("<br />");

        return strBuffer;
    }

    public StringBuilder renderDayContents(ExamsMapSlot examsMapSlot, Integer year1, Integer year2,
            String typeUser) {
        StringBuilder strBuffer = new StringBuilder();

        for (int i = 0; i < examsMapSlot.getExams().size(); i++) {
            InfoExam infoExam = (InfoExam) examsMapSlot.getExams().get(i);
            Integer curicularYear = infoExam.getInfoExecutionCourse().getCurricularYear();

            if (curicularYear.equals(year1) || curicularYear.equals(year2)) {
                boolean isOnValidWeekDay = onValidWeekDay(infoExam);

                InfoExecutionCourse infoExecutionCourse = infoExam.getInfoExecutionCourse();
                String courseInitials = infoExam.getInfoExecutionCourse().getSigla();

                if (typeUser.equals("sop")) {
                    strBuffer.append("<a href='viewExamsMap.do?method=edit"
                            + "&amp;executionCourseInitials="
                            + infoExecutionCourse.getSigla()
                            + "&amp;ePName="
                            + infoExecutionCourse.getInfoExecutionPeriod().getName()
                            + "&amp;eYName="
                            + infoExecutionCourse.getInfoExecutionPeriod().getInfoExecutionYear()
                                    .getYear() + "&amp;season=" + infoExam.getSeason().getseason()
                            + "&amp;" + SessionConstants.EXECUTION_PERIOD_OID + "="
                            + infoExecutionCourse.getInfoExecutionPeriod().getIdInternal() + "&amp;"
                            + SessionConstants.EXECUTION_DEGREE_OID + "="
                            + examsMap.getInfoExecutionDegree().getIdInternal() + "&amp;"
                            + SessionConstants.EXECUTION_COURSE_OID + "="
                            + infoExecutionCourse.getIdInternal() + getCurricularYearsArgs() + "'>");

                    if (isOnValidWeekDay) {
                        strBuffer.append(courseInitials);
                    } else {
                        strBuffer.append("<span class='redtxt'>" + courseInitials + "</span>");
                    }

                } else if (typeUser.equals("public")) {
                    strBuffer.append("<a href='executionCourse.do?method=firstPage&amp;executionCourseID="
                            + infoExecutionCourse.getIdInternal() + "'>");
                    strBuffer.append(courseInitials);
                }
                strBuffer.append("</a>");
                if (infoExam.getBeginning() != null) {
                    boolean isAtValidHour = atValidHour(infoExam);
                    String hoursText = infoExam.getBeginning().get(Calendar.HOUR_OF_DAY) + "H";

                    strBuffer.append(" às ");
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

    private String monthToString(int month) {
        switch (month) {
        case Calendar.JANUARY:
            return "Janeiro";
        case Calendar.FEBRUARY:
            return "Fevereiro";
        case Calendar.MARCH:
            return "Março";
        case Calendar.APRIL:
            return "Abril";
        case Calendar.MAY:
            return "Maio";
        case Calendar.JUNE:
            return "Junho";
        case Calendar.JULY:
            return "Julho";
        case Calendar.AUGUST:
            return "Agosto";
        case Calendar.SEPTEMBER:
            return "Setembro";
        case Calendar.OCTOBER:
            return "Outubro";
        case Calendar.NOVEMBER:
            return "Novembro";
        case Calendar.DECEMBER:
            return "Dezembro";
        case Calendar.UNDECIMBER:
            return "Undecember";
        default:
            return "Error";
        }
    }

    public StringBuilder renderDayContents(ExamsMapSlot examsMapSlot, String typeUser) {
        StringBuilder strBuffer = new StringBuilder();

        for (int i = 0; i < examsMapSlot.getExams().size(); i++) {
            InfoExam infoExam = (InfoExam) examsMapSlot.getExams().get(i);
            //InfoExecutionCourse infoExecutionCourse =
            //	infoExam.getInfoExecutionCourse();
            String courseInitials = infoExam.getInfoExecutionCourse().getSigla();

            strBuffer.append("<b><span class='redtxt'>" + courseInitials);

            if (infoExam.getBeginning() != null) {
                String hoursText = infoExam.getBeginning().get(Calendar.HOUR_OF_DAY) + "H";

                strBuffer.append(" às ");
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

}