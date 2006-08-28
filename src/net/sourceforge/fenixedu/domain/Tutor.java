package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Tutor extends Tutor_Base {

    public Tutor(Teacher teacher) {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
        setTeacher(teacher);
	}

	private Boolean getCanBeDeleted() {
        return Boolean.TRUE;
    }
    
    public void delete() {
        if (getCanBeDeleted()) {
            removeRootDomainObject();
            deleteDomainObject();
        } else {
            throw new DomainException("tutor.cant.delete");
        }
    }

    public static List<Tutor> getAllTutorsByTeacherNumber(Integer tutorNumber) {
        List<Tutor> result = new ArrayList<Tutor>();
        
        if (tutorNumber == null) {
            return result;
        }
        
        for (Tutor tutor : RootDomainObject.getInstance().getTutors()) {
            if (! tutorNumber.equals(tutor.getTeacher().getTeacherNumber())) {
                continue;
            }
            
            result.add(tutor);
        }
        
        return result;
    }

}
