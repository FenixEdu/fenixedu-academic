/*
 * Author : Goncalo Luiz
 * Creation Date: Jun 27, 2006,12:07:53 PM
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.UnitBoardPermittedGroupType;
import net.sourceforge.fenixedu.domain.accessControl.AllDegreesStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.AllMasterDegreesStudents;
import net.sourceforge.fenixedu.domain.accessControl.DegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsiblesGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.InstitutionSiteManagers;
import net.sourceforge.fenixedu.domain.accessControl.MasterDegreeCoordinatorsGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleTypeGroup;
import net.sourceforge.fenixedu.domain.accessControl.TeachersAndInstitutionSiteManagersGroup;
import net.sourceforge.fenixedu.domain.accessControl.UnitEmployeesGroup;
import net.sourceforge.fenixedu.domain.accessControl.WebSiteManagersGroup;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jun 27, 2006,12:07:53 PM
 * 
 */
public class CreateUnitAnnouncementBoard {

    public static class UnitAnnouncementBoardParameters {
        public String unitId;

        public String name;

        public Boolean mandatory;

        public UnitBoardPermittedGroupType readersGroupType;

        public UnitBoardPermittedGroupType writersGroupType;

        public UnitBoardPermittedGroupType managementGroupType;

        public UnitAnnouncementBoardParameters() {
            super();
        }

        public UnitAnnouncementBoardParameters(String unitId, String name, Boolean mandatory,
                UnitBoardPermittedGroupType readers, UnitBoardPermittedGroupType writers, UnitBoardPermittedGroupType management) {

            this();
            this.unitId = unitId;
            this.name = name;
            this.mandatory = mandatory;
            this.readersGroupType = readers;
            this.writersGroupType = writers;
            this.managementGroupType = management;
        }
    }

    @Atomic
    public static void run(UnitAnnouncementBoardParameters parameters) throws FenixServiceException {
        Unit unit = (Unit) FenixFramework.getDomainObject(parameters.unitId);
        UnitAnnouncementBoard board = new UnitAnnouncementBoard(unit);

        board.setUnitPermittedReadGroupType(parameters.readersGroupType);
        board.setUnitPermittedWriteGroupType(parameters.writersGroupType);
        board.setUnitPermittedManagementGroupType(parameters.managementGroupType);
        board.setName(new MultiLanguageString(parameters.name));
        board.setMandatory(parameters.mandatory);
        board.setReaders(buildGroup(parameters.readersGroupType, unit));
        board.setWriters(buildGroup(parameters.writersGroupType, unit));
        board.setManagers(buildGroup(parameters.managementGroupType, unit));
    }

    protected static Group buildGroup(UnitBoardPermittedGroupType type, Unit unit) {
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
            group = new InstitutionSiteManagers();
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
        case UB_UNITSITE_MANAGERS:
            group = new WebSiteManagersGroup(unit.getSite());
            break;
        case UB_TEACHER_AND_WEBSITE_MANAGER:
            group = new TeachersAndInstitutionSiteManagersGroup();
            break;
        }

        if (group != null) {
            group = new GroupUnion(managers, group);
        }
        return group;
    }

}