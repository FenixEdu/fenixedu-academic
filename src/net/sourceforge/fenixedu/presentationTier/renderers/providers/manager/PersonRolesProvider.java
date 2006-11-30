package net.sourceforge.fenixedu.presentationTier.renderers.providers.manager;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.manager.loginsManagement.LoginAliasBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;

public class PersonRolesProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	Set<RoleType> roleTypes = new HashSet<RoleType>();
	LoginAliasBean bean = (LoginAliasBean) source;
	if(bean != null) {
	    Person person = bean.getLogin().getUser().getPerson();
	    for (Role role : person.getPersonRoles()) {
		roleTypes.add(role.getRoleType());
	    }
	}
	return roleTypes;
    }

    public Converter getConverter() {
	return new EnumConverter();	
    }
}
