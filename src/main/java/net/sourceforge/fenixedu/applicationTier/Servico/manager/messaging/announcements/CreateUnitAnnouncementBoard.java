/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Author : Goncalo Luiz
 * Creation Date: Jun 27, 2006,12:07:53 PM
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.UnitBoardPermittedGroupType;
import net.sourceforge.fenixedu.domain.accessControl.CoordinatorGroup;
import net.sourceforge.fenixedu.domain.accessControl.ManagersOfUnitSiteGroup;
import net.sourceforge.fenixedu.domain.accessControl.ResponsibleForExecutionCourseGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.accessControl.StudentGroup;
import net.sourceforge.fenixedu.domain.accessControl.UnitGroup;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.AnyoneGroup;
import org.fenixedu.bennu.core.groups.Group;

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
        RoleType roleType = RoleType.MANAGER;
        Group managers = RoleGroup.get(roleType);
        switch (type) {
        case UB_PUBLIC:
            group = AnyoneGroup.get();
            break;
        case UB_MANAGER:
            group = managers;
            break;
        case UB_UNIT_PERSONS:
            group = UnitGroup.recursiveWorkers(unit);
            break;
        case UB_DEGREE_COORDINATOR:
            group = CoordinatorGroup.get(DegreeType.DEGREE);
            break;
        case UB_EMPLOYEE:
            group = RoleGroup.get(RoleType.EMPLOYEE);
            break;
        case UB_TEACHER:
            group = RoleGroup.get(RoleType.TEACHER);
            break;
        case UB_WEBSITE_MANAGER:
            group = ManagersOfUnitSiteGroup.get(Bennu.getInstance().getInstitutionUnit().getSite());
            break;
        case UB_DEGREE_STUDENT:
            group =
                    StudentGroup.get(DegreeType.DEGREE).or(StudentGroup.get(DegreeType.BOLONHA_DEGREE))
                            .or(StudentGroup.get(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));
            break;
        case UB_EXECUTION_COURSE_RESPONSIBLE:
            group = ResponsibleForExecutionCourseGroup.get();
            break;
        case UB_MASTER_DEGREE_COORDINATOR:
            group = CoordinatorGroup.get(DegreeType.MASTER_DEGREE);
            break;
        case UB_MASTER_DEGREE_STUDENT:
            group = StudentGroup.get(DegreeType.MASTER_DEGREE).or(StudentGroup.get(DegreeType.BOLONHA_MASTER_DEGREE));
            break;
        case UB_UNITSITE_MANAGERS:
            group = ManagersOfUnitSiteGroup.get(unit.getSite());
            break;
        case UB_TEACHER_AND_WEBSITE_MANAGER:
            group =
                    RoleGroup.get(RoleType.TEACHER).or(
                            ManagersOfUnitSiteGroup.get(Bennu.getInstance().getInstitutionUnit().getSite()));
            break;
        }

        if (group != null) {
            group = managers.or(group);
        }
        return group;
    }

}