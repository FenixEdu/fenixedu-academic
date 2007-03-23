package net.sourceforge.fenixedu.applicationTier.Servico.research.project;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectEventAssociationFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectEventAssociationSimpleCreationBean;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateProjectEventAssociation extends Service  {

    /**
     * Service responsible for creating an association between a project and an event
     * @param bean - Bean responsible for carrying the information from the presentation to the services layer
     * @param projectId - the identifier of the Project for whom the association is being created 
     * @return the newly created ProjectEventAssociation
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException - In case the project doesn't exist.
     */
    public ProjectEventAssociation run(ProjectEventAssociationSimpleCreationBean bean, Integer projectId) throws ExcepcaoPersistencia, FenixServiceException {
        ProjectEventAssociation association = null;
        final Project project = rootDomainObject.readProjectByOID(projectId);
        if(project == null){
            throw new FenixServiceException();
        }
        
        association = new ProjectEventAssociation();
        association.setProject(project);
        association.setEventEdition(bean.getEvent());
        association.setRole(bean.getRole());
        return association;
    }
    
    /**
     * Service responsible for creating an association between a project and an event
     * @param bean - Bean responsible for carrying the information from the presentation to the services layer
     * @param projectId - the identifier of the Project for whom the association is being created 
     * @return the newly created ProjectEventAssociation
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException - In case the project doesn't exist.
     */
    public ProjectEventAssociation run(ProjectEventAssociationFullCreationBean bean, Integer projectId) throws ExcepcaoPersistencia, FenixServiceException {
        final ProjectEventAssociation association;
        
        final Project project = rootDomainObject.readProjectByOID(projectId);
        if(project == null){
            throw new FenixServiceException();
        }
        
        final EventEdition event = new EventEdition(bean.getEventName());
        
        //Insert this line when inner enums are supported by the domain factory
//      participation = new ProjectParticipation(project, externalPerson.getPerson(), bean.getRole());
        association = new ProjectEventAssociation();
        association.setProject(project);
        association.setEventEdition(event);
        association.setRole(bean.getRole());        
        
        return association;
    }    
}
