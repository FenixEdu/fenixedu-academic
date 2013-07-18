package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.researchActivity;


import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.MergeEventPageContainerBean;
import net.sourceforge.fenixedu.domain.research.activity.EventParticipation;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.DomainObject;

public class MergeEvents {

    protected void run(MergeEventPageContainerBean mergeEventPageContainerBean) {
        ResearchEvent event =
                new ResearchEvent(mergeEventPageContainerBean.getName(), mergeEventPageContainerBean.getEventType(),
                        mergeEventPageContainerBean.getResearchActivityLocationType());
        event.setUrl(mergeEventPageContainerBean.getUrl());

        for (DomainObject domainObject : mergeEventPageContainerBean.getSelectedObjects()) {
            ResearchEvent event2 = (ResearchEvent) domainObject;
            event.getEventEditions().addAll(event2.getEventEditions());

            for (EventParticipation eventParticipation : event2.getParticipationsSet()) {
                event.addUniqueParticipation(eventParticipation);
            }
            event2.delete();
        }
    }

    // Service Invokers migrated from Berserk

    private static final MergeEvents serviceInstance = new MergeEvents();

    @Service
    public static void runMergeEvents(MergeEventPageContainerBean mergeEventPageContainerBean) throws NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(mergeEventPageContainerBean);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                serviceInstance.run(mergeEventPageContainerBean);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}