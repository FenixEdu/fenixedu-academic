package net.sourceforge.fenixedu.domain.alumni;

import java.text.Collator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.email.Recipient;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CerimonyInquiry extends CerimonyInquiry_Base implements Comparable<CerimonyInquiry> {

    public CerimonyInquiry() {
        super();
        setRootDomainObject(Bennu.getInstance());
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

    @Atomic
    public static CerimonyInquiry createNew() {
        return new CerimonyInquiry();
    }

    @Atomic
    public CerimonyInquiryAnswer createNewAnswer() {
        return new CerimonyInquiryAnswer(this);
    }

    @Atomic
    public void addPeople(final Set<String> usernames) {
        for (final String username : usernames) {
            final User user = User.findByUsername(username);
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

    @Atomic
    public void delete() {
        if (getGroup() != null) {
            throw new DomainException("error.cerimonyInquiry.cannotDeleteCerimonyInquiryUsedInAccessControl");
        }
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

    @Atomic
    public void toggleObservationFlag() {
        final Boolean allowComments = getAllowComments();
        final boolean value = !(allowComments != null && allowComments.booleanValue());
        setAllowComments(Boolean.valueOf(value));
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryPerson> getCerimonyInquiryPerson() {
        return getCerimonyInquiryPersonSet();
    }

    @Deprecated
    public boolean hasAnyCerimonyInquiryPerson() {
        return !getCerimonyInquiryPersonSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryAnswer> getCerimonyInquiryAnswer() {
        return getCerimonyInquiryAnswerSet();
    }

    @Deprecated
    public boolean hasAnyCerimonyInquiryAnswer() {
        return !getCerimonyInquiryAnswerSet().isEmpty();
    }

    @Deprecated
    public boolean hasEnd() {
        return getEnd() != null;
    }

    @Deprecated
    public boolean hasText() {
        return getText() != null;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasAllowComments() {
        return getAllowComments() != null;
    }

    @Deprecated
    public boolean hasBegin() {
        return getBegin() != null;
    }

}
