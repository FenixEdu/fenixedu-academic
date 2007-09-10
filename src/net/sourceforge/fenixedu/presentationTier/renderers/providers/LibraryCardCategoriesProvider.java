package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class LibraryCardCategoriesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        List<RoleType> roleTypes = new ArrayList<RoleType>();
        roleTypes.add(RoleType.TEACHER);
        roleTypes.add(RoleType.RESEARCHER);
        roleTypes.add(RoleType.EMPLOYEE);
        return roleTypes;
    }

    public Converter getConverter() {
        return new EnumConverter();
    }

}
