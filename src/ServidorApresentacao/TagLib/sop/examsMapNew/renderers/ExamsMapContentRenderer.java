/*
 * Created on 2003/10/28  
 */

package ServidorApresentacao.TagLib.sop.examsMapNew.renderers;

import java.util.Calendar;

import org.apache.commons.lang.time.DateFormatUtils;

import DataBeans.InfoExam;
import DataBeans.InfoExecutionCourse;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.TagLib.sop.examsMapNew.ExamsMap;
import ServidorApresentacao.TagLib.sop.examsMapNew.ExamsMapSlot;

/**
 * @author Ana e Ricardo
 */
public class ExamsMapContentRenderer implements ExamsMapSlotContentRenderer
{

    private ExamsMap examsMap;

    public StringBuffer renderDayLabel(ExamsMapSlot examsMapSlot, ExamsMap examsMap, String typeUser)
    {
        this.examsMap = examsMap;
        StringBuffer strBuffer = new StringBuffer();
      

        boolean isFirstDayOfSeason =
            ((examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH)
                == examsMap.getFirstDayOfSeason().get(Calendar.DAY_OF_MONTH))
                && (examsMapSlot.getDay().get(Calendar.MONTH)
                    == examsMap.getFirstDayOfSeason().get(Calendar.MONTH))
                && (examsMapSlot.getDay().get(Calendar.YEAR)
                    == examsMap.getFirstDayOfSeason().get(Calendar.YEAR)));

