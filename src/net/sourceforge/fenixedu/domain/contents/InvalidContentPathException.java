package net.sourceforge.fenixedu.domain.contents;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class InvalidContentPathException extends DomainException {

    private String path;
    private Content content;
    
    public InvalidContentPathException(Content content, String path) {
	this.path = path;
	this.content = content;
    }

    public Content getContent() {
        return content;
    }

    public String getPath() {
        return path;
    }
    
    
    

}
