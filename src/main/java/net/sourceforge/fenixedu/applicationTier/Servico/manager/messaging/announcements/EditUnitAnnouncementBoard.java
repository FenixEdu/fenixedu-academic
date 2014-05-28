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
 * Creation Date: Jun 28, 2006,4:04:39 PM
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jun 28, 2006,4:04:39 PM
 * 
 */
public class EditUnitAnnouncementBoard extends CreateUnitAnnouncementBoard {

    protected void run(UnitAnnouncementBoard board, UnitAnnouncementBoardParameters parameters) throws FenixServiceException {

        board.setUnitPermittedReadGroupType(parameters.readersGroupType);
        board.setUnitPermittedWriteGroupType(parameters.writersGroupType);
        board.setUnitPermittedManagementGroupType(parameters.managementGroupType);
        board.setName(new MultiLanguageString(parameters.name));
        board.setMandatory(parameters.mandatory);
        board.setReaders(this.buildGroup(parameters.readersGroupType, board.getUnit()));
        board.setWriters(this.buildGroup(parameters.writersGroupType, board.getUnit()));
        board.setManagers(this.buildGroup(parameters.managementGroupType, board.getUnit()));
    }

    // Service Invokers migrated from Berserk

    private static final EditUnitAnnouncementBoard serviceInstance = new EditUnitAnnouncementBoard();

    @Atomic
    public static void runEditUnitAnnouncementBoard(UnitAnnouncementBoard board, UnitAnnouncementBoardParameters parameters)
            throws FenixServiceException {
        serviceInstance.run(board, parameters);
    }

}