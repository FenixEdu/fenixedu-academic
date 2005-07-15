package net.sourceforge.fenixedu.domain.publication;

public class Authorship extends Authorship_Base {
    
    public Authorship() {
        super();
    }
    
    public void delete()
    {
        setAuthor(null);
        setPublication(null);
    }
}
