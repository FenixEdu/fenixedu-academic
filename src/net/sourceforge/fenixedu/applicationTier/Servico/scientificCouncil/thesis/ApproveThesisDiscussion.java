package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.thesis;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender.MailBean;

public class ApproveThesisDiscussion extends ThesisServiceWithMailNotification {

    private static final String SUBJECT_KEY = "thesis.evaluation.approve.subject";
    private static final String BODY_KEY = "thesis.evaluation.approve.body";
    
    @Override
    public void process(Thesis thesis) {
        thesis.approveEvaluation();
        
        
    }

    @Override
    protected void setMessage(Thesis thesis, MailBean bean) {
        Person currentPerson = AccessControl.getPerson();
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        
        String title = thesis.getTitle().getContent();
        String year = executionYear.getYear();
        String degreeName = thesis.getDegree().getName();
        String studentName = thesis.getStudent().getPerson().getName();
        String studentNumber = thesis.getStudent().getNumber().toString();

        String date = String.format(new Locale("pt"), "%1$td de %1$tB de %1$tY", new Date());
        String currentPersonName = currentPerson.getName();
        
        bean.setSubject(getMessage(SUBJECT_KEY, title));
        bean.setMessage(getMessage(BODY_KEY,
                year,
                degreeName,
                studentName, studentNumber,
                date,
                currentPersonName
        ));
    }

    @Override
    protected Collection<Person> getReceivers(Thesis thesis) {
        Person student = thesis.getStudent().getPerson();
        Person president = getPerson(thesis.getPresident());
        
        Set<Person> persons = personSet(student, president); 
        
        ExecutionYear executionYear = thesis.getEnrolment().getExecutionYear();
        for (ScientificCommission member : thesis.getDegree().getScientificCommissionMembers(executionYear)) {
            if (member.isContact()) {
                persons.add(member.getPerson());
            }
        }
        
        return persons;
    }

}
