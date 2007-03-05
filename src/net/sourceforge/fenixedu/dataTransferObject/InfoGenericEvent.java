package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;
import java.util.List;

import org.joda.time.Interval;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

public class InfoGenericEvent extends InfoShowOccupation implements GanttDiagramEvent {
       
    private Calendar beginTime;
    
    private Calendar endTime;
    
    private int diaSemana;
    
    private DomainReference<GenericEvent> genericEventReference;

    public InfoGenericEvent(GenericEvent genericEvent) {
	genericEventReference = new DomainReference<GenericEvent>(genericEvent);
    }
    
    public InfoGenericEvent(GenericEvent genericEvent, int diaSemana_) {
	genericEventReference = new DomainReference<GenericEvent>(genericEvent);
	diaSemana = diaSemana_;
	beginTime = genericEvent.getBeginTimeCalendar();
	endTime = genericEvent.getEndTimeCalendar();
    }
    
    public InfoGenericEvent(GenericEvent genericEvent, int diaSemana_, Calendar beginTime_, Calendar endTime_) {
	genericEventReference = new DomainReference<GenericEvent>(genericEvent);
	diaSemana = diaSemana_;
	beginTime = beginTime_;
	endTime = endTime_;
    }
 
    public GenericEvent getGenericEvent() {
        return genericEventReference == null ? null : genericEventReference.getObject();
    }
    
    public Integer getIdInternal() {
	return getGenericEvent().getIdInternal();
    }
    
    public String getTitle() {
	return getGenericEvent().getTitle().getContent(LanguageUtils.getLanguage());
    }

    public String getDescription() {
	return getGenericEvent().getDescription().getContent(LanguageUtils.getLanguage());
    }
    
    @Override
    public DiaSemana getDiaSemana() {
	return new DiaSemana(diaSemana);
    }

    @Override
    public Calendar getFim() {	
	return endTime;
    }

    @Override
    public Calendar getInicio() {
	return beginTime;
    }
       
    @Override
    public InfoRoomOccupation getInfoRoomOccupation() {
	return (getGenericEvent().getRoomOccupations().isEmpty()) ? null : 
	    InfoRoomOccupation.newInfoFromDomain(getGenericEvent().getRoomOccupations().get(0));	
    }

    @Override
    public InfoShift getInfoShift() {
	return null;
    }

    @Override
    public ShiftType getTipo() {
	return null;
    }

    public String getEventIdentifierForGanttDiagram() {
	return getGenericEvent().getPunctualRoomsOccupationRequest().getIdInternal().toString();
    }

    public MultiLanguageString getEventNameForGanttDiagram() {
	return getGenericEvent().getPunctualRoomsOccupationRequest().getFirstComment().getSubject();
    }

    public String getEventObservationsForGanttDiagram() {
	return getGenericEvent().getEventObservationsForGanttDiagram();
    }

    public int getEventOffsetForGanttDiagram() {
	return 0;
    }

    public String getEventPeriodForGanttDiagram() {
	return getGenericEvent().getEventPeriodForGanttDiagram();
    }

    public List<Interval> getEventSortedIntervalsForGanttDiagram() {
	return getGenericEvent().getEventSortedIntervalsForGanttDiagram();
    }   
}
