package net.sourceforge.fenixedu.applicationTier.Servico.framework;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.FileItemPermittedGroupType;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * 
 * @author naat
 * 
 */
public abstract class FileItemService extends Service {

    protected Group createPermittedGroup(FileItemPermittedGroupType fileItemPermittedGroupType,
            DomainObject domainObject) throws FenixServiceException {

        if (fileItemPermittedGroupType == FileItemPermittedGroupType.PUBLIC) {
            return null;
        } else if (fileItemPermittedGroupType == FileItemPermittedGroupType.INSTITUTION_PERSONS) {
            final Role personRole = Role.getRoleByRoleType(RoleType.PERSON);
            return new RoleGroup(personRole);
        } else if (fileItemPermittedGroupType == FileItemPermittedGroupType.EXECUTION_COURSE_TEACHERS_AND_STUDENTS) {
            final ExecutionCourse executionCourse = (ExecutionCourse) domainObject;
            return new GroupUnion(new ExecutionCourseTeachersGroup(executionCourse),
                    new ExecutionCourseStudentsGroup(executionCourse));
        } else {
            throw new FenixServiceException("error.exception");
        }

    }

}