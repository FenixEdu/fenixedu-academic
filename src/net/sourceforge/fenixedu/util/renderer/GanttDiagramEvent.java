package net.sourceforge.fenixedu.util.renderer;

import java.util.List;

import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.Interval;

public interface GanttDiagramEvent {
        
    public List<Interval> getEventSortedIntervalsForGanttDiagram();
    
    public MultiLanguageString getEventNameForGanttDiagram();    
    
    public int getEventOffsetForGanttDiagram();  
    
    public String getEventObservationsForGanttDiagram();
    
    public String getEventIdentifierForGanttDiagram();
}
