package net.sourceforge.fenixedu.util.renderer;

import java.util.List;

import org.joda.time.Interval;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public interface GanttDiagramEvent {

    public List<Interval> getGanttDiagramEventSortedIntervals();

    public MultiLanguageString getGanttDiagramEventName();

    public int getGanttDiagramEventOffset();

    public String getGanttDiagramEventPeriod();

    public String getGanttDiagramEventObservations();

    public String getGanttDiagramEventIdentifier();

}
