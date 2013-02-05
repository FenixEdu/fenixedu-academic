package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.Person;

public class ConcreteReplyTo extends ConcreteReplyTo_Base {

    public ConcreteReplyTo(String address) {
        super();
        setReplyToAddress(address);
    }

    @Override
    public String getReplyToAddress(final Person person) {
        return getReplyToAddress();
    }

}
