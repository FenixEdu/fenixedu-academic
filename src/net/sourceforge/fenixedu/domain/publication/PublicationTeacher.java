package net.sourceforge.fenixedu.domain.publication;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
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
        verifyIfAuthor(teacher, publication);
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
 
    
    /********************************************************************
     *                         PRIVATE METHODS                          *
     ********************************************************************/
    
    private void verifyIfAuthor(ITeacher teacher, IPublication publication) {
        boolean isAuthor = false;
        IPerson author = teacher.getPerson();
        for(IAuthorship authorship : publication.getPublicationAuthorships()) {
            if (authorship.getAuthor().equals(author)) isAuthor = true;
        }
        if (!isAuthor) throw new DomainException("error.publication.teacherNotAuthor");
    }

}
