package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class RoleTypeForInternalRelationProvider implements DataProvider {

    @Override
    public Object provide(final Object source, final Object currentValue) {
        final List<RoleType> roleTypes = new ArrayList<RoleType>();
        roleTypes.add(RoleType.TEACHER);
        roleTypes.add(RoleType.RESEARCHER);
        roleTypes.add(RoleType.EMPLOYEE);
        roleTypes.add(RoleType.GRANT_OWNER);
        return roleTypes;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
