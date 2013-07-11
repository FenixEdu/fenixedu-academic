package net.sourceforge.fenixedu.domain.alumni;

import java.text.Collator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.util.email.Recipient;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class CerimonyInquiry extends CerimonyInquiry_Base implements Comparable<CerimonyInquiry> {

    public CerimonyInquiry() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setBegin(new DateTime().plusDays(1));
        setEnd(getBegin().plusDays(15));
    }

    @Override
    public int compareTo(final CerimonyInquiry cerimonyInquiry) {
        if (getDescription() == null || cerimonyInquiry.getDescription() == null) {
            return 1;
        }
        final int c = Collator.getInstance().compare(getDescription(), cerimonyInquiry.getDescription());
        return c == 0 ? getExternalId().compareTo(cerimonyInquiry.getExternalId()) : c;
    }

    public SortedSet<CerimonyInquiryAnswer> getOrderedCerimonyInquiryAnswer() {
        return new TreeSet<CerimonyInquiryAnswer>(getCerimonyInquiryAnswerSet());
    }

    @Service
    public static CerimonyInquiry createNew() {
        return new CerimonyInquiry();
    }

    @Service
    public CerimonyInquiryAnswer createNewAnswer() {
        return new CerimonyInquiryAnswer(this);
    }

    @Service
    public void addPeople(final Set<String> usernames) {
        for (final String username : usernames) {
            final User user = User.readUserByUserUId(username);
            if (user != null) {
                final Person person = user.getPerson();
                if (!containsPerson(person)) {
                    new CerimonyInquiryPerson(this, person);
                }
            }
        }
    }

    private boolean containsPerson(final Person person) {
        for (final CerimonyInquiryPerson cerimonyInquiryPerson : person.getCerimonyInquiryPersonSet()) {
            if (cerimonyInquiryPerson.getCerimonyInquiry() == this) {
                return true;
            }
        }
        return false;
    }

    @Service
    public void delete() {
        for (final CerimonyInquiryAnswer cerimonyInquiryAnswer : getCerimonyInquiryAnswerSet()) {
            cerimonyInquiryAnswer.delete();
        }
        for (final CerimonyInquiryPerson cerimonyInquiryPerson : getCerimonyInquiryPersonSet()) {
            cerimonyInquiryPerson.delete();
        }
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public boolean isOpen() {
        return getBegin() != null && getBegin().isBeforeNow() && (getEnd() == null || getEnd().isAfterNow());
    }

    public Recipient createRecipient() {
        final Group group = new CerimonyInquiryGroup(this);
        return Recipient.newInstance("Inquiridos: " + getDescription(), group);
    }

    @Service
    public void toggleObservationFlag() {
        final Boolean allowComments = getAllowComments();
        final boolean value = !(allowComments != null && allowComments.booleanValue());
        setAllowComments(Boolean.valueOf(value));
    }

}
