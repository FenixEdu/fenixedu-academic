package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationComment;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.util.DayType;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.joda.time.Interval;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class InfoGenericEvent extends InfoShowOccupation implements GanttDiagramEvent {

    private Calendar beginTime;

    private Calendar endTime;

    private int diaSemana;

    private final GenericEvent genericEventReference;

    public InfoGenericEvent(GenericEvent genericEvent) {
	genericEventReference = genericEvent;
    }

    public InfoGenericEvent(GenericEvent genericEvent, int diaSemana_) {
	genericEventReference = genericEvent;
	diaSemana = diaSemana_;
	beginTime = genericEvent.getBeginTimeCalendar();
	endTime = genericEvent.getEndTimeCalendar();
    }

    public InfoGenericEvent(GenericEvent genericEvent, int diaSemana_, Calendar beginTime_, Calendar endTime_) {
	genericEventReference = genericEvent;
	diaSemana = diaSemana_;
	beginTime = beginTime_;
	endTime = endTime_;
    }

    public GenericEvent getGenericEvent() {
	return genericEventReference;
    }

    @Override
    public Integer getIdInternal() {
	final GenericEvent genericEvent = getGenericEvent();
	return genericEvent == null ? null : genericEvent.getIdInternal();
    }

    public String getTitle() {
	final GenericEvent genericEvent = getGenericEvent();
	return genericEvent == null ? null : genericEvent.getTitle().getContent(Language.getLanguage());
    }

    public String getDescription() {
	final GenericEvent genericEvent = getGenericEvent();
	return genericEvent == null ? null : genericEvent.getDescription().getContent(Language.getLanguage());
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
	final GenericEvent genericEvent = getGenericEvent();
	return (genericEvent == null || genericEvent.getGenericEventSpaceOccupations().isEmpty()) ? null : InfoRoomOccupation
		.newInfoFromDomain(genericEvent.getGenericEventSpaceOccupations().get(0));
    }

    @Override
    public InfoShift getInfoShift() {
	return null;
    }

    @Override
    public ShiftType getTipo() {
	return null;
    }

    public String getGanttDiagramEventIdentifier() {
	final GenericEvent genericEvent = getGenericEvent();
	final PunctualRoomsOccupationRequest punctualRoomsOccupationRequest = genericEvent == null ? null : getGenericEvent()
		.getPunctualRoomsOccupationRequest();
	return punctualRoomsOccupationRequest == null ? null : punctualRoomsOccupationRequest.getIdInternal().toString();
    }

    public MultiLanguageString getGanttDiagramEventName() {
	final GenericEvent genericEvent = getGenericEvent();
	final PunctualRoomsOccupationComment punctualRoomsOccupationComment = genericEvent == null ? null : getGenericEvent()
		.getPunctualRoomsOccupationRequest().getFirstComment();
	return punctualRoomsOccupationComment == null ? null : punctualRoomsOccupationComment.getSubject();
    }

    public String getGanttDiagramEventObservations() {
	final GenericEvent genericEvent = getGenericEvent();
	return genericEvent == null ? null : genericEvent.getGanttDiagramEventObservations();
    }

    public int getGanttDiagramEventOffset() {
	return 0;
    }

    public String getGanttDiagramEventPeriod() {
	final GenericEvent genericEvent = getGenericEvent();
	return genericEvent == null ? null : genericEvent.getGanttDiagramEventPeriod();
    }

    public List<Interval> getGanttDiagramEventSortedIntervals() {
	final GenericEvent genericEvent = getGenericEvent();
	return genericEvent == null ? null : genericEvent.getGanttDiagramEventSortedIntervals();
    }

    public Integer getGanttDiagramEventMonth() {
	return null;
    }

    public String getGanttDiagramEventUrlAddOns() {
	return null;
    }

    public boolean isGanttDiagramEventIntervalsLongerThanOneDay() {
	return false;
    }

    public boolean isGanttDiagramEventToMarkWeekendsAndHolidays() {
	return false;
    }

    public DayType getGanttDiagramEventDayType(Interval interval) {
	return null;
    }
}
