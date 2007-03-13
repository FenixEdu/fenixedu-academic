package net.sourceforge.fenixedu.domain.thesis;

public class ThesisFile extends ThesisFile_Base {
    
    public ThesisFile(String uniqueId, String name) {
        super();
        
        init(name, name, null, null, null, null, uniqueId, null);
    }

    public void delete() {
        removeRootDomainObject();
        removeDissertationThesis();
        removeAbstractThesis();
        
        deleteDomainObject();
    }
    
}
