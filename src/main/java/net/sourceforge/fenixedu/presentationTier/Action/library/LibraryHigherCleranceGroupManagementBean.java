package net.sourceforge.fenixedu.presentationTier.Action.library;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;

public class LibraryHigherCleranceGroupManagementBean implements Serializable {

    private Person operator;
    private Group higherClearenceGroup;
    private String searchUserId;

    public void setOperator(User operator) {
        this.operator = operator.getPerson();
    }

    public boolean getBelongsToGroup() {
        return higherClearenceGroup.isMember(operator.getUser());
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
        User res = User.findByUsername(getSearchUserId());

        if (res != null && res.getPerson().hasRole(RoleType.LIBRARY)) {
            setOperator(res);
        } else {
            setOperator(null);
        }
    }
}
