package net.sourceforge.fenixedu.domain.alumni;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.DomainBackedGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.PersistentCerimonyInquiryGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;

public class CerimonyInquiryGroup extends DomainBackedGroup<CerimonyInquiry> {

    public CerimonyInquiryGroup(final CerimonyInquiry cerimonyInquiry) {
        super(cerimonyInquiry);
    }

    @Override
    public Set<Person> getElements() {
        final Set<Person> people = new HashSet<Person>();
        for (final CerimonyInquiryPerson cerimonyInquiryPerson : getObject().getCerimonyInquiryPersonSet()) {
            final Person person = cerimonyInquiryPerson.getPerson();
            people.add(person);
        }
        return people;
    }

    @Override
    public boolean isMember(final Person person) {
        if (person == null) {
            return false;
        }
        for (final CerimonyInquiryPerson cerimonyInquiryPerson : person.getCerimonyInquiryPersonSet()) {
            if (cerimonyInquiryPerson.getCerimonyInquiry() == getObject()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            CerimonyInquiry cerimonyInquiry;

            try {
                cerimonyInquiry = (CerimonyInquiry) arguments[0];
            } catch (ClassCastException e) {
                throw new WrongTypeOfArgumentException(0, CerimonyInquiry.class, arguments[0].getClass());
            }

            return new CerimonyInquiryGroup(cerimonyInquiry);
        }

        @Override
        public int getMinArguments() {
            return 1;
        }

        @Override
        public int getMaxArguments() {
            return 1;
        }

    }

    @Override
    public PersistentCerimonyInquiryGroup convert() {
        return PersistentCerimonyInquiryGroup.getInstance(getObject());
    }
}
