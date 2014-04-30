package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.organizationalStructure.ManagementCouncilUnit;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ManagementCouncilSite extends ManagementCouncilSite_Base {

    public ManagementCouncilSite(ManagementCouncilUnit unit) {
        super();
        setUnit(unit);
    }

    @Override
    public Group getOwner() {
        return UserGroup.of(Person.convertToUsers(getManagers()));
    }

    @Override
    public MultiLanguageString getName() {
        return new MultiLanguageString("");
    }
}
