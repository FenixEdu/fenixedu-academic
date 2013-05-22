package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.researchActivity;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.MergeEventEditionPageContainerBean;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.EventEditionParticipation;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.DomainObject;

public class MergeEventEditions extends FenixService {

    protected void run(MergeEventEditionPageContainerBean mergeEventEditionPageContainerBean) {
        EventEdition eventEdition = new EventEdition(mergeEventEditionPageContainerBean.getEvent());
        eventEdition.setEdition(mergeEventEditionPageContainerBean.getEdition());
        eventEdition.setEventLocation(mergeEventEditionPageContainerBean.getEventLocation());
        eventEdition.setStartDate(mergeEventEditionPageContainerBean.getStartDate());
        eventEdition.setEndDate(mergeEventEditionPageContainerBean.getEndDate());
        eventEdition.setUrl(mergeEventEditionPageContainerBean.getUrl());
        eventEdition.setOrganization(mergeEventEditionPageContainerBean.getOrganization());

        for (DomainObject domainObject : mergeEventEditionPageContainerBean.getSelectedObjects()) {
            EventEdition edition = (EventEdition) domainObject;
            eventEdition.getEventConferenceArticlesAssociations().addAll(edition.getEventConferenceArticlesAssociations());
            eventEdition.getAssociatedProjects().addAll(edition.getAssociatedProjects());

            for (EventEditionParticipation eventEditionParticipation : edition.getParticipationsSet()) {
                eventEdition.addUniqueParticipation(eventEditionParticipation);
            }

            edition.delete();
        }
    }

    // Service Invokers migrated from Berserk

    private static final MergeEventEditions serviceInstance = new MergeEventEditions();

    @Service
    public static void runMergeEventEditions(MergeEventEditionPageContainerBean mergeEventEditionPageContainerBean)
            throws NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(mergeEventEditionPageContainerBean);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                serviceInstance.run(mergeEventEditionPageContainerBean);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}