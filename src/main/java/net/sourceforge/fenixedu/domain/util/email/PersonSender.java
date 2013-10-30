package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.BundleUtil;
import pt.ist.fenixframework.Atomic;

public class PersonSender extends PersonSender_Base {

    public PersonSender() {
        super();
        setFromAddress(Sender.getNoreplyMail());
        addReplyTos(new CurrentUserReplyTo());
    }

    public PersonSender(final Person person) {
        this();
        setPerson(person);
        setMembers(new PersonGroup(person));
    }

    @Override
    public String getFromName() {
        return BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "message.email.sender.template",
                Unit.getInstitutionAcronym(), getPerson().getName());
    }

    @Atomic
    public static PersonSender newInstance(final Person person) {
        return person.hasSender() ? person.getSender() : new PersonSender(person);
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
