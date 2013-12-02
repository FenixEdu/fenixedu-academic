package net.sourceforge.fenixedu.domain;

import pt.ist.bennu.core.domain.Bennu;

public class DeleteFileRequest extends DeleteFileRequest_Base {

    private DeleteFileRequest() {
        super();
        this.setRootDomainObject(Bennu.getInstance());
    }

    public DeleteFileRequest(Person person, String storageID, Boolean deleteItem) {
        this(person, storageID);
        this.setDeleteItem(deleteItem);
    }

    public DeleteFileRequest(Person person, String storageID) {
        this();
        this.setExternalStorageIdentification(storageID);
        this.setRequestorIstUsername(person.getUsername());
        this.setDeleteItem(Boolean.TRUE);
    }

    public void delete() {
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
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
