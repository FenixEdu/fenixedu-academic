package net.sourceforge.fenixedu.applicationTier.Servico.research.project;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectEventAssociationFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectEventAssociationSimpleCreationBean;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.predicates.ResultPredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateProjectEventAssociation {

    /**
     * Service responsible for creating an association between a project and an
     * event
     * 
     * @param bean
     *            - Bean responsible for carrying the information from the
     *            presentation to the services layer
     * @param projectId
     *            - the identifier of the Project for whom the association is
     *            being created
     * @return the newly created ProjectEventAssociation
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException
     *             - In case the project doesn't exist.
     */
    @Atomic
    public static ProjectEventAssociation run(ProjectEventAssociationSimpleCreationBean bean, String projectId)
            throws FenixServiceException {
        check(ResultPredicates.author);
        ProjectEventAssociation association = null;
        final Project project = FenixFramework.getDomainObject(projectId);
        if (project == null) {
            throw new FenixServiceException();
        }

        association = new ProjectEventAssociation();
        association.setProject(project);
        association.setEventEdition(bean.getEvent());
        association.setRole(bean.getRole());
        return association;
    }

    /**
     * Service responsible for creating an association between a project and an
     * event
     * 
     * @param bean
     *            - Bean responsible for carrying the information from the
     *            presentation to the services layer
     * @param projectId
     *            - the identifier of the Project for whom the association is
     *            being created
     * @return the newly created ProjectEventAssociation
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException
     *             - In case the project doesn't exist.
     */
    @Atomic
    public static ProjectEventAssociation run(ProjectEventAssociationFullCreationBean bean, String projectId)
            throws FenixServiceException {
        check(ResultPredicates.author);
        final ProjectEventAssociation association;

        final Project project = FenixFramework.getDomainObject(projectId);
        if (project == null) {
            throw new FenixServiceException();
        }

        final EventEdition event = new EventEdition(bean.getEventName());

        // Insert this line when inner enums are supported by the domain factory
        // participation = new ProjectParticipation(project,
        // externalPerson.getPerson(), bean.getRole());
        association = new ProjectEventAssociation();
        association.setProject(project);
        association.setEventEdition(event);
        association.setRole(bean.getRole());

        return association;
    }
}