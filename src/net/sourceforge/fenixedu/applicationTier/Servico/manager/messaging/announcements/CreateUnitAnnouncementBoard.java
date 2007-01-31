/*
 * Author : Goncalo Luiz
 * Creation Date: Jun 27, 2006,12:07:53 PM
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.UnitBoardPermittedGroupType;
import net.sourceforge.fenixedu.domain.accessControl.AllDegreesStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllMasterDegreesStudents;
import net.sourceforge.fenixedu.domain.accessControl.DegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsiblesGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.MasterDegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleTypeGroup;
import net.sourceforge.fenixedu.domain.accessControl.UnitEmployeesGroup;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jun 27, 2006,12:07:53 PM
 * 
 */
public class CreateUnitAnnouncementBoard extends Service {

    public static class UnitAnnouncementBoardParameters {
        public Integer unitId;

        public String name;

        public Boolean mandatory;

        public UnitBoardPermittedGroupType readersGroupType;

        public UnitBoardPermittedGroupType writersGroupType;

        public UnitBoardPermittedGroupType managementGroupType;
    }

    public void run(UnitAnnouncementBoardParameters parameters) throws FenixServiceException {
        Unit unit = (Unit) rootDomainObject.readPartyByOID(parameters.unitId);
        UnitAnnouncementBoard board = new UnitAnnouncementBoard(unit);

        board.setUnitPermittedReadGroupType(parameters.readersGroupType);
        board.setUnitPermittedWriteGroupType(parameters.writersGroupType);
        board.setUnitPermittedManagementGroupType(parameters.managementGroupType);
        board.setName(parameters.name);
        board.setMandatory(parameters.mandatory);
        board.setReaders(this.buildGroup(parameters.readersGroupType, unit));
        board.setWriters(this.buildGroup(parameters.writersGroupType, unit));
        board.setManagers(this.buildGroup(parameters.managementGroupType, unit));
    }

    protected Group buildGroup(UnitBoardPermittedGroupType type, Unit unit) {
        Group group = null;
        Group managers = new RoleTypeGroup(RoleType.MANAGER);
        switch (type) {
        case UB_PUBLIC:
            break;
        case UB_MANAGER:
            group = managers;
            break;
        case UB_UNIT_PERSONS:
            group = new UnitEmployeesGroup(unit);
            break;
        case UB_DEGREE_COORDINATOR:
            group = new DegreeCoordinatorsGroup();
            break;
        case UB_EMPLOYEE:
            group = new RoleTypeGroup(RoleType.EMPLOYEE);
            break;
        case UB_TEACHER:
            group = new RoleTypeGroup(RoleType.TEACHER);
            break;
        case UB_WEBSITE_MANAGER:
            group = new RoleTypeGroup(RoleType.WEBSITE_MANAGER);
            break;
        case UB_DEGREE_STUDENT:
            group = new AllDegreesStudentsGroup();
            break;
        case UB_EXECUTION_COURSE_RESPONSIBLE:
            group = new ExecutionCourseResponsiblesGroup();
            break;
        case UB_MASTER_DEGREE_COORDINATOR:
            group = new MasterDegreeCoordinatorsGroup();
            break;
        case UB_MASTER_DEGREE_STUDENT:
            group = new AllMasterDegreesStudents();
            break;
        }

        if (group != null) {
            group = new GroupUnion(managers, group);
        }
        return group;
    }

}
