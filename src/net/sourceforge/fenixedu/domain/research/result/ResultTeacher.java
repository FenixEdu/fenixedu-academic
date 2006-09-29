package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.PublicationArea;

public class ResultTeacher extends ResultTeacher_Base {
    
    static {
        Teacher.ResultTeacherTeacher.addListener(new ResultTeacherTeacherListener());
    }
    
    
    public ResultTeacher()
    {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public ResultTeacher(Result result, Teacher teacher, PublicationArea area) {
        this();
        verifyIfParticipator(teacher, result);
        setPublicationArea(area);
        setResult(result);
        setTeacher(teacher);
    }

    public void delete()
    {
        removeResult();
        removeTeacher();
        setPublicationArea(null);
        removeRootDomainObject();
        super.deleteDomainObject();
    }
 
    private void verifyIfParticipator(Teacher teacher, Result result) {
        boolean isParticipator = false;
        Person author = teacher.getPerson();
        for(ResultParticipation participation: result.getResultParticipations()) {
            if (participation.getPerson().equals(author)) isParticipator = true;
        }
        if (!isParticipator) throw new DomainException("error.result.teacherNotAuthor");
    }
    
    private static class ResultTeacherTeacherListener extends dml.runtime.RelationAdapter<Teacher,ResultTeacher> {
        @Override
        public void beforeAdd(Teacher teacher, ResultTeacher result) {
            if (teacher != null && !teacher.canAddResultToTeacherInformationSheet(result.getPublicationArea())) {
                throw new DomainException("error.teacherSheetFull", result.getPublicationArea().getName());
            }
        }
    }
}
