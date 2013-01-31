package net.sourceforge.fenixedu.dataTransferObject.person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.person.RoleType;

public class InternalPersonBean extends PersonBean {

	private Set<RoleType> relationTypes = new HashSet<RoleType>();

	public InternalPersonBean() {
		super();
	}

	public Set<RoleType> getRelationTypes() {
		return relationTypes;
	}

	public void setRelationTypes(final Set<RoleType> relationTypes) {
		this.relationTypes = relationTypes;
	}

	public List<RoleType> getRelationTypesAsList() {
		return new ArrayList<RoleType>(relationTypes);
	}

	public void setRelationTypesAsList(final List<RoleType> relationTypes) {
		this.relationTypes.clear();
		this.relationTypes.addAll(relationTypes);
	}

}
