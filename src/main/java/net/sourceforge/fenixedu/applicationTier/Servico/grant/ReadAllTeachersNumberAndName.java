package net.sourceforge.fenixedu.applicationTier.Servico.grant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadAllTeachersNumberAndName extends FenixService {

    @Checked("RolePredicates.GRANT_OWNER_MANAGER_PREDICATE")
    @Service
    public static List run() throws FenixServiceException {
        final List<InfoTeacher> result = new ArrayList<InfoTeacher>();

        final Collection<Teacher> teachers = rootDomainObject.getTeachers();
        for (final Teacher teacher : teachers) {
            InfoTeacher infoTeacher = new InfoTeacher(teacher);
            result.add(infoTeacher);
        }

        return result;
    }

}