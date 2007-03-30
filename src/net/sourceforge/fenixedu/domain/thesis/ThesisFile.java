package net.sourceforge.fenixedu.domain.thesis;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ThesisFile extends ThesisFile_Base {
    
    public ThesisFile(String uniqueId, String name) {
        super();
        
        init(name, name, null, null, null, null, uniqueId, null);
    }

    public void delete() {
        Thesis thesis = getDissertationThesis();
        if (thesis == null) {
            thesis = getAbstractThesis();
        }
        
        if (! thesis.isWaitingConfirmation()) {
            throw new DomainException("thesis.file.delete.notAllowed");
        }
        
        removeRootDomainObject();
        removeDissertationThesis();
        removeAbstractThesis();
        
        deleteDomainObject();
    }
    
}
