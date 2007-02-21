package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.LanguageUtils;

public class InfoGenericEvent extends InfoShowOccupation  {

    private DomainReference<GenericEvent> genericEventReference;
    
    public InfoGenericEvent(GenericEvent genericEvent) {
	genericEventReference = new DomainReference<GenericEvent>(genericEvent);
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
	return getGenericEvent().getWeekDay();
    }

    @Override
    public Calendar getFim() {	
	return getGenericEvent().getEndTimeCalendar(); 
    }

    @Override
    public Calendar getInicio() {
	return getGenericEvent().getBeginTimeCalendar();
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
