package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;

public class WebSiteManagersGroup extends DomainBackedGroup<UnitSite> {

    /**
     * Default serial id.
     */
    private static final long serialVersionUID = 1L;

    public WebSiteManagersGroup(UnitSite site) {
        super(site);
    }

    @Override
    public Set<Person> getElements() {
        final UnitSite unitSite = getObject();
        return unitSite == null ? Collections.EMPTY_SET : unitSite.getManagersSet();
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getObject()) };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getObject().getUnit().getName() };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            UnitSite site = (UnitSite) arguments[0];
            return new WebSiteManagersGroup(site);
        }

        @Override
        public int getMaxArguments() {
            return 1;
        }

        @Override
        public int getMinArguments() {
            return 1;
        }
    }

    @Override
    public PersistentManagersOfUnitSiteGroup convert() {
        return PersistentManagersOfUnitSiteGroup.getInstance(getObject());
    }
}
