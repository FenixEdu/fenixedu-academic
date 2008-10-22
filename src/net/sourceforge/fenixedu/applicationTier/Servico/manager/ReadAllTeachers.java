package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;

public class ReadAllTeachers extends FenixService {

    public List<InfoTeacher> run() throws FenixServiceException {
	final List<InfoTeacher> result = new ArrayList<InfoTeacher>();

	for (final Teacher teacher : rootDomainObject.getTeachers()) {
	    result.add(InfoTeacher.newInfoFromDomain(teacher));
	}
	return result;
    }

}
