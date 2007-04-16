package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.thesis;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ScientificCommission;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender.MailBean;

public class ApproveThesisProposal extends ThesisServiceWithMailNotification {

    private static final int FIELD_ON = 1;
    private static final int FIELD_OFF = 0;
    
    private static final String SUBJECT_KEY = "thesis.proposal.jury.approve.subject";
    private static final String BODY_KEY = "thesis.proposal.jury.approve.body";

    @Override
    void process(Thesis thesis) {
        thesis.approveProposal();
    }
    
    @Override
    protected Collection<Person> getReceivers(Thesis thesis) {
        Person student = thesis.getStudent().getPerson();
        Person orientator = thesis.getOrientator().getPerson();
        Person coorientator = getPerson(thesis.getCoorientator());
        Person president = getPerson(thesis.getPresident());
        
        Set<Person> persons = personSet(student, orientator, coorientator, president); 
        for (ThesisEvaluationParticipant participant : thesis.getVowels()) {
            persons.add(participant.getPerson());
        }
        
        // also send proposal approval to the contact team
        ExecutionYear executionYear = thesis.getEnrolment().getExecutionYear();
        for (ScientificCommission member : thesis.getDegree().getScientificCommissionMembers(executionYear)) {
            if (member.isContact()) {
                persons.add(member.getPerson());
            }
        }
        
        return persons;
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
        String presidentName = name(thesis.getPresident());
        String presidentAffiliation = affiliation(thesis.getPresident());
        String orientatorName = name(thesis.getOrientator());
        String orientatorAffiliation = affiliation(thesis.getOrientator());
        String coorientatorName = name(thesis.getCoorientator());
        String coorientatorAffiliation = affiliation(thesis.getCoorientator());
        String vowel1Name = name(thesis.getVowels(), 0);
        String vowel1Affiliation = affiliation(thesis.getVowels(), 0);
        String vowel2Name = name(thesis.getVowels(), 1);
        String vowel2Affiliation = affiliation(thesis.getVowels(), 1);
        String vowel3Name = name(thesis.getVowels(), 2);
        String vowel3Affiliation = affiliation(thesis.getVowels(), 2);

        String date = String.format(new Locale("pt"), "%1$td de %1$tB de %1$tY", new Date());
        String currentPersonName = currentPerson.getName();
        
        bean.setSubject(getMessage(SUBJECT_KEY, title));
        bean.setMessage(getMessage(BODY_KEY,
                year,
                degreeName,
                studentName, 
                studentNumber,
                presidentName, presidentAffiliation,
                orientatorName, orientatorAffiliation,
                includeFlag(coorientatorName), coorientatorName, coorientatorAffiliation,
                includeFlag(vowel1Name), vowel1Name, vowel1Affiliation,
                includeFlag(vowel2Name), vowel2Name, vowel2Affiliation,
                includeFlag(vowel3Name), vowel3Name, vowel3Affiliation,
                date,
                currentPersonName
        ));
        
    }

    private int includeFlag(String value) {
        return value == null ? FIELD_OFF : FIELD_ON;
    }

    private String name(ThesisEvaluationParticipant participant) {
        return participant == null ? null : participant.getPersonName();
    }

    private String name(List<ThesisEvaluationParticipant> participants, int index) {
        if (participants.size() > index) {
            return name(participants.get(index));
        }
        else { 
            return null;
        }
    }

    private String affiliation(ThesisEvaluationParticipant participant) {
        return participant == null ? null : participant.getAffiliation();
    }

    private String affiliation(List<ThesisEvaluationParticipant> participants, int index) {
        if (participants.size() > index) {
            return affiliation(participants.get(index));
        }
        else { 
            return null;
        }
    }

}
