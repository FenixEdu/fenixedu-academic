package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.SearchService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixframework.DomainObject;

public class FindTeachersService extends SearchService {

    @Override
    protected InfoObject newInfoFromDomain(DomainObject object) {
        return InfoTeacher.newInfoFromDomain((Teacher) object);
    }

    @Override
    protected List doSearch(HashMap searchParameters) {
        Teacher teacher = Teacher.readTeacherByUsername((((String) searchParameters.get("teacherId"))).trim());

        List<Teacher> returnList = new ArrayList<Teacher>();
        if (teacher != null) {
            returnList.add(teacher);
        }
        return returnList;
    }

}