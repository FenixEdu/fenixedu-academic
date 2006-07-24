package net.sourceforge.fenixedu.applicationTier.Servico.research.event;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.event.EventParticipantFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.event.EventParticipantSimpleCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.event.EventParticipantUnitCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.event.EventParticipantionFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.event.EventParticipantionSimpleCreationBean;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.event.EventParticipation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateEventParticipant extends Service  {

    /**
     * Service responsible for creating an event participation
     * @param bean - Bean responsible for carrying the information from the presentation to the services layer
     * @param eventId - the identifier of the Event for whom the participation is being created 
     * @return the newly created EventParticipation
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException - In case the project doesn't exist.
     */
    public EventParticipation run(EventParticipantSimpleCreationBean bean, Integer eventId) throws ExcepcaoPersistencia, FenixServiceException {
        EventParticipation participation = null;
        final Event event = rootDomainObject.readEventByOID(eventId);
        if(event == null){
            throw new FenixServiceException();
        }
        
        participation = new EventParticipation();
        participation.setEvent(event);
        participation.setParty(bean.getPerson());
        participation.setRole(bean.getRole());
        return participation;
    }
    
    /**
     * Service responsible for creating an event participation
     * @param bean - Bean responsible for carrying the information from the presentation to the services layer
     * @param personId - the identifier of the Event for whom the participation is being created 
     * @return the newly created EventParticipation
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException - In case the project doesn't exist.
     */
    public EventParticipation run(EventParticipantionSimpleCreationBean bean, Integer personId) throws ExcepcaoPersistencia, FenixServiceException {
        EventParticipation participation = null;
        final Party person = rootDomainObject.readPartyByOID(personId);
        if(person == null){
            throw new FenixServiceException();
        }
        
        participation = new EventParticipation();
        participation.setEvent(bean.getEvent());
        participation.setParty(person);
        participation.setRole(bean.getRole());
        return participation;
    }    
    
    /**
     * Service responsible for creating an event participation
     * @param bean - Bean responsible for carrying the information from the presentation to the services layer
     * @param eventId - the identifier of the Project for whom the participation is being created 
     * @return the newly created ProjectParticipation
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException - In case the project doesn't exist.
     */
    public EventParticipation run(EventParticipantFullCreationBean bean, Integer eventId) throws ExcepcaoPersistencia, FenixServiceException {
        final EventParticipation participation;
        final ExternalPerson externalPerson;
        
        final Event event = rootDomainObject.readEventByOID(eventId);
        if(event == null){
            throw new FenixServiceException();
        }
        
        final InsertExternalPerson insertExternalPerson = new InsertExternalPerson();
        
        if (bean.getOrganization() == null) {
            //In this case both an ExternalPerson and an Unit are being created
            externalPerson = insertExternalPerson.run(bean.getPersonName(), bean.getOrganizationName());
        }
        else {
            externalPerson = insertExternalPerson.run(bean.getPersonName(), bean.getOrganization());

        }
        //Insert this line when inner enums are supported by the domain factory
//      participation = new ProjectParticipation(project, externalPerson.getPerson(), bean.getRole());
        participation = new EventParticipation();
        participation.setEvent(event);
        participation.setParty(externalPerson.getPerson());
        participation.setRole(bean.getRole());        
        
        return participation;
    }    

    /**
     * Service responsible for creating an event participation
     * @param bean - Bean responsible for carrying the information from the presentation to the services layer
     * @param eventId - the identifier of the Project for whom the participation is being created 
     * @return the newly created ProjectParticipation
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException - In case the project doesn't exist.
     */
    public EventParticipation run(EventParticipantionFullCreationBean bean, Integer personId) throws ExcepcaoPersistencia, FenixServiceException {
        final EventParticipation participation;
        
        final Party party = rootDomainObject.readPartyByOID(personId);
        if(party == null){
            throw new FenixServiceException();
        }
        
        final Event event = new Event();
        event.setName(bean.getEventName());
        event.setEventType(bean.getEventType());
        event.setEventLocation(bean.getLocal());
        event.setStartDate(bean.getStartDate());
        event.setEndDate(bean.getEndDate());
        event.setFee(bean.getFee());
        
        participation = new EventParticipation();
        participation.setEvent(event);
        participation.setParty(party);
        participation.setRole(bean.getRole());        
        
        return participation;
    }       
    
    
    
    
    public EventParticipation run(EventParticipantUnitCreationBean bean, Integer eventId) throws ExcepcaoPersistencia, FenixServiceException {
        final EventParticipation participation;
        final Unit unit;
        
        final Event event = rootDomainObject.readEventByOID(eventId);
        if(event == null){
            throw new FenixServiceException();
        }
        
        if (bean.getUnit() == null) {          
        	unit = Unit.createNewExternalInstitution(bean.getUnitName());
        }
        else{
        	unit = bean.getUnit();
        }
        participation = new EventParticipation();
        participation.setEvent(event);
        participation.setParty(unit);
        participation.setRole(bean.getRole());        
        
        return participation;
    }    
}
