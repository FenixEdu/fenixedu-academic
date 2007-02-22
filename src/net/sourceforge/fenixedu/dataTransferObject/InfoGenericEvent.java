package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.LanguageUtils;

public class InfoGenericEvent extends InfoShowOccupation  {

    private Calendar beginTime;
    
    private Calendar endTime;
    
    private int diaSemana;
    
    private DomainReference<GenericEvent> genericEventReference;
    
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
}
