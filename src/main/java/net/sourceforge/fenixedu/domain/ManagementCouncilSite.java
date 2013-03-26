package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.organizationalStructure.ManagementCouncilUnit;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ManagementCouncilSite extends ManagementCouncilSite_Base {

    public ManagementCouncilSite(ManagementCouncilUnit unit) {
        super();
        setUnit(unit);
    }

    @Override
    public IGroup getOwner() {
        return new GroupUnion(new FixedSetGroup(getManagers()));
    }

    @Override
    public void appendReversePathPart(final StringBuilder stringBuilder) {
    }

    @Override
    public MultiLanguageString getName() {
        return new MultiLanguageString("");
    }
}
