package net.sourceforge.fenixedu.domain.publication;

public class PublicationTeacher extends PublicationTeacher_Base implements IPublicationTeacher {
    
    public void delete()
    {
        setPublication(null);
        setPublicationArea(null);
        setTeacher(null);
    }

}
