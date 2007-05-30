package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class UnitFileBean implements Serializable {

	private DomainReference<UnitFile> file;

	private String name;

	private String description;

	private List<IGroup> groups;

	public UnitFileBean(UnitFile file) {
		this.file = new DomainReference<UnitFile>(file);
		groups = new ArrayList<IGroup>();
		this.name = file.getDisplayName();
		this.description = file.getDescription();
		setupGroups(file.getPermittedGroup());
	}

	private void setupGroups(Group permittedGroup) {
		if (permittedGroup instanceof GroupUnion) {
			GroupUnion group = (GroupUnion) permittedGroup;
			groups.addAll(group.getChildren());
		} else {
			groups.add(permittedGroup);
		}
		

	}

	public UnitFile getFile() {
		return file.getObject();
	}

	public List<IGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<IGroup> groups) {
		this.groups = groups;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Group getGroup() {
		return new GroupUnion(getGroups());
	}

	public Unit getUnit() {
		return getFile().getUnit();
	}
}
