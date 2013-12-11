package net.sourceforge.fenixedu.domain.research.result;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.PublicationArea;

public class ResultTeacher extends ResultTeacher_Base {

    public ResultTeacher() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public ResultTeacher(ResearchResult result, Teacher teacher, PublicationArea area) {
        this();
        if (result == null) {
            throw new DomainException("error.researcher.ResultTeacher.Result.null");
        }
        if (teacher == null) {
            throw new DomainException("error.researcher.ResultTeacher.Teacher.null");
        }
        if (area == null) {
            throw new DomainException("error.researcher.ResultTeacher.PublicationArea.null");
        }
        verifyIfParticipator(teacher, result);
        setPublicationArea(area);
        setResult(result);
        setTeacher(teacher);
    }

    public void delete() {
        setResult(null);
        setTeacher(null);
        setPublicationArea(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    private void verifyIfParticipator(Teacher teacher, ResearchResult result) {
        boolean isParticipator = false;
        Person author = teacher.getPerson();
        for (ResultParticipation participation : result.getResultParticipations()) {
            if (participation.getPerson().equals(author)) {
                isParticipator = true;
            }
        }
        if (!isParticipator) {
            throw new DomainException("error.result.teacherNotAuthor");
        }
    }

    @Deprecated
    public boolean hasResult() {
        return getResult() != null;
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPublicationArea() {
        return getPublicationArea() != null;
    }

}
