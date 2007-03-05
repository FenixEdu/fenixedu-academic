package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.CreateIssueBean;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;

public class CreateJournalIssue extends Service {

    public JournalIssue run(CreateIssueBean bean) {
	
	ScientificJournal journal;
	if(bean.getJournal()==null) {
	    CreateScientificJournal service = new CreateScientificJournal();
	    journal = service.run(bean.getScientificJournalName(), bean.getLocation());
	}
	else {
	    journal = bean.getJournal();
	}
	JournalIssue issue = new JournalIssue(journal);
	issue.setYear(bean.getYear());
	issue.setMonth(bean.getMonth());
	issue.setVolume(bean.getVolume());
	issue.setNumber(bean.getNumber());
	issue.setPublisher(bean.getPublisher());
	issue.setUrl(bean.getUrl());
	return issue;
    }
 }
