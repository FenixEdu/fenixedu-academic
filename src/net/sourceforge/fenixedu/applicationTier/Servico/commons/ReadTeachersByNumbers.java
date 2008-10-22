package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadTeachersByNumbers extends FenixService {

    @Service
    public static Collection<InfoTeacher> run(Collection<Integer> teacherNumbers) {
	Collection<InfoTeacher> infoTeachers = new ArrayList(teacherNumbers.size());
	Collection<Teacher> teachers = Teacher.readByNumbers(teacherNumbers);

	for (Teacher teacher : teachers) {
	    infoTeachers.add(InfoTeacher.newInfoFromDomain(teacher));
	}

	return infoTeachers;
    }

}