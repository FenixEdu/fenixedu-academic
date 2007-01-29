package net.sourceforge.fenixedu.presentationTier.TagLib;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.util.renderer.GanttDiagram;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;
import net.sourceforge.fenixedu.util.renderer.GanttDiagram.ViewType;

import org.apache.struts.taglib.TagUtils;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class GanttDiagramTagLib extends TagSupport {

    // Attributes
    private String ganttDiagram;

    private String eventUrl;
    
    private String eventParameter;
    
    private String bundle;
    
    private String nextUrl;
        
    private String firstDayParameter;
    
    
    // Auxiliar variables 
    private GanttDiagram ganttDiagramObject;

    private List<GanttDiagramEvent> events;
           
    private HttpServletRequest request;
   
    private ViewType viewTypeEnum;
    
    private BigDecimal PX_TO_EM_CONVERSION_DIVISOR = BigDecimal.valueOf(10);
    
    private BigDecimal ONE_UNIT = BigDecimal.valueOf(0.1);
    
    private BigDecimal EMPTY_UNIT = BigDecimal.valueOf(0);
    
    private int numberOfDayHalfHours = 48;
    
    private int numberOf5MinUnits = 288;  
    
    
    
    public int doStartTag() throws JspException {
	String ganttDiagram = "";
	Object object = pageContext.findAttribute(getGanttDiagram());
	
	if (object != null) {	    	    
	    setGanttDiagramObject((GanttDiagram) object);
	    setViewTypeEnum(getGanttDiagramObject().getViewType());
	    setEvents(getGanttDiagramObject().getEvents());	   
	    setRequest((HttpServletRequest) pageContext.getRequest());	      	    
	    ganttDiagram = generateGanttDiagramString();
	}
	
	try {
	    pageContext.getOut().print(ganttDiagram);
	} catch (IOException e) {
	    e.printStackTrace(System.out);
	}
	return SKIP_BODY;
    }    
    
    
    private String generateGanttDiagramString() throws JspException {
	switch (getViewTypeEnum()) {
	
	case TOTAL:
	    return generateGanttDiagramInTotalMode().toString();	   
		   
	case MONTHLY:
	    return generateGanttDiagramInTotalMode().toString();
	    
	case WEEKLY:	    	    
	    return generateGanttDiagramInTimeMode().toString();
	
	case DAILY:
	    return generateGanttDiagramInTimeMode().toString();
	    
	default:
	    return "";
	}
    }
           
    private StringBuilder generateGanttDiagramInTimeMode() throws JspException {
	StringBuilder builder = new StringBuilder();
	
	builder.append("<table class=\"tcalendar thlight\">");
	
	generateHeaders(builder);
	
	int numberOfUnits = getNumberOfUnits();
	
	for (GanttDiagramEvent event : getEvents()) {	    
	    
	    String eventUrl = getRequest().getContextPath() + getEventUrl() + "&amp;" + getEventParameter() + "=" + event.getEventIdentifierForGanttDiagram();
	    String eventName = event.getEventNameForGanttDiagram().getContent(Language.valueOf(getGanttDiagramObject().getLocale().getLanguage()));
	    String paddingStyle ="padding-left:" + event.getEventOffsetForGanttDiagram() * 15 + "px";	    	    
	    String selectedEvent = getRequest().getParameter(getEventParameter());
	    Object selectedEventObject = getRequest().getAttribute(getEventParameter());	    
	    
	    if(event.getEventIdentifierForGanttDiagram().equals(selectedEvent) || 
		    (selectedEventObject != null && event.getEventIdentifierForGanttDiagram().equals(selectedEventObject.toString()))) {
		builder.append("<tr class=\"selected\">");
	    } else {
		builder.append("<tr>");
	    }
	    	    
	    builder.append("<td class=\"padded\">").append("<span style=\"").append(paddingStyle).append("\">");
	    builder.append("<a href=\"").append(eventUrl).append("\">").append("*").append(eventName);
	    builder.append("</a></span></td>");
	    
	    for (DateTime day : getGanttDiagramObject().getDays()) {
		
		int startIndex = 0, endIndex = 0;
		int dayOfMonth = day.getDayOfMonth();
		int monthOfYear = day.getMonthOfYear();
		int year = day.getYear();		
				
		builder.append("<td style=\"width: ").append(convertToEm(numberOfUnits)).append("em;\"><div style=\"position: relative;\">");		
		for (Interval interval : event.getEventSortedIntervalsForGanttDiagram()) {		    				    		                  
					    
		    if(interval.getStart().getYear() <= year && interval.getEnd().getYear() >= year) {
			
			if(interval.getStart().getYear() < year && interval.getEnd().getYear() > year) {			    
			    addSpecialDiv(builder, convertToEm(numberOfUnits), EMPTY_UNIT);
			}
			// Started in same year and Ended after
			else if(interval.getStart().getYear() == year && interval.getEnd().getYear() > year) {			 
			    
			    if(interval.getStart().getMonthOfYear() < monthOfYear) {
				addSpecialDiv(builder, convertToEm(numberOfUnits), EMPTY_UNIT);
			    
			    } else if(interval.getStart().getMonthOfYear() == monthOfYear) {
				
				if(interval.getStart().getDayOfMonth() == dayOfMonth) {				    		   				    
				    startIndex = calculateTimeOfDay(interval.getStart());				    				    
				    addSpecialDiv(builder, convertToEm(numberOfUnits - startIndex).add(ONE_UNIT), convertToEm(startIndex - 1));
				    
				} else if(interval.getStart().getDayOfMonth() < dayOfMonth) {
				    addSpecialDiv(builder, convertToEm(numberOfUnits), EMPTY_UNIT);				    
				}				
			    }
			}		
			// Ended in same year and started before
			else if(interval.getStart().getYear() < year && interval.getEnd().getYear() == year) {
			    
			    if(interval.getEnd().getMonthOfYear() > monthOfYear) {				
				addSpecialDiv(builder, convertToEm(numberOfUnits), EMPTY_UNIT);
				
			    } else if(interval.getEnd().getMonthOfYear() == monthOfYear) {				
				
				if(interval.getEnd().getDayOfMonth() > dayOfMonth) {
				    addSpecialDiv(builder, convertToEm(numberOfUnits), EMPTY_UNIT);

				} else if(interval.getEnd().getDayOfMonth() == dayOfMonth) {				    		   				    
				    endIndex = calculateTimeOfDay(interval.getEnd());				    				    
				    addSpecialDiv(builder, convertToEm(numberOfUnits - endIndex).add(ONE_UNIT), EMPTY_UNIT);				    
				}				
			    }	
			}
			// Ended and Started In Same Year
			else if(interval.getStart().getYear() == year && interval.getEnd().getYear() == year) {
			    
			    if (interval.getStart().getMonthOfYear() <= monthOfYear && interval.getEnd().getMonthOfYear() >= monthOfYear) {

				if (interval.getStart().getMonthOfYear() == monthOfYear && interval.getEnd().getMonthOfYear() > monthOfYear) {
				    
				    if (interval.getStart().getDayOfMonth() == dayOfMonth) {
					startIndex = calculateTimeOfDay(interval.getStart());
					addSpecialDiv(builder, convertToEm(numberOfUnits - startIndex).add(ONE_UNIT), convertToEm(startIndex - 1));
					
				    } else if(interval.getStart().getDayOfMonth() < dayOfMonth) {
					addSpecialDiv(builder, convertToEm(numberOfUnits), EMPTY_UNIT);				    
				    }

				} else if (interval.getStart().getMonthOfYear() < monthOfYear && interval.getEnd().getMonthOfYear() == monthOfYear) {

				    if (interval.getEnd().getDayOfMonth() > dayOfMonth) {
					addSpecialDiv(builder, convertToEm(numberOfUnits), EMPTY_UNIT);

				    } else if (interval.getEnd().getDayOfMonth() == dayOfMonth) {
					endIndex = calculateTimeOfDay(interval.getEnd());
					addSpecialDiv(builder, convertToEm(numberOfUnits	- endIndex).add(ONE_UNIT), EMPTY_UNIT);
				    
				    } 

				} else if (interval.getStart().getMonthOfYear() == monthOfYear && interval.getEnd().getMonthOfYear() == monthOfYear) {

				    if(interval.getStart().getDayOfMonth() <= dayOfMonth && interval.getEnd().getDayOfMonth() >= dayOfMonth) {
					
					if(interval.getStart().getDayOfMonth() == dayOfMonth && interval.getEnd().getDayOfMonth() > dayOfMonth) {
					    startIndex = calculateTimeOfDay(interval.getStart());
					    addSpecialDiv(builder, convertToEm(numberOfUnits - startIndex).add(ONE_UNIT), convertToEm(startIndex - 1));				    
					}
					
					else if(interval.getStart().getDayOfMonth() < dayOfMonth && interval.getEnd().getDayOfMonth() == dayOfMonth) {
					    endIndex = calculateTimeOfDay(interval.getEnd());
					    addSpecialDiv(builder, convertToEm(numberOfUnits - endIndex).add(ONE_UNIT), EMPTY_UNIT);
					}
					
					else if(interval.getStart().getDayOfMonth() == dayOfMonth && interval.getEnd().getDayOfMonth() == dayOfMonth) {
					    startIndex = calculateTimeOfDay(interval.getStart());
					    endIndex = calculateTimeOfDay(interval.getEnd());
					    addSpecialDiv(builder, convertToEm(numberOfUnits - (startIndex - endIndex)).add(ONE_UNIT), convertToEm(startIndex - 1));					    
					}					
				    }
				}
			    }
			}
		    }					    		    		   
		}		
		builder.append("</div></td>");			
	    }	    
	    builder.append("<td class=\"padded smalltxt\">").append(event.getEventObservationsForGanttDiagram()).append("</td>");		
	}
		
	insertNextAndBeforeLinks(builder);
	
	builder.append("</table>");
	return builder;
    }
       
    private StringBuilder generateGanttDiagramInTotalMode() throws JspException {
	
	StringBuilder builder = new StringBuilder();	
	builder.append("<table class=\"tcalendar thlight\">");	
	
	generateHeaders(builder);
	
	int scale = getScale();
	
	for (GanttDiagramEvent event : getEvents()) {	    
	    
	    String eventUrl = getRequest().getContextPath() + getEventUrl() + "&amp;" + getEventParameter() + "=" + event.getEventIdentifierForGanttDiagram();
	    String eventName = event.getEventNameForGanttDiagram().getContent(Language.valueOf(getGanttDiagramObject().getLocale().getLanguage()));
	    String paddingStyle ="padding-left:" + event.getEventOffsetForGanttDiagram() * 15 + "px";	    	    
	    String selectedEvent = getRequest().getParameter(getEventParameter());
	    Object selectedEventObject = getRequest().getAttribute(getEventParameter());	    
	    
	    if(event.getEventIdentifierForGanttDiagram().equals(selectedEvent) || 
		    (selectedEventObject != null && event.getEventIdentifierForGanttDiagram().equals(selectedEventObject.toString()))) {
		builder.append("<tr class=\"selected\">");
	    } else {
		builder.append("<tr>");
	    }
	    	    
	    builder.append("<td class=\"padded\">").append("<span style=\"").append(paddingStyle).append("\">");
	    builder.append("<a href=\"").append(eventUrl).append("\">").append("*").append(eventName);
	    builder.append("</a></span></td>");
	    
	    for (DateTime month : getGanttDiagramObject().getMonths()) {
						
		DateTime firstDayOfMonth = (month.getDayOfMonth() != 1) ? month.withDayOfMonth(1) : month;
		DateTime lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1);										
		int monthNumberOfDays = Days.daysBetween(firstDayOfMonth, lastDayOfMonth).getDays() + 1;			
		BigDecimal entryDays = EMPTY_UNIT, startDay = EMPTY_UNIT;
		
		builder.append("<td style=\"width: ").append(convertToEm(monthNumberOfDays * scale)).append("em;\"><div style=\"position: relative;\">");		
		
		for (Interval interval : event.getEventSortedIntervalsForGanttDiagram()) {		    
			
		    DateMidnight intervalStart = interval.getStart().toDateMidnight();
		    DateMidnight intervalEnd = interval.getEnd().toDateMidnight();
		    
                    // Started in this month
                    if(intervalStart.getMonthOfYear() == month.getMonthOfYear() && intervalStart.getYear() == month.getYear()){																							    
                        
                        // Ended in this month
                        if(interval.getEnd().getMonthOfYear() == month.getMonthOfYear() && intervalEnd.getYear() == month.getYear()){										
                    		
                            // Started in first day of this month
                            if(intervalStart.getDayOfMonth() == 1){									    
                		  
                        	// Ended in the last day of this month
                        	if(intervalEnd.getDayOfMonth() == monthNumberOfDays){                       
                        	    entryDays = convertToEm((Days.daysBetween(intervalStart, lastDayOfMonth).getDays() + 1) * scale);                                	
                        	    startDay = convertToEm((intervalStart.getDayOfMonth() - 1) * scale);                                	  
                        	    addSpecialDiv(builder, entryDays, startDay);
                        	}									    
                		    
                        	// Ended before last day of this month
                        	else {		                        	   
                        	    entryDays = convertToEm((Days.daysBetween(intervalStart, intervalEnd).getDays() + 1) * scale);                                	
                        	    startDay = convertToEm((intervalStart.getDayOfMonth() - 1) * scale);                                	  
                        	    addSpecialDiv(builder, entryDays, startDay);
                        	}																		
                            }									
                		
                            // Started after first day of this month    								
                            else {									    
                		
                        	// Ended in the last day of this month
                        	if(intervalEnd.getDayOfMonth() == monthNumberOfDays){		                    			                     
                        	    entryDays = convertToEm((Days.daysBetween(intervalStart, lastDayOfMonth).getDays() + 1) * scale);                                	
                        	    startDay = convertToEm((intervalStart.getDayOfMonth() - 1) * scale);                                	  
                        	    addSpecialDiv(builder, entryDays, startDay);
                        	}									    
                		    
                        	// Ended before last day of this month
                        	else {	                                                                                      
                        	    entryDays = convertToEm((Days.daysBetween(intervalStart, intervalEnd).getDays() + 1) * scale);                                	
                        	    startDay = convertToEm((intervalStart.getDayOfMonth() - 1) * scale);                                	  
                        	    addSpecialDiv(builder, entryDays, startDay);
                        	}									    									    
                            }																																
                        } 							    
                    	
                        // Ended after this month
                        else { 									                    		                                           
                            entryDays = convertToEm((Days.daysBetween(intervalStart, lastDayOfMonth).getDays() + 1) * scale);                                	
                            startDay = convertToEm((intervalStart.getDayOfMonth() - 1) * scale);                                	  
                            addSpecialDiv(builder, entryDays, startDay);                            														
                        }							 
                    
                    // Not Started in this month    
                    } else { 
                        
                        // Started before this month
                        if(intervalStart.getYear() < month.getYear() || (intervalStart.getYear() == month.getYear() && 
                    		    intervalStart.getMonthOfYear() < month.getMonthOfYear())) {								
                    		
                            // Ended after this month
                            if(intervalEnd.getYear() > month.getYear() || 
                    		    (intervalEnd.getYear() == month.getYear() && 
                    			    intervalEnd.getMonthOfYear() > month.getMonthOfYear())){
                    	
                        	entryDays = convertToEm((Days.daysBetween(firstDayOfMonth, lastDayOfMonth).getDays() + 1) * scale);                                	
                        	startDay = convertToEm((firstDayOfMonth.getDayOfMonth() - 1) * scale);                                	  
                        	addSpecialDiv(builder, entryDays, startDay);
                            }                     		                          
                            else {	
                    		    
                        	//Ended in this month
                        	if(intervalEnd.getMonthOfYear() == month.getMonthOfYear() && intervalEnd.getYear() == month.getYear()){        		                        	                          	    
                        	   
                        	    entryDays = convertToEm((Days.daysBetween(firstDayOfMonth, intervalEnd).getDays() + 1) * scale);                                	
                                    startDay = convertToEm((firstDayOfMonth.getDayOfMonth() - 1) * scale);                                	  
                                    addSpecialDiv(builder, entryDays, startDay);                        	   											
                        	}                     		                            									    									    									   																								    									    									    									    										 
                            }
                        }
                    }                                                           
		}
		builder.append("</div></td>");		
	    }	    
	    builder.append("<td class=\"padded smalltxt\">").append(event.getEventObservationsForGanttDiagram()).append("</td>");		
	}
	
	insertNextAndBeforeLinks(builder);
	builder.append("</table>");
	return builder;
    }
    
    private int getScale() {
	switch (getViewTypeEnum()) {
	case TOTAL:
	    return 1;
	    
	case MONTHLY:
	    return 10;

	default:
	    return 0;
	}			
    }

    private void generateHeaders(StringBuilder builder) throws JspException {
	
	switch (getViewTypeEnum()) {
	
	case WEEKLY:
	    builder.append("<tr>");
            builder.append("<th rowspan=\"4\">").append(getMessage("label.ganttDiagram.event")).append("</th>");	
            for (Integer year : getGanttDiagramObject().getYearsView().keySet()) {
                builder.append("<th colspan=\"").append(getGanttDiagramObject().getYearsView().get(year)).append("\">").append(year).append("</th>");	
            }	
            builder.append("<th rowspan=\"4\">").append(getMessage("label.ganttDiagram.observations")).append("</th>");
            builder.append("</tr>");
            
            builder.append("<tr>");		
            for (YearMonthDay month : getGanttDiagramObject().getMonthsView().keySet()) {
                builder.append("<th colspan=\"").append(getGanttDiagramObject().getMonthsView().get(month)).append("\">").append(month.toString("MMM", getGanttDiagramObject().getLocale())).append("</th>");	    
            }	
            builder.append("</tr>");	
            
            builder.append("<tr>");		
            for (DateTime day : getGanttDiagramObject().getDays()) {
                builder.append("<th>").append(day.toString("E", getGanttDiagramObject().getLocale())).append("</th>");	    
            }	
            builder.append("</tr>");
            
            builder.append("<tr>");		
            for (DateTime day : getGanttDiagramObject().getDays()) {
                builder.append("<th>").append(day.getDayOfMonth()).append("</th>");	    
            }	
            builder.append("</tr>");	    
	    break;
	
	case DAILY:
	    builder.append("<tr>");
            builder.append("<th>").append(getMessage("label.ganttDiagram.event")).append("</th>");	
            builder.append("<th>").append(getGanttDiagramObject().getFirstInstant().toString("E", getGanttDiagramObject().getLocale())).append(", ")
                .append(getGanttDiagramObject().getFirstInstant().getDayOfMonth()).append("-").append(getGanttDiagramObject().getFirstInstant().getMonthOfYear())
                .append("-").append(getGanttDiagramObject().getFirstInstant().getYear()).append("</th>");	    	
            builder.append("<th>").append(getMessage("label.ganttDiagram.observations")).append("</th>");
            builder.append("</tr>");                
	    break;
	    
	case TOTAL:
	    builder.append("<tr>");
            builder.append("<th rowspan=\"2\">").append(getMessage("label.ganttDiagram.event")).append("</th>");	
            for (Integer year : getGanttDiagramObject().getYearsView().keySet()) {
                builder.append("<th colspan=\"").append(getGanttDiagramObject().getYearsView().get(year)).append("\">").append(year).append("</th>");	
            }	
            builder.append("<th rowspan=\"2\">").append(getMessage("label.ganttDiagram.observations")).append("</th>");
            builder.append("</tr>");
            
            builder.append("<tr>");		
            for (DateTime month : getGanttDiagramObject().getMonths()) {
                builder.append("<th>").append(month.toString("MMM", getGanttDiagramObject().getLocale())).append("</th>");	    
            }	
            builder.append("</tr>");
	    break;
	
	case MONTHLY:
	    builder.append("<tr>");
            builder.append("<th>").append(getMessage("label.ganttDiagram.event")).append("</th>");	                           
            builder.append("<th>").append(getGanttDiagramObject().getFirstInstant().toString("MMM", getGanttDiagramObject().getLocale())).append(" ").append(getGanttDiagramObject().getFirstInstant().getYear()).append("</th>");	           
            builder.append("<th>").append(getMessage("label.ganttDiagram.observations")).append("</th>");
            builder.append("</tr>");                          
	    break;
	    
	default:
	    break;
	}	
    }
    
    private void insertNextAndBeforeLinks(StringBuilder builder) throws JspException {
	YearMonthDay firstDay = getGanttDiagramObject().getFirstInstant().toYearMonthDay();
	if(firstDay != null) {	
	    
	    String nextUrl = "";
	    String beforeUrl = "";
	    
	    switch (getViewTypeEnum()) {
	    
	    case WEEKLY:
		nextUrl = getRequest().getContextPath() + getNextUrl() + "&amp;" + getFirstDayParameter() + "=" + firstDay.plusDays(8).toString("ddMMyyyy");
		beforeUrl = getRequest().getContextPath() + getNextUrl() + "&amp;" + getFirstDayParameter() + "=" + firstDay.minusDays(8).toString("ddMMyyyy");
		builder.append("<tr><td colspan=\"9\" class=\"acenter tcalendarlinks\"> <span class=\"smalltxt\"><a href=\"").append(beforeUrl).append("\">").append("&lt;&lt; ").append(getMessage("label.previous.week")).append("</a>");
		builder.append(" , ").append("<a href=\"").append(nextUrl).append("\">").append(getMessage("label.next.week")).append(" &gt;&gt;").append("</a>").append("</span></td></tr>");
		break;
	    
	    case DAILY:
		nextUrl = getRequest().getContextPath() + getNextUrl() + "&amp;" + getFirstDayParameter() + "=" + firstDay.plusDays(1).toString("ddMMyyyy");
		beforeUrl = getRequest().getContextPath() + getNextUrl() + "&amp;" + getFirstDayParameter() + "=" + firstDay.minusDays(1).toString("ddMMyyyy");
		builder.append("<tr><td colspan=\"3\" class=\"acenter tcalendarlinks\"><span class=\"smalltxt\"><a href=\"").append(beforeUrl).append("\">").append("&lt;&lt; ").append(getMessage("label.previous.day")).append("</a>");
		builder.append(" , ").append("<a href=\"").append(nextUrl).append("\">").append(getMessage("label.next.day")).append(" &gt;&gt;").append("</a>").append("</span></td></tr>");
		break;
			
	    case MONTHLY:
		nextUrl = getRequest().getContextPath() + getNextUrl() + "&amp;" + getFirstDayParameter() + "=" + firstDay.plusMonths(1).toString("ddMMyyyy");
		beforeUrl = getRequest().getContextPath() + getNextUrl() + "&amp;" + getFirstDayParameter() + "=" + firstDay.minusMonths(1).toString("ddMMyyyy");
		builder.append("<tr><td colspan=\"3\" class=\"acenter tcalendarlinks\"><span class=\"smalltxt\"><a href=\"").append(beforeUrl).append("\">").append("&lt;&lt; ").append(getMessage("label.previous.month")).append("</a>");
		builder.append(" , ").append("<a href=\"").append(nextUrl).append("\">").append(getMessage("label.next.month")).append(" &gt;&gt;").append("</a>").append("</span></td></tr>");
		break;
		
		
	    default:
		break;
	    }
	   
	}
    }    
    
    private int calculateTimeOfDay(DateTime time) {
	int hourOfDay = time.getHourOfDay();
        int minuteOfHour = time.getMinuteOfHour();
        
	switch (getViewTypeEnum()) {
	
	case WEEKLY:	               
           
	    // unit = 15 minutes
	    int result = (hourOfDay + 1) * 2;
            if(minuteOfHour <= 30) {
                return result - 1;
            } else {
                return result;
            }	
            
        case DAILY:  
            
            // unit = 5 minutes
            for (int i = 1, j = 0; j < 60; j += 5, i++) {        	
		if(minuteOfHour < j + 5) {
		    return i + (12 * hourOfDay);
		}
	    }            
            
	default:	   
	    return 0;
	}	
    }
    
    private int getNumberOfUnits() {
	switch (getViewTypeEnum()) {
	    case WEEKLY:
		return getNumberOfDayHalfHours();		
	    case DAILY:
		return getNumberOf5MinUnits();	
	    default:
		break;	   
	}	
	return 0;
    }
        
    private void addSpecialDiv(StringBuilder builder, BigDecimal entryDays, BigDecimal startDay) {	
	builder.append("<div style=\"width: ").append(entryDays).append("em; position: absolute; left: ");
	builder.append(startDay);
	builder.append("em; top: -0.7em;\" class=\"tdbar\">&nbsp;</div>");
    }
       
    private BigDecimal convertToEm(int value) {	
	return BigDecimal.valueOf(value).divide(PX_TO_EM_CONVERSION_DIVISOR);
    }
    
    private String getMessage(String key) throws JspException {
	String message = getMessageFromBundle(key);
	return (message != null) ? message : key;
    }

    private String getMessageFromBundle(String key) throws JspException {
	return (getBundle() != null) ? ((TagUtils.getInstance().present(this.pageContext, getBundle(),
		getGanttDiagramObject().getLocale().toString(), key)) ? TagUtils.getInstance().message(
		this.pageContext, getBundle(), getGanttDiagramObject().getLocale().toString(), key) : null) : null;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public List<GanttDiagramEvent> getEvents() {
        return events;
    }

    public void setEvents(List<GanttDiagramEvent> events) {
        this.events = events;
    }

    public GanttDiagram getGanttDiagramObject() {
        return ganttDiagramObject;
    }

    public void setGanttDiagramObject(GanttDiagram ganttDiagramObject) {
        this.ganttDiagramObject = ganttDiagramObject;
    }      

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String url) {
        this.eventUrl = url;
    }

    public String getEventParameter() {
        return eventParameter;
    }

    public void setEventParameter(String parameter) {
        this.eventParameter = parameter;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getGanttDiagram() {
        return ganttDiagram;
    }

    public void setGanttDiagram(String ganttDiagram) {
        this.ganttDiagram = ganttDiagram;
    }    
       
    public ViewType getViewTypeEnum() {
        return viewTypeEnum;
    }

    public void setViewTypeEnum(ViewType viewTypeEnum) {
        this.viewTypeEnum = viewTypeEnum;
    }
    
    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public String getFirstDayParameter() {
        return firstDayParameter;
    }

    public void setFirstDayParameter(String firstDayParameter) {
        this.firstDayParameter = firstDayParameter;
    }

    public int getNumberOf5MinUnits() {
        return numberOf5MinUnits;
    }

    public void setNumberOf5MinUnits(int numberOf5MinUnits) {
        this.numberOf5MinUnits = numberOf5MinUnits;
    }

    public int getNumberOfDayHalfHours() {
        return numberOfDayHalfHours;
    }

    public void setNumberOfDayHalfHours(int numberOfDayHalfHours) {
        this.numberOfDayHalfHours = numberOfDayHalfHours;
    }   
}
