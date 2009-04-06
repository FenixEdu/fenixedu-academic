package net.sourceforge.fenixedu.presentationTier.Action.delegate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.Group;

public class GroupsBean implements Serializable {
	List<Group> groups;
	List<Group> selected;

	public GroupsBean() {
		selected = new ArrayList<Group>();
	}

	public GroupsBean(List<Group> groups) {
		setGroups(groups);
		selected = new ArrayList<Group>();
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Group> getSelected() {
		return selected;
	}

	public void setSelected(List<Group> groups) {
		selected = groups;
	}
}
