package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.researchActivity;


import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.MergeJournalIssuePageContainerBean;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssueParticipation;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;

public class MergeJournalIssues {

    protected void run(MergeJournalIssuePageContainerBean mergeJournalIssuePageContainerBean) {
        JournalIssue journalIssue = new JournalIssue(mergeJournalIssuePageContainerBean.getScientificJournal());
        journalIssue.setVolume(mergeJournalIssuePageContainerBean.getVolume());
        journalIssue.setYear(mergeJournalIssuePageContainerBean.getYear());
        journalIssue.setNumber(mergeJournalIssuePageContainerBean.getNumber());
        journalIssue.setMonth(mergeJournalIssuePageContainerBean.getMonth());
        journalIssue.setUrl(mergeJournalIssuePageContainerBean.getUrl());
        journalIssue.setSpecialIssue(mergeJournalIssuePageContainerBean.getSpecialIssue());
        journalIssue.setSpecialIssueComment(mergeJournalIssuePageContainerBean.getSpecialIssueComment());

        for (DomainObject domainObject : mergeJournalIssuePageContainerBean.getSelectedObjects()) {
            JournalIssue issue = (JournalIssue) domainObject;
            journalIssue.getArticleAssociations().addAll(issue.getArticleAssociations());

            for (JournalIssueParticipation journalIssueParticipation : issue.getParticipationsSet()) {
                journalIssue.addUniqueParticipation(journalIssueParticipation);
            }

            issue.delete();
        }
    }

    // Service Invokers migrated from Berserk

    private static final MergeJournalIssues serviceInstance = new MergeJournalIssues();

    @Atomic
    public static void runMergeJournalIssues(MergeJournalIssuePageContainerBean mergeJournalIssuePageContainerBean)
            throws NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(mergeJournalIssuePageContainerBean);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                serviceInstance.run(mergeJournalIssuePageContainerBean);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}