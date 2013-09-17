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
        setRootDomainObject(null);
        super.deleteDomainObject();
    }
    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasRequestorIstUsername() {
        return getRequestorIstUsername() != null;
    }

    @Deprecated
    public boolean hasExternalStorageIdentification() {
        return getExternalStorageIdentification() != null;
    }

    @Deprecated
    public boolean hasDeleteItem() {
        return getDeleteItem() != null;
    }

}
