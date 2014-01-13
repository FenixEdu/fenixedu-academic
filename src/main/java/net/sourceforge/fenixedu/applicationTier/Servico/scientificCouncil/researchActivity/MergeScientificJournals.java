package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.researchActivity;

import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.MergeScientificJournalPageContainerBean;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournalParticipation;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;

public class MergeScientificJournals {

    protected void run(MergeScientificJournalPageContainerBean mergeScientificJournalPageContainerBean) {
        ScientificJournal scientificJournal =
                new ScientificJournal(mergeScientificJournalPageContainerBean.getName(),
                        mergeScientificJournalPageContainerBean.getResearchActivityLocationType());
        scientificJournal.setIssn(mergeScientificJournalPageContainerBean.getIssn());
        scientificJournal.setUrl(mergeScientificJournalPageContainerBean.getUrl());

        for (DomainObject domainObject : mergeScientificJournalPageContainerBean.getSelectedObjects()) {
            ScientificJournal journal = (ScientificJournal) domainObject;
            scientificJournal.getJournalIssuesSet().addAll(journal.getJournalIssuesSet());

            for (ScientificJournalParticipation scientificJournalParticipation : journal.getParticipationsSet()) {
                scientificJournal.addUniqueParticipation(scientificJournalParticipation);
            }

            journal.delete();
        }
    }

    // Service Invokers migrated from Berserk

    private static final MergeScientificJournals serviceInstance = new MergeScientificJournals();

    @Atomic
    public static void runMergeScientificJournals(MergeScientificJournalPageContainerBean mergeScientificJournalPageContainerBean)
            throws NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(mergeScientificJournalPageContainerBean);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                serviceInstance.run(mergeScientificJournalPageContainerBean);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}