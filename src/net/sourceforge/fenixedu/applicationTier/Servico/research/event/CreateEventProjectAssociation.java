package net.sourceforge.fenixedu.applicationTier.Servico.research.event;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.event.EventProjectAssociationFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.event.EventProjectAssociationSimpleCreationBean;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateEventProjectAssociation extends Service  {

    /**
     * Service responsible for creating an association between a project and an event
     * @param bean - Bean responsible for carrying the information from the presentation to the services layer
     * @param eventId - the identifier of the Event for whom the association is being created 
     * @return the newly created ProjectEventAssociation
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException - In case the project doesn't exist.
     */
    public ProjectEventAssociation run(EventProjectAssociationSimpleCreationBean bean, Integer eventId) throws ExcepcaoPersistencia, FenixServiceException {
        ProjectEventAssociation association = null;
        final Event event = rootDomainObject.readEventByOID(eventId);
        if(event == null){
            throw new FenixServiceException();
        }
        
        association = new ProjectEventAssociation();
        association.setEvent(event);
        association.setProject(bean.getProject());
        association.setRole(bean.getRole());
        return association;
    }
    
    /**
     * Service responsible for creating an association between a project and an event
     * @param bean - Bean responsible for carrying the information from the presentation to the services layer
     * @param eventId - the identifier of the Event for whom the association is being created 
     * @return the newly created ProjectEventAssociation
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException - In case the project doesn't exist.
     */
    public ProjectEventAssociation run(EventProjectAssociationFullCreationBean bean, Integer eventId) throws ExcepcaoPersistencia, FenixServiceException {
        final ProjectEventAssociation association;
        
        final Event event = rootDomainObject.readEventByOID(eventId);
        if(event == null){
            throw new FenixServiceException();
        }
        
        final Project project = new Project(bean.getProjectTitle(), bean.getProjectType());
        
        //Insert this line when inner enums are supported by the domain factory
//      participation = new ProjectParticipation(project, externalPerson.getPerson(), bean.getRole());
        association = new ProjectEventAssociation();
        association.setProject(project);
        association.setEvent(event);
        association.setRole(bean.getRole());        
        
        return association;
    }    
}
