package net.sourceforge.fenixedu.applicationTier.Servico.research.project;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectParticipantFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectParticipantSimpleCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectParticipantUnitCreationBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;
import net.sourceforge.fenixedu.predicates.ResultPredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateProjectParticipant {

    /**
     * Service responsible for creating a project participation
     * 
     * @param bean
     *            - Bean responsible for carrying the information from the
     *            presentation to the services layer
     * @param projectId
     *            - the identifier of the Project for whom the participation is
     *            being created
     * @return the newly created ProjectParticipation
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException
     *             - In case the project doesn't exist.
     */
    @Atomic
    public static ProjectParticipation run(ProjectParticipantSimpleCreationBean bean, String projectId)
            throws FenixServiceException {
        check(ResultPredicates.author);
        ProjectParticipation participation = null;
        final Project project = FenixFramework.getDomainObject(projectId);
        if (project == null) {
            throw new FenixServiceException();
        }

        participation = new ProjectParticipation();
        participation.setProject(project);
        participation.setParty(bean.getPerson());
        participation.setRole(bean.getRole());
        return participation;
    }

    /**
     * Service responsible for creating a project participation
     * 
     * @param bean
     *            - Bean responsible for carrying the information from the
     *            presentation to the services layer
     * @param projectId
     *            - the identifier of the Project for whom the participation is
     *            being created
     * @return the newly created ProjectParticipation
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException
     *             - In case the project doesn't exist.
     */
    @Atomic
    public static ProjectParticipation run(ProjectParticipantFullCreationBean bean, String projectId)
            throws FenixServiceException {
        check(ResultPredicates.author);
        final ProjectParticipation participation;
        final ExternalContract externalPerson;

        final Project project = FenixFramework.getDomainObject(projectId);
        if (project == null) {
            throw new FenixServiceException();
        }

        final InsertExternalPerson insertExternalPerson = new InsertExternalPerson();

        if (bean.getOrganization() == null) {
            // In this case both an ExternalPerson and an Unit are being created
            externalPerson = insertExternalPerson.run(bean.getPersonName(), bean.getOrganizationName());
        } else {
            externalPerson =
                    insertExternalPerson.run(new InsertExternalPerson.ServiceArguments(bean.getPersonName(), bean
                            .getOrganization()));

        }
        // Insert this line when inner enums are supported by the domain factory
        // participation = new ProjectParticipation(project,
        // externalPerson.getPerson(), bean.getRole());
        participation = new ProjectParticipation();
        participation.setProject(project);
        participation.setParty(externalPerson.getPerson());
        participation.setRole(bean.getRole());

        return participation;
    }

    @Atomic
    public static ProjectParticipation run(ProjectParticipantUnitCreationBean bean, String projectId)
            throws FenixServiceException {
        check(ResultPredicates.author);
        final ProjectParticipation participation;
        final Unit unit;

        final Project project = FenixFramework.getDomainObject(projectId);
        if (project == null) {
            throw new FenixServiceException();
        }

        if (bean.getUnit() == null) {
            unit = Unit.createNewNoOfficialExternalInstitution(bean.getUnitName());
        } else {
            unit = bean.getUnit();
        }
        participation = new ProjectParticipation();
        participation.setProject(project);
        participation.setParty(unit);
        participation.setRole(bean.getRole());

        return participation;
    }
}