        boolean isSecondDayOfMonthAndFirstDayWasASunday =
            (examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH) == 2
                && examsMapSlot.getDay().get(Calendar.DAY_OF_WEEK) == 2);
		
		if(examsMap.getInfoExecutionDegree() != null && typeUser.equals("sop"))
        {  
        	strBuffer.append(
                "<a href='showExamsManagement.do?method=createByDay"
                + "&amp;"
                + SessionConstants.EXECUTION_DEGREE_OID
                + "="
                + examsMap.getInfoExecutionDegree().getIdInternal()
                + "&amp;"
                + SessionConstants.CURRICULAR_YEAR_OID
                + "="
                + examsMap.getCurricularYears().get(0)
                + "&amp;"
                + SessionConstants.DAY
                + "="
                + examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH)
                + "&amp;"
                + SessionConstants.MONTH
                + "="
                + (examsMapSlot.getDay().get(Calendar.MONTH) + 1)
                + "&amp;"
                + SessionConstants.YEAR
                + "="
                + examsMapSlot.getDay().get(Calendar.YEAR) 
                + "'>");
        }
        strBuffer.append(examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH));
        if ((examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH) == 1)
            || isFirstDayOfSeason
            || isSecondDayOfMonthAndFirstDayWasASunday)
        {
            strBuffer.append(" de ");
            strBuffer.append(monthToString(examsMapSlot.getDay().get(Calendar.MONTH)));
        }
        if (examsMapSlot.getDay().get(Calendar.DAY_OF_YEAR) == 1)
        {
            strBuffer.append(", ");
            strBuffer.append(examsMapSlot.getDay().get(Calendar.YEAR));
        }
        
		if(examsMap.getInfoExecutionDegree() != null && typeUser.equals("sop"))
		{  
        	strBuffer.append("</a>");
		}
		
        strBuffer.append("<br />");

        return strBuffer;
    }

    public StringBuffer renderDayContents(
        ExamsMapSlot examsMapSlot,
        Integer year1,
        Integer year2,
        String typeUser)
    {
        StringBuffer strBuffer = new StringBuffer();
        for (int i = 0; i < examsMapSlot.getExams().size(); i++)
        {
            InfoExam infoExam = (InfoExam) examsMapSlot.getExams().get(i);					
            Integer curicularYear = infoExam.getInfoExecutionCourse().getCurricularYear();

            if (curicularYear.equals(year1) || curicularYear.equals(year2))
            {
                boolean isOnValidWeekDay = onValidWeekDay(infoExam);

                InfoExecutionCourse infoExecutionCourse = infoExam.getInfoExecutionCourse();
                String courseInitials = infoExam.getInfoExecutionCourse().getSigla();

                if (typeUser.equals("sop"))
                {
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
                            + curicularYear.toString()
                            + "&amp;"
                            + SessionConstants.EXAM_OID
                            + "="
                            + infoExam.getIdInternal()
                            + "'>");
                    if (isOnValidWeekDay)
                    {
                        strBuffer.append(courseInitials);
                    }
                    else
                    {
                        strBuffer.append("<span class='redtxt'>" + courseInitials + "</span>");
                    }

                }
                else if (typeUser.equals("public"))
                {
                    strBuffer.append(
                        "<a href='viewSite.do?method=firstPage&amp;objectCode="
                            + infoExecutionCourse.getIdInternal()
							+ "&amp;executionPeriodOID="
						    + infoExecutionCourse.getInfoExecutionPeriod().getIdInternal()
						    + "&amp;degreeID="
						    + examsMap.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getInfoDegree().getIdInternal()
							+ "&amp;"
						    + SessionConstants.EXECUTION_COURSE_OID
						    + "="
						    + infoExecutionCourse.getIdInternal()
							+ "&amp;executionDegreeID="	
						    + examsMap.getInfoExecutionDegree().getIdInternal()
						    + "&amp;degreeCurricularPlanID="
						    + examsMap.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getIdInternal()	
                            + "'>"); 
                    strBuffer.append(courseInitials);
                }
                strBuffer.append("</a>");
                if (infoExam.getBeginning() != null)
                {
                    boolean isAtValidHour = atValidHour(infoExam);
                    String hoursText =
                        infoExam.getBeginning().get(Calendar.HOUR_OF_DAY)
                            + "h"
                            + DateFormatUtils.format(infoExam.getBeginning().getTime(), "mm");

                    strBuffer.append(" às ");
                    if (isAtValidHour || !typeUser.equals("sop"))
                    {
                        strBuffer.append(hoursText);
                    }
                    else
                    {
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
    private boolean atValidHour(InfoExam infoExam)
    {
        int curricularYear = infoExam.getInfoExecutionCourse().getCurricularYear().intValue();
        int beginning = infoExam.getBeginning().get(Calendar.HOUR_OF_DAY);
        int weekDay = infoExam.getDay().get(Calendar.DAY_OF_WEEK);

        return ((curricularYear == 1 || curricularYear == 2) && (beginning == 9))
            || (curricularYear == 3 && beginning == 17)
            || (curricularYear == 4
                && (((weekDay == Calendar.TUESDAY || weekDay == Calendar.THURSDAY) && beginning == 17)
                    || (weekDay == Calendar.SATURDAY && beginning == 9)))
            || (curricularYear == 5 && beginning == 13);
    }

    private boolean onValidWeekDay(InfoExam infoExam)
    {
        int curricularYear = infoExam.getInfoExecutionCourse().getCurricularYear().intValue();
        int weekDay = infoExam.getDay().get(Calendar.DAY_OF_WEEK);

        return (
            (curricularYear == 1 || curricularYear == 3 || curricularYear == 5)
                && (weekDay == Calendar.MONDAY
                    || weekDay == Calendar.WEDNESDAY
                    || weekDay == Calendar.FRIDAY))
            || ((curricularYear == 2 || curricularYear == 4)
                && (weekDay == Calendar.TUESDAY
                    || weekDay == Calendar.THURSDAY
                    || weekDay == Calendar.SATURDAY));
    }

    private String monthToString(int month)
    {
        switch (month)
        {
            case Calendar.JANUARY :
                return "Janeiro";
            case Calendar.FEBRUARY :
                return "Fevereiro";
            case Calendar.MARCH :
                return "Março";
            case Calendar.APRIL :
                return "Abril";
            case Calendar.MAY :
                return "Maio";
            case Calendar.JUNE :
                return "Junho";
            case Calendar.JULY :
                return "Julho";
            case Calendar.AUGUST :
                return "Agosto";
            case Calendar.SEPTEMBER :
                return "Setembro";
            case Calendar.OCTOBER :
                return "Outubro";
            case Calendar.NOVEMBER :
                return "Novembro";
            case Calendar.DECEMBER :
                return "Dezembro";
            case Calendar.UNDECIMBER :
                return "Undecember";
            default :
                return "Error";
        }
    }

    /*
     * (non-Javadoc) @see ServidorApresentacao.TagLib.sop.examsMap.renderers.ExamsMapSlotContentRenderer#renderDayContents(ServidorApresentacao.TagLib.sop.examsMap.ExamsMapSlot,
     * java.lang.String)
     */
    public StringBuffer renderDayContents(ExamsMapSlot examsMapSlot, ExamsMap examsMap, String typeUser)
    {
        StringBuffer strBuffer = new StringBuffer();		
			
        for (int i = 0; i < examsMapSlot.getExams().size(); i++)
        {			
            InfoExam infoExam = (InfoExam) examsMapSlot.getExams().get(i);			
            //InfoExecutionCourse infoExecutionCourse =
            //	infoExam.getInfoExecutionCourse();
            String courseInitials = infoExam.getInfoExecutionCourse().getSigla();
			  
            strBuffer.append("<b><span class='redtxt'>");
            
/*			strBuffer.append(
				"<a href='showExamsManagement.do?method=edit&amp;"
					+ SessionConstants.EXECUTION_COURSE_OID
					+ "="
					+ infoExam.getInfoExecutionCourse().getIdInternal()
					+ "&amp;"
					+ SessionConstants.EXECUTION_PERIOD_OID
					+ "="
					+ infoExam.getInfoExecutionCourse().getInfoExecutionPeriod().getIdInternal()
					+ "&amp;"
					+ SessionConstants.EXECUTION_DEGREE_OID
					+ "="
					+ examsMap.getInfoExecutionDegree().getIdInternal()
					+ "&amp;"
					+ SessionConstants.CURRICULAR_YEAR_OID
					+ "="
					+ infoExam.getInfoExecutionCourse().getCurricularYear()
					+ "&amp;"
					+ SessionConstants.EXAM_OID
					+ "="
					+ infoExam.getIdInternal()
					+ "'>");*/
			strBuffer.append(courseInitials);
//			strBuffer.append("</a>");
            
            if (infoExam.getBeginning() != null)
            {
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

}
