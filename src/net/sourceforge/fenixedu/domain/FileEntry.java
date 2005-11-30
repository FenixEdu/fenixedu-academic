package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public class FileEntry extends FileEntry_Base {
    
    public FileEntry() {
        super();
    }
    
    public FileEntry(ContentType contentType, ByteArray content, IPerson person) {
        super();
        setContentType(contentType);
        setContent(content);
        setPerson(person);
    }
    
    public byte[] getContents(){
        return this.getContent().getBytes();        
    }
    
    public void delete(){
        this.removePerson();
        super.deleteDomainObject();
    }
    
}
