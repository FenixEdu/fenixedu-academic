package net.sourceforge.fenixedu.domain;

import java.util.Set;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;

public class PersonalGroup extends PersonalGroup_Base implements IGroup {

    public PersonalGroup() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setPerson(null);

        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Override
    public int getElementsCount() {
        return this.getGroup().getElementsCount();
    }

    @Override
    public boolean isMember(Person person) {
        return this.getGroup().isMember(person);
    }

    @Override
    public boolean allows(User userView) {
        return this.getGroup().allows(userView);
    }

    @Override
    public Set<Person> getElements() {
        return this.getGroup().getElements();
    }

    @Deprecated
    public Group getGroup() {
        return super.getConcreteGroup();
    }

    @Deprecated
    public void setGroup(Group group) {
        super.setConcreteGroup(group);
    }

    @Override
    public String getExpression() {
        return getConcreteGroup().getExpression();
    }

    @Override
    public boolean hasPresentationNameDynamic() {
        return true;
    }

    @Override
    public String getPresentationNameBundle() {
        return null;
    }

    @Override
    public String getPresentationNameKey() {
        return null;
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return null;
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
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
    public boolean hasConcreteGroup() {
        return getConcreteGroup() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

    @Override
    public org.fenixedu.bennu.core.domain.groups.Group convert() {
        throw new UnsupportedOperationException();
    }

}
