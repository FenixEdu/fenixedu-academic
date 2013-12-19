package net.sourceforge.fenixedu.presentationTier.Action.library;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class LibraryHigherCleranceGroupManagementBean implements Serializable {

    private Person operator;
    private Group higherClearenceGroup;
    private String searchUserId;

    public void setOperator(Person operator) {
        this.operator = operator;
    }

    public boolean getBelongsToGroup() {
        return higherClearenceGroup.getElements().contains(operator);
    }

    public Person getOperator() {
        return operator;
    }

    public void setHigherClearenceGroup(Group higherClearenceGroup) {
        this.higherClearenceGroup = higherClearenceGroup;
    }

    public Group getHigherClearenceGroup() {
        return higherClearenceGroup;
    }

    public void setSearchUserId(String searchUserId) {
        this.searchUserId = searchUserId;
    }

    public String getSearchUserId() {
        return searchUserId;
    }

    public void search() {
        Person res = Person.readPersonByUsername(getSearchUserId());

        if (res != null && res.hasRole(RoleType.LIBRARY)) {
            setOperator(res);
        } else {
            setOperator(null);
        }
    }
}
