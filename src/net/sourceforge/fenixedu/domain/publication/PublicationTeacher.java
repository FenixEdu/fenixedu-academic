package net.sourceforge.fenixedu.domain.publication;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.PublicationArea;

public class PublicationTeacher extends PublicationTeacher_Base {

    static {
        Teacher.PublicationTeacherTeacher.addListener(new PublicationTeacherTeacherListener());
    }

    
    public PublicationTeacher()
    {
        super();
    }
    
    /********************************************************************
     *                        BUSINESS SERVICES                         *
     ********************************************************************/
    
    public PublicationTeacher(Publication publication, Teacher teacher, PublicationArea area) {
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
    
    private void verifyIfAuthor(Teacher teacher, Publication publication) {
        boolean isAuthor = false;
        Person author = teacher.getPerson();
        for(Authorship authorship : publication.getPublicationAuthorships()) {
            if (authorship.getAuthor().equals(author)) isAuthor = true;
        }
        if (!isAuthor) throw new DomainException("error.publication.teacherNotAuthor");
    }



    private static class PublicationTeacherTeacherListener extends dml.runtime.RelationAdapter<Teacher,PublicationTeacher> {
        @Override
        public void beforeAdd(Teacher teacher, PublicationTeacher publication) {
            if (teacher != null && !teacher.canAddPublicationToTeacherInformationSheet(publication.getPublicationArea())) {
                throw new DomainException("error.teacherSheetFull", publication.getPublicationArea().getName());
            }
        }
    }
}
