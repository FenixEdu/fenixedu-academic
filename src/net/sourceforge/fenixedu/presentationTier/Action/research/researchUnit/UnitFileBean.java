package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.UnitFileTag;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class UnitFileBean implements Serializable {

	private DomainReference<UnitFile> file;

	private String name;

	private String description;

	private List<IGroup> groups;

	private String tags;

	protected UnitFileBean() {
		this.file = new DomainReference<UnitFile>(null);
		groups = new ArrayList<IGroup>();
	}
	
	public UnitFileBean(UnitFile file) {
		this.file = new DomainReference<UnitFile>(file);
		this.name = file.getDisplayName();
		this.description = file.getDescription();
		setupGroups(file.getPermittedGroup());
		setupTags(file.getUnitFileTags());
	}

	private void setupTags(List<UnitFileTag> unitFileTags) {
		String tags = "";
		int i = unitFileTags.size();
		for (UnitFileTag tag : unitFileTags) {
			tags += tag.getName();
			if (--i > 0) {
				tags += " ";
			}
		}
		setTags(tags);
	}

	private void setupGroups(Group permittedGroup) {
		if (permittedGroup instanceof GroupUnion) {
			groups = flatten((GroupUnion) permittedGroup);
		} else {
			groups = new ArrayList<IGroup>();
			groups.add(permittedGroup);
		}

	}

	private List<IGroup> flatten(GroupUnion group) {
		List<IGroup> groups = new ArrayList<IGroup>();
		for (IGroup children : group.getChildren()) {
			if (children instanceof GroupUnion) {
				groups.addAll(flatten((GroupUnion) children));
			} else {
				groups.add(children);
			}
		}
		return groups;
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

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

}
