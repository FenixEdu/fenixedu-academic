package net.sourceforge.fenixedu.domain.publication;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.util.PublicationArea;

public class PublicationTeacher extends PublicationTeacher_Base implements IPublicationTeacher {

    
    public PublicationTeacher()
    {
        super();
    }
    
    /********************************************************************
     *                        BUSINESS SERVICES                         *
     ********************************************************************/
    
    public PublicationTeacher(IPublication publication, ITeacher teacher, PublicationArea area) {
    	setPublicationArea(area);
        setPublication(publication);
        setTeacher(teacher);
    }

    public void delete()
    {
        removePublication();
        removeTeacher();
        setPublicationArea(null);
        super.deleteDomainObject();
    }

}
