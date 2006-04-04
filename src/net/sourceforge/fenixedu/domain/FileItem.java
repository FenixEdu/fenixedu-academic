package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;


public class FileItem extends FileItem_Base {
    
    static
    {
        ItemFileItem.addListener(new ItemFileItemListener());
    }

    public FileItem() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        
        if (this.getItems().size() != 0)
        {
            throw new DomainException("fileItem.cannotBeDeleted");
        }
        super.deleteDomainObject();
    }
    
    private static class ItemFileItemListener extends dml.runtime.RelationAdapter<FileItem,Item>
    {
        @Override
        public void afterRemove(FileItem fileItem, Item item) {
            fileItem.delete();
        }
        
    }

}
