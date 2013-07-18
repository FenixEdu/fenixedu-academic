package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;


import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.CreateIssueBean;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.util.Month;
import pt.ist.fenixWebFramework.services.Service;

public class CreateJournalIssue {

    @Service
    public static JournalIssue run(CreateIssueBean bean) {

        ScientificJournal journal;
        if (bean.getJournal() == null) {
            CreateScientificJournal service = new CreateScientificJournal();
            journal = service.run(bean.getScientificJournalName(), bean.getIssn(), bean.getPublisher(), bean.getLocation());
        } else {
            journal = bean.getJournal();
        }
        return run(journal, bean.getYear(), bean.getMonth(), bean.getVolume(), bean.getNumber(), bean.getUrl(),
                bean.getSpecialIssue(), bean.getSpecialIssueComment());
    }

    @Service
    public static JournalIssue run(ScientificJournal journal, Integer year, Month month, String volume, String number,
            String url, Boolean specialIssue, String specialIssueComment) {
        JournalIssue issue = new JournalIssue(journal);
        issue.setYear(year);
        issue.setMonth(month);
        issue.setVolume(volume);
        issue.setNumber(number);
        issue.setUrl(url);
        issue.setSpecialIssue(specialIssue);
        issue.setSpecialIssueComment(specialIssueComment);
        return issue;
    }
}