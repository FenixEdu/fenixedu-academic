package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class UnitFile extends UnitFile_Base {
    
    
	public UnitFile(Unit unit, Person person, String description, String filename, String displayName, String mimeType, String checksum, String checksumAlgorithm, int size, String uniqueId, Group permittedGroup) {
		super();
		setUnit(unit);
		setUploader(person);
		setDescription(description);
		init(filename, displayName, mimeType, checksum, checksumAlgorithm, size, uniqueId, permittedGroup);
	}
	
	public void delete() {
		if(isEditableByCurrentUser()){
			removeUnit();
			removeRootDomainObject();
			super.deleteDomainObject();
		}
		else {
			throw new DomainException("error.cannot.delete.file");
		}
	}
	
	public boolean isEditableByUser(Person person) {
		return getUploader().equals(person);
	}
	
	public boolean isEditableByCurrentUser() {
		return isEditableByUser(AccessControl.getPerson());
	}
}
