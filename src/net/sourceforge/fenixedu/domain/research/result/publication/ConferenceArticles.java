package net.sourceforge.fenixedu.domain.research.result.publication;

/**
 * Used for relation of Inproceedings and Proceedings with Event
 * Required fields: Conference (Event)
 */
public class ConferenceArticles extends ConferenceArticles_Base {
    
    public  ConferenceArticles() {
        super();
    }
    
    public void delete()
    {
        removeEvent();
        super.delete();
    }
    
}
