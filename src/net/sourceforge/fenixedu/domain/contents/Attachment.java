package net.sourceforge.fenixedu.domain.contents;

import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class Attachment extends Attachment_Base {
    
    protected  Attachment() {
        super();
    }
    
    public Attachment(FileItem fileItem) {
	this();
	setFileItem(fileItem);
    }

    @Override
    protected void disconnect() {
        super.disconnect();
        if(hasFileItem()) {
            getFileItem().delete();
        }
    }
    
    @Override
    public MultiLanguageString getName() {
	return new MultiLanguageString(getFileItem().getDisplayName()); 
    }
    
    @Override
    public boolean isParentAccepted(Container parent) {
        return parent instanceof Item;
    }
    
}
