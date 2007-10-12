package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import dml.runtime.RelationAdapter;

public class UnitFile extends UnitFile_Base {

	static {
		FileTagging.addListener(new RelationAdapter<UnitFile, UnitFileTag>() {

			@Override
			public void afterRemove(UnitFile file, UnitFileTag tag) {
				super.afterRemove(file, tag);
				if (file != null && tag != null && tag.getTaggedFilesCount() == 0) {
					tag.delete();
				}
			}

		});
	}

	public UnitFile(Unit unit, Person person, String description, String tags, String filename,
			String displayName, String mimeType, String checksum, String checksumAlgorithm, int size,
			String uniqueId, Group permittedGroup) {
		super();
		setUnit(unit);
		setUploader(person);
		setDescription(description);
		if (tags != null && tags.length() > 0) {
			setUnitFileTags(tags);
		}
		init(filename, displayName, mimeType, checksum, checksumAlgorithm, size, uniqueId,
				permittedGroup);
	}

	public void setUnitFileTags(String tag) {
		getUnitFileTags().clear();
		String[] tagNames = tag.split("\\p{Space}+");
		for (int i = 0; i < tagNames.length; i++) {
			UnitFileTag unitFileTag = getUnit().getUnitFileTag(tagNames[i].trim());
			addUnitFileTags((unitFileTag != null) ? unitFileTag
					: new UnitFileTag(getUnit(), tagNames[i].trim()));
		}
	}

	public void delete() {
		if (isEditableByCurrentUser()) {
			removeUnit();
			for(;!getUnitFileTags().isEmpty(); getUnitFileTags().get(0).removeTaggedFiles(this));
			removeRootDomainObject();
			removeUploader();
			super.deleteDomainObject();
		} else {
			throw new DomainException("error.cannot.delete.file");
		}
	}

	public boolean isEditableByUser(Person person) {
		return getUploader().equals(person);
	}

	public boolean isEditableByCurrentUser() {
		return isEditableByUser(AccessControl.getPerson());
	}

	public void updatePermissions(PersistentGroup group) {
		Group currentGroup = getPermittedGroup();
		if (currentGroup instanceof GroupUnion) {
			setPermittedGroup(updateGroupUnion((GroupUnion) currentGroup, group));
		} else {
			if (currentGroup.equals(group)) {
				setPermittedGroup(new EveryoneGroup());
			}
		}
	}

	private Group updateGroupUnion(GroupUnion group, PersistentGroup groupToRemove) {
		return new GroupUnion(processGroup(group, groupToRemove));
	}

	private List<IGroup> processGroup(GroupUnion groupUnion, PersistentGroup groupToRemove) {
		List<IGroup> groups = new ArrayList<IGroup>();
		for (IGroup group : groupUnion.getChildren()) {
			if (group instanceof GroupUnion) {
				groups.addAll(processGroup((GroupUnion) group, groupToRemove));
			} else {
				if (!group.equals(groupToRemove)) {
					groups.add(group);
				}
			}
		}
		return groups;
	}
	
	public boolean hasUnitFileTags(Collection<UnitFileTag> tags) {
		return getUnitFileTags().containsAll(tags);
	}

}
