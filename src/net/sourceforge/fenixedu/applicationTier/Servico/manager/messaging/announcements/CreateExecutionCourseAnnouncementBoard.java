/*
 * Author : Goncalo Luiz
 * Creation Date: Jun 30, 2006,5:19:09 PM
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseBoardPermittedGroupType;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.RoleTypeGroup;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseAnnouncementBoard;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jun 30, 2006,5:19:09 PM
 * 
 */
public class CreateExecutionCourseAnnouncementBoard extends Service {
    public static class ExecutionCourseBoardAnnouncementBoardParameters {
        public Integer executionCourseId;

        public String name;

        public Boolean mandatory;

        public ExecutionCourseBoardPermittedGroupType readersGroupType;

        public ExecutionCourseBoardPermittedGroupType writersGroupType;

        public ExecutionCourseBoardPermittedGroupType managementGroupType;
    }

    public void run(ExecutionCourseBoardAnnouncementBoardParameters parameters)
            throws FenixServiceException {
        ExecutionCourseAnnouncementBoard board = new ExecutionCourseAnnouncementBoard();
        ExecutionCourse executionCourse = (ExecutionCourse) rootDomainObject
                .readExecutionCourseByOID(parameters.executionCourseId);

        board.setExecutionCourse(executionCourse);
        board.setExecutionCoursePermittedManagementGroupType(parameters.readersGroupType);
        board.setExecutionCoursePermittedWriteGroupType(parameters.writersGroupType);
        board.setExecutionCoursePermittedManagementGroupType(parameters.managementGroupType);
        board.setName(parameters.name);
        board.setMandatory(parameters.mandatory);
        board.setReaders(this.buildGroup(parameters.readersGroupType, executionCourse));
        board.setWriters(this.buildGroup(parameters.writersGroupType, executionCourse));
        board.setManagers(this.buildGroup(parameters.managementGroupType, executionCourse));

    }

    protected Group buildGroup(ExecutionCourseBoardPermittedGroupType type,
            ExecutionCourse executionCourse) {
        Group group = null;
        Group managers = new RoleTypeGroup(RoleType.MANAGER);
        switch (type) {
        case ECB_PUBLIC:
            break;
        case ECB_MANAGER:
            group = managers;
            break;
        case ECB_EXECUTION_COURSE_TEACHERS:
            group = new ExecutionCourseTeachersGroup(executionCourse);
            break;
        case ECB_EXECUTION_COURSE_STUDENTS:
            group = new ExecutionCourseStudentsGroup(executionCourse);
            break;
        case ECB_EXECUTION_COURSE_PERSONS:
            group = new ExecutionCourseStudentsGroup(executionCourse);
            group = new GroupUnion(new ExecutionCourseTeachersGroup(executionCourse), group);
            break;
        }

        if (group != null) {
            group = new GroupUnion(managers, group);
        }
        return group;
    }
}
