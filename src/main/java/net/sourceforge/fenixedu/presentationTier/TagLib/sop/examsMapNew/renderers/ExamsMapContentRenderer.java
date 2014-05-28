/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 2003/10/28  
 */

package net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew.renderers;

import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew.ExamsMap;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew.ExamsMapSlot;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.struts.Globals;
import org.apache.struts.util.RequestUtils;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

/**
 * @author Ana e Ricardo
 */
public class ExamsMapContentRenderer implements ExamsMapSlotContentRenderer {

    private ExamsMap examsMap;

    @Override
    public StringBuilder renderDayLabel(ExamsMapSlot examsMapSlot, ExamsMap examsMap, String typeUser, PageContext pageContext) {
        this.examsMap = examsMap;
        StringBuilder strBuffer = new StringBuilder();

        boolean isFirstDayOfSeason =
                ((examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH) == examsMap.getFirstDayOfSeason().get(Calendar.DAY_OF_MONTH))
                        && (examsMapSlot.getDay().get(Calendar.MONTH) == examsMap.getFirstDayOfSeason().get(Calendar.MONTH)) && (examsMapSlot
                        .getDay().get(Calendar.YEAR) == examsMap.getFirstDayOfSeason().get(Calendar.YEAR)));

        boolean isSecondDayOfMonthAndFirstDayWasASunday =
                (examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH) == 2 && examsMapSlot.getDay().get(Calendar.DAY_OF_WEEK) == 2);

        if (examsMap.getInfoExecutionDegree() != null && typeUser.equals("sop")) {
            strBuffer.append("<a href='showExamsManagement.do?method=createByDay" + "&amp;"
                    + PresentationConstants.EXECUTION_DEGREE_OID + "=" + examsMap.getInfoExecutionDegree().getExternalId()
                    + "&amp;" + PresentationConstants.EXECUTION_PERIOD_OID + "="
                    + examsMap.getInfoExecutionPeriod().getExternalId() + "&amp;" + PresentationConstants.CURRICULAR_YEAR_OID
                    + "=" + examsMap.getCurricularYears().iterator().next() + "&amp;" + PresentationConstants.DAY + "="
                    + examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH) + "&amp;" + PresentationConstants.MONTH + "="
                    + (examsMapSlot.getDay().get(Calendar.MONTH) + 1) + "&amp;" + PresentationConstants.YEAR + "="
                    + examsMapSlot.getDay().get(Calendar.YEAR) + "'>");
        }
        strBuffer.append(examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH));
        if ((examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH) == 1) || isFirstDayOfSeason
                || isSecondDayOfMonthAndFirstDayWasASunday) {
            strBuffer.append(" ");
            strBuffer.append(getMessageResource(pageContext, "public.degree.information.label.of"));
            strBuffer.append(" ");
            Locale locale = pageContext.getRequest().getLocale();
            strBuffer.append(monthToString(examsMapSlot.getDay().get(Calendar.MONTH), locale));
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

    @Override
    public StringBuilder renderDayContents(ExamsMapSlot examsMapSlot, Integer year1, Integer year2, String typeUser,
            PageContext pageContext) {
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
                            + PresentationConstants.EXECUTION_COURSE_OID + "=" + infoExecutionCourse.getExternalId() + "&amp;"
                            + PresentationConstants.EXECUTION_PERIOD_OID + "="
                            + infoExecutionCourse.getInfoExecutionPeriod().getExternalId() + "&amp;"
                            + PresentationConstants.EXECUTION_DEGREE_OID + "="
                            + examsMap.getInfoExecutionDegree().getExternalId() + "&amp;"
                            + PresentationConstants.CURRICULAR_YEAR_OID + "=" + curicularYear.toString() + "&amp;"
                            + PresentationConstants.EXAM_OID + "=" + infoExam.getExternalId() + "'>");
                    if (isOnValidWeekDay) {
                        strBuffer.append(courseInitials);
                    } else {
                        strBuffer.append("<span class='redtxt'>" + courseInitials + "</span>");
                    }

                } else if (typeUser.equals("public")) {

                    final Site site = infoExecutionCourse.getExecutionCourse().getSite();
                    strBuffer.append(GenericChecksumRewriter.NO_CHECKSUM_PREFIX);
                    strBuffer.append("<a href=\"").append(((HttpServletRequest) pageContext.getRequest()).getContextPath());
                    strBuffer.append(site.getReversePath());
                    strBuffer.append("\">");
                    strBuffer.append(courseInitials);
                }

                strBuffer.append("</a>");
                if (infoExam.getBeginning() != null) {
                    boolean isAtValidHour = atValidHour(infoExam);
                    String hoursText =
                            infoExam.getBeginning().get(Calendar.HOUR_OF_DAY) + "h"
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
                || ((curricularYear == 2 || curricularYear == 4) && (weekDay == Calendar.TUESDAY || weekDay == Calendar.THURSDAY || weekDay == Calendar.SATURDAY));
    }

    private String monthToString(int month, Locale locale) {
        switch (month) {

        case Calendar.JANUARY:
            if (locale.getLanguage().equals("pt")) {
                return "Janeiro";
            }
            return "January";
        case Calendar.FEBRUARY:
            if (locale.getLanguage().equals("pt")) {
                return "Fevereiro";
            }
            return "February";
        case Calendar.MARCH:
            if (locale.getLanguage().equals("pt")) {
                return "Março";
            }
            return "March";
        case Calendar.APRIL:
            if (locale.getLanguage().equals("pt")) {
                return "Abril";
            }
            return "April";
        case Calendar.MAY:
            if (locale.getLanguage().equals("pt")) {
                return "Maio";
            }
            return "May";
        case Calendar.JUNE:
            if (locale.getLanguage().equals("pt")) {
                return "Junho";
            }
            return "June";
        case Calendar.JULY:
            if (locale.getLanguage().equals("pt")) {
                return "Julho";
            }
            return "July";
        case Calendar.AUGUST:
            if (locale.getLanguage().equals("pt")) {
                return "Agosto";
            }
            return "August";
        case Calendar.SEPTEMBER:
            if (locale.getLanguage().equals("pt")) {
                return "Setembro";
            }
            return "September";
        case Calendar.OCTOBER:
            if (locale.getLanguage().equals("pt")) {
                return "Outubro";
            }
            return "October";
        case Calendar.NOVEMBER:
            if (locale.getLanguage().equals("pt")) {
                return "Novembro";
            }
            return "November";
        case Calendar.DECEMBER:
            if (locale.getLanguage().equals("pt")) {
                return "Dezembro";
            }
            return "December";
        case Calendar.UNDECIMBER:
            return "Undecember";
        default:
            return "Error";
        }
    }

    @Override
    public StringBuilder renderDayContents(ExamsMapSlot examsMapSlot, ExamsMap examsMap, String typeUser, PageContext pageContext) {
        StringBuilder strBuffer = new StringBuilder();

        for (int i = 0; i < examsMapSlot.getExams().size(); i++) {
            InfoExam infoExam = (InfoExam) examsMapSlot.getExams().get(i);
            // InfoExecutionCourse infoExecutionCourse =
            // infoExam.getInfoExecutionCourse();
            String courseInitials = infoExam.getInfoExecutionCourse().getSigla();

            strBuffer.append("<b><span class='redtxt'>");

            strBuffer.append(courseInitials);
            // strBuffer.append("</a>");

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