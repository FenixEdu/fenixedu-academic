package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class SpecificUnitSite extends SpecificUnitSite_Base {

    protected SpecificUnitSite() {
        super();
    }

    public SpecificUnitSite(Unit unit) {
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
