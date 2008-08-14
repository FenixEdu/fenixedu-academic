package net.sourceforge.fenixedu.domain;

public class DeleteFileRequest extends DeleteFileRequest_Base {

    private DeleteFileRequest() {
	super();
	this.setRootDomainObject(RootDomainObject.getInstance());
    }

    public DeleteFileRequest(Person person, String storageID, Boolean deleteItem) {
	this(person, storageID);
	this.setDeleteItem(deleteItem);
    }

    public DeleteFileRequest(Person person, String storageID) {
	this();
	this.setExternalStorageIdentification(storageID);
	this.setRequestorIstUsername(person.hasIstUsername() ? person.getIstUsername() : person.getMostImportantAlias());
	this.setDeleteItem(Boolean.TRUE);
    }

    public void delete() {
	removeRootDomainObject();
	super.deleteDomainObject();
    }
}
