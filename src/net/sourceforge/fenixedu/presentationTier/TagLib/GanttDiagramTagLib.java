package net.sourceforge.fenixedu.presentationTier.TagLib;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.renderer.GanttDiagram;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.apache.fop.fo.expr.PPColWidthFunction;
import org.apache.struts.taglib.TagUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;

public class GanttDiagramTagLib extends TagSupport {

    // Attributes
    private String ganttDiagram;

    private String eventUrl;
    
    private String eventParameter;
    
    private String bundle;
    
    
    // Auxiliar variables 
    private GanttDiagram ganttDiagramObject;

    private List<GanttDiagramEvent> events;
    
    private Locale locale;
    
    private HttpServletRequest request;
   
    
    public int doStartTag() throws JspException {
	String ganttDiagram = "";
	Object object = pageContext.findAttribute(getGanttDiagram());
	
	if (object != null) {	    	    
	    setGanttDiagramObject((GanttDiagram) object);	    
	    setEvents(getGanttDiagramObject().getEvents());
	    setLocale(LanguageUtils.getLocale());
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
	StringBuilder builder = new StringBuilder();
		
	builder.append("<table class=\"tcalendar thlight\">");
	
	builder.append("<tr>");
	builder.append("<th rowspan=\"2\">").append(getMessage("label.ganttDiagram.event")).append("</th>");	
	for (Integer year : getGanttDiagramObject().getYears().keySet()) {
	    builder.append("<th colspan=\"").append(getGanttDiagramObject().getYears().get(year)).append("\">").append(year).append("</th>");	
	}	
	builder.append("<th rowspan=\"2\">").append(getMessage("label.ganttDiagram.observations")).append("</th>");
	builder.append("</tr>");
		
	builder.append("<tr>");		
	for (DateTime month : getGanttDiagramObject().getMonths()) {
	    builder.append("<th>").append(month.toString("MMM", getLocale())).append("</th>");	    
	}	
	builder.append("</tr>");
			
	for (GanttDiagramEvent event : getEvents()) {	    
	    
	    String eventUrl = getRequest().getContextPath() + getEventUrl() + "&amp;" + getEventParameter() + "=" + event.getEventIdentifierForGanttDiagram();
	    String eventName = event.getEventNameForGanttDiagram().getContent(Language.valueOf(getLocale().getLanguage()));
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
						
		DateTime firstDayOfMonth = month.withDayOfMonth(1);
		DateTime lastDayOfMonth = month.plusMonths(1).withDayOfMonth(1).minusDays(1);										
		int monthNumberOfDays = Days.daysBetween(firstDayOfMonth, lastDayOfMonth).getDays() + 1;
		BigDecimal entryDays = BigDecimal.valueOf(0.0);
		
		builder.append("<td style=\"width: ").append(BigDecimal.valueOf(monthNumberOfDays).divide(BigDecimal.valueOf(10))).append("em;\"><div style=\"position: relative;\">");		
		for (Interval interval : event.getEventSortedIntervalsForGanttDiagram()) {		    
				    		   
                    // Started in this month
                    if(interval.getStart().getMonthOfYear() == month.getMonthOfYear() && 
                    	interval.getStart().getYear() == month.getYear()){																							    
                        
                        // Ended in this month
                        if(interval.getEnd().getMonthOfYear() == month.getMonthOfYear() && 
                    		interval.getEnd().getYear() == month.getYear()){										
                    		
                            // Started in first day of this month
                            if(interval.getStart().getDayOfMonth() == 1){									    
                		  
                        	// Ended in the last day of this month
                        	if(interval.getEnd().getDayOfMonth() == monthNumberOfDays){
                        	    entryDays = BigDecimal.valueOf(Days.daysBetween(interval.getStart(), lastDayOfMonth).getDays()).divide(BigDecimal.valueOf(10)).add(BigDecimal.valueOf(0.1));                        	    
                        	    builder.append("<div style=\"width: ").append(entryDays).append("em; position: absolute; left: ");
                        	    builder.append(BigDecimal.valueOf((interval.getStart().getDayOfMonth() - 1)).divide(BigDecimal.valueOf(10)));
                        	    builder.append("em; top: -0.7em;\" class=\"tdbar\">&nbsp;</div>");                        	                            	                            	  
                        	}									    
                		    
                        	// Ended before last day of this month
                        	else {		
                        	    entryDays = BigDecimal.valueOf(Days.daysBetween(interval.getStart(), interval.getEnd()).getDays()).divide(BigDecimal.valueOf(10)).add(BigDecimal.valueOf(0.1));                        	    
                        	    builder.append("<div style=\"width: ").append(entryDays).append("em; position: absolute; left: ");
                        	    builder.append(BigDecimal.valueOf((interval.getStart().getDayOfMonth() - 1)).divide(BigDecimal.valueOf(10)));
                        	    builder.append("em; top: -0.7em;\" class=\"tdbar\">&nbsp;</div>");                          	                            	                           	 
                        	}																		
                            }									
                		
                            // Started after first day of this month    								
                            else {									    
                		
                        	// Ended in the last day of this month
                        	if(interval.getEnd().getDayOfMonth() == monthNumberOfDays){		                    			
                        	    entryDays = BigDecimal.valueOf(Days.daysBetween(interval.getStart(), lastDayOfMonth).getDays()).divide(BigDecimal.valueOf(10)).add(BigDecimal.valueOf(0.1));;                        	    
                        	    builder.append("<div style=\"width: ").append(entryDays).append("em; position: absolute; left: ");
                        	    builder.append(BigDecimal.valueOf((interval.getStart().getDayOfMonth() - 1)).divide(BigDecimal.valueOf(10)));
                        	    builder.append("em; top: -0.7em;\" class=\"tdbar\">&nbsp;</div>");                             	    
                        	}									    
                		    
                        	// Ended before last day of this month
                        	else {	                                                                        
                                    entryDays = BigDecimal.valueOf(Days.daysBetween(interval.getStart(), interval.getEnd()).getDays()).divide(BigDecimal.valueOf(10)).add(BigDecimal.valueOf(0.1));;
                        	    builder.append("<div style=\"width: ").append(entryDays).append("em; position: absolute; left: ");
                        	    builder.append(BigDecimal.valueOf((interval.getStart().getDayOfMonth() - 1)).divide(BigDecimal.valueOf(10)));
                        	    builder.append("em; top: -0.7em;\" class=\"tdbar\">&nbsp;</div>");                                       
                        	}									    									    
                            }																																
                        } 							    
                    	
                        // Ended after this month
                        else { 									
                    		
                            // Started in first day of this month
                            if(interval.getStart().getDayOfMonth() == 1) {
                        	entryDays = BigDecimal.valueOf(Days.daysBetween(interval.getStart(), lastDayOfMonth).getDays()).divide(BigDecimal.valueOf(10)).add(BigDecimal.valueOf(0.1));;                        	
                        	builder.append("<div style=\"width: ").append(entryDays).append("em; position: absolute; left: ");
                        	builder.append(interval.getStart().getDayOfMonth() - 1);
                        	builder.append(BigDecimal.valueOf((interval.getStart().getDayOfMonth() - 1)).divide(BigDecimal.valueOf(10)));
                        	builder.append("em; top: -0.7em;\" class=\"tdbar\">&nbsp;</div>");                                  
                            }									
                            
                            // Started after first day of this month      
                            else {	                        	
                        	entryDays = BigDecimal.valueOf(Days.daysBetween(interval.getStart(), lastDayOfMonth).getDays()).divide(BigDecimal.valueOf(10)).add(BigDecimal.valueOf(0.1));;
                        	builder.append("<div style=\"width: ").append(entryDays).append("em; position: absolute; left: ");                        	
                        	builder.append(BigDecimal.valueOf((interval.getStart().getDayOfMonth() - 1)).divide(BigDecimal.valueOf(10)));
                        	builder.append("em; top: -0.7em;\" class=\"tdbar\">&nbsp;</div>");                                   	
                            }																
                        }							 
                    
                    // Not Started in this month    
                    } else { 
                        
                        // Started before this month
                        if(interval.getStart().getYear() < month.getYear() || 
                    	    (interval.getStart().getYear() == month.getYear() && 
                    		    interval.getStart().getMonthOfYear() < month.getMonthOfYear())) {								
                    		
                            // Ended after this month
                            if(interval.getEnd().getYear() > month.getYear() || 
                    		    (interval.getEnd().getYear() == month.getYear() && 
                    			    interval.getEnd().getMonthOfYear() > month.getMonthOfYear())){
                    		    	
                        	entryDays = BigDecimal.valueOf(Days.daysBetween(firstDayOfMonth, lastDayOfMonth).getDays()).divide(BigDecimal.valueOf(10)).add(BigDecimal.valueOf(0.1));;                        	
                        	builder.append("<div style=\"width: ").append(entryDays).append("em; position: absolute; left: ");                        	
                        	builder.append(BigDecimal.valueOf((firstDayOfMonth.getDayOfMonth() - 1)).divide(BigDecimal.valueOf(10)));
                        	builder.append("em; top: -0.7em;\" class=\"tdbar\">&nbsp;</div>");                		    
                            } 
                    		
                            // Not Ended after this month
                            else {	
                    		    
                        	//Ended in this month
                        	if(interval.getEnd().getMonthOfYear() == month.getMonthOfYear() && 
                    		    interval.getEnd().getYear() == month.getYear()){
                    		
                        	    //Ended in the last day of this month
                        	    if(interval.getEnd().getDayOfMonth() == monthNumberOfDays){		
                        		entryDays = BigDecimal.valueOf(Days.daysBetween(firstDayOfMonth, lastDayOfMonth).getDays()).divide(BigDecimal.valueOf(10)).add(BigDecimal.valueOf(0.1));;                        		
                                	builder.append("<div style=\"width: ").append(entryDays).append("em; position: absolute; left: ");                                	
                                	builder.append(BigDecimal.valueOf((firstDayOfMonth.getDayOfMonth() - 1)).divide(BigDecimal.valueOf(10)));
                                	builder.append("em; top: -0.7em;\" class=\"tdbar\">&nbsp;</div>");                     			    
                        	    }									    
                                        
                                        // Ended before last day of this month
                        	    else {		
                        		entryDays = BigDecimal.valueOf(Days.daysBetween(firstDayOfMonth, interval.getEnd()).getDays()).divide(BigDecimal.valueOf(10)).add(BigDecimal.valueOf(0.1));;
                                	builder.append("<div style=\"width: ").append(entryDays).append("em; position: absolute; left: ");
                                	builder.append(BigDecimal.valueOf((firstDayOfMonth.getDayOfMonth() - 1)).divide(BigDecimal.valueOf(10)));
                                	builder.append("em; top: -0.7em;\" class=\"tdbar\">&nbsp;</div>");                                             
                        	    }													
                        	} 
                    		    
                        	// Ended before this month
                        	else {
                        	   // Do Nothing!!!!
                        	}									    									    									   																								    									    									    									    										    
                            }																																                    	
                        
                        // Started after this month
                        } else {						
                            // Do Nothing!!!!															
                        }
                    }                                                           
		}
		builder.append("</div></td>");		
	    }	    
	    builder.append("<td class=\"padded smalltxt\">").append(event.getEventObservationsForGanttDiagram()).append("</td>");		
	}
	
	builder.append("</table>");
	return builder.toString();
    }
    
    private String getMessage(String key) throws JspException {
	String message = getMessageFromBundle(key);
	return (message != null) ? message : key;
    }

    private String getMessageFromBundle(String key) throws JspException {
	return (getBundle() != null) ? ((TagUtils.getInstance().present(this.pageContext, getBundle(),
		getLocale().toString(), key)) ? TagUtils.getInstance().message(
		this.pageContext, getBundle(), getLocale().toString(), key) : null) : null;
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

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
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

}
