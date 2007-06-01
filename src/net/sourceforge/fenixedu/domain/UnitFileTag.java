package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class UnitFileTag extends UnitFileTag_Base {

	public UnitFileTag(Unit unit, String name) {
		setRootDomainObject(RootDomainObject.getInstance());
		setUnit(unit);
		setName(name);
	}

	public Integer getFileTagCount(Person person) {
		int count = 0;
		for (UnitFile file : getTaggedFiles()) {
			if (file.isPersonAllowedToAccess(person)) {
				count++;
			}
		}
		return count;
	}
	
	public boolean isTagAccessibleToUser(Person person) {
		return getFileTagCount(person) > 0;
	}
	
	public void delete() {
		if (getTaggedFilesCount() > 0) {
			throw new DomainException("error.cannot.delete.tag.with.files");
		}
		removeUnit();
		super.deleteDomainObject();
	}
}